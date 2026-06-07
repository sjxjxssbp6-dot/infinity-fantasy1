package cupcake.nikky.imperium.tower.entity.monster.exact;

import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.knockback.KnockBackApplier;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Lich extends FantasyMonster {

    private static final KnockBackApplier knockBackApplier = new KnockBackApplier(.3);

    public Lich(@NotNull final Location spawnLocation, @NotNull final MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.LICH, new ItemStack[]{null, null, null, new ItemStack(Material.GOLD_HELMET)}, new ItemStack(Material.STONE_SWORD), null);
    }

    @Override
    public double onInboundDamage(@NotNull LivingEntity entity, double damage) {
        knockBackApplier.apply(getSpigot().getLocation().getDirection().multiply(new Vector(0, 1.5, 0)), entity);
        return super.onInboundDamage(entity, damage);
    }

}
