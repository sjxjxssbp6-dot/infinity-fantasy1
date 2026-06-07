package cupcake.nikky.imperium.tower.entity.monster.exact;

import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Alp extends FantasyMonster {

    public Alp(@NotNull final Location spawnLocation, @NotNull final MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.ALP, new ItemStack[]{null, null, new ItemStack(Material.LEATHER_CHESTPLATE), null}, null, null);
    }

    @Override
    public double onOutboundDamage(@NotNull LivingEntity entity, double damage) {
        val heal = damage / 100D * 7.5D;

        this.changeHealth(heal);

        return super.onOutboundDamage(entity, damage);
    }

    @Override
    public void onSpawn() {
        super.onSpawn();
        ((Zombie) getSpigot()).setBaby(false);
    }

}
