package cupcake.nikky.imperium.tower.handler;

import cupcake.nikky.imperium.tower.data.prompt.ArmorUtility;
import cupcake.nikky.imperium.tower.tooltip.FantasyTooltip;
import cupcake.nikky.imperium.tower.tooltip.position.TooltipLeftSidePosition;
import cupcake.nikky.imperium.tower.utility.ItemUtility;
import dev.xdark.clientapi.event.render.ItemTooltipRender;
import dev.xdark.clientapi.item.ItemStack;
import dev.xdark.clientapi.item.TooltipFlag;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;

public class ItemPromptHandler {

    private final FantasyTooltip tooltip = new FantasyTooltip(new TooltipLeftSidePosition());

    public ItemPromptHandler(@NotNull final FantasyTooltip customTooltip) {
        Enginex.registerEvent(ItemTooltipRender.class, itemTooltipRender -> {
            if (customTooltip.isOverflow()) return;

            val player = Enginex.getPlayer();
            val item = itemTooltipRender.getStack();
            val type = ArmorUtility.findTypeByItem(item);
            if (type == null) return;

            for (ItemStack next : player.getArmorInventoryList()) {
                if (next.equals(item)) break;

                val playerArmorType = ArmorUtility.findTypeByItem(next);
                if (playerArmorType == null) continue;

                if (playerArmorType == type) {
                    val currentContent = next.getTooltip(player, TooltipFlag.NORMAL);
                    String title = "";
                    if (!currentContent.isEmpty()) title = currentContent.remove(0) + " (Надето)";

                    if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL)) {
                        this.tooltip.render(ItemUtility.findColorByItemRarity(next), title, ItemUtility.transformComparedLore(currentContent, next));
                    } else {
                        itemTooltipRender.getContent().add("§8CTRL сравнить снаряжение");
                    }
                }
            }
        }, 10);
    }

}
