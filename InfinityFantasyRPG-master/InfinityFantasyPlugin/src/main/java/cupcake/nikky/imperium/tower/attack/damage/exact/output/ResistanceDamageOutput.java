package cupcake.nikky.imperium.tower.attack.damage.exact.output;

import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.DamageType;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ElementalType;
import lombok.val;
import org.jetbrains.annotations.NotNull;

public class ResistanceDamageOutput extends DamageProcess {

    @Override
    @InvokeIgnore
    public void invoke(@NotNull DamageContainer container) {
        if (container.getDamageType() == DamageType.OVER_TIME) return;
        container.getAttackInfluenceCallback().ifAttributablePresent(attacker -> container.getDefender().entityTypeCallback().ifAttributablePresent(defender -> {
            for (ElementalType value : ElementalType.values()) {
                val influenceType = InfluenceType.valueOf(value.name());

                val totalResistance = defender.findAttribute(value.getResistance()).findValueWithLimit() - attacker.findAttribute(value.getPenetration()).findValueWithLimit();
                val currentElementalDamage = container.findDamageByInfluence(influenceType);

                container.setDamage(influenceType, currentElementalDamage - (currentElementalDamage * totalResistance / 100D));
            }
        }));
    }

}
