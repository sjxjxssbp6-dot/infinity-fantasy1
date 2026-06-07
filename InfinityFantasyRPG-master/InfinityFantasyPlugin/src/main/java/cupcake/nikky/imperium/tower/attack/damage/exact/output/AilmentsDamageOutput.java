package cupcake.nikky.imperium.tower.attack.damage.exact.output;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.*;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.buff.exact.*;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import me.anfanik.steda.api.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

public class AilmentsDamageOutput extends DamageProcess {

    @Override
    @InvokeIgnore
    public void invoke(@NotNull DamageContainer container) {
        if (container.getDamageType() != DamageType.HIT && container.getDamageType() != DamageType.SPELL) return;

        container.getAttackInfluenceCallback().ifAttributablePresent(attributable -> {
            //var ailmentDuration = attributable.findAttribute(ImperiumAttribute.AILMENT_DURATION).findValue() / 100D;

            var chanceToPoison = attributable.findAttribute(ImperiumAttribute.CHANCE_TO_POISON).findValue() + container.findAttributeValue(ImperiumAttribute.CHANCE_TO_POISON);
            var chanceToBleed = attributable.findAttribute(ImperiumAttribute.CHANCE_TO_CAUSE_BLEEDING).findValue() + container.findAttributeValue(ImperiumAttribute.CHANCE_TO_CAUSE_BLEEDING);

            var physical = container.findDamageByInfluence(InfluenceType.PHYSICAL);
            var chaos = container.findDamageByInfluence(InfluenceType.CHAOS);

            if (chanceToPoison > 0 && (physical > 0 || chaos > 0) && MathUtility.randomInRange(0, 100) <= chanceToPoison) {
                var poison = Fantasy.buffService().appendBuff(container.getDefender().getLivingEntity().getUniqueId(), BuffType.POISON, .3 * (physical + chaos), 3);
                if (poison != null) ((Poison) poison).setInfluence(container.getAttackInfluence());
            }

            if (chanceToBleed > 0 && physical > 0 && MathUtility.randomInRange(0, 100) <= chanceToBleed) {
                var bleeding = Fantasy.buffService().appendBuff(container.getDefender().getLivingEntity().getUniqueId(), BuffType.BLEEDING, .7 * physical, 5);
                if (bleeding != null) ((Bleeding) bleeding).setInfluence(container.getAttackInfluence());
            }

            var lightningDamage = container.findDamageByInfluence(InfluenceType.LIGHTNING);
            if (lightningDamage > 0D) {
                var chanceToShock = attributable.findAttribute(ImperiumAttribute.CHANCE_TO_SHOCK_ON_HIT).findValue() + container.findAttributeValue(ImperiumAttribute.CHANCE_TO_SHOCK_ON_HIT);

                if (container.containsDamageFlag(DamageFlag.CRITICAL) || chanceToShock > 0 && MathUtility.randomInRange(0, 100) <= chanceToShock) {
                    var shockEffect = attributable.findAttribute(ImperiumAttribute.SHOCK_EFFECT).findValue() / 100D;
                    var ailmentThreshold = attributable.findAttribute(ImperiumAttribute.AILMENT_THRESHOLD).findValue();

                    var value = .5 * Math.pow(lightningDamage / ailmentThreshold, .4) * shockEffect;
                    if (value >= 1) {
                        var shock = Fantasy.buffService().appendBuff(container.getDefender().getLivingEntity().getUniqueId(), BuffType.SHOCK, Math.min(value, 50D), 2);
                        if (shock != null) ((Shock) shock).setInfluence(container.getAttackInfluence());
                    }
                }
            }

            var fireDamage = container.findDamageByInfluence(InfluenceType.FIRE);
            if (fireDamage > 0D) {
                var chanceToIgnite = attributable.findAttribute(ImperiumAttribute.CHANCE_TO_IGNITE_ON_HIT).findValue() + container.findAttributeValue(ImperiumAttribute.CHANCE_TO_IGNITE_ON_HIT);

                if (container.containsDamageFlag(DamageFlag.CRITICAL) || chanceToIgnite > 0 && MathUtility.randomInRange(0, 100) <= chanceToIgnite) {
                    var igniteDamage = attributable.findAttribute(ImperiumAttribute.IGNITE_DAMAGE).findValue() / 100D;
                    var value = fireDamage * igniteDamage;

                    if (value >= 1) {
                        var ignite = Fantasy.buffService().appendBuff(container.getDefender().getLivingEntity().getUniqueId(), BuffType.IGNITE, value, 4);
                        if (ignite != null) ((Ignite) ignite).setInfluence(container.getAttackInfluence());
                    }
                }
            }

            var coldDamage = container.findDamageByInfluence(InfluenceType.COLD);
            if (coldDamage > 0D) {
                var chanceToFreeze = attributable.findAttribute(ImperiumAttribute.CHANCE_TO_FREEZE_ON_HIT).findValue() + container.findAttributeValue(ImperiumAttribute.CHANCE_TO_FREEZE_ON_HIT);

                if (container.containsDamageFlag(DamageFlag.CRITICAL) || chanceToFreeze > 0 && MathUtility.randomInRange(0, 100) <= chanceToFreeze) {
                    var freezeDuration = attributable.findAttribute(ImperiumAttribute.FREEZE_DURATION).findValue() / 100;
                    var value = Math.max(coldDamage / attributable.findAttribute(ImperiumAttribute.AILMENT_THRESHOLD).findValue(), .1);
                    var duration = Math.round((1 + value / 0.25) * freezeDuration);

                    if (duration >= 1) {
                        var freeze = Fantasy.buffService().appendBuff(container.getDefender().getLivingEntity().getUniqueId(), BuffType.FREEZE, 0, (int) duration);
                        if (freeze != null) ((Freeze) freeze).setInfluence(container.getAttackInfluence());
                    }
                }
            }
        });
    }

}
