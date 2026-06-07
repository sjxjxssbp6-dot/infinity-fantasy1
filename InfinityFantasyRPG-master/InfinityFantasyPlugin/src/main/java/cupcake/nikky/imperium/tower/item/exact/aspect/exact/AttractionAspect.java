package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.entity.monster.modifier.ModifierCollection;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectUseEvent;
import cupcake.nikky.imperium.tower.knockback.KnockBackApplier;
import cupcake.nikky.imperium.tower.utility.EntityUtility;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;

import java.util.Arrays;

public class AttractionAspect extends AspectBase {

    public AttractionAspect() {
        super("Аспект: Притягивание", AspectType.WEAPON, AspectCategory.ATTACK,
                level -> Arrays.asList(
                        "При использовании Вы притягиваете к себе",
                        "врагов в радиусе %.0f блоков".formatted(6 + level / 4D),
                        "&cНе действует на монстров редкости &6Уникальный",
                        "Перезарядка: %.1f %s".formatted(5 - level * .15, TextUtility.transformViaAmount((int) (5 - level * .15), "секунда", "секунды", "секунд"))),
                new CostData(CostProcessType.DEFAULT_MANA, level -> 20 + level));

        this.applyEvent(AspectUseEvent.class, event -> {
            val player = event.getUser().getSpigot();
            val playerVector = player.getLocation().toVector();
            val knockBackApplier = new KnockBackApplier(.3);

            val radius = 6F + event.getLevel() / 4D;

            EntityUtility.findInRadius(player.getLocation(), radius)
                    .stream()
                    .filter(livingEntity ->
                            Fantasy.monsterProvider().findByUUID(livingEntity.getUniqueId())
                                    .filter(monster -> monster.getModifierEngine().getCurrentModifiers() != ModifierCollection.UNIQUE)
                                    .isPresent())
                    .forEach(entity -> {
                        val vector = entity.getLocation().toVector().subtract(playerVector);
                        knockBackApplier.apply(vector.setY(Math.min(vector.getY(), .75)), entity);
                        Fantasy.monsterProvider().findByUUID(entity.getUniqueId()).ifPresent(monster -> monster.getTargetTracker().goalTarget(player));
                    });
            event.setCooldown((int) Math.round(20 * (5 - event.getLevel() * .15)));
        });
    }

}
