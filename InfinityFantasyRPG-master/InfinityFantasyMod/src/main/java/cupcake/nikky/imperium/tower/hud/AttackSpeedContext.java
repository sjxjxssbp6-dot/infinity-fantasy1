package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.progress.FantasyProgressBar;
import lombok.val;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.time.Instant;

public class AttackSpeedContext extends FantasyProgressBar {

    public AttackSpeedContext() {
        super(0, 0, Palette.WHITE.alpha(.37));
        this.initialize();
    }

    public void initialize() {
        this.setSize(new V3(30, 5)).setPos(new V3(0, 12.5));
        this.setOrigin(Relative.TOP).setAlign(Relative.CENTER);
        this.setCarveSize(1D);

        this.setProgressInstant(1D);

        this.setEnabled(false);

        this.onPreTransform(element -> {
            if (getRenderProgress() == 0D) this.setEnabled(false);
        });
    }

    public void enable(final int duration) {
        this.setEnabled(true);
        this.setProgressInstant(1D);

        val currentMillis = Instant.now().toEpochMilli();
        this.setTimer(currentMillis, currentMillis + duration);
    }

    public void disable() {
        this.setEnabled(false);
        this.setProgressInstant(1D);
        this.setProgress(1D);
    }

}
