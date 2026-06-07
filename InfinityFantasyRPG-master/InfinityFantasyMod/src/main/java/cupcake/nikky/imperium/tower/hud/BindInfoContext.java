package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.handler.key.KeyHandler;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

public class BindInfoContext extends VerticalLayout {

    public BindInfoContext() {
        super(8D);
        this.initialize();
        this.update();
    }

    private void initialize() {
        this.setOrigin(Relative.BOTTOM_RIGHT).setAlign(Relative.BOTTOM_RIGHT).setPos(new V3(-6, -40));
    }

    public void update() {
        this.clearChilds();
        this.addChild(
                this.createElement(KeyHandler.TELEPORT),
                this.createElement(KeyHandler.SETTINGS),
                this.createElement(KeyHandler.HELP),
                this.createElement(KeyHandler.BATTLEPASS),
                this.createElement(KeyHandler.DONATE_SHOP)
        );
    }

    private AbstractElement createElement(@NotNull final KeyHandler keyHandler) {
        val text = new Text(keyHandler.container().description()).setShadow(true);
        val bindText = new Text(String.format("[%s]", keyHandler.container().keyChar())).setColor(Palette.YELLOW_LIGHT);
        val bind = new CarvedRectangle(2D).setColor(Palette.BLACK).setSize(new V3(bindText.getSize().getX() + 16, 26));
        bind.addChild(text.setOrigin(Relative.RIGHT).setAlign(Relative.LEFT).setPos(new V3(-8, 0)), bindText.setOrigin(Relative.CENTER).setAlign(Relative.CENTER));

        return bind.setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
    }

}
