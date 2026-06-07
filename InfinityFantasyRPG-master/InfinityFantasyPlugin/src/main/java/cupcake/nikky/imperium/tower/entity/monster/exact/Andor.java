package cupcake.nikky.imperium.tower.entity.monster.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.OutboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class Andor extends FantasyMonster {

    public Andor(@NotNull final Location spawnLocation, @NotNull final MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.ANDOR);

        this.appendCast(new OutboundDamageCast(this) {
            @Override
            public boolean canApply() {
                return true;
            }

            @Override
            public void apply(@NotNull final LivingEntity entity) {
                Fantasy.buffService().appendBuff(entity.getUniqueId(), BuffType.SLOWNESS);
            }
        });
    }

}
