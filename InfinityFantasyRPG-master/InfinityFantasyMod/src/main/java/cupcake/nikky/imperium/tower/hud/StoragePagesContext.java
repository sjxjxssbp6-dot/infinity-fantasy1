package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.StorageClickPacket;
import cupcake.nikky.imperium.tower.client.protocol.storage.PageState;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import cupcake.nikky.imperium.tower.data.PlayerInventoryStorage;
import dev.xdark.clientapi.event.gui.ScreenDisplay;
import dev.xdark.clientapi.event.render.ItemTooltipRender;
import dev.xdark.clientapi.opengl.GlStateManager;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.HorizontalLayout;
import ru.cristalix.enginex.element.Text;
import ru.cristalix.enginex.renderer.contexts.PostOverlay;
import ru.cristalix.enginex.util.ButtonColor;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.time.Instant;

public class StoragePagesContext extends HorizontalLayout {

    private final FantasyMod fantasyMod;
    private final Text tooltipText = new Text();

    private long lastTooltipUse;

    private boolean storageEnabled;

    public StoragePagesContext(@NotNull final FantasyMod fantasyMod) {
        super(4D);

        this.fantasyMod = fantasyMod;

        Enginex.registerEvent(ScreenDisplay.class, screenDisplay -> {
            if (screenDisplay.getScreen() == null) {
                this.setEnabled(false);
                tooltipText.setEnabled(false);
                storageEnabled = false;
            }
        });

        this.onPreRender(element -> {
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
        });
        this.onPostRender(element -> GlStateManager.enableDepth());

        tooltipText.onPreRender(element -> {
            GlStateManager.disableDepth();
            GlStateManager.disableLighting();
        });
        tooltipText.onPostRender(element -> GlStateManager.enableDepth());

        this.update();

        this.setEnabled(false);

        Enginex.registerEvent(ItemTooltipRender.class, itemTooltipRender -> {
            lastTooltipUse = Instant.now().toEpochMilli();
        });

        tooltipText.setEnabled(false);
        tooltipText.setOrigin(Relative.LEFT).setAlign(Relative.CENTER).setPos(new V3(180, 35));
        this.onPreTick(element -> {
            if (lastTooltipUse + 10L > Instant.now().toEpochMilli()) {
                this.setEnabled(false);
            } else if (storageEnabled) {
                this.setEnabled(true);
            }
        });

        PostOverlay.add(tooltipText);
    }

    public void update() {
        this.clearChilds();

        val data = PlayerInventoryStorage.getInstance().getStorageData();
        if (data == null) return;

        this.setEnabled(true);
        storageEnabled = true;
        data.getPages().forEach((page, state) -> this.addChild(this.createButton(page, state)));

        this.setOrigin(Relative.CENTER).setAlign(Relative.CENTER).setPos(new V3(70, 40));
    }

    private FantasyButton createButton(final int page, @NotNull final PageState state) {
        val button = new FantasyButton(String.valueOf(page));
        button.getText().setScale(new V3(.8, .8));
        button.getText().setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
        button.setButtonColor(ButtonColor.GREY).setSize(new V3(24, 24));
        button.setActive(state == PageState.CAN_BUY || state == PageState.PURCHASED);

        button.onLeftClick(element -> {
            if (CooldownService.getInstance().getAndExtendCooldown("storage_click", .2)) {
                fantasyMod.sendPacket(new StorageClickPacket(page));
            }
        });

        button.onHover((element, isHover) -> {
            if (state == PageState.PURCHASED) {
                tooltipText.setValue("§cОткрыть §eхранилище §c#" + page).setColor(Palette.WHITE);
            } else if (state == PageState.CAN_BUY) {
                tooltipText.setValue("§cКупить §eхранилище &c#" + page + "\n§fза §e999§r\uE104§f?").setColor(Palette.WHITE);
            } else if (state == PageState.CANT_BUY) {
                tooltipText.setValue("Необходимо купить\nпрошлое хранилище").setColor(Palette.RED);
            }

            if (isHover) tooltipText.setEnabled(true);
            else tooltipText.setEnabled(false);
        });

        return button;
    }

}
