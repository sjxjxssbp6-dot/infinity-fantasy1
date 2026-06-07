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

public class IceExplosionAspect extends AspectBase {

    public IceExplosionAspect() {
        super("Аспект: Ледяной взрыв", AspectType.WEAPON, AspectCategory.ATTACK,
                level -> Arrays.asList(
                        "При использовании создает заклинание, которое",
                        "в радиусе %.1f блоков вокруг вызывающего".formatted(4 + .16 * level),
                        "наносит %d-%d урона холодом".formatted(60 + level * 60 + level * 10, 80 + level * 80 + level * 45),
                        "25% шанс заморозить противников",
                        "Перезарядка: 6 секунд"),
                new CostData(CostProcessType.DEFAULT_MANA, level -> 14 + level));

        this.applyEvent(AspectUseEvent.class, event -> {
            val player = event.getUser().getSpigot();
            val radius = 4 + .16 * event.getLevel();
            val monsters = EntityUtility.findInRadius(player.getLocation(), radius);
            val level = event.getLevel();
            val damage = MathUtility.randomInRange(60 + level * 60 + level * 10, 80 + level * 80 + level * 45);

            Fantasy.particleProvider().sendIceExplosion(player, radius, player.getEntityId());

            Fantasy.getAttackExecutor().execute(() -> {
                monsters.stream().filter(livingEntity -> !livingEntity.getUniqueId().equals(player.getUniqueId())).forEach(entity ->
                        Fantasy.damageProvider().processDamage(entity)
                                .setAttribute(ImperiumAttribute.CHANCE_TO_FREEZE_ON_HIT, new AttributeInstance(25D))
                                .setDamage(InfluenceType.COLD, damage)
                                .setDamageType(DamageType.SPELL)
                                .setAttackInfluence(player)
                                .apply()
                );
            });

            Fantasy.bukkitProvider().sound(player, Sound.BLOCK_GLASS_BREAK);
            event.setCooldown(6 * 20);
        });
    }

}
