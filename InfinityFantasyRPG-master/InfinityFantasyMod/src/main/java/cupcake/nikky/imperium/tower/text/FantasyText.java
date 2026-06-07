package cupcake.nikky.imperium.tower.text;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.event.FantasyTooltipRender;
import dev.xdark.clientapi.event.render.TooltipRender;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.enginex.element.Text;
import ru.cristalix.enginex.util.Color;

import java.util.ArrayList;

public class FantasyText extends Text {

    public FantasyText() {
    }

    public FantasyText(String text) {
        super(text);
    }

    public FantasyText(String... lines) {
        super(lines);
    }

    public FantasyText(final double lineHeight, @Nullable final String line) {
        this(lineHeight, line == null ? new String[]{"null"} : line.split("\n"));
    }

    public FantasyText(final double lineHeight, @NotNull final String... lines) {
        this.lineHeight = lineHeight;
        setColor(new Color(1d, 1d, 1d, 1d));
        setValue(lines);
    }

    @Override
    protected void preRender(double partialTicks, double mouseX, double mouseY) {
        if (this.preRenderListeners != null) {
            this.preRenderListeners.forEach(imageConsumer -> imageConsumer.accept(this.genericThis));
        }

        if (this.tooltipData != null && this.isHover()) {
            val event = TooltipRender.BUS.fire(new FantasyTooltipRender(new ArrayList<>(this.tooltipData.getLines())));
            if (!event.isCancelled()) {
                FantasyMod.customTooltip().render(event.getContent());
            }
        }
    }

}
