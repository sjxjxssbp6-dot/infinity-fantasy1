package cupcake.nikky.imperium.tower.entity.monster.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.OutboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class ElementalSkeleton extends FantasyMonster {

    public ElementalSkeleton(@NotNull Location spawnLocation, @NotNull MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.ELEMENTAL_SKELETON);

        this.appendCast(new OutboundDamageCast(this) {
            @Override
            public boolean canApply() {
                return MathUtility.randomInRange(1, 100) <= 30;
            }

            @Override
            public void apply(LivingEntity entity) {
                Fantasy.runLater(() -> {
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 10));
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 6, 10));
                });
            }
        });
    }

}
