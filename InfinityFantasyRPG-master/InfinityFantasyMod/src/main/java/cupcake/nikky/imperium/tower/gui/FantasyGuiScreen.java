package cupcake.nikky.imperium.tower.gui;

import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.GuiScreen;

public class FantasyGuiScreen extends GuiScreen {

    @Override
    public void open() {
        if (Enginex.getScreen() != null) {
            Enginex.getScreen().closed();
        }

        super.open();
    }

}
