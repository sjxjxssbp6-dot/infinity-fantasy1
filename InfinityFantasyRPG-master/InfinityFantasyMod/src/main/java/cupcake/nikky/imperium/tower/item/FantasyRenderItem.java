package cupcake.nikky.imperium.tower.item;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.event.FantasyItemTooltipRender;
import dev.xdark.clientapi.event.render.ItemTooltipRender;
import dev.xdark.clientapi.item.ItemStack;
import dev.xdark.clientapi.item.TooltipFlag;
import dev.xdark.clientapi.opengl.GlStateManager;
import dev.xdark.clientapi.opengl.RenderHelper;
import dev.xdark.clientapi.render.RenderItem;
import lombok.val;
import org.lwjgl.opengl.GL11;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.ItemRender;
import ru.cristalix.enginex.util.Color;

public class FantasyRenderItem extends ItemRender {

    public FantasyRenderItem() {
    }

    public FantasyRenderItem(ItemStack item) {
        super(item);
    }

    public FantasyRenderItem(ItemStack item, boolean renderOverlay) {
        super(item, renderOverlay);
    }

    public FantasyRenderItem(ItemStack item, boolean renderOverlay, boolean renderTooltip) {
        super(item, renderOverlay, renderTooltip);
    }

    @Override
    public void render(double partialTicks, double mouseX, double mouseY) {
        if (this.getItem() != null) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(this.getSize().getX() / 16.0, this.getSize().getY() / 16.0, 1.0);
            RenderHelper.enableGUIStandardItemLighting();
            Color color = this.color.clone();
            color.setAlphaMultiplier(color.getAlphaMultiplier() * this.getAbsoluteAlpha());
            int decimalColor = color.getDecimal();
            boolean isDepth = GL11.glGetBoolean(2929);
            GlStateManager.enableDepth();
            GlStateManager.enableBlend();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableAlpha();
            GlStateManager.blendFunc(770, 771);
            RenderItem renderItem = Enginex.getClientApi().renderItem();
            renderItem.renderItemAndEffectIntoGUI(this.getItem(), 0, 0, decimalColor);
            if (this.isRenderOverlay()) {
                int count = this.getItem().getCount();
                String countText = "";
                if (count > 1) {
                    countText = String.valueOf(count >= 1000 ? (int)Math.floor(count / 1000D) + "К" : count);
                }

                renderItem.renderItemOverlayIntoGUI(Enginex.getClientApi().fontRenderer(), this.getItem(), 0, 0, countText);
            }

            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            if (!isDepth) {
                GlStateManager.disableDepth();
            }

            RenderHelper.disableStandardItemLighting();
            if (this.isRenderTooltip() && this.isHover) {
                val event = ItemTooltipRender.BUS.fire(new FantasyItemTooltipRender(this.getItem(), this.getItem().getTooltip(Enginex.getPlayer(), TooltipFlag.NORMAL)));
                if (!event.isCancelled()) {
                    FantasyMod.customTooltip().render(event.getContent());
                }
            }

        }
    }

}
