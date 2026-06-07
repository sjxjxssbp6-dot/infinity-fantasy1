package cupcake.nikky.imperium.tower.entity.monster.location;

import com.mongodb.client.model.Filters;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.protocol.database.Collections;
import lombok.val;
import org.bson.Document;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MonsterLocationRepository {

    private final Map<MonsterType, MonsterLocationStorage> data = new ConcurrentHashMap<>(128, 1F, 2);

    public MonsterLocationStorage findByType(@NotNull final MonsterType type) {
        MonsterLocationStorage storage = this.data.get(type);

        if (storage == null) {
            storage = new MonsterLocationStorage();
            this.registerNew(type, storage);
        }

        return storage;
    }

    public void registerNew(@NotNull final MonsterType type, @NotNull final MonsterLocationStorage storage) {
        this.data.put(type, storage);
    }

    public void registerNewLocation(@NotNull final MonsterType type, @NotNull final Location location) {
        this.findByType(type).registerNew(location);
    }

    public CompletableFuture<Void> loadAll() {
        return Fantasy.databaseProvider().asyncCollection(Collections.MONSTER_SPAWN_LOCATION).thenAcceptAsync(documentMongoCollection -> {
            documentMongoCollection.find().forEach(document -> {
                val name = document.get("_id", "");
                if (name.isEmpty()) return;

                MonsterType monsterType;
                try {
                    monsterType = MonsterType.valueOf(name);
                } catch (IllegalArgumentException | NullPointerException ignore) {
                    return;
                }

                val storage = MonsterLocationStorage.deserialize(document.get("data", ""));
                this.registerNew(monsterType, storage);
            });
        });
    }

    public void saveLocationsAsync() {
        this.invalidateTableData().thenApplyAsync(ignore -> Fantasy.databaseProvider().asyncCollection(Collections.MONSTER_SPAWN_LOCATION).join()).thenAcceptAsync(documentMongoCollection -> {
            documentMongoCollection.insertMany(
                    this.data.keySet().stream()
                            .map(monsterType ->
                                    new Document().append("_id", monsterType.name())
                                            .append("data", this.data.get(monsterType).serialize())
                            )
                    .toList());
        });
    }

    private CompletableFuture<Void> invalidateTableData() {
        return Fantasy.databaseProvider().asyncCollection(Collections.MONSTER_SPAWN_LOCATION).thenAcceptAsync(documentMongoCollection -> {
            documentMongoCollection.deleteMany(Filters.empty());
        });
    }

}
