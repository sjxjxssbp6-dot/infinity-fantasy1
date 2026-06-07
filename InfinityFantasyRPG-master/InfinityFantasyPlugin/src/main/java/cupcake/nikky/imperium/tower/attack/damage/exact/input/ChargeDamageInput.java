package cupcake.nikky.imperium.tower.attack.damage.exact.input;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.DamageType;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import me.anfanik.steda.api.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

public class ChargeDamageInput extends DamageProcess {

    @Override
    @InvokeIgnore
    public void invoke(@NotNull DamageContainer container) {
        if (container.getDamageType() != DamageType.HIT) return;
        container.getAttackInfluenceCallback().ifAttributablePresent(attributable -> {
            var chanceToGainPowerCharge = attributable.findAttribute(ImperiumAttribute.CHANCE_TO_GAIN_POWER_CHARGE_ON_HIT).findValueWithLimit();
            var chanceToGainEnduranceCharge = attributable.findAttribute(ImperiumAttribute.CHANCE_TO_GAIN_ENDURANCE_CHARGE_ON_HIT).findValueWithLimit();
            var chanceToGainFrenzyCharge = attributable.findAttribute(ImperiumAttribute.CHANCE_TO_GAIN_FRENZY_CHARGE_ON_HIT).findValueWithLimit();

            if (chanceToGainPowerCharge > 0 && MathUtility.randomInRange(0, 100) <= chanceToGainPowerCharge) {
                Fantasy.buffService().appendBuff(container.getAttacker().getLivingEntity().getUniqueId(), BuffType.POWER_CHARGE);
            }

            if (chanceToGainEnduranceCharge > 0 && MathUtility.randomInRange(0, 100) <= chanceToGainEnduranceCharge) {
                Fantasy.buffService().appendBuff(container.getAttacker().getLivingEntity().getUniqueId(), BuffType.ENDURANCE_CHARGE);
            }

            if (chanceToGainFrenzyCharge > 0 && MathUtility.randomInRange(0, 100) <= chanceToGainFrenzyCharge) {
                Fantasy.buffService().appendBuff(container.getAttacker().getLivingEntity().getUniqueId(), BuffType.FRENZY_CHARGE);
            }
        });
    }

}
