package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectHandEvent;

import java.util.List;

public class ColdPenetrationAspect extends AspectBase {

    public ColdPenetrationAspect() {
        super("Аспект: Пробивание холода", AspectType.ALL, AspectCategory.ATTACK,
                level -> List.of(
                        "Вы пробиваете %d%% сопротивления холоду".formatted(10 + level),
                        "%d%% увеличение урона холодом при атаке".formatted(1 + level / 2)
                ),
                new CostData(CostProcessType.MANA_RESERVE_PERCENT, 20));

        this.applyEvent(AspectHandEvent.class, event -> {
            event.getAttributes().find(ImperiumAttribute.MIN_COLD_DAMAGE).applyIncrease((1 + event.getLevel() / 2D) / 100D);
            event.getAttributes().find(ImperiumAttribute.MAX_COLD_DAMAGE).applyIncrease((1 + event.getLevel() / 2D) / 100D);
            event.getAttributes().find(ImperiumAttribute.COLD_RESISTANCE_PENETRATION).applyBaseValue(10 + event.getLevel());
        });
    }

}
