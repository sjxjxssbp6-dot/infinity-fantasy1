package cupcake.nikky.imperium.tower.hud.analyze;

import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyzeType;
import cupcake.nikky.imperium.tower.data.analyze.ItemAnalyzeStorage;
import cupcake.nikky.imperium.tower.gui.FantasyGuiScreen;
import cupcake.nikky.imperium.tower.handler.key.KeyHandler;
import cupcake.nikky.imperium.tower.inventory.InventoryContext;
import dev.xdark.clientapi.gui.Screen;
import dev.xdark.clientapi.item.Items;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.*;

public class ItemAnalyzeContext extends FantasyGuiScreen {

    @Getter
    private final VerticalLayout generalLayout = new VerticalLayout(5D);

    private FantasyButton prefixButton = new FantasyButton("Префиксы");
    private FantasyButton suffixButton = new FantasyButton("Суффиксы");
    private FantasyButton directionButton = new FantasyButton("Направления");

    @Setter
    private int lastActiveIndex = -1;

    @Getter
    private final InventoryContext inventoryContext = new InventoryContext((index, item) -> {
        if (item.getItem() != Items.AIR && item.getItem() != null) {
            if (lastActiveIndex == index) {
                ItemAnalyzeStorage.getInstance().clear();

                getInventoryContext().updateItemColor(index, 0);
                lastActiveIndex = -1;
                currentType = ItemAnalyzeType.NONE;
                this.update();

                return;
            }

            ItemAnalyzeStorage.getInstance().setShouldClear(true);
            new ModTransfer().writeInt(index).send("imp:item-al-click");
        }
    });

    private final HorizontalLayout buttonsLayout = new HorizontalLayout(5D);

    private ItemAnalyzeType currentType = ItemAnalyzeType.NONE;

    public ItemAnalyzeContext() {
        this.initialize();
        this.update();
    }

    private void initialize() {
        val bottomText = new Text("\uE3FE Показать модификаторы предмета");

        final VerticalScrollView<?> scroll = new VerticalScrollView<>().setDisableBarOnInactive(true).setSize(new V3(1_100, 500 + 38 + 7.5));
        scroll.addChild(this.generalLayout);

        this.addChild(new VerticalLayout(7.5D).addChild(
                this.createTopComponent(),
                this.createButtonsComponent().setPos(new V3(-550D + buttonsLayout.getSize().getX() / 2D, 0)),
                scroll.setColor(Palette.GREY.alpha(.67)),
                inventoryContext,
                bottomText
        ).setOrigin(Relative.CENTER).setAlign(Relative.CENTER).setRenderLayer(1));
    }

    private CarvedRectangle createTopComponent() {
        val topComponent = new CarvedRectangle(2D).setColor(Palette.GREY.alpha(.67));
        val titleText = new Text("Модификатор").setShadow(true).setOrigin(Relative.LEFT).setAlign(Relative.LEFT);
        val tiersText = new Text("Ур. модификатора").setShadow(true).setOrigin(Relative.CENTER).setAlign(Relative.LEFT);
        val baseLevelText = new Text("Ур. предмета").setShadow(true).setOrigin(Relative.CENTER).setAlign(Relative.LEFT);
        val weightText = new Text("Вес модификатора").setShadow(true).setOrigin(Relative.CENTER).setAlign(Relative.LEFT);

        topComponent.setSize(new V3(1_100, 30)).addChild(
                titleText.setPos(new V3(10, 0)),
                tiersText.setPos(new V3(600, 0)),
                baseLevelText.setPos(new V3(800, 0)),
                weightText.setPos(new V3(1000, 0))
        );

        return topComponent;
    }

    private AbstractElement createButtonsComponent() {
        prefixButton.getText().setShadow(true);
        suffixButton.getText().setShadow(true);
        directionButton.getText().setShadow(true);

        prefixButton.setSize(new V3(prefixButton.getText().getSize().getX() + 16 * 2, 38)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
        suffixButton.setSize(new V3(suffixButton.getText().getSize().getX() + 16 * 2, 38)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
        directionButton.setSize(new V3(directionButton.getText().getSize().getX() + 16 * 2, 38)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);

        prefixButton.setButtonColor(ButtonColor.GREY).setActive(false);
        suffixButton.setButtonColor(ButtonColor.GREY).setActive(false);
        directionButton.setButtonColor(ButtonColor.GREY).setActive(false);

        prefixButton.onLeftClick(element -> {
            if (currentType == ItemAnalyzeType.NONE) {
                prefixButton.setPlaySound(false);
                prefixButton.setForceCanClick(false);
                return;
            } else {
                prefixButton.setPlaySound(true);
                prefixButton.setForceCanClick(true);
            }

            if (currentType != ItemAnalyzeType.PREFIX) {
                currentType = ItemAnalyzeType.PREFIX;
                prefixButton.setColor(ButtonColor.GREY.getActiveColor());
                suffixButton.setColor(ButtonColor.GREY.getDefaultColor());
                directionButton.setColor(ButtonColor.GREY.getDefaultColor());
                this.update();
            }
        });

        suffixButton.onLeftClick(element -> {
            if (currentType == ItemAnalyzeType.NONE) {
                prefixButton.setPlaySound(false);
                prefixButton.setForceCanClick(false);
                return;
            } else {
                prefixButton.setPlaySound(true);
                prefixButton.setForceCanClick(true);
            }

            if (currentType != ItemAnalyzeType.SUFFIX) {
                currentType = ItemAnalyzeType.SUFFIX;
                prefixButton.setColor(ButtonColor.GREY.getDefaultColor());
                directionButton.setColor(ButtonColor.GREY.getDefaultColor());
                suffixButton.setColor(ButtonColor.GREY.getActiveColor());
                this.update();
            }
        });

        directionButton.onLeftClick(element -> {
            if (currentType == ItemAnalyzeType.NONE) {
                prefixButton.setPlaySound(false);
                prefixButton.setForceCanClick(false);
                return;
            } else {
                prefixButton.setPlaySound(true);
                prefixButton.setForceCanClick(true);
            }

            if (currentType != ItemAnalyzeType.DIRECTION) {
                currentType = ItemAnalyzeType.DIRECTION;
                prefixButton.setColor(ButtonColor.GREY.getDefaultColor());
                suffixButton.setColor(ButtonColor.GREY.getDefaultColor());
                directionButton.setColor(ButtonColor.GREY.getActiveColor());
                this.update();
            }
        });

        return buttonsLayout.addChild(prefixButton, suffixButton, directionButton);
    }

    public void update() {
        if (!ItemAnalyzeStorage.getInstance().isEmpty() && currentType == ItemAnalyzeType.NONE) {
            currentType = ItemAnalyzeType.PREFIX;
        }

        generalLayout.clearChilds();
        ItemAnalyzeStorage.getInstance().findAll(currentType).forEach(container ->
                generalLayout.addChild(new ItemAnalyzeButton(container, this).setOrigin(Relative.CENTER).setAlign(Relative.CENTER)));

        if (currentType == ItemAnalyzeType.PREFIX) {
            prefixButton.setActive(false);
            suffixButton.setActive(true);
            directionButton.setActive(true);
        } else if (currentType == ItemAnalyzeType.SUFFIX) {
            prefixButton.setActive(true);
            suffixButton.setActive(false);
            directionButton.setActive(true);
        } else if (currentType == ItemAnalyzeType.DIRECTION) {
            prefixButton.setActive(true);
            suffixButton.setActive(true);
            directionButton.setActive(false);
        } else {
            prefixButton.setActive(false);
            suffixButton.setActive(false);
            directionButton.setActive(false);
        }

    }

    @Override
    public void keyTyped(Screen screen, char c, int code) {
        if (code == Keyboard.KEY_ESCAPE || code == Enginex.getClientApi().settingsManager().getBindingByName("key.inventory").getKeyCode() || code == KeyHandler.FIRMOLOG.container().key()) {
            this.close();
        }
    }

    @Override
    public void open() {
        super.open();
        getInventoryContext().fillItems();
    }

    @Override
    public void close() {
        super.close();

        ItemAnalyzeStorage.getInstance().clear();

        this.getInventoryContext().resetColors();
        lastActiveIndex = -1;
        currentType = ItemAnalyzeType.NONE;

        this.update();
    }

}
