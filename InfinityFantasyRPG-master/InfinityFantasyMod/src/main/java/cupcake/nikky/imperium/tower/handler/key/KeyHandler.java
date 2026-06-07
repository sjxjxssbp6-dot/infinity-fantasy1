package cupcake.nikky.imperium.tower.handler.key;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.lwjgl.input.Keyboard;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.util.ModTransfer;

import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum KeyHandler {

    QUESTS(KeyContainer.of(Keyboard.KEY_J, "Меню", "Журнал заданий"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("quests-click", .5)) return;
        new ModTransfer().send("imp:quests-click");
    }),
    STORAGE_PREMIUM(KeyContainer.of(Keyboard.KEY_U, "Меню", "Хранилище"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("storage-click", .5)) return;
        new ModTransfer().send("imp:storage-click");
    }),
    TREE(KeyContainer.of(Keyboard.KEY_K, "Меню", "Древо искажений"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("tree-click", .5)) return;
        new ModTransfer().send("imp:tree-click");
    }),
    DONATE_SHOP(KeyContainer.of(Keyboard.KEY_F10, "Меню", "Донат-магазин"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("donate-shop-click", .5)) return;
        new ModTransfer().send("imp:d-shop-click");
    }),
    SETTINGS(KeyContainer.of(Keyboard.KEY_L, "Меню", "Настройки"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("settings-click", .5)) return;
        mod.getSettingsContext().open();
    }),
    BATTLEPASS(KeyContainer.of(Keyboard.KEY_F6, "Меню", "Боевой пропуск"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("battlepass-click", .5)) return;
        new ModTransfer().send("imp:battlepass");
    }),
    BESTIARY(KeyContainer.of(Keyboard.KEY_F4, "Меню", "Бестиарий"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("best-click", .5)) return;
        new ModTransfer().send("imp:best-click");
    }),
    TELEPORT(KeyContainer.of(Keyboard.KEY_N, "Меню", "Телепорты"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("tp-navigate", .5)) return;
        new ModTransfer().send("imp:tp-navigate");
    }),
    LOOT_FILTER_SETTINGS(KeyContainer.of(Keyboard.KEY_O, "Меню", "Фильтр предметов"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("loot-filter-click", .5)) return;
        new ModTransfer().send("imp:filter-click");
    }),
    FIRMOLOG(KeyContainer.of(Keyboard.KEY_H, "Меню", "Фирмолог"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("firm-click", .5)) return;
        new ModTransfer().send("imp:firm-click");
    }),
    HELP(KeyContainer.of(Keyboard.KEY_F8, "Меню", "Подсказки"), mod -> {
        if (Enginex.getCurrentScreen() == null)
            mod.getGuideContext().open();
    }),
    GENERAL(KeyContainer.of(Keyboard.KEY_B, "Меню", "Выбор визуала"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("general-click", .5)) return;
        new ModTransfer().send("imp:general-click");
    }),
    MOUNT_USE(KeyContainer.of(Keyboard.KEY_C, "Использование", "Спутник"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("mount-use", .5)) return;
        new ModTransfer().send("imp:mount-use");
    }),
    ACHIEVEMENT(KeyContainer.of(Keyboard.KEY_I, "Меню", "Достижения"), mod -> {
        if (!CooldownService.getInstance().getAndExtendCooldown("achievement-click", .5)) return;
        new ModTransfer().send("imp:achieve-click");
    });

    private final KeyContainer container;
    private final Consumer<FantasyMod> handler;

    public static Consumer<FantasyMod> findHandler(final int key) {
        for (KeyHandler value : values()) {
            if (value.container().key() == key) return value.handler();
        }

        return null;
    }

}
