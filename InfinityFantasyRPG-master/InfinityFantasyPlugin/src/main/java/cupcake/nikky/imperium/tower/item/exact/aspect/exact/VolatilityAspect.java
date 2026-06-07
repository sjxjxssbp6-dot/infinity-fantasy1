package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectHandEvent;

import java.util.List;

public class VolatilityAspect extends AspectBase {

    public VolatilityAspect() {
        super("Аспект: Непостоянство", AspectType.ALL, AspectCategory.ATTACK,
                level -> List.of(
                        "Вы наносите на %d%% больше максимального урона".formatted(30 + level),
                        "Вы наносите на %d%% меньше минимального урона".formatted(50 + level)
                ),
                new CostData(CostProcessType.MANA_RESERVE_PERCENT, 25));

        this.applyEvent(AspectHandEvent.class, event -> {
            event.getAttributes().find(ImperiumAttribute.MAX_PHYSICAL_DAMAGE).applyMore(1 + (30 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.MAX_FIRE_DAMAGE).applyMore(1 + (30 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.MAX_COLD_DAMAGE).applyMore(1 + (30 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).applyMore(1 + (30 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.MAX_CHAOS_DAMAGE).applyMore(1 + (30 + event.getLevel()) / 100D);

            event.getAttributes().find(ImperiumAttribute.MIN_PHYSICAL_DAMAGE).applyMore(1 - (50 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.MIN_FIRE_DAMAGE).applyMore(1 - (50 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.MIN_COLD_DAMAGE).applyMore(1 - (50 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).applyMore(1 - (50 + event.getLevel()) / 100D);
            event.getAttributes().find(ImperiumAttribute.MIN_CHAOS_DAMAGE).applyMore(1 - (50 + event.getLevel()) / 100D);
        });
    }

}