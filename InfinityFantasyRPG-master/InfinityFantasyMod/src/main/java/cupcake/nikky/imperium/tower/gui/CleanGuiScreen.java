package cupcake.nikky.imperium.tower.gui;

import dev.xdark.clientapi.gui.Screen;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.util.Color;

public class CleanGuiScreen extends FantasyGuiScreen {

    public CleanGuiScreen() {
        super();

        this.setColor(new Color(1.0D, 1.0D, 1.0D, 0.0D));
    }

    @Override
    public void keyTyped(Screen screen, char c, int code) {
        if (code == Keyboard.KEY_ESCAPE || code == Enginex.getClientApi().settingsManager().getBindingByName("key.inventory").getKeyCode()) {
            this.close();
        }
    }
}
