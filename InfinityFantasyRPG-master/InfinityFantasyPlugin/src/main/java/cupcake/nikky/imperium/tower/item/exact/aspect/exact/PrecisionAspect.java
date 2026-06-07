package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectHandEvent;

import java.util.Arrays;

public class PrecisionAspect extends AspectBase {

    public PrecisionAspect() {
        super("Аспект: Точность", AspectType.ARMOR, AspectCategory.ATTACK,
                level -> Arrays.asList(
                        "%.1f%% увеличение шанса крит. удара".formatted(5 + level * 1.3D),
                        "+%d%% к Вашей точности".formatted(5 + level / 2)
                ),
                new CostData(CostProcessType.MANA_RESERVE_VALUE, level -> 20 + level * 2));

        this.applyEvent(AspectHandEvent.class, event -> {
            event.getAttributes().find(ImperiumAttribute.CRITICAL_RATE).applyIncrease((5 + event.getLevel() * 1.3D) / 100D);
            event.getAttributes().find(ImperiumAttribute.ACCURACY).applyAdditional(5 + event.getLevel() / 2D);
        });
    }

}
