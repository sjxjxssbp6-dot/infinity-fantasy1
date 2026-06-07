package cupcake.nikky.imperium.tower.entity.monster.damage;

import org.bukkit.entity.LivingEntity;

public interface InboundDamageProcess {

    default double onInboundDamage(final LivingEntity source, final double damage) {
        return damage;
    }

}