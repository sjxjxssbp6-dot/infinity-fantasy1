package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectMonsterKillEvent;

import java.util.Arrays;

public class WindKillAspect extends AspectBase {

    public WindKillAspect() {
        super("Аспект: Ускорение ликвидации", AspectType.ALL, AspectCategory.ATTACK,
                level -> Arrays.asList(
                        "При убийстве монстра Вы получаете",
                        "%d%% увеличение Скорости атаки".formatted(15 + level),
                        "и +%d%% к Скорости передвижения".formatted(15 + level),
                        "в течение 3 секунд"),
                new CostData(CostProcessType.DEFAULT_MANA, 15));

        this.applyEvent(AspectMonsterKillEvent.class, event -> {
            Fantasy.buffService().appendBuff(event.getUser().getUniqueId(), BuffType.WIND_KILL, 15 + event.getLevel(), 3);
        }, aspectEvent -> Fantasy.buffService().findById(aspectEvent.getUser().getUniqueId()).map(buffedEntity -> buffedEntity.find(BuffType.WIND_KILL).isEmpty()).orElse(true));
    }

}
