package cupcake.nikky.imperium.tower.hud.box;

import cupcake.nikky.imperium.tower.carved.FantasyCarvedRectangle;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseRarity;
import cupcake.nikky.imperium.tower.image.FantasyImageRender;
import cupcake.nikky.imperium.tower.item.FantasyRenderItem;
import dev.xdark.clientapi.item.ItemStack;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.enginex.animation.Animation;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

public class CaseIconBox extends FantasyCarvedRectangle {

    public CaseIconBox(@NotNull final Object icon, @Nullable final String iconTitle, @NotNull final CaseRarity caseRarity) {
        super(4D);
        this.setColor(Palette.GREY_DARK.alpha(.62)).setSize(new V3(92, 92));

        if (icon instanceof String) {
            val iconElement = new FantasyImageRender((String) icon);
            val iconRender = iconElement.setSize(new V3(64, 64)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
            if (iconTitle != null) iconRender.setTooltip(iconTitle);

            this.addChild(iconRender);
        } else {
            val itemStack = (ItemStack) icon;
            if (!itemStack.getDisplayName().contains("шт.)")) {
                itemStack.setDisplayName(itemStack.getDisplayName() + String.format(" &6(%d шт.)", itemStack.getCount()));
            }

            val iconElement = new FantasyRenderItem((ItemStack) icon, false, true);
            val iconRender = iconElement.setSize(new V3(64, 64)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);

            this.addChild(iconRender);
        }

        this.setRarityColor(getColorByRarity(caseRarity));
    }

    private Palette getColorByRarity(@NotNull final CaseRarity caseRarity) {
        switch (caseRarity) {
            case COMMON: return Palette.GREY;
            case RARE: return Palette.BLUE;
            case EPIC: return Palette.PURPLE;
            case UNIQUE: return Palette.YELLOW;
            case EDGE: return Palette.RED;
        }

        return Palette.WHITE;
    }

    public void animateWin() {
        this.setColor(Palette.GREEN.alpha(.62));

        val icon = getChilds().get(0);
        Animation.play(this, "win", 1, (abstractElement, aDouble) -> icon.setScale(new V3(1 + .1 * aDouble, 1 + .1 * aDouble)));
    }

}
