package cupcake.nikky.imperium.tower.buff.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageType;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.ailment.AilmentType;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Ignite extends FantasyBuff {

    private final double damage;
    private final AtomicReference<LivingEntity> influence = new AtomicReference<>();

    public Ignite(final double damage, final int duration) {
        super(duration, "Поджог");
        this.damage = damage;
        this.setShouldInvalidateAfterDeath(true);
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList(TextUtility.colorize("&6Периодический урон в &3%.0fед.".formatted(damage)));
    }

    @Override
    public void onTick(@NotNull LivingEntity entity) {
        super.onTick(entity);

        var influenceEntity = influence.get();
        var owner = influenceEntity instanceof Player player ? player : entity instanceof Player player2 ? player2 : null;
        if (owner != null)
            Fantasy.particleProvider().sendAilment(owner, entity.getEntityId(), AilmentType.IGNITE, Instant.now().toEpochMilli() + (getDurationSeconds() - getCurrentSeconds()) * 1000L);

        Fantasy.damageProvider().processDamage(null, entity)
                .setAttackInfluence(influenceEntity)
                .setDamage(InfluenceType.FIRE, damage * getMultiplier())
                .setDamageType(DamageType.OVER_TIME)
                .apply();
    }

    @Override
    public void onCompletion(LivingEntity entity) {
        super.onCompletion(entity);
        var influenceEntity = influence.get();
        var owner = influenceEntity instanceof Player player ? player : entity instanceof Player player2 ? player2 : null;
        if (owner != null) Fantasy.particleProvider().sendAilment(owner, entity.getEntityId(), AilmentType.IGNITE, 0);
    }

    public void setInfluence(LivingEntity influence) {
        for (;;) {
            var old = this.influence.get();
            if (this.influence.compareAndSet(old, influence)) break;
        }
    }

}
