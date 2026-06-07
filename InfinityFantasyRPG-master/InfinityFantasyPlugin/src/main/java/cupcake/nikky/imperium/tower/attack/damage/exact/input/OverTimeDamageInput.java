package cupcake.nikky.imperium.tower.attack.damage.exact.input;

import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.DamageType;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import org.jetbrains.annotations.NotNull;

public class OverTimeDamageInput extends DamageProcess {

    @Override
    @InvokeIgnore
    public void invoke(@NotNull DamageContainer container) {
        if (container.getDamageType() != DamageType.OVER_TIME) return;

        container.getAttackInfluenceCallback().ifAttributablePresent(attributable -> {
            for (InfluenceType value : InfluenceType.values()) {
                var multiplier = attributable.findAttribute(ImperiumAttribute.DAMAGE_OVER_TIME).findValue() / 100;
                container.setDamage(value, container.findDamageByInfluence(value) * multiplier);
            }
        });
    }

}
