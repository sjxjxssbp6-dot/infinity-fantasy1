package cupcake.nikky.imperium.tower.hud.box;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.CaseClickPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseCurrency;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseExactReward;
import cupcake.nikky.imperium.tower.hud.notification.NotificationData;
import dev.xdark.clientapi.gui.Screen;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.ButtonColor;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.Map;

public class CaseContext extends GuiScreen {

    private final Text title = new Text();
    @Getter private final CaseAmountContext caseAmountContext;
    @Getter private final CaseRewardContext caseRewardContext = new CaseRewardContext();

    private final CaseMainRollContext caseMainRollContext = new CaseMainRollContext();
    private final CaseManyRollsContext caseManyRollsContext2 = new CaseManyRollsContext(2);
    private final CaseManyRollsContext caseManyRollsContext5 = new CaseManyRollsContext(5);

    private final FantasyButton openButton = new FantasyButton();

    private final CarvedRectangle caseRectangle = new CarvedRectangle(6D).setOrigin(Relative.CENTER).setAlign(Relative.CENTER).setColor(Palette.GREY_DARK.alpha(.62));

    private final FantasyMod fantasyMod;

    private int price;
    private CaseCurrency caseCurrency;

    public CaseContext(@NotNull final FantasyMod fantasyMod) {
        this.fantasyMod = fantasyMod;
        this.caseAmountContext = new CaseAmountContext(fantasyMod);
        this.initialize();
    }

    private void initialize() {
        this.title.setOrigin(Relative.BOTTOM_LEFT).setAlign(Relative.TOP_LEFT).setPos(new V3(0, -14));
        this.openButton.setCarveSize(4D).setSize(new V3(394, 38)).setButtonColor(ButtonColor.BLUE);

        this.openButton.onLeftClick(button -> this.fantasyMod.sendPacket(new CaseClickPacket()));
        caseMainRollContext.addPoints();

        this.addChild(caseRectangle.addChild(title, new VerticalLayout(36D).addChild(
                caseMainRollContext, new VerticalLayout(12D).addChild(new HorizontalLayout(12).addChild(caseAmountContext, openButton), caseRewardContext))
                .setOrigin(Relative.CENTER).setAlign(Relative.CENTER).setRenderLayer(1))
        );
    }

    public void fillItems(@NotNull final Map<Integer, CaseExactReward> items) {
        if (items.size() > 6) caseRectangle.setSize(new V3(662, 541));
        else caseRectangle.setSize(new V3(662, 403));
        caseRewardContext.updateRewards(items);
        caseMainRollContext.fillItems(items);
        caseManyRollsContext2.fillItems(items);
        caseManyRollsContext5.fillItems(items);
        caseMainRollContext.setRolling(false);
    }

    public void roll(final int[] winId) {
        switch (winId.length) {
            case 1: {
                this.caseMainRollContext.roll(winId[0]);
                break;
            }
            case 2: {
                this.caseManyRollsContext2.open();
                this.caseManyRollsContext2.roll(winId);
                break;
            }
            case 5: {
                this.caseManyRollsContext5.open();
                this.caseManyRollsContext5.roll(winId);
                break;
            }
            default: {
                Enginex.sendChatMessage("Произошла ошибка. Попробуйте позже");
                break;
            }
        }
    }

    public void updateTitle(@NotNull final String title) {
        this.title.setValue(title);
        this.caseManyRollsContext2.updateTitle(title);
        this.caseManyRollsContext5.updateTitle(title);
    }

    public void updatePrice(final int price, @NotNull final CaseCurrency caseCurrency) {
        this.price = price;
        this.caseCurrency = caseCurrency;
        this.updateButtonTitle();
    }

    public void updateButtonTitle() {
        openButton.setText(String.format("Открыть за %d%s", price * caseAmountContext.getCurrentAmount(), caseCurrency.getIcon()));
    }

    @Override
    public void keyTyped(Screen screen, char c, int code) {
        if (caseMainRollContext.isRolling()) return;
        if (code == Keyboard.KEY_ESCAPE || code == Enginex.getClientApi().settingsManager().getBindingByName("key.inventory").getKeyCode()) {
            this.close();

            caseAmountContext.setCurrentIndex(0);
            caseAmountContext.setSelected(0);
        }
    }

}
