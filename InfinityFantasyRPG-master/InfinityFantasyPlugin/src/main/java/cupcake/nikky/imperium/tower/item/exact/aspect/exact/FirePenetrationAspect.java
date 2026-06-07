package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectHandEvent;

import java.util.List;

public class FirePenetrationAspect extends AspectBase {

    public FirePenetrationAspect() {
        super("Аспект: Пробивание огня", AspectType.ALL, AspectCategory.ATTACK,
                level -> List.of(
                        "Вы пробиваете %d%% сопротивления огню".formatted(10 + level),
                        "%d%% увеличение урона огнем при атаке".formatted(1 + level / 2)
                ),
                new CostData(CostProcessType.MANA_RESERVE_PERCENT, 20));

        this.applyEvent(AspectHandEvent.class, event -> {
            event.getAttributes().find(ImperiumAttribute.MIN_FIRE_DAMAGE).applyIncrease((1 + event.getLevel() / 2D) / 100D);
            event.getAttributes().find(ImperiumAttribute.MAX_FIRE_DAMAGE).applyIncrease((1 + event.getLevel() / 2D) / 100D);
            event.getAttributes().find(ImperiumAttribute.FIRE_RESISTANCE_PENETRATION).applyBaseValue(10 + event.getLevel());
        });
    }

}
