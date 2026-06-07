package cupcake.nikky.imperium.tower.attack.damage.listener;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BukkitDamageListener implements DefaultListener {

    private final Cache<UUID, Boolean> voidDamageCooldown = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onArmorAbsorb(@NotNull final EntityDamageEvent event) {
        event.setCancelled(true);

        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        if (event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            Fantasy.damageProvider().processDamage(event.getEntity()).setDamage(InfluenceType.PHYSICAL, 500D).apply();
            if (!Fantasy.combatProvider().isInCombat(entity.getUniqueId()))
                Fantasy.combatProvider().registerCombat(entity.getUniqueId(), 1_000);
        } else if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            val target = entity.getPassengers().isEmpty() ? entity : entity.getPassengers().getFirst();
            Fantasy.userService().findById(target.getUniqueId()).ifPresent(user -> {
                val jumpData = user.getJumpData();
                if (jumpData.getFallDistance() == 0F) {
                    Fantasy.damageProvider().processDamage(target).setDamageByPercentOfDefenderHealth(InfluenceType.PHYSICAL, entity.getFallDistance() * 1.25D).apply();
                    if (!Fantasy.combatProvider().isInCombat(target.getUniqueId()))
                        Fantasy.combatProvider().registerCombat(target.getUniqueId(), 1_000);
                } else if (jumpData.getFallDistance() > 0F) {
                    Fantasy.damageProvider().processDamage(target).setDamageByPercentOfDefenderHealth(InfluenceType.PHYSICAL, jumpData.getFallDistance() * 1.25D).apply();
                    if (!Fantasy.combatProvider().isInCombat(target.getUniqueId()))
                        Fantasy.combatProvider().registerCombat(target.getUniqueId(), 1_000);
                }
                jumpData.cancel();
            });
        } else if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            val target = entity.getPassengers().isEmpty() ? entity : entity.getPassengers().getFirst();
            if (voidDamageCooldown.getIfPresent(target.getUniqueId()) != null) return;
            voidDamageCooldown.put(target.getUniqueId(), true);
            Fantasy.damageProvider().processDamage(target).setDamage(InfluenceType.PHYSICAL, Integer.MAX_VALUE).apply();
            if (!Fantasy.combatProvider().isInCombat(target.getUniqueId()))
                Fantasy.combatProvider().registerCombat(target.getUniqueId(), 1_000);
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onProjectileLaunch(@NotNull final ProjectileLaunchEvent event) {
        val projectile = event.getEntity();
        val source = projectile.getShooter();

        if (source instanceof Player player) {
            val imperiumItem = Fantasy.userService().findById(player.getUniqueId()).map(user -> user.getFantasyInventory().getHeldItem()).orElse(null);
            projectile.setMetadata("itemInHand", new FixedMetadataValue(Fantasy.getJavaPlugin(), imperiumItem == null ? null : imperiumItem.serialize()));
        } else if (source instanceof LivingEntity) {
            projectile.setMetadata("itemInHand", new FixedMetadataValue(Fantasy.getJavaPlugin(), null));
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent event) {
        event.getEntity().removeMetadata("itemInHand", Fantasy.getJavaPlugin());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamageEntity(@NotNull final EntityDamageByEntityEvent event) {
        event.setCancelled(true);

        if (event.getDamager() == null || event.getEntity() == null) return;
        if (!(event.getDamager() instanceof Player) && (event.getDamager() instanceof LivingEntity || event.getDamager() instanceof Projectile)) {
            Entity entity = null;
            if (event.getEntity() instanceof Player player) {
                entity = player;
            } else if (event.getEntity() instanceof LivingEntity livingEntity) {
                val passengers = livingEntity.getPassengers();
                if (!passengers.isEmpty())
                    entity = passengers.getFirst();
            }

            if (entity == null) return;

            val finalEntity = entity;
            Fantasy.getAttackExecutor().execute(() -> Fantasy.damageProvider().processDamage(event.getDamager(), finalEntity).apply());
        }
    }

}
