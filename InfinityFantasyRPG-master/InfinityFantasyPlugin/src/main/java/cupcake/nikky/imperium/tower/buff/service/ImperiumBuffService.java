package cupcake.nikky.imperium.tower.buff.service;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.Buff;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.buff.BuffedEntity;
import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.buff.repository.ImperiumBuffRepository;
import cupcake.nikky.imperium.tower.protocol.packet.ImperiumBuffPacket;
import lombok.val;
import me.anfanik.steda.api.utility.Pair;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class ImperiumBuffService {

    private final ImperiumBuffRepository repository = new ImperiumBuffRepository();

    public Optional<BuffedEntity> findById(UUID uuid) {
        return Optional.ofNullable(this.repository.findById(uuid));
    }

    public Collection<BuffedEntity> findAll() {
        return this.repository.findAll();
    }

    public CompletableFuture<BuffedEntity> registerBuffedEntity(@NotNull final UUID uuid, final boolean player) {
        return this.repository.registerEntity(uuid, player);
    }

    public List<Bson> saveEntity(@NotNull final UUID uuid) {
        return this.repository.saveEntity(uuid);
    }

    public List<Bson> invalidateEntity(@NotNull final UUID uuid) {
        return this.repository.invalidateEntity(uuid);
    }

    public void appendBuff(@NotNull final UUID uuid, @NotNull final BuffType buffType) {
        this.appendBuff(uuid, buffType, 1D, 0);
    }

    public Buff appendBuff(@NotNull final UUID uuid, @NotNull final BuffType buffType, final double value, final int duration) {
        var entity = this.findById(uuid).orElse(null);
        if (entity == null) return null;

        val isNew = entity.registerType(buffType, value, duration);

        val userOptional = Fantasy.userService().findById(uuid);

        if (userOptional.isPresent()) {
            val user = userOptional.get();
            val buff = entity.find(buffType);
            buff.ifPresent(buff1 -> {
                if (!isNew && buff1.isJustUpdatedMultiplier()) {
                    new ImperiumBuffPacket(user, buff1).notice(1);
                    buff1.setJustUpdatedMultiplier(false);
                } else if (isNew) {
                    buff1.onReceive(user.getSpigot());
                    new ImperiumBuffPacket(user, buff1).notice(0);
                }
            });
        } else {
            Fantasy.monsterProvider().findByUUID(uuid).ifPresent(monster -> {
                val buff = entity.find(buffType);
                buff.ifPresent(buff1 -> {
                    if (!isNew && buff1.isJustUpdatedMultiplier()) {
                        buff1.setJustUpdatedMultiplier(false);
                    } else if (isNew) {
                        buff1.onReceive(monster.getSpigot());
                    }
                });
            });
        }

        return entity.find(buffType).orElse(null);
    }

    public void invalidateBuff(UUID uuid, BuffType buffType) {
        this.findById(uuid).ifPresent(entity -> {
            Fantasy.userService().findById(uuid).ifPresent(user -> {
                val buff = entity.find(buffType);
                buff.ifPresent(buff1 -> new ImperiumBuffPacket(user, buff1).notice(2));
            });

            entity.invalidateType(buffType);
        });
    }

    public void invalidateBuff(BuffedEntity entity, BuffType buffType) {
        Fantasy.userService().findById(entity.getUuid()).ifPresent(user -> {
            val buff = entity.find(buffType);
            buff.ifPresent(buff1 -> new ImperiumBuffPacket(user, buff1).notice(2));
        });

        entity.invalidateType(buffType);
    }

}
