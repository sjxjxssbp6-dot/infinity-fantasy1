package cupcake.nikky.imperium.tower.item.exact.aspect.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageType;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.attribute.AttributeInstance;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectBase;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectCategory;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectType;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostProcessType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectUseEvent;
import cupcake.nikky.imperium.tower.utility.EntityUtility;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Sound;

import java.util.Arrays;

public class SparksAspect extends AspectBase {

    public SparksAspect() {
        super("Аспект: Искры", AspectType.WEAPON, AspectCategory.ATTACK,
                level -> Arrays.asList(
                        "При использовании создает заклинание, которое",
                        "в радиусе %.0f блоков вокруг вызывающего".formatted(4 + level / 5D),
                        "наносит %d-%d урона огнем".formatted(40 + level * 40 + level * 5, 50 + level * 50 + level * 30),
                        "25% шанс поджечь противников",
                        "Перезарядка: 3 секунд"),
                new CostData(CostProcessType.DEFAULT_MANA, level -> 14 + level));

        this.applyEvent(AspectUseEvent.class, event -> {
            val player = event.getUser().getSpigot();
            val radius = 4 + event.getLevel() / 5D;
            val monsters = EntityUtility.findInRadius(player.getLocation(), radius);
            val level = event.getLevel();
            val damage = MathUtility.randomInRange(40 + level * 40 + level * 5, 50 + level * 50 + level * 30);

            Fantasy.particleProvider().sendSparks(player, radius, player.getEntityId());

            Fantasy.getAttackExecutor().execute(() -> monsters.stream().filter(livingEntity -> !livingEntity.getUniqueId().equals(player.getUniqueId())).forEach(entity ->
                    Fantasy.damageProvider().processDamage(entity).setDamage(InfluenceType.FIRE, damage)
                    .setAttribute(ImperiumAttribute.CHANCE_TO_IGNITE_ON_HIT, new AttributeInstance(25D))
                    .setDamageType(DamageType.SPELL)
                    .setAttackInfluence(player)
                    .apply())
            );

            Fantasy.bukkitProvider().sound(player, Sound.ENTITY_BLAZE_SHOOT);
            event.setCooldown(3 * 20);
        });
    }

}
