package cupcake.nikky.imperium.tower.hud.teleport;

import com.google.common.base.Strings;
import cupcake.nikky.imperium.tower.carved.FantasyCarvedRectangle;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import cupcake.nikky.imperium.tower.data.TeleportStorage;
import cupcake.nikky.imperium.tower.gui.FantasyGuiScreen;
import cupcake.nikky.imperium.tower.handler.key.KeyHandler;
import dev.xdark.clientapi.gui.Screen;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.*;

import java.util.function.Consumer;

public class TeleportContext extends FantasyGuiScreen {

    private final GridLayout generalLayout = new GridLayout(LayoutPriority.HORIZONTAL, 0, 3, 24D);

    public TeleportContext() {
        this.initialize();
        this.update();
    }

    @Override
    public void keyTyped(Screen screen, char c, int code) {
        if (code == Keyboard.KEY_ESCAPE || code == Enginex.getClientApi().settingsManager().getBindingByName("key.inventory").getKeyCode() || code == KeyHandler.TELEPORT.container().key()) {
            this.close();
        }
    }

    private void initialize() {
        val scroll = new ScrollView();
        scroll.setSize(new V3(280D * 3D + 24 * 2, 255D * 3D));
        scroll.setDisableBarOnInactive(true).setColor(Palette.GREY).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);

        val title = new Text("Места для перемещения").setShadow(true);
        val subtitle = new Text("Выполняйте поручения и повышайте свой уровень для открытия новых мест!").setShadow(true).setColor(Palette.GREY_LIGHT);

        val titleLayout = new VerticalLayout(14D).addChild(
                title.setOrigin(Relative.LEFT).setAlign(Relative.LEFT),
                subtitle.setOrigin(Relative.LEFT).setAlign(Relative.LEFT))
                .setOrigin(Relative.LEFT).setAlign(Relative.LEFT);

        titleLayout.setPos(new V3((titleLayout.getSize().getX() - scroll.getSize().getX()) / 2D, 0));

        this.addChild(new VerticalLayout(24D).setRenderLayer(1).addChild(
                titleLayout,
                scroll.addChild(generalLayout)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER)
        );
    }

    public void update() {
        this.generalLayout.clearChilds();

        TeleportStorage.getInstance().getData().forEach((id, container) -> this.generalLayout.addChild(this.createSection(id, container)));
    }

    private AbstractElement createSection(final int id, @NotNull final TeleportContainer teleportContainer) {
        val rectangle = new CarvedRectangle(3D);
        rectangle.setSize(new V3(280, 146)).setColor(Palette.WHITE.alpha(0D));

        val imageElement = new Image(teleportContainer.getResourceLocation());
        imageElement.setSize(new V3(274, 140)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);

        val hoverRectangle = new FantasyCarvedRectangle(0D).setSize(imageElement.getSize().clone()).setColor(Palette.BLACK.alpha(.28));
        imageElement.addChild(hoverRectangle.setAlpha(0D).setOrigin(Relative.CENTER).setAlign(Relative.CENTER));

        if (!teleportContainer.isBlocked()) {
            val hoverText = new Text("Перейти").setColor(Palette.WHITE).setAlpha(0D).setScale(new V3(2D, 2D)).setAlign(Relative.CENTER).setOrigin(Relative.CENTER);
            rectangle.onHover((element, aBoolean) -> {
                if (aBoolean) {
                    rectangle.getColor().setA(1D);
                    hoverRectangle.setAlpha(1D);
                    hoverText.setAlpha(1D);
                } else {
                    rectangle.getColor().setA(0D);
                    hoverRectangle.setAlpha(0D);
                    hoverText.setAlpha(0D);
                }
            });

            rectangle.addChild(imageElement, hoverText);
        } else rectangle.addChild(imageElement, hoverRectangle.setTooltip("§fНеобходимо: §6" + teleportContainer.getRequirement()).setAlpha(1D).setColor(Palette.BLACK.alpha(.62))
                .addChild(new Text("\uE347").setOrigin(Relative.CENTER).setAlign(Relative.CENTER)));

        rectangle.onLeftClick(element -> {
            if (!CooldownService.getInstance().getAndExtendCooldown("teleport-context-click", 0.1)) return;
            if (teleportContainer.isBlocked()) {
                SoundUtil.play("sounds/custom/confusion.ogg", 1F, 1F);
                return;
            }

            new ModTransfer().writeInt(id).send("imp:tp-click");
        });

        val position = this.findDotPosition(teleportContainer.getTitle()) + 1;
        val prefix = "\n" + this.repeat(" ", position);

        val text = new Text(teleportContainer.getTitle() + (teleportContainer.isBlocked() ? "\n" : prefix + "Стоимость: " + teleportContainer.getTeleportCost() + "\uE0FE"))
                .setLineHeight(22.4).setShadow(true);
        text.setOrigin(Relative.LEFT).setAlign(Relative.LEFT);

        return new VerticalLayout(8D).addChild(rectangle, text);
    }

    private String repeat(@NotNull final String whatRepeat, final int count) {
        val result = new StringBuilder();
        for (int i = 0; i < count; i++)
            result.append(whatRepeat);
        return result.toString();
    }

    private int findDotPosition(@NotNull final String text) {
        for (int i = 0; i < text.length(); i++)
            if (text.charAt(i) == '.') return i + 1;
        return -1;
    }

    @Override
    public void close() {
        super.close();

        new ModTransfer().send("imp:tp-close");
    }
}
