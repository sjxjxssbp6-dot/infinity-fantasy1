package cupcake.nikky.imperium.tower.buff.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.ailment.AilmentType;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Freeze extends FantasyBuff {

    private final AtomicReference<LivingEntity> influence = new AtomicReference<>();

    public Freeze(final double damage, final int duration) {
        super(duration, "Заморозка");
        this.setShouldInvalidateAfterDeath(true);
        this.registerMultiplierAttribute(ImperiumAttribute.ATTACK_SPEED, -.5);
        this.registerMultiplierAttribute(ImperiumAttribute.MOVEMENT_SPEED, -.5);
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList(TextUtility.colorize("&6Уменьшает скорость передвижения и скорость атаки в два раза"));
    }

    @Override
    public void onTick(@NotNull LivingEntity entity) {
        super.onTick(entity);

        var influenceEntity = influence.get();
        var owner = influenceEntity instanceof Player player ? player : entity instanceof Player player2 ? player2 : null;
        if (owner != null)
            Fantasy.particleProvider().sendAilment(owner, entity.getEntityId(), AilmentType.FREEZE, Instant.now().toEpochMilli() + (getDurationSeconds() - getCurrentSeconds()) * 1000L);
    }

    @Override
    public void onCompletion(LivingEntity entity) {
        super.onCompletion(entity);
        var influenceEntity = influence.get();
        var owner = influenceEntity instanceof Player player ? player : entity instanceof Player player2 ? player2 : null;
        if (owner != null) Fantasy.particleProvider().sendAilment(owner, entity.getEntityId(), AilmentType.FREEZE, 0);
    }

    public void setInfluence(LivingEntity influence) {
        for (; ; ) {
            var old = this.influence.get();
            if (this.influence.compareAndSet(old, influence)) break;
        }
    }
}
