package cupcake.nikky.imperium.tower.entity.monster.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.OutboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class ShadowOcelot extends FantasyMonster {

    public ShadowOcelot(@NotNull final Location spawnLocation, @NotNull final MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.SHADOW_OCELOT);

        this.appendCast(new OutboundDamageCast(this) {
            @Override
            public boolean canApply() {
                return true;
            }

            @Override
            public void apply(LivingEntity entity) {
                Fantasy.runLater(() -> entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 4)));
            }
        });
    }

}
