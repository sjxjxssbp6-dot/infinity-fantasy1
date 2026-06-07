package cupcake.nikky.imperium.tower.entity.monster.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.OutboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class IronSkeleton extends FantasyMonster {

    public IronSkeleton(@NotNull Location spawnLocation, @NotNull MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.IRON_SKELETON);

        this.appendCast(new OutboundDamageCast(this) {
            @Override
            public boolean canApply() {
                return MathUtility.randomInRange(1, 100) <= 20;
            }

            @Override
            public void apply(LivingEntity entity) {
                Fantasy.buffService().appendBuff(entity.getUniqueId(), BuffType.IMMOBILITY, 0, 1);
            }
        });
    }

}
