package cupcake.nikky.imperium.tower.entity.monster.battle;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class EntityBattleData {

    @Getter private final UUID userUuid;
    private final AtomicDouble damage = new AtomicDouble(0);
    private final AtomicLong startBattle;
    private final AtomicLong lastReceiveDamage;

    public EntityBattleData(@NotNull final UUID userUuid, final double damage) {
        this.userUuid = userUuid;
        this.changeDamage(damage);
        this.startBattle = new AtomicLong(Instant.now().toEpochMilli());
        this.lastReceiveDamage = new AtomicLong(Instant.now().toEpochMilli());
    }

    public double getDamagePerSecond() {
        var duration = Math.max((double) TimeUnit.MILLISECONDS.toSeconds(Instant.now().toEpochMilli() - getStartBattle()), 1D);
        return getDamage() / duration;
    }

    public void changeDamage(final double delta) {
        for (;;) {
            var old = damage.get();
            if (damage.compareAndSet(old, old + delta)) break;
        }
    }

    public void setLastReceiveDamage(final long lastReceiveDamage) {
        for (;;) {
            var old = this.lastReceiveDamage.get();
            if (this.lastReceiveDamage.compareAndSet(old, lastReceiveDamage)) break;
        }
    }

    public double getDamage() {
        return damage.get();
    }

    public long getStartBattle() {
        return startBattle.get();
    }

    public long getLastReceiveDamage() {
        return lastReceiveDamage.get();
    }
}
