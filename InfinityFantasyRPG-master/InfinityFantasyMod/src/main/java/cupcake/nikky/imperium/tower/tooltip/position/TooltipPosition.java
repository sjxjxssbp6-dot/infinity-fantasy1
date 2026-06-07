package cupcake.nikky.imperium.tower.tooltip.position;

import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.CarvedRectangle;
import ru.cristalix.enginex.element.Mask;
import ru.cristalix.enginex.util.V3;

public abstract class TooltipPosition {

    public abstract V3 getPosition(@NotNull final V3 mousePosition, @NotNull final Mask panel);

    public boolean canTouchPosition(@NotNull final V3 mousePosition, @NotNull final Mask panel) {
        return true;
    }

    public abstract boolean canOverflow();

}
