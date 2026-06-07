package cupcake.nikky.imperium.tower.buff.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.ailment.AilmentType;
import lombok.Getter;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Shock extends FantasyBuff {

    @Getter private final double increaseDamageInfo;
    private final AtomicReference<LivingEntity> influence = new AtomicReference<>();

    public Shock(final double increaseDamageInfo, final int duration) {
        super(duration, "Шок");
        this.increaseDamageInfo = increaseDamageInfo;
        this.setShouldInvalidateAfterDeath(true);
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList(TextUtility.colorize("&6Получаемый урон увеличен на &3%.0f%%".formatted(increaseDamageInfo)));
    }

    @Override
    public void onTick(@NotNull LivingEntity entity) {
        super.onTick(entity);

        var influenceEntity = influence.get();
        var owner = influenceEntity instanceof Player player ? player : entity instanceof Player player2 ? player2 : null;
        if (owner != null)
            Fantasy.particleProvider().sendAilment(owner, entity.getEntityId(), AilmentType.SHOCK, Instant.now().toEpochMilli() + (getDurationSeconds() - getCurrentSeconds()) * 1000L);
    }

    @Override
    public void onCompletion(LivingEntity entity) {
        super.onCompletion(entity);
        var influenceEntity = influence.get();
        var owner = influenceEntity instanceof Player player ? player : entity instanceof Player player2 ? player2 : null;
        if (owner != null) Fantasy.particleProvider().sendAilment(owner, entity.getEntityId(), AilmentType.SHOCK, 0);
    }

    public void setInfluence(LivingEntity influence) {
        for (;;) {
            var old = this.influence.get();
            if (this.influence.compareAndSet(old, influence)) break;
        }
    }

}
