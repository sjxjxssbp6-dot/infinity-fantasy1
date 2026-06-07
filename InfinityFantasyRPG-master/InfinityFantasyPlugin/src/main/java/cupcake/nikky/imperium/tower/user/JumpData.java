package cupcake.nikky.imperium.tower.user;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class JumpData {

    private final AtomicDouble fallDistance = new AtomicDouble();
    private final AtomicInteger tick = new AtomicInteger();

    public void setFallDistance(float fallDistance) {
        this.fallDistance.set(fallDistance);
    }

    public float getFallDistance() {
        return fallDistance.floatValue();
    }

    public int getTick() {
        return tick.get();
    }

    public void increaseTick() {
        for (;;) {
            val oldFallDistance = this.getFallDistance();
            val oldTick = this.getTick();

            if (oldFallDistance >= 0F || oldFallDistance < -1F) break;

            if (oldFallDistance == -1F) {
                if (!this.tick.compareAndSet(oldTick, oldTick + 1)) continue;

                if (oldTick * FantasyUser.ASYNC_TICK >= 100) {
                    this.tick.set(0);
                    cancel();
                }

                break;
            } else if (oldTick > 0) {
                if (this.tick.compareAndSet(oldTick, 0)) break;
            }
        }
    }

    public void cancel() {
        this.tick.set(0);
        this.fallDistance.set(0);
    }

}
