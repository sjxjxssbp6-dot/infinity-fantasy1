package cupcake.nikky.imperium.tower.buff.repository;

import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffedEntity;
import cupcake.nikky.imperium.tower.utility.lock.Locker;
import lombok.val;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ImperiumBuffRepository {

    private final Map<UUID, BuffedEntity> data = new ConcurrentHashMap<>(128, 0.75F, 2);
    private final List<UUID> temp = new ArrayList<>();

    private final Locker tempLocker = Locker.create();

    public BuffedEntity findById(@NotNull final UUID uuid) {
        return this.data.get(uuid);
    }

    public Collection<BuffedEntity> findAll() {
        return new ArrayList<>(this.data.values());
    }

    public CompletableFuture<BuffedEntity> registerEntity(@NotNull final UUID uuid, final boolean isPlayer) {
        if (!isPlayer) {
            val entity = BuffedEntity.deserialize(uuid, null);
            this.data.put(uuid, entity);
            tempLocker.writePure(() -> this.temp.add(uuid));
            return CompletableFuture.completedFuture(entity);
        }

        return Fantasy.databaseProvider().findUser(uuid).thenApplyAsync(document -> {
            if (document.isEmpty()) return new BuffedEntity(uuid);

            val buffData = document.get("buffData", "");
            return BuffedEntity.deserialize(uuid, buffData);
        }).thenApplyAsync(buffedEntity -> {
            this.data.put(uuid, buffedEntity);

            return buffedEntity;
        });
    }

    public List<Bson> saveEntity(@NotNull final UUID uuid) {
        val entity = this.findById(uuid);
        if (entity == null || tempLocker.read(() -> this.temp.contains(uuid))) {
            return Collections.emptyList();
        }

        val serialized = entity.serialize();
        entity.getAll().forEach(buff -> buff.onCompletion(Bukkit.getPlayer(uuid)));

        return Collections.singletonList(Updates.set("buffData", serialized));
    }

    public List<Bson> invalidateEntity(@NotNull final UUID uuid) {
        val updates = this.saveEntity(uuid);

        this.data.remove(uuid);
        tempLocker.writePure(() -> this.temp.remove(uuid));
        return updates;
    }

}
