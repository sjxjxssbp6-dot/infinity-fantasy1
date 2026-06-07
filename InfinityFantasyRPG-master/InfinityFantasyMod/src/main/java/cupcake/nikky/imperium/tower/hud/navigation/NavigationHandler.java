package cupcake.nikky.imperium.tower.hud.navigation;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.data.NavigationStorage;
import cupcake.nikky.imperium.tower.data.UserMetadata;
import cupcake.nikky.imperium.tower.handler.key.KeyHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.cristalix.enginex.util.ButtonColor;
import ru.cristalix.enginex.util.Color;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@RequiredArgsConstructor
public enum NavigationHandler {

    TREE(() -> String.format("[%s]", KeyHandler.TREE.container().keyChar()),
            mod -> {
                if (NavigationStorage.getInstance().isTreeIndicator()) {
                    return "§cДоступны очки искажений!\n§fДрево искажений";
                } else return "Древо искажений";
            },
            mod -> ButtonColor.GREY.getDefaultColor(),
            FantasyImage.TREE_ICON.toTexture(),
            mod -> KeyHandler.TREE.handler().accept(mod)),
    VISUAL_SELECTION(() -> String.format("[%s]", KeyHandler.GENERAL.container().keyChar()),
            fantasyMod -> "Меню с выбором визуала",
            fantasyMod -> ButtonColor.GREY.getDefaultColor(),
            FantasyImage.WARDROBE.toTexture(),
            fantasyMod -> KeyHandler.GENERAL.handler().accept(fantasyMod)),
    BESTIARY(() -> String.format("[%s]", KeyHandler.BESTIARY.container().keyChar()),
            mod -> "Бестиарий",
            mod -> ButtonColor.GREY.getDefaultColor(),
            FantasyImage.BESTIARY.toTexture(),
            mod -> KeyHandler.BESTIARY.handler().accept(mod)),
    LOOT_FILTER(() -> String.format("[%s]", KeyHandler.LOOT_FILTER_SETTINGS.container().keyChar()),
            mod -> "Фильтр предметов",
            mod -> ButtonColor.GREY.getDefaultColor(),
            FantasyImage.LOOT_FILTER.toTexture(),
            mod -> KeyHandler.LOOT_FILTER_SETTINGS.handler().accept(mod)),
    QUESTS(() -> String.format("[%s]", KeyHandler.QUESTS.container().keyChar()),
            mod -> {
                if (UserMetadata.getInstance().isSubscribed()) {
                    return "Журнал заданий";
                } else {
                    return "Журнал заданий\nС премиум подпиской здесь\nВы также сможете брать и сдавать задания";
                }
            },
            mod -> UserMetadata.getInstance().isSubscribed() ? ButtonColor.GREY.getDefaultColor() : ButtonColor.ORANGE.getDefaultColor(),
            FantasyImage.QUESTS.toTexture(),
            mod -> KeyHandler.QUESTS.handler().accept(mod)),
    FIRMOLOG(() -> String.format("[%s]", KeyHandler.FIRMOLOG.container().keyChar()),
            mod -> {
                if (UserMetadata.getInstance().isSubscribed()) {
                    return "Фирмолог";
                } else {
                    return "Фирмолог\nМожно открыть только с премиум подпиской";
                }
            },
            mod -> UserMetadata.getInstance().isSubscribed() ? ButtonColor.GREY.getDefaultColor() : ButtonColor.RED.getDefaultColor(),
            FantasyImage.PAPER.toTexture(),
            mod -> KeyHandler.FIRMOLOG.handler().accept(mod)),
    STORAGE(() -> String.format("[%s]", KeyHandler.STORAGE_PREMIUM.container().keyChar()),
            mod -> {
                if (UserMetadata.getInstance().isSubscribed()) {
                    return "Хранилище";
                } else {
                    return "Хранилище\nМожно открыть только с премиум подпиской";
                }
            },
            mod -> UserMetadata.getInstance().isSubscribed() ? ButtonColor.GREY.getDefaultColor() : ButtonColor.RED.getDefaultColor(),
            FantasyImage.CHEST.toTexture(),
            mod -> KeyHandler.STORAGE_PREMIUM.handler().accept(mod)),
    ACHIEVEMENT(() -> String.format("[%s]", KeyHandler.ACHIEVEMENT.container().keyChar()),
            fantasyMod -> "Достижения",
            fantasyMod -> ButtonColor.GREY.getDefaultColor(),
            FantasyImage.ACHIEVEMENTS.toTexture(),
            fantasyMod -> KeyHandler.ACHIEVEMENT.handler().accept(fantasyMod));

    private final Supplier<String> title;
    private final Function<FantasyMod, String> hoverHandler;
    private final Function<FantasyMod, Color> buttonColorHandler;
    private final String icon;
    private final Consumer<FantasyMod> handlerClick;

}
