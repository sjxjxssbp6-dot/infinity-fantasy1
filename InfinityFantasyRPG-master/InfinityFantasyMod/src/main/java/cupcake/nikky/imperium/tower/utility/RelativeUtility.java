package cupcake.nikky.imperium.tower.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.AbstractElement;
import ru.cristalix.enginex.util.V3;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RelativeUtility {

    public static void setOrigin(@NotNull final V3 relative, @NotNull final AbstractElement... elements) {
        for (AbstractElement element : elements) {
            element.setOrigin(relative);
        }
    }

    public static void setAlign(@NotNull final V3 relative, @NotNull final AbstractElement... elements) {
        for (AbstractElement element : elements) {
            element.setAlign(relative);
        }
    }

}
