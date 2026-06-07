package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectUseEvent;
import lombok.val;

import java.util.Arrays;

public class JumpAspect extends AspectBase {

    public JumpAspect() {
        super("Аспект: Прыжок", AspectType.WEAPON, AspectCategory.MOBILITY,
                level -> Arrays.asList(
                        "При использовании Вы прыгаете в направлении взгляда",
                        "Сила прыжка: %.1f".formatted(1 + .05 * level),
                        "Перезарядка: 2 секунды"),
                new CostData(CostProcessType.DEFAULT_MANA, 10));

        this.applyEvent(AspectUseEvent.class, event -> {
            val player = event.getUser().getSpigot();

            if (player.getFallDistance() >= 2F) event.getUser().getJumpData().setFallDistance(player.getFallDistance());
            else event.getUser().getJumpData().setFallDistance(-1F);

            player.setVelocity(player.getLocation().getDirection().multiply(1 + .05 * event.getLevel()));
            event.setCooldown(2 * 20);
        }, aspectEvent -> !aspectEvent.getUser().getSpigot().isInsideVehicle());
    }

}
