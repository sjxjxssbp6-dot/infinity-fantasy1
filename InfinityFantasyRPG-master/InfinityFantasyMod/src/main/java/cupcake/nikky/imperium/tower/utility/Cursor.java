package cupcake.nikky.imperium.tower.utility;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ru.cristalix.enginex.Enginex;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Cursor {

    public static boolean isCursorShowed() {
        return Enginex.getClientApi().minecraft().currentScreen() != null;
    }

}
