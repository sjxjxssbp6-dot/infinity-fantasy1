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

public class CanrianEndermite extends FantasyMonster {

    public CanrianEndermite(@NotNull Location spawnLocation, @NotNull MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.CANRIAN_ENDERMITE);

        this.appendCast(new OutboundDamageCast(this) {
            @Override
            public boolean canApply() {
                return MathUtility.randomInRange(1, 100) <= 50;
            }

            @Override
            public void apply(LivingEntity entity) {
                Fantasy.buffService().appendBuff(entity.getUniqueId(), BuffType.ATTACK_SPEED_DEBUFF);
            }
        });
    }

}
