package cupcake.nikky.imperium.tower.tooltip.position;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsType;
import cupcake.nikky.imperium.tower.data.SettingsStorage;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.Mask;
import ru.cristalix.enginex.renderer.contexts.PostOverlay;
import ru.cristalix.enginex.util.V3;

public class TooltipInventoryRightSidePosition extends TooltipPosition {

    @Override
    public V3 getPosition(@NotNull V3 mousePosition, @NotNull Mask panel) {
        return PostOverlay.get().getSize().clone().divide(2D).add(200, -panel.getSize().getY() / 2D, 0);
    }

    @Override
    public boolean canTouchPosition(@NotNull V3 mousePosition, @NotNull Mask panel) {
        return SettingsStorage.getInstance().getSettings().getBoolean(SettingsType.CRAFT_VIEW_VISIBILITY);
        //return 200 + panel.getSize().getX() <= PostOverlay.get().getSize().getX() / 2D;
    }

    @Override
    public boolean canOverflow() {
        return false;
    }
}
