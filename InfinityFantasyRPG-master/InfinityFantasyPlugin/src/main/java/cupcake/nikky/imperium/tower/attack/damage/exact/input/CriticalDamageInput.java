package cupcake.nikky.imperium.tower.attack.damage.exact.input;

import com.google.common.util.concurrent.AtomicDouble;
import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageFlag;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.attribute.AttributeInstance;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

public class CriticalDamageInput extends DamageProcess {

    @Override
    @InvokeIgnore
    public void invoke(@NotNull DamageContainer container) {
        container.getAttackInfluenceCallback().ifAttributablePresent(attributable -> {
            if (attributable.findAttribute(ImperiumAttribute.FRAGMENT_CANNOT_DEAL_CRITICAL_STRIKE).findValue() > 0) return;

            AtomicDouble criticalRate = new AtomicDouble(attributable.findAttribute(ImperiumAttribute.CRITICAL_RATE).findValueWithLimit());

            container.getDefender().entityTypeCallback().ifUserPresent(user -> {
                val criticalRateInstance = new AttributeInstance(0);
                criticalRateInstance.applyInstance(attributable.getAttributes().find(ImperiumAttribute.CRITICAL_RATE));
                criticalRateInstance.applyIncrease(attributable.getAttributes().find(ImperiumAttribute.INCREASE_CRITICAL_RATE_ON_PLAYER).getValue() / 100);
                criticalRate.set(criticalRateInstance.getValue());
            });

            if (criticalRate.get() != 0D && MathUtility.randomInRange(0D, 100D) <= criticalRate.get()) {
                val criticalDamage = attributable.findAttribute(ImperiumAttribute.CRITICAL_DAMAGE).findValueWithLimit();
                container.applyDamageFlag(DamageFlag.CRITICAL);

                for (InfluenceType value : InfluenceType.values())
                    container.setDamage(value, container.findDamageByInfluence(value) * (1 + criticalDamage / 100D));
            }
        });
    }

}
