package cupcake.nikky.imperium.tower.utility;

import lombok.*;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.util.V3;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScaleUtility {

    public static V3 findCurrentScale() {
        val resolution = Enginex.getClientApi().resolution();
        val scaledWidth = (double) Enginex.getMinecraft().getDisplayWidth();
        val scaledHeight = (double) Enginex.getMinecraft().getDisplayHeight();

        double scaleFactor = 0;
        while (scaledWidth - (1920 * scaleFactor) >= 480 && scaledHeight - (1080 * scaleFactor) >= 270) {
            scaleFactor += 0.25;
        }

        if (scaleFactor < 1.0D) {
            scaleFactor += 0.25D;
        }

        return new V3(scaleFactor, scaleFactor, scaleFactor).divide(resolution.getScaleFactor()).multiply(2);
    }

}
