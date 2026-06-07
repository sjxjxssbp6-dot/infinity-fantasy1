package cupcake.nikky.imperium.tower.image;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.event.FantasyTooltipRender;
import dev.xdark.clientapi.event.render.TooltipRender;
import dev.xdark.clientapi.resource.ResourceLocation;
import lombok.val;
import ru.cristalix.enginex.element.Image;

import java.util.ArrayList;

public class FantasyImageRender extends Image {

    public FantasyImageRender() {
    }

    public FantasyImageRender(String texture) {
        super(texture);
    }

    public FantasyImageRender(char icon) {
        super(icon);
    }

    public FantasyImageRender(ResourceLocation texture) {
        super(texture);
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
