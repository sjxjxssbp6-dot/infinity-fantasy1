package cupcake.nikky.imperium.tower.buff.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageType;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Bleeding extends FantasyBuff {

    private final double damage;
    private final AtomicReference<LivingEntity> influence = new AtomicReference<>();

    public Bleeding(final int durationSeconds, final double physDamage) {
        super(durationSeconds, "Кровотечение");

        this.damage = physDamage;

        this.setShouldInvalidateAfterDeath(true);
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList(TextUtility.colorize("&6Периодический урон в &3%.0fед.", damage));
    }

    @Override
    public void onTick(@NotNull final LivingEntity entity) {
        super.onTick(entity);

        Fantasy.damageProvider().processDamage(null, entity).setDamage(InfluenceType.PHYSICAL, damage * getMultiplier())
                .setAttackInfluence(influence.get())
                .setDamageType(DamageType.OVER_TIME)
                .apply();
    }

    public void setInfluence(LivingEntity influence) {
        for (;;) {
            var old = this.influence.get();
            if (this.influence.compareAndSet(old, influence)) break;
        }
    }

}
