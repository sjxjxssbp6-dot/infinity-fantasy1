package cupcake.nikky.imperium.tower.entity.monster.updater;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageFlag;
import cupcake.nikky.imperium.tower.attribute.AttributeInstance;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.goal.exact.ImmobilizedGoalUpdater;
import lombok.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MonsterUpdater {

    @Getter
    private static final MonsterUpdater instance = new MonsterUpdater();

    private final Map<UUID, Double> movementUpdate = new ConcurrentHashMap<>(1024, 0.75F, 2);

    public void invalidateMovementCache(@NotNull final UUID uuid) {
        movementUpdate.remove(uuid);
    }

    public void update(@NotNull final Monster monster, final boolean second) {
        val spigot = monster.getSpigot();
        if (spigot == null || !monster.isExist() || monster.isShouldDeath()) return;

        monster.setInvulnerability(monster.getInvulnerabilityTicksLeft() - 10);

        val spigotLocation = spigot.getLocation();
        if (spigotLocation.getY() < 50) {
            Fantasy.runLater(() -> {
                if (!spigot.isValid()) return;
                spigot.teleport(monster.getSpawnLocation());
            });
            monster.getTargetTracker().goalTarget(null);
            return;
        }

        if (monster.getTargetTracker().isForgotTarget()) {
            val maxHealth = monster.findAttribute(ImperiumAttribute.HEALTH).findValue();
            val currentHealth = monster.getHealth();

            if (currentHealth < maxHealth) {
                monster.changeHealth(maxHealth / 20);
            } else {
                monster.getTargetTracker().reset();
                if (monster.isInvulnerability()) monster.setInvulnerability(0);
            }
        }

        val trackerEntry = ((CraftWorld) MapServiceProcess.GAME_WORLD).getHandle().tracker.trackedEntities.get(spigot.getEntityId());
        if (second) monster.getTargetTracker().update(trackerEntry);

        if (second) {
            val healInFight = Math.max(monster.findAttribute(ImperiumAttribute.REGENERATION_HEALTH_IN_FIGHT).findValue(), 0);
            monster.changeHealth(healInFight);
        }

        monster.getTargetBarData().updateAll();

        if (trackerEntry != null && trackerEntry.trackedPlayers.isEmpty()) return;

        Fantasy.buffService().findById(monster.getMonsterUniqueId()).ifPresent(entity -> {
            val buffs = entity.getAll();
            monster.getAttributes().forEach(AttributeInstance::clearForeign);

            buffs.forEach(buff -> monster.getAttributes().join(buff.getAttributes()));
            if (monster.isAuraDamager()) {
                var attackSpeed = monster.findAttribute(ImperiumAttribute.ATTACK_SPEED).findValue() / 100D - 1;
                if (attackSpeed != 0D) {
                    monster.getAttributes().find(ImperiumAttribute.MIN_PHYSICAL_DAMAGE).applyIncrease(attackSpeed);
                    monster.getAttributes().find(ImperiumAttribute.MAX_PHYSICAL_DAMAGE).applyIncrease(attackSpeed);
                    monster.getAttributes().find(ImperiumAttribute.MIN_FIRE_DAMAGE).applyIncrease(attackSpeed);
                    monster.getAttributes().find(ImperiumAttribute.MAX_FIRE_DAMAGE).applyIncrease(attackSpeed);
                    monster.getAttributes().find(ImperiumAttribute.MIN_COLD_DAMAGE).applyIncrease(attackSpeed);
                    monster.getAttributes().find(ImperiumAttribute.MAX_COLD_DAMAGE).applyIncrease(attackSpeed);
                    monster.getAttributes().find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).applyIncrease(attackSpeed);
                    monster.getAttributes().find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).applyIncrease(attackSpeed);
                    monster.getAttributes().find(ImperiumAttribute.MIN_CHAOS_DAMAGE).applyIncrease(attackSpeed);
                    monster.getAttributes().find(ImperiumAttribute.MAX_CHAOS_DAMAGE).applyIncrease(attackSpeed);
                }
            }
        });

        val movementSpeedAttribute = spigot.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);

        if (movementSpeedAttribute != null) {
            var last = this.movementUpdate.get(monster.getMonsterUniqueId());
            if (last == null) last = -1D;

            val current = 1 + monster.findAttribute(ImperiumAttribute.MOVEMENT_SPEED).findValueWithLimit() / 100F;
            if (last != current) {
                Fantasy.runLater(() -> {
                    if (monster.getGoalUpdater() instanceof ImmobilizedGoalUpdater) {
                        movementSpeedAttribute.setBaseValue(-.6F);
                    } else movementSpeedAttribute.setBaseValue(.3F * current);

                    this.movementUpdate.put(monster.getMonsterUniqueId(), current);
                });
            }
        }

        if (monster.isAuraDamager()) {
            monster.getAttackers().forEach(player -> {
                if (player.getLocation().distanceSquared(spigotLocation) <= monster.getAuraRadius() * monster.getAuraRadius()) {
                    Fantasy.damageProvider().processDamage(spigot, player).applyFlag(DamageFlag.IGNORE_KNOCK_BACK).apply();
                }
            });
        }
    }

}
