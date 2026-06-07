package cupcake.nikky.imperium.tower.attack;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.WeaponItem;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseAxe;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSpear;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSword;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.EntityUtility;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.NumberConversions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public record PlayerAttackHandler(Player player, User user, CraftEntity craftEntity) {

    public void handle() {
        val currentMillis = Instant.now().toEpochMilli();
        val isSourcePvp = user.getCurrentZone().pvp();

        val imperiumItem = user.getFantasyInventory().getHeldItem();
        if (imperiumItem == null) {
            if (craftEntity instanceof CraftPlayer && !isSourcePvp)
                return;

            user.setNextAvailableAttack(currentMillis + 1_000);
            Fantasy.damageProvider().processDamage(player, craftEntity).apply();
            return;
        }

        val attackSpeed = imperiumItem.getDamageSettings().getAttacks();

        if (attackSpeed != -1) {
            val attributeAttackSpeed = user.findAttribute(ImperiumAttribute.ATTACK_SPEED).findValueWithLimit();

            val millis = Math.round(attackSpeed / (attributeAttackSpeed / 100D));
            if (millis > 0) user.setNextAvailableAttack(currentMillis + millis);
        } else user.setNextAvailableAttack(currentMillis + 1_000);

        final List<CraftEntity> targets = new ArrayList<>();

        boolean isSoloTarget = false;

        if (imperiumItem.checkInstance(WeaponItem.class)) {
            if (imperiumItem.checkInstance(BaseSword.class) || imperiumItem.checkInstance(BaseSpear.class) || imperiumItem.checkInstance(BaseAxe.class)) {
                var radius = imperiumItem.checkInstance(BaseAxe.class) ? 2.5 : 1.5;
                val entities = EntityUtility.findInRadius(craftEntity.getLocation(), radius, 1, radius);
                if (imperiumItem.checkInstance(BaseAxe.class)) {
                    entities.forEach(entity -> {
                        if (craftEntity.getEntityId() != entity.getEntityId() && player.getEntityId() != entity.getEntityId() && canAttackPlayer(((CraftLivingEntity) entity))) {
                            var velocity = craftEntity.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
                            if (!NumberConversions.isFinite(velocity.getX()) || !NumberConversions.isFinite(velocity.getY()) || !NumberConversions.isFinite(velocity.getZ()))
                                return;
                            entity.setVelocity(velocity);
                        }
                        targets.add((CraftEntity) entity);
                    });
                } else {
                    entities.forEach(entity -> targets.add((CraftEntity) entity));
                }
            } else isSoloTarget = true;
        } else isSoloTarget = true;

        if (isSoloTarget) {
            if (craftEntity instanceof CraftPlayer) {
                if (isSourcePvp) targets.add(craftEntity);
            } else {
                targets.add(craftEntity);
            }
        }

        targets.stream().filter(craftEntity -> craftEntity.getEntityId() != player.getEntityId())
                .filter(craftEntity -> craftEntity.getPassengers().isEmpty())
                .filter(this::canAttackPlayer)
                .forEach(entity -> Fantasy.damageProvider().processDamage(player, entity).apply());
    }

    private boolean canAttackPlayer(CraftEntity defender) {
        if (!(defender instanceof CraftPlayer craftPlayer)) return true;
        var user = player.<User>getBungeePlayer();
        var defenderUser = craftPlayer.<User>getBungeePlayer();

        var attackerZone = user.getCurrentZone();
        var defenderZone = defenderUser.getCurrentZone();

        if (defenderZone.anySpawnIn(craftEntity.getLocation()) || attackerZone.anySpawnIn(player.getLocation())) return false;

        return attackerZone.pvp() && defenderZone.pvp() || defenderUser.findPvpSettings().localPvpState() && user.findPvpSettings().localPvpState();
    }

}
