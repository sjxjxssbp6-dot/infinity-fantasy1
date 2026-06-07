package cupcake.nikky.imperium.tower.entity.monster.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.OutboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.knockback.KnockBackApplier;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LichDisciple extends FantasyMonster {

    private static final KnockBackApplier knockBackApplier = new KnockBackApplier(.3);

    public LichDisciple(@NotNull final Location spawnLocation, @NotNull final MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.DISCIPLE_OF_LICH,
                new ItemStack[]{null, null, null, new ItemStack(Material.DIAMOND_HELMET)}, new ItemStack(Material.GOLD_SWORD), null);

        this.appendCast(new OutboundDamageCast(this) {
            @Override
            public boolean canApply() {
                return MathUtility.randomInRange(1, 100) <= 50;
            }

            @Override
            public void apply(LivingEntity entity) {
                Fantasy.buffService().appendBuff(entity.getUniqueId(), BuffType.BURN, 35, 2);
            }
        });
    }

    @Override
    public double onOutboundDamage(@NotNull LivingEntity entity, double damage) {
        var spigot = getSpigot();
        if (spigot == null) return super.onOutboundDamage(entity, damage);

        knockBackApplier.apply(spigot.getLocation().getDirection(), entity);
        return super.onOutboundDamage(entity, damage);
    }
}
