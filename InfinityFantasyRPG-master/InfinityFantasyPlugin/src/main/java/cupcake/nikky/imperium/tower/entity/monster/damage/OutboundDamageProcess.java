package cupcake.nikky.imperium.tower.entity.monster.damage;

import org.bukkit.entity.LivingEntity;

public interface OutboundDamageProcess {

    default double onOutboundDamage(final LivingEntity target, final double damage) {
        return damage;
    }

}
