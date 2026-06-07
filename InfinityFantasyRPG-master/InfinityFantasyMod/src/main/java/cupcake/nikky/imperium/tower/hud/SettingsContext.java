package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.button.FantasyButton;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsType;
import cupcake.nikky.imperium.tower.data.SettingsStorage;
import dev.xdark.clientapi.gui.Screen;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.ButtonColor;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.EnumMap;
import java.util.Map;

@Getter
public class SettingsContext extends GuiScreen {

    private final Map<SettingsType, Toggle> elements = new EnumMap<>(SettingsType.class);
    private final FantasyMod fantasyMod;
    private boolean changed;

    public SettingsContext(@NotNull final FantasyMod fantasyMod) {
        this.initialize();
        this.fantasyMod = fantasyMod;
    }

    private void initialize() {
        val settings = SettingsStorage.getInstance().getSettings();
        val playerVisibility = new Toggle(settings.getOrDefault(SettingsType.PLAYER_VISIBILITY, true));
        val bloodEffectVisibility = new Toggle(settings.getOrDefault(SettingsType.BLOOD_EFFECT_VISIBILITY, true));
        val questColumnVisibility = new Toggle(settings.getOrDefault(SettingsType.QUEST_COLUMN_VISIBILITY, true));
        val damageVisibility = new Toggle(settings.getOrDefault(SettingsType.DAMAGE_INDICATOR_OWN_VISIBILITY, true));
        val otherDamageVisibility = new Toggle(settings.getOrDefault(SettingsType.DAMAGE_INDICATOR_OTHER_VISIBILITY, true));
        val glowScreen = new Toggle(settings.getOrDefault(SettingsType.GLOW_SCREEN_EFFECT, true));
        val cursorInfo = new Toggle(settings.getOrDefault(SettingsType.CURSOR_INFORMATION, true));
        val hotBarWithChat = new Toggle(settings.getOrDefault(SettingsType.HOT_BAR_WITH_CHAT_VISIBILITY, true));
        val craftView = new Toggle(settings.getOrDefault(SettingsType.CRAFT_VIEW_VISIBILITY, true));

        this.elements.put(SettingsType.PLAYER_VISIBILITY, playerVisibility);
        this.elements.put(SettingsType.BLOOD_EFFECT_VISIBILITY, bloodEffectVisibility);
        this.elements.put(SettingsType.QUEST_COLUMN_VISIBILITY, questColumnVisibility);
        this.elements.put(SettingsType.DAMAGE_INDICATOR_OWN_VISIBILITY, damageVisibility);
        this.elements.put(SettingsType.DAMAGE_INDICATOR_OTHER_VISIBILITY, otherDamageVisibility);
        this.elements.put(SettingsType.GLOW_SCREEN_EFFECT, glowScreen);
        this.elements.put(SettingsType.CURSOR_INFORMATION, cursorInfo);
        this.elements.put(SettingsType.HOT_BAR_WITH_CHAT_VISIBILITY, hotBarWithChat);
        this.elements.put(SettingsType.CRAFT_VIEW_VISIBILITY, craftView);

        this.elements.forEach((type, toggle) -> toggle.onToggle((element, aBoolean) -> changed = true));

        val closeButton = new FantasyButton("Применить");
        closeButton.setButtonColor(ButtonColor.GREY);
        closeButton.setSize(new V3(closeButton.getText().getSize().getX() + 32, 32));
        closeButton.onLeftClick(element -> this.close());

        this.addChild(new CarvedRectangle(4D)
                .addChild(
                        new Text("Настройки").setOrigin(Relative.TOP).setAlign(Relative.TOP).setPos(new V3(0, 16)).setScale(new V3(2, 2)),
                        new VerticalLayout(8D).addChild(
                                this.createToggleElement(playerVisibility, "Скрыть игроков"),
                                this.createToggleElement(bloodEffectVisibility, "Показывать эффект крови"),
                                this.createToggleElement(questColumnVisibility, "Показывать задания"),
                                this.createToggleElement(damageVisibility, "Показывать индикатор своего урона"),
                                this.createToggleElement(otherDamageVisibility, "Показывать индикатор чужого урона"),
                                this.createToggleElement(glowScreen, "Подсветка экрана"),
                                this.createToggleElement(cursorInfo, "Информация возле курсора"),
                                this.createToggleElement(hotBarWithChat, "Показывать интерфейс поверх чата"),
                                this.createToggleElement(craftView, "Показывать предмет при его изменении")
                        ).setOrigin(Relative.LEFT).setAlign(Relative.LEFT).setPos(new V3(14, 0)),
                        closeButton.setOrigin(Relative.BOTTOM).setAlign(Relative.BOTTOM).setPos(new V3(0, -16))
                ).setSize(new V3(500, 425))
                .setColor(Palette.GREY_DARK.alpha(.67))
                .setOrigin(Relative.CENTER).setAlign(Relative.CENTER));
    }

    private AbstractElement createToggleElement(@NotNull final Toggle toggle, @NotNull final String title) {
        return new HorizontalLayout(16D).addChild(toggle, new Text(title)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
    }

    private void updateSettings() {
        this.elements.forEach((settingsType, toggle) -> SettingsStorage.getInstance().getSettings().put(settingsType, toggle.getStatus()));
    }

    public void readCachedSettings() {
        SettingsStorage.getInstance().getSettings().forEach((type, state) -> this.elements.get(type).setStatus(state, true));
    }

    private void sendUpdate() {
        fantasyMod.sendPacket(new SettingsPacket(new Object2BooleanOpenHashMap<>(SettingsStorage.getInstance().getSettings())));
    }

    @Override
    public void keyTyped(Screen screen, char c, int code) {
        if (code == Keyboard.KEY_ESCAPE || code == Enginex.getClientApi().settingsManager().getBindingByName("key.inventory").getKeyCode()) {
            this.close();
        }
    }

    @Override
    public void close() {
        super.close();
        this.updateSettings();
        if (changed) this.sendUpdate();
        changed = false;
    }

}
