package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.progress.FantasyProgressBar;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.animation.Animation;
import ru.cristalix.enginex.renderer.contexts.Overlay;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.concurrent.TimeUnit;

public class TeleportIndicator extends FantasyProgressBar {

    private final long millis;
    private static final String TELEPORT_TEXT = "Не двигайтесь во время перемещения";
    private static final String CANCEL_TEXT = "Перемещение отменено";

    public TeleportIndicator(@NotNull final FantasyMod fantasyMod, final long millis) {
        this.millis = millis;

        this.setText(TELEPORT_TEXT).setCarveSize(2D).setSize(new V3(364, 12))
                .setOrigin(Relative.BOTTOM)
                .setAlign(Relative.BOTTOM)
                .setPos(new V3(0, -44D - 130D - 6D));
        this.getBar().setColor(Palette.ORANGE);
        this.getText().setShadow(true);

        this.onPreTick(element -> {
            if (fantasyMod.getHotBarContext().getAdvancedHotBarBox().isExist())
                this.setPos(new V3(0, -44D - 130D - 6D));
            else this.setPos(new V3(0, -44D - 104D - 6D));
        });

        this.getText().setOrigin(Relative.BOTTOM).setAlign(Relative.TOP).setPos(new V3(0, -8));
    }

    public void start(@NotNull final FantasyMod mod) {
        mod.setCurrentIndicator(this);

        Overlay.get().addChild(this);
        this.setProgressInstant(1D).setTimer(millis);
    }

    public void stop(final long millis) {
        this.setTimer(0L);
        this.timer = false;

        this.setText(CANCEL_TEXT).setProgressInstant(1D);
        this.getBar().setColor(Palette.RED);

        Enginex.getClientApi().syncExecutorService().schedule(() -> {
            Animation.play(this, "hide", .4D, (element, aDouble) -> this.setAlpha(1 - aDouble))
                    .onComplete(element -> Overlay.get().removeChild(this));
        }, millis, TimeUnit.MILLISECONDS);
    }

}
