package cupcake.nikky.imperium.tower.incubator;

import java.util.concurrent.atomic.AtomicInteger;

public class IncubatorData {

    private final AtomicInteger currentDeaths = new AtomicInteger(0);

    public void setCurrentDeaths(final int newValue) {
        for (;;) {
            var oldValue = getCurrent();
            if (currentDeaths.compareAndSet(oldValue, newValue)) break;
        }
    }

    public int increment() {
        return this.currentDeaths.incrementAndGet();
    }

    public int getCurrent() {
        return currentDeaths.get();
    }

}
