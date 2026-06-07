package cupcake.nikky.imperium.tower.hud.trade;

import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import cupcake.nikky.imperium.tower.hud.economy.EconomyContext;
import cupcake.nikky.imperium.tower.hud.economy.EconomyDataType;
import cupcake.nikky.imperium.tower.inventory.InventoryContext;
import dev.xdark.clientapi.resource.ResourceLocation;
import lombok.Getter;
import lombok.val;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.*;

public class TradeSellContext extends VerticalLayout {

    @Getter
    private final InventoryContext inventoryContext = new InventoryContext((index, item) -> {
        new ModTransfer().writeInt(index).send("imp:trade-sell");
    });

    private final FantasyButton sellButton = new FantasyButton();

    private final Text priceText = new Text().setShadow(true);
    private final Image currencyImage = new Image();

    @Getter
    private final EconomyContext economyContext = new EconomyContext();

    public TradeSellContext() {
        super(5D);

        val topRectangle = new CarvedRectangle().setColor(Palette.BLACK.alpha(0)).setSize(new V3(516, 40)).setInteractive(false);

        sellButton.setButtonColor(ButtonColor.GREY).setOrigin(Relative.BOTTOM_RIGHT).setAlign(Relative.BOTTOM_RIGHT);

        economyContext.setPos(new V3()).setOrigin(Relative.BOTTOM_LEFT).setAlign(Relative.BOTTOM_LEFT);
        economyContext.apply(EconomyDataType.GRAN, EconomyDataType.SPECIAL);
        economyContext.getElements().values().forEach(dataBox -> dataBox.setColor(Palette.GREY_MIDDLE.alpha(.26)));
        economyContext.onPreTick(element -> economyContext.update());

        currencyImage.setTexture(ResourceLocation.of(FantasyImage.GRAN.toTexture())).setSize(new V3(16, 16));

        sellButton.onPreTransform(element -> sellButton.setSize(new V3(priceText.getSize().getX() + 18 + 16 * 2, 38)));

        sellButton.onLeftClick(element -> {
            if (!CooldownService.getInstance().getAndExtendCooldown("trade-sell-confirm", 0.5)) return;

            new ModTransfer().send("imp:trade-confirm");
            inventoryContext.resetColors();
            this.updatePrice(-1);
        });

        topRectangle.addChild(economyContext, sellButton);

        this.setOrigin(Relative.CENTER).setAlign(Relative.CENTER).addChild(topRectangle, inventoryContext);

        this.updatePrice(-1);
    }

    public void updatePrice(final int price) {
        if (price >= 0 && !inventoryContext.getCachedFlags().isEmpty()) {
            priceText.setValue("Продать за " + price);

            sellButton.clearChilds();
            sellButton.addChild(new HorizontalLayout(2D)
                    .addChild(priceText.setInteractive(false), currencyImage.setInteractive(false))
                    .setOrigin(Relative.CENTER).setAlign(Relative.CENTER).setInteractive(false));
            sellButton.setEnabled(true);
        } else {
            sellButton.setEnabled(false);
        }
    }

}
