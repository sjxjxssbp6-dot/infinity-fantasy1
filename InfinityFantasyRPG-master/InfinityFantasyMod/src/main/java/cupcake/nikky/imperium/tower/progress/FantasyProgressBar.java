package cupcake.nikky.imperium.tower.progress;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.event.FantasyTooltipRender;
import dev.xdark.clientapi.event.render.TooltipRender;
import lombok.val;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.ProgressBar;
import ru.cristalix.enginex.util.Color;
import ru.cristalix.enginex.util.MathUtil;

import java.time.Instant;
import java.util.ArrayList;

public class FantasyProgressBar extends ProgressBar {

    public FantasyProgressBar() {
    }

    public FantasyProgressBar(double progress) {
        super(progress);
    }

    public FantasyProgressBar(double progress, double inset) {
        super(progress, inset);
    }

    public FantasyProgressBar(double progress, double inset, Color color) {
        super(progress, inset, color);
    }

    @Override
    public void preRender(double partialTicks, double mouseX, double mouseY) {
        if (this.preRenderListeners != null) {
            this.preRenderListeners.forEach(imageConsumer -> imageConsumer.accept(this.genericThis));
        }

        if (this.tooltipData != null && this.isHover()) {
            val event = TooltipRender.BUS.fire(new FantasyTooltipRender(new ArrayList<>(this.tooltipData.getLines())));
            if (!event.isCancelled()) {
                FantasyMod.customTooltip().render(event.getContent());
            }
        }

        long currentMillis = Instant.now().toEpochMilli();
        double newProgress;
        if (currentMillis < this.endTime) {
            newProgress = (double)this.endTime - (double)this.startTime;
            double dif2 = (double)this.endTime - (double)currentMillis;
            if (dif2 <= 50.0) {
                dif2 = 0.0;
            }

            this.setProgress(dif2 / newProgress);
            int seconds = (int)(dif2 / 1000.0);
            if (this.autoTextOnTimer) {
                this.text.setValue(Enginex.formatSeconds(seconds));
            }

            this.onTimerTickListeners.forEach(consumer -> consumer.accept(this, seconds));
        } else if (this.timer) {
            this.timer = false;
            this.setProgressInstant(0.0);
        }

        newProgress = MathUtil.lerp(this.progress, this.needProgress, this.changeSpeed * (double)Enginex.getPartialTicks());
        if (newProgress != this.progress) {
            this.internalSetProgress0(newProgress);
        }
    }

    private void internalSetProgress0(double progress) {
        this.progress = Math.min(Math.max(progress, 0.0), 1.0);
        this.updateSize();
        this.onChangeProgressListeners.forEach(progressBarDoubleBiConsumer -> progressBarDoubleBiConsumer.accept(this, this.progress));
    }

}
