package cupcake.nikky.imperium.tower.attack.damage.exact.input;

import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.DamageType;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import org.jetbrains.annotations.NotNull;

public class SpellDamageInput extends DamageProcess {

    @Override
    @InvokeIgnore
    public void invoke(@NotNull DamageContainer container) {
        container.getAttackInfluenceCallback().ifAttributablePresent(attributable -> {
            if (container.getDamageType() != DamageType.SPELL) return;

            for (InfluenceType value : InfluenceType.values()) {
                var multiplier = attributable.findAttribute(ImperiumAttribute.SPELL_DAMAGE).findValue() / 100;
                container.setDamage(value, container.findDamageByInfluence(value) * multiplier);
            }
        });
    }

}
