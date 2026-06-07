package cupcake.nikky.imperium.tower.attack.damage.exact.output;

import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.DamageType;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class DefenseOutput extends DamageProcess {

    @Override
    @InvokeIgnore
    public void invoke(@NotNull DamageContainer container) {
        container.getDefender().entityTypeCallback().ifAttributablePresent(attributable -> {
            val globalDefense = attributable.findAttribute(ImperiumAttribute.GLOBAL_DEFENSE).findValueWithLimit();

            for (InfluenceType value : InfluenceType.values()) {
                val damage = container.findDamageByInfluence(value);
                container.setDamage(value, damage - damage * (globalDefense / 100D));
            }

            if (attributable.findAttribute(ImperiumAttribute.CAN_RECEIVE_ONLY_PHYSICAL_AND_CHAOS_DAMAGE_BY_PLAYER).findValue() != 0) {
                for (InfluenceType value : InfluenceType.values()) {
                    if (value == InfluenceType.PHYSICAL || value == InfluenceType.CHAOS) continue;
                    container.setDamage(value, 0);
                }
            }
        });

        if (container.getDamageType() == DamageType.OVER_TIME) return;

        container.getDefender().entityTypeCallback().ifAttributablePresent(defenser -> {
            container.getAttackInfluenceCallback().ifAttributablePresent(attacker -> {
                val physicalDamage = container.findDamageByInfluence(InfluenceType.PHYSICAL);
                val defense = defenser.findAttribute(ImperiumAttribute.DEFENSE).findValueWithLimit();
                val penetration = attacker.findAttribute(ImperiumAttribute.DEFENSE_PENETRATION).findValueWithLimit();
                container.setDamage(InfluenceType.PHYSICAL, physicalDamage * (1 - (defense - penetration) / 100D));
            });
        });
    }

}
