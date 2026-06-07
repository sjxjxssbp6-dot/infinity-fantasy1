package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectHandEvent;

import java.util.Collections;

public class ChaosDamageAspect extends AspectBase {

    public ChaosDamageAspect() {
        super("Аспект: Хаос урон", AspectType.ALL, AspectCategory.ATTACK,
                level -> Collections.singletonList(
                        "Добавляет %d-%d урона хаосом".formatted(10 + level * 8, 16 + level * 10)
                ),
                new CostData(CostProcessType.MANA_RESERVE_PERCENT, 10));

        this.applyEvent(AspectHandEvent.class, event -> {
            event.getAttributes().find(ImperiumAttribute.MIN_CHAOS_DAMAGE).applyBaseValue(10 + event.getLevel() * 8);
            event.getAttributes().find(ImperiumAttribute.MAX_CHAOS_DAMAGE).applyBaseValue(16 + event.getLevel() * 10);
        });
    }

}
