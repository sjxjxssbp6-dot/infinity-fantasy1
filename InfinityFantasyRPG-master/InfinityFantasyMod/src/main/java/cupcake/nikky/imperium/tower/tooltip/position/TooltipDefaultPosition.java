package cupcake.nikky.imperium.tower.tooltip.position;

import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.CarvedRectangle;
import ru.cristalix.enginex.element.Mask;
import ru.cristalix.enginex.util.V3;

public class TooltipDefaultPosition extends TooltipPosition {

    @Override
    public V3 getPosition(@NotNull V3 mousePosition, @NotNull Mask panel) {
        return mousePosition.multiply(2).add(panel.getScale().clone().multiply(new V3(24, -24)));
    }

    @Override
    public boolean canOverflow() {
        return true;
    }
}
