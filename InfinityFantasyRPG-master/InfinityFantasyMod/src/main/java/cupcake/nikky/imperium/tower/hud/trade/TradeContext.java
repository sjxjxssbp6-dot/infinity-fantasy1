package cupcake.nikky.imperium.tower.hud.trade;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.client.protocol.trade.TradeCurrency;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import cupcake.nikky.imperium.tower.data.EconomyStorage;
import cupcake.nikky.imperium.tower.data.trade.TradeContainer;
import cupcake.nikky.imperium.tower.data.trade.TradeStorage;
import cupcake.nikky.imperium.tower.gui.FantasyGuiScreen;
import cupcake.nikky.imperium.tower.item.FantasyRenderItem;
import cupcake.nikky.imperium.tower.text.FantasyText;
import dev.xdark.clientapi.gui.Screen;
import dev.xdark.clientapi.opengl.GlStateManager;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TradeContext extends FantasyGuiScreen {

    private final ScrollView generalLayout = new ScrollView();
    private final Text title = new Text();
    private final Text bottomText = new Text();

    private final Map<Integer, Text> limits = new HashMap<>();

    @Getter
    private final TradeSellContext sellContext;

    public TradeContext() {
        sellContext = new TradeSellContext();
        this.initialize();
    }

    @Override
    public void keyTyped(Screen screen, char c, int code) {
        if (code == Keyboard.KEY_ESCAPE || code == Enginex.getClientApi().settingsManager().getBindingByName("key.inventory").getKeyCode()) {
            this.close();
            this.sellContext.getInventoryContext().resetColors();
            this.sellContext.updatePrice(-1);
        }
    }

    private void initialize() {
        title.setAlign(Relative.CENTER).setOrigin(Relative.CENTER).setScale(new V3(1.2, 1.2));
        generalLayout.setDisableBarOnInactive(true).setAlign(Relative.CENTER).setOrigin(Relative.CENTER).setColor(Palette.GREY);
        bottomText.setOrigin(Relative.CENTER).setAlign(Relative.CENTER);

        this.addChild(new VerticalLayout(10D).setRenderLayer(1)
                .addChild(title, generalLayout, sellContext, bottomText)
                .setAlign(Relative.CENTER).setOrigin(Relative.CENTER));
        this.onClose(element -> new ModTransfer().send("imp:trade-close"));
    }

    public void updateAll() {
        sellContext.getInventoryContext().fillItems();

        this.update();
        this.updateTrades();
    }

    public void update() {
        title.setValue(TradeStorage.getInstance().getTitle());
        bottomText.setValue("\uE3FE Выбрать предмет для продажи");
    }

    public void updateTrades() {
        generalLayout.clearChilds();

        val table = new GridLayout(LayoutPriority.HORIZONTAL, 0, 2, 5D);

        TradeStorage.getInstance().getTrades().forEach(tradeContainer -> table.addChild(this.createTradeSection(tradeContainer)));

        generalLayout.setSize(new V3(220 * table.getColumns() + 5, 60 * 8));
        generalLayout.addChild(table);
    }

    public void updateLimits() {
        limits.forEach((id, text) -> {
            val trade = TradeStorage.getInstance().findTrade(id);
            if (trade == null) return;

            text.setValue(trade.currentCount() + "/" + trade.maxCount());
            if (trade.currentCount() == trade.maxCount()) text.setColor(Palette.RED);
            else text.setColor(Palette.WHITE);
        });
    }

    private CarvedRectangle createTradeSection(@NotNull final TradeContainer container) {
        String imageLocation;
        if (container.tradeCurrency() == TradeCurrency.GRAN)
            imageLocation = FantasyImage.GRAN.toTexture();
        else if (container.tradeCurrency() == TradeCurrency.FLOWER)
            imageLocation = FantasyImage.FLOWER_1.toTexture();
        else imageLocation = FantasyImage.MINE.toTexture();

        val result = new CarvedRectangle(4D);
        result.setSize(new V3(220, 60)).setColor(Palette.GREY_MIDDLE.alpha(0.67));

        val button = new FantasyButton();
        val buttonImage = new Image(imageLocation).setSize(new V3(16, 16));
        val buttonPrice = new Text(String.valueOf(container.price())).setShadow(true);

        val buttonLayout = new HorizontalLayout(1D)
                .addChild(buttonPrice.setInteractive(false), buttonImage.setInteractive(false))
                .setOrigin(Relative.CENTER).setAlign(Relative.CENTER).setInteractive(false);

        button.addChild(buttonLayout);

        button.setButtonColor(ButtonColor.GREY);
        button.onPreTransform(element -> button.setSize(new V3(buttonLayout.getSize().getX() + 16 * 2, 38)));

        button.onPreRender(element -> {
            val canClick = EconomyStorage.getInstance().getGran() >= container.price();
            button.setPlaySound(canClick);
        });

        button.onLeftClick(element -> {
            if (!CooldownService.getInstance().getAndExtendCooldown("trade-context-click", 0.1)) return;
            new ModTransfer().writeInt(container.id()).send("imp:trade-click");
        });

        val item = new FantasyRenderItem(container.itemStack(), true, true);

        if (container.maxCount() != -1) {
            val limit = new FantasyText();
            limit.setScale(new V3(.8, .8)).setOrigin(Relative.CENTER).setAlign(Relative.BOTTOM);

            limit.onPreRender(element -> GlStateManager.disableDepth());
            limit.onPostRender(element -> GlStateManager.enableDepth());

            val resetTime = new FantasyText("\uE0C5");
            resetTime.setScale(new V3(1.25, 1.25));
            FantasyMod.tooltipHandler().applyHoverTooltip(resetTime,
                    abstractElement -> {
                        val localTime = LocalDateTime.now(ZoneId.of("GMT+3")).atZone(ZoneId.of("GMT+3")).toInstant().toEpochMilli();
                        val time = TimeUnit.MILLISECONDS.toSeconds(container.whenReset() - localTime);
                        return Collections.singletonList("§fДо обновления: §e" + Enginex.formatSeconds((int) time));
                    });

            result.addChild(resetTime.setOrigin(Relative.LEFT).setAlign(Relative.LEFT).setPos(new V3(70, 0)));

            FantasyMod.tooltipHandler().applyHoverTooltip(limit, Collections.singletonList("§6Лимитированный предмет!"));

            limits.put(container.id(), limit);
            this.updateLimits();

            item.addChild(limit);
        }

        result.addChild(button.setOrigin(Relative.RIGHT).setAlign(Relative.RIGHT).setPos(new V3(-20, 0)),
                item.setSize(new V3(40, 40)).setOrigin(Relative.LEFT).setAlign(Relative.LEFT).setPos(new V3(20, 0)));

        return result;
    }

}
