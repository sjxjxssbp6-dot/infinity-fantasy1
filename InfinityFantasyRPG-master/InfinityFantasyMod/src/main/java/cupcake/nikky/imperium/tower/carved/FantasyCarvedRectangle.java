package cupcake.nikky.imperium.tower.carved;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.event.FantasyTooltipRender;
import dev.xdark.clientapi.event.render.TooltipRender;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.CarvedRectangle;
import ru.cristalix.enginex.element.Rectangle;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.ArrayList;

public class FantasyCarvedRectangle extends CarvedRectangle {

    public FantasyCarvedRectangle() {
    }

    public FantasyCarvedRectangle(double carveSize) {
        super(carveSize);
    }

    public FantasyCarvedRectangle(double carveSize, double outline) {
        super(carveSize, outline);
    }

    public void setRarityColor(@NotNull final Palette palette) {
        this.childs.removeIf(abstractElement -> abstractElement.getId().startsWith("rarity"));

        val rarity = new Rectangle().setId("rarity1").setSize(new V3(getSize().getX() - getCarveSize() * 2, getOutline())).setColor(palette).setOrigin(Relative.BOTTOM).setAlign(Relative.BOTTOM);
        val rarity1 = new Rectangle().setId("rarity2").setSize(new V3(getOutline(), getCarveSize())).setColor(palette).setOrigin(Relative.BOTTOM_LEFT).setAlign(Relative.TOP_LEFT);
        val rarity2 = new Rectangle().setId("rarity3").setSize(new V3(getCarveSize(), getOutline())).setColor(palette).setOrigin(Relative.TOP_RIGHT).setAlign(Relative.TOP_LEFT);
        val rarity3 = new Rectangle().setId("rarity4").setSize(new V3(getOutline(), getCarveSize())).setColor(palette).setOrigin(Relative.BOTTOM_RIGHT).setAlign(Relative.TOP_RIGHT);
        val rarity4 = new Rectangle().setId("rarity5").setSize(new V3(getCarveSize(), getOutline())).setColor(palette).setOrigin(Relative.TOP_LEFT).setAlign(Relative.TOP_RIGHT);

        this.addChild(rarity.addChild(rarity1.addChild(rarity2), rarity3.addChild(rarity4)));
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
