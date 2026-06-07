package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageFlag;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectOutboundDamageEvent;
import lombok.val;

import java.util.Arrays;

public class DefenseDistortionAspect extends AspectBase {

    public DefenseDistortionAspect() {
        super("Аспект: Искажение защиты", AspectType.ALL, AspectCategory.ATTACK,
                level -> Arrays.asList(
                        "При нанесении урона вся защита",
                        "противника будет уменьшена на %d%%".formatted(5 + level),
                        "в течение 4 секунд"),
                new CostData(CostProcessType.DEFAULT_MANA, level -> 20 + level));

        this.applyEvent(AspectOutboundDamageEvent.class, event -> {
            val container = event.getTarget().getLastDamageContainer();
            if (container == null) return;

            if (!container.containsDamageFlag(DamageFlag.EVASION) && !container.containsDamageFlag(DamageFlag.NOT_AVAILABLE)) {
                Fantasy.buffService().appendBuff(event.getTarget().getMonsterUniqueId(), BuffType.DEFENSE_DISTORTION, -5D - event.getLevel(), 4);
            }
        }, aspectEvent -> {
            val container = aspectEvent.getTarget().getLastDamageContainer();
            if (container == null) return false;

            if (!container.containsDamageFlag(DamageFlag.EVASION) && !container.containsDamageFlag(DamageFlag.NOT_AVAILABLE))
                return Fantasy.buffService().findById(aspectEvent.getTarget().getMonsterUniqueId()).map(buffedEntity -> buffedEntity.find(BuffType.DEFENSE_DISTORTION).isEmpty()).orElse(true);

            return false;
        });
    }

}
