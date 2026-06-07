package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectHandEvent;

import java.util.List;

public class FasterAttackAspect extends AspectBase {

    public FasterAttackAspect() {
        super("Аспект: Быстрая атака", AspectType.ALL, AspectCategory.ATTACK,
                level -> List.of(
                        "%d%% увеличение скорости атаки".formatted(20 + level)
                ),
                new CostData(CostProcessType.MANA_RESERVE_PERCENT, 10));

        this.applyEvent(AspectHandEvent.class, event -> {
            event.getAttributes().find(ImperiumAttribute.ATTACK_SPEED).applyIncrease((20 + event.getLevel()) / 100D);
        });
    }

}
