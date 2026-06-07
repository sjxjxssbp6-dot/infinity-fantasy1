package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectHandEvent;

import java.util.List;

public class LightningAuraAspect extends AspectBase {

    public LightningAuraAspect() {
        super("Аспект: Аура молнии", AspectType.ARMOR, AspectCategory.DEFENSE,
                level -> List.of(
                        "%d%% увеличение Вашего сопротивления молнии".formatted(25 + level),
                        "%d%% увеличение Вашего урона молнией при атаке".formatted(20 + level),
                        "На %d%% меньше получаемого отраженного элементального урона".formatted(1 + level / 2)
                ),
                new CostData(CostProcessType.MANA_RESERVE_PERCENT, 30));

        this.applyEvent(AspectHandEvent.class, event -> {
            event.getAttributes().find(ImperiumAttribute.LIGHTNING_RESISTANCE).applyIncrease((25 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).applyIncrease((20 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).applyIncrease((20 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.FRAGMENT_REFLECT_ELEMENTAL_DAMAGE).applyMore(1 - (1 + event.getLevel() / 2D) / 100D);
        });
    }

}
