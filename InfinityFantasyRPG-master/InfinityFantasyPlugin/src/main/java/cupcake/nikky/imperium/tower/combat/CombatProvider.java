package cupcake.nikky.imperium.tower.combat;

import cupcake.nikky.imperium.tower.Fantasy;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CombatProvider {

    private final Map<UUID, UUID> combats = new ConcurrentHashMap<>(32, 0.75F, 4);
    private final Map<UUID, Long> updater = new ConcurrentHashMap<>(32, 0.75F, 4);

    public void registerCombat(@NotNull final UUID entity) {
        this.registerCombat(entity, 5_000);
    }

    public void registerCombat(@NotNull final UUID entity, final int duration) {
        this.updater.put(entity, Instant.now().toEpochMilli() + duration);
        this.combats.put(entity, entity);

        Fantasy.userService().findById(entity).ifPresent(user -> Fantasy.clientProvider().showBattleDuration(user));
    }

    public void registerCombat(@Nullable final UUID damager, @Nullable final UUID entity) {
        this.registerCombat(damager, entity, 5_000);
    }

    public void registerCombat(@Nullable final UUID damager, @Nullable final UUID entity, final int duration) {
        this.combats.put(damager, entity);
        this.combats.put(entity, damager);

        this.updater.put(damager, Instant.now().toEpochMilli() + duration);
        this.updater.put(entity, Instant.now().toEpochMilli() + duration);

        if (damager != null) Fantasy.userService().findById(damager).ifPresent(user -> Fantasy.clientProvider().showBattleDuration(user));
        if (entity != null) Fantasy.userService().findById(entity).ifPresent(user -> Fantasy.clientProvider().showBattleDuration(user));
    }

    public boolean isInCombat(@NotNull final UUID uuid) {
        if (updater.containsKey(uuid) && updater.getOrDefault(uuid, 0L) <= Instant.now().toEpochMilli()) {
            this.updater.remove(uuid);
            val enemy = this.combats.remove(uuid);
            if (enemy != null) {
                this.updater.remove(enemy);
                this.combats.remove(enemy);
            }
        }
        return combats.containsKey(uuid);
    }

    public long findEndBattle(@NotNull final UUID uuid) {
        return updater.containsKey(uuid) ? updater.getOrDefault(uuid, -1L) : -1;
    }

    public void invalidateCombat(@NotNull final Collection<UUID> uuids) {
        for (UUID uuid : uuids) {
            this.combats.remove(uuid);
            this.updater.remove(uuid);
            Fantasy.userService().findById(uuid).ifPresent(user -> Fantasy.clientProvider().hideBattleDuration(user));
        }
    }

    public void invalidateCombat(@NotNull final UUID... uuids) {
        for (UUID uuid : uuids) {
            this.combats.remove(uuid);
            this.updater.remove(uuid);
            Fantasy.userService().findById(uuid).ifPresent(user -> Fantasy.clientProvider().hideBattleDuration(user));
        }
    }

}
