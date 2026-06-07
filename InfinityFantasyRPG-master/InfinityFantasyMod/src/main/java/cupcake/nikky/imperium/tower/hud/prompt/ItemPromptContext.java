package cupcake.nikky.imperium.tower.hud.prompt;

import cupcake.nikky.imperium.tower.data.InventoryOverlayTextStorage;
import cupcake.nikky.imperium.tower.handler.key.KeyHandler;
import cupcake.nikky.imperium.tower.utility.ScaleUtility;
import lombok.val;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.Text;
import ru.cristalix.enginex.element.VerticalLayout;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

public class ItemPromptContext extends VerticalLayout {

    public ItemPromptContext() {
        super(8D);

        this.setOrigin(Relative.BOTTOM_LEFT).setAlign(Relative.BOTTOM_LEFT).setPos(new V3(16, -16)).setAlpha(.62);
        this.update();
        this.onPreTransform(element -> this.setScale(ScaleUtility.findCurrentScale()));
    }

    public void update() {
        this.clearChilds();

        val text = InventoryOverlayTextStorage.getInstance().text();
        if (text.length > 0) {
            val mountKey = KeyHandler.MOUNT_USE.name();
            for (int i = 0; i < text.length; i++) {
                val line = text[i];
                if (line.contains(mountKey)) {
                    text[i] = line.replace(mountKey, KeyHandler.MOUNT_USE.container().keyChar());
                }

                val textElement = new Text(text[i]);
                if (line.contains("Сезон")) {
                    textElement.onLeftClick(text1 -> Enginex.getClientApi().minecraft().openUrl(InventoryOverlayTextStorage.getInstance().seasonUrl()));
                }

                this.addChild(textElement);
            }
        }
    }

}
