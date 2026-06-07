package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player.TradeStatePacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player.TradeTryClickItemPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player.TradeTryConfirmPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player.TradeUpdateCurrencyPacket;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import cupcake.nikky.imperium.tower.data.EconomyStorage;
import cupcake.nikky.imperium.tower.hud.economy.EconomyContext;
import cupcake.nikky.imperium.tower.hud.economy.EconomyDataType;
import cupcake.nikky.imperium.tower.inventory.InventoryContext;
import cupcake.nikky.imperium.tower.utility.NumberUtility;
import dev.xdark.clientapi.gui.Screen;
import dev.xdark.clientapi.item.Items;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.*;

import java.util.Arrays;

@Getter
public class TradePlayerContext extends GuiScreen {

    private final FantasyMod fantasyMod;

    private final InventoryContext participantInventory = new InventoryContext();
    private final InventoryContext ownInventory;

    private final EconomyContext participantEconomy = new EconomyContext();
    private final EconomyContext ownEconomyContext = new EconomyContext();

    private final Text traderName = new Text();
    private final FantasyButton confirmButton = new FantasyButton("Подтвердить");

    private final Input inputGran = new Input();
    private final Input inputFirm = new Input();

    @Setter
    private boolean confirmed;

    public TradePlayerContext(@NotNull final FantasyMod mod) {
        this.fantasyMod = mod;
        this.ownInventory = new InventoryContext((slot, itemStack) -> {
            if (this.isConfirmed() || itemStack == null || itemStack.getItem() == null || itemStack.getItem() == Items.AIR) return;
            if (!CooldownService.getInstance().getAndExtendCooldown("player-trade-click", .1)) return;

            fantasyMod.sendPacket(new TradeTryClickItemPacket(slot, false));
        });

        this.initialize();
    }

    public void updateParticipantEconomy(final int gran, final int firm) {
        val granBox = getParticipantEconomy().getElements().get(EconomyDataType.GRAN);
        val specialBox = getParticipantEconomy().getElements().get(EconomyDataType.SPECIAL);

        FantasyMod.tooltipHandler().applyHoverTooltip(granBox, Arrays.asList("Граны", "Точное количество: " + NumberUtility.classicalFormat(gran)));
        FantasyMod.tooltipHandler().applyHoverTooltip(specialBox, Arrays.asList("Фирм-монеты", "Точное количество: " + NumberUtility.classicalFormat(firm)));

        granBox.updateContent(FantasyImage.GRAN.toTexture(), NumberUtility.format(gran));
        specialBox.updateContent(FantasyImage.CURRENCY.toTexture(), NumberUtility.format(firm));
    }

    @Override
    public void open() {
        super.open();
        ownInventory.fillItems();
        participantInventory.fillItems(true);

        fantasyMod.sendPacket(new TradeStatePacket(false));
    }

    private void initialize() {
        participantEconomy.setOrigin(Relative.BOTTOM_LEFT).setAlign(Relative.BOTTOM_LEFT);
        ownEconomyContext.setOrigin(Relative.BOTTOM_LEFT).setAlign(Relative.BOTTOM_LEFT);

        participantEconomy.applyClean(EconomyDataType.GRAN, EconomyDataType.SPECIAL);
        ownEconomyContext.apply(EconomyDataType.GRAN, EconomyDataType.SPECIAL);
        ownEconomyContext.onPreTick(abstractElement -> ownEconomyContext.update());
        traderName.setOrigin(Relative.BOTTOM_RIGHT).setAlign(Relative.BOTTOM_RIGHT);

        confirmButton.setButtonColor(ButtonColor.BLUE).setOrigin(Relative.TOP_RIGHT).setAlign(Relative.TOP_RIGHT);
        confirmButton.onPreTransform(abstractElement -> abstractElement.setSize(new V3(confirmButton.getText().getSize().getX() * 1.5, 38)));
        confirmButton.onLeftClick(abstractElement -> {
            if (!CooldownService.getInstance().getAndExtendCooldown("player-trade-click", .1)) return;
            fantasyMod.sendPacket(new TradeTryConfirmPacket(!confirmed));
        });

        inputGran.setFilter(Input.NUMBERS_FILTER).getSize().setX(144);
        inputGran.setPlaceHolder("Граны");
        inputGran.setLimit(10);
        inputGran.onTextChange((element, currentText) -> {
            val currentValue = currentText.trim().isEmpty() ? 0 : NumberUtility.parseInteger(currentText);
            fantasyMod.sendPacket(new TradeUpdateCurrencyPacket(currentValue, inputFirm.getText().trim().isEmpty() ? 0 : NumberUtility.parseInteger(inputFirm.getText())));
        });

        inputFirm.setFilter(Input.NUMBERS_FILTER).getSize().setX(144);
        inputFirm.setPlaceHolder("Фирмы");
        inputFirm.setLimit(10);
        inputFirm.onTextChange((element, currentText) -> {
            val currentValue = currentText.trim().isEmpty() ? 0 : NumberUtility.parseInteger(currentText);
            fantasyMod.sendPacket(new TradeUpdateCurrencyPacket(inputGran.getText().trim().isEmpty() ? 0 : NumberUtility.parseInteger(inputGran.getText()), currentValue));
        });

        this.addChild(new VerticalLayout(54).addChild(
                new VerticalLayout(8).addChild(new Rectangle().setColor(Palette.BLACK.alpha(0)).setSize(new V3(participantInventory.getSize().getX(), 38))
                        .addChild(participantEconomy, traderName), participantInventory),
                new VerticalLayout(8).addChild(new Rectangle().setColor(Palette.BLACK.alpha(0)).setSize(new V3(participantInventory.getSize().getX(), 38))
                        .addChild(ownEconomyContext, new HorizontalLayout(8D).addChild(inputGran, inputFirm).setOrigin(Relative.BOTTOM_RIGHT).setAlign(Relative.BOTTOM_RIGHT)),
                        ownInventory, new Rectangle().setColor(Palette.BLACK.alpha(0)).setSize(new V3(participantInventory.getSize().getX(), 38))
                                .addChild(new Text("\uE3FE Выбрать предмет").setOrigin(Relative.LEFT).setAlign(Relative.LEFT), confirmButton)))
                .setOrigin(Relative.CENTER).setAlign(Relative.CENTER));
    }

    @Override
    public void keyTyped(Screen screen, char c, int code) {
        if (!this.isConfirmed()) {
            if (code == Keyboard.KEY_ESCAPE && (inputGran.isFocused() || inputFirm.isFocused())) {
                inputGran.setFocused(false);
                inputFirm.setFocused(false);
                return;
            }

            if (inputGran.isFocused()) {
                val text = inputGran.getText() + c;
                val currentValue = text.trim().isEmpty() ? 0 : NumberUtility.parseInteger(text);
                if (EconomyStorage.getInstance().getGran() >= currentValue) {
                    inputGran.keyPress(code);
                }
            } else if (inputFirm.isFocused()) {
                val text = inputFirm.getText() + c;
                val currentValue = text.trim().isEmpty() ? 0 : NumberUtility.parseInteger(text);
                if (EconomyStorage.getInstance().getSpecial() >= currentValue) {
                    inputFirm.keyPress(code);
                }
            }
        }

        if (code == Keyboard.KEY_ESCAPE) {
            this.close();
        }
    }

    @Override
    public void close() {
        super.close();

        traderName.setValue("");
        inputGran.setText("");
        inputFirm.setText("");
        participantInventory.clearInventory();
        ownInventory.clearInventory();
        ownInventory.resetColors();
        ownInventory.setOutlineColor((Color) null);
        participantInventory.setOutlineColor((Color) null);
        confirmButton.setText("Подтвердить").setButtonColor(ButtonColor.BLUE);
        this.confirmed = false;
        this.updateParticipantEconomy(0, 0);

        fantasyMod.sendPacket(new TradeStatePacket(true));
    }
}
