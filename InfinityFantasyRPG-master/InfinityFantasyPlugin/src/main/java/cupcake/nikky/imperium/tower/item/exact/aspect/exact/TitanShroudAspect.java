package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectUseEvent;
import me.anfanik.steda.api.utility.TextUtility;

import java.util.Arrays;

public class TitanShroudAspect extends AspectBase {

    public TitanShroudAspect() {
        super("Аспект: Покров титана", AspectType.WEAPON, AspectCategory.ATTACK,
                level -> Arrays.asList(
                        "При использовании Вы получаете усиление",
                        "'Покров титана', которое уменьшает входящий",
                        "урон на %d%% в течение %d секунд".formatted(10 + level * 2, 7 + level / 6),
                        String.format("Перезарядка: %d %s", 31 - level / 4, TextUtility.transformViaAmount(31 - level / 4, "секунда", "секунды", "секунд"))),
                new CostData(CostProcessType.DEFAULT_MANA, level -> 40 + level));

        this.applyEvent(AspectUseEvent.class, event -> {
            Fantasy.buffService().appendBuff(event.getUser().getUniqueId(), BuffType.TITAN_SHROUD, 10D + event.getLevel() * 2D, 7 + event.getLevel() / 6);
            event.setCooldown(31 * 20 - event.getLevel() * 20 / 4);
        });
    }

}
