package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectHandEvent;

import java.util.List;

public class ArmourAuraAspect extends AspectBase {

    public ArmourAuraAspect() {
        super("Аспект: Аура защиты", AspectType.ARMOR, AspectCategory.DEFENSE,
                level -> List.of(
                        "%.1f%% увеличение Вашей защиты".formatted(15 + level * 1.5D),
                        "%d%% увеличение Вашего физического урона при атаке".formatted(20 + level),
                        "На %d%% меньше получаемого отраженного физического урона".formatted(5 + level)
                ),
                new CostData(CostProcessType.MANA_RESERVE_PERCENT, 30));

        this.applyEvent(AspectHandEvent.class, event -> {
            event.getAttributes().find(ImperiumAttribute.DEFENSE).applyIncrease((15 + event.getLevel() * 1.5D) / 100D);
            event.getAttributes().find(ImperiumAttribute.MIN_PHYSICAL_DAMAGE).applyIncrease((20 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.MAX_PHYSICAL_DAMAGE).applyIncrease((20 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.FRAGMENT_REFLECT_PHYSICAL_DAMAGE).applyMore(1 - (5 + event.getLevel()) / 100D);
        });
    }

}
