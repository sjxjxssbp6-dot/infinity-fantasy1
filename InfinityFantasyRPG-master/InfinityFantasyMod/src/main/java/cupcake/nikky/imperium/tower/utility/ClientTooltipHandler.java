package cupcake.nikky.imperium.tower.utility;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;
import ru.cristalix.enginex.Enginex;

import java.util.Arrays;
import java.util.List;

public final class ClientTooltipHandler {

    public static void render(@NotNull final List<String> lines) {
        val resolution = Enginex.getClientApi().resolution();

        Enginex.getClientApi().minecraft().currentScreen().drawHoveringText(lines,
                Mouse.getX() / resolution.getScaleFactor(),
                (int) (resolution.getScaledHeight_double() * resolution.getScaleFactor() / resolution.getScaleFactor() - Mouse.getY() / resolution.getScaleFactor()) + 20);
    }

    public static void render(@NotNull final String... lines) {
        ClientTooltipHandler.render(Arrays.asList(lines));
    }

}
