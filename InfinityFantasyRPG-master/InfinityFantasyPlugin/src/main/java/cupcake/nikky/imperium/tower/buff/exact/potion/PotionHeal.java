package cupcake.nikky.imperium.tower.buff.exact.potion;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.exact.regeneration.RegenerationInTickBuff;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class PotionHeal extends RegenerationInTickBuff {

    public PotionHeal(final double percent) {
        super(7, percent);
        this.setShouldInvalidateAfterDeath(true);
    }

    @Override
    public void onReceive(LivingEntity entity) {
        super.onReceive(entity);

        if (entity instanceof Player player) Fantasy.particleProvider().sendHeal(player, entity.getEntityId());
    }
}
