package cupcake.nikky.imperium.tower.block;

import cupcake.nikky.imperium.tower.Fantasy;

public class RegenerationRunnable implements Runnable {

    public static final int TICK = 20;

    @Override
    public void run() {
        Fantasy.regenerationProvider().findAll().forEach(data -> {
            data.increaseTick(TICK);

            if (data.isDone()) {
                Fantasy.regenerationProvider().unregister(data.getBlock());
            }
        });
    }

}
