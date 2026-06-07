package cupcake.nikky.imperium.tower.handler;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.data.CraftViewStorage;
import cupcake.nikky.imperium.tower.event.FantasyItemTooltipRender;
import cupcake.nikky.imperium.tower.tooltip.FantasyTooltip;
import cupcake.nikky.imperium.tower.tooltip.position.TooltipInventoryRightSidePosition;
import cupcake.nikky.imperium.tower.utility.ItemUtility;
import dev.xdark.clientapi.event.render.ItemTooltipRender;
import dev.xdark.clientapi.event.render.RenderTickPre;
import dev.xdark.clientapi.gui.ingame.InventorySurvivalScreen;
import dev.xdark.clientapi.item.TooltipFlag;
import lombok.val;
import ru.cristalix.enginex.Enginex;

public class CraftViewHandler {

    public CraftViewHandler() {
        Enginex.registerEvent(RenderTickPre.class, renderTickPre -> {
            if (Enginex.getMinecraft().currentScreen() != null) {
                this.render();
            } else {
                CraftViewStorage.getInstance().state(0);
            }
        });
    }

    public void render() {
        val state = CraftViewStorage.getInstance().state();
        if (state == 0) return;

        val item = CraftViewStorage.getInstance().craftStack();
        if (item == null) return;

        val content = CraftViewStorage.getInstance().craftStack().getTooltip(Enginex.getPlayer(), TooltipFlag.NORMAL);
        val event = ItemTooltipRender.BUS.fire(new FantasyItemTooltipRender(item, content));

        if (!event.isCancelled()) FantasyMod.customTooltip().render(content);
    }

}
