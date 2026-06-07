package cupcake.nikky.imperium.tower.filter;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.card.DivinationCardItem;
import cupcake.nikky.imperium.tower.item.exact.fragment.FragmentItem;
import cupcake.nikky.imperium.tower.item.exact.incubator.IncubatorItem;
import cupcake.nikky.imperium.tower.item.exact.mask.MaskItem;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationItem;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.tree.TreeIcon;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.exact.LootFilterSettings;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.func.mod.reactive.ReactiveButton;
import me.func.mod.ui.menu.selection.Selection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LootFilterSelection {

    @Getter
    private static final LootFilterSelection instance = new LootFilterSelection();

    private final ItemStack rareIcon = ItemObject.MIXED_REAGENT_VERGE_MOD_ITEM.getItem().createNew();
    private final ItemStack levelIcon = ItemObject.ORB_OF_DISTORTION_MOD_ITEM.getItem().createNew();
    private final ItemStack aspectIcon = ItemObject.ATTRACTION_ASPECT.getItem().createNew();
    private final ItemStack exactItemsIcon = ItemObject.DROP_OF_EDGE_MOD_ITEM.getItem().createNew();
    private final ItemStack gearIcon = new ItemStack(Material.IRON_CHESTPLATE);
    private final ItemStack cardIcon = ItemObject.DIVINATION_CARD_1.getItem().createNew();
    private final ItemStack incubatorIcon = ItemObject.INCUBATOR_RANDOM_CATALYST_ITEM.getItem().createNew();
    private final ItemStack maskIcon = TreeIcon.MASK.getIcon();

    public void open(@NotNull final Player player) {
        val selectionBuilder = Selection.builder().title("Настройки фильтра предметов").rows(4).columns(2);

        val lootFilterSettings = player.<User>getBungeePlayer().findLootFilterSettings();
        if (lootFilterSettings == null) return;

        selectionBuilder.storage(
                this.updateRareButton(new ReactiveButton(), lootFilterSettings),
                this.updateItemButton(new ReactiveButton(), lootFilterSettings),
                this.updateGearButton(new ReactiveButton(), lootFilterSettings),
                this.updateAspectButton(new ReactiveButton(), lootFilterSettings),
                this.createExactItemsIgnoreButton(new ReactiveButton(), lootFilterSettings),
                this.createDivinationCardsIgnoreButton(new ReactiveButton(), lootFilterSettings),
                this.createIncubatorsIgnoreButton(new ReactiveButton(), lootFilterSettings),
                this.createMasksIgnoreButton(new ReactiveButton(), lootFilterSettings)
        );

        selectionBuilder.info("""
                        Игнорируемые предметы будут исчезать при выпадении из монстров.
                        Если у Вас имеется премиум-подписка, то вместо исчезновения,
                        они будут автоматически продаваться.
                        """).build().open(player);
    }

    private ReactiveButton createMasksIgnoreButton(@NotNull final ReactiveButton button, @NotNull final LootFilterSettings lootFilterSettings) {
        button.item(maskIcon);
        button.title("Слепки Грани");
        button.description("&7Предметов\n&7игнорируется: &6" + lootFilterSettings.getMasks().size());
        button.hover("&7Позволяет &6игнорировать &7конкретные слепки Грани,\n&7которые Вы выберете в меню\n\n\uE3FE &fОткрыть меню");
        button.hint("Открыть");
        button.onLeftClick((player, i, reactiveButton) -> {
            val selection = Selection.builder()
                    .title("Выбор предметов")
                    .hint("Изменить")
                    .columns(3).rows(4).build();

            final List<ReactiveButton> storage = new ArrayList<>();

            final List<ItemObject> items = Arrays.stream(ItemObject.values())
                    .filter(itemObject -> itemObject.getItem().checkInstance(MaskItem.class))
                    .collect(Collectors.toList());

            items.forEach(value -> {
                val item = value.getItem();

                final List<String> description = new ArrayList<>();
                description.add(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName());
                description.addAll(item.generateDescription());

                storage.add(new ReactiveButton()
                        .title(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName())
                        .item(item.createNew())
                        .hover(description)
                        .description("&fСтатус: &6" + (lootFilterSettings.getMasks().contains(value) ? "Игнорировать" : "Подбирать"))
                        .onLeftClick((player1, i1, reactiveButton1) -> {
                            if (lootFilterSettings.getMasks().contains(value)) {
                                lootFilterSettings.getMasks().remove(value);
                            } else lootFilterSettings.getMasks().add(value);

                            reactiveButton1.setDescription("&fСтатус: &6" + (lootFilterSettings.getMasks().contains(value) ? "Игнорировать" : "Подбирать"));
                            button.setDescription("&7Предметов\n&7игнорируется: &6" + lootFilterSettings.getMasks().size());
                            Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                        })
                );
            });

            selection.setStorage(storage);
            selection.open(player);

            Fantasy.bukkitProvider().sound(player, AdvancedSounds.CLICK_1);
        }); return button;
    }

    private ReactiveButton createIncubatorsIgnoreButton(@NotNull final ReactiveButton button, @NotNull final LootFilterSettings lootFilterData) {
        button.item(incubatorIcon);
        button.title("Инкубаторы");
        button.description("&7Предметов\n&7игнорируется: &6" + lootFilterData.getIncubators().size());
        button.hover("&7Позволяет &6игнорировать &7конкретные кристаллы-инкубаторы,\n&7которые Вы выберете в меню\n\n\uE3FE &fОткрыть меню");
        button.hint("Открыть");
        button.onLeftClick((player, i, reactiveButton) -> {
            val selection = Selection.builder()
                    .title("Выбор предметов")
                    .hint("Изменить")
                    .columns(3).rows(4).build();

            final List<ReactiveButton> storage = new ArrayList<>();

            final List<ItemObject> items = Arrays.stream(ItemObject.values())
                    .filter(itemObject -> itemObject.getItem().checkInstance(IncubatorItem.class))
                    .collect(Collectors.toList());

            items.forEach(value -> {
                val item = value.getItem();

                final List<String> description = new ArrayList<>();
                description.add(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName());
                description.addAll(item.generateDescription());

                storage.add(new ReactiveButton()
                        .title(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName())
                        .item(item.createNew())
                        .hover(description)
                        .description("&fСтатус: &6" + (lootFilterData.getIncubators().contains(value) ? "Игнорировать" : "Подбирать"))
                        .onLeftClick((player1, i1, reactiveButton1) -> {
                            if (lootFilterData.getIncubators().contains(value)) {
                                lootFilterData.getIncubators().remove(value);
                            } else lootFilterData.getIncubators().add(value);

                            reactiveButton1.setDescription("&fСтатус: &6" + (lootFilterData.getIncubators().contains(value) ? "Игнорировать" : "Подбирать"));
                            button.setDescription("&7Предметов\n&7игнорируется: &6" + lootFilterData.getIncubators().size());
                            Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                        })
                );
            });

            selection.setStorage(storage);
            selection.open(player);

            Fantasy.bukkitProvider().sound(player, AdvancedSounds.CLICK_1);
        }); return button;
    }

    private ReactiveButton createDivinationCardsIgnoreButton(@NotNull final ReactiveButton button, @NotNull final LootFilterSettings lootFilterData) {
        button.item(cardIcon);
        button.title("Гадальные\nкарты");
        button.description("&7Предметов\n&7игнорируется: &6" + lootFilterData.getDivinationCardsToIgnore().size());
        button.hover("&7Позволяет &6игнорировать &7конкретные гадальные карты,\n&7которые Вы выберете в меню\n\n\uE3FE &fОткрыть меню");
        button.hint("Открыть");
        button.onLeftClick((player, i, reactiveButton) -> {
            val selection = Selection.builder()
                    .title("Выбор предметов")
                    .hint("Изменить")
                    .columns(3).rows(4).build();

            final List<ReactiveButton> storage = new ArrayList<>();

            final List<ItemObject> items = Arrays.stream(ItemObject.values())
                    .filter(itemObject -> itemObject.getItem().checkInstance(DivinationCardItem.class))
                    .collect(Collectors.toList());

            items.forEach(value -> {
                val item = value.getItem();

                final List<String> description = new ArrayList<>();
                description.add(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName());
                description.addAll(item.generateDescription());

                storage.add(new ReactiveButton()
                        .title(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName())
                        .item(item.createNew())
                        .hover(description)
                        .description("&fСтатус: &6" + (lootFilterData.getDivinationCardsToIgnore().contains(value) ? "Игнорировать" : "Подбирать"))
                        .onLeftClick((player1, i1, reactiveButton1) -> {
                            if (lootFilterData.getDivinationCardsToIgnore().contains(value)) {
                                lootFilterData.getDivinationCardsToIgnore().remove(value);
                            } else lootFilterData.getDivinationCardsToIgnore().add(value);

                            reactiveButton1.setDescription("&fСтатус: &6" + (lootFilterData.getDivinationCardsToIgnore().contains(value) ? "Игнорировать" : "Подбирать"));
                            button.setDescription("&7Предметов\n&7игнорируется: &6" + lootFilterData.getDivinationCardsToIgnore().size());
                            Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                        })
                );
            });

            selection.setStorage(storage);
            selection.open(player);

            Fantasy.bukkitProvider().sound(player, AdvancedSounds.CLICK_1);
        }); return button;
    }

    private ReactiveButton createExactItemsIgnoreButton(@NotNull final ReactiveButton button, @NotNull final LootFilterSettings lootFilterData) {
        button.item(exactItemsIcon);
        button.title("Базовые\nпредметы");
        button.description("&7Предметов\n&7игнорируется: &6" + lootFilterData.getItemsToIgnore().size());
        button.hover("&7Позволяет &6игнорировать &7конкретные предметы,\n&7которые Вы выберете в меню\n\n\uE3FE &fОткрыть меню");
        button.hint("Открыть");
        button.onLeftClick((player, i, reactiveButton) -> {
            val selection = Selection.builder()
                    .title("Выбор предметов")
                    .hint("Изменить")
                    .columns(3).rows(4).build();

            final List<ReactiveButton> storage = new ArrayList<>();

            final List<ItemObject> items = Arrays.stream(ItemObject.values())
                    .filter(itemObject -> itemObject.getItem().checkInstance(ModificationItem.class) ||
                            itemObject == ItemObject.RESET_ORB ||
                            itemObject == ItemObject.STACKED_DECK ||
                            itemObject.getItem().checkInstance(FragmentItem.class))
                    .toList();

            items.forEach(value -> {
                val item = value.getItem();

                final List<String> description = new ArrayList<>();
                description.add(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName());
                description.addAll(item.generateDescription());

                storage.add(new ReactiveButton()
                        .title(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName())
                        .item(item.createNew())
                        .hover(description)
                        .description("&fСтатус: &6" + (lootFilterData.getItemsToIgnore().contains(value) ? "Игнорировать" : "Подбирать"))
                        .onLeftClick((player1, i1, reactiveButton1) -> {
                            if (lootFilterData.getItemsToIgnore().contains(value)) {
                                lootFilterData.getItemsToIgnore().remove(value);
                            } else lootFilterData.getItemsToIgnore().add(value);

                            reactiveButton1.setDescription("&fСтатус: &6" + (lootFilterData.getItemsToIgnore().contains(value) ? "Игнорировать" : "Подбирать"));
                            button.setDescription("&7Предметов\n&7игнорируется: &6" + lootFilterData.getItemsToIgnore().size());
                            Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                        })
                );
            });

            selection.setStorage(storage);
            selection.open(player);

            Fantasy.bukkitProvider().sound(player, AdvancedSounds.CLICK_1);
        }); return button;
    }

    private ReactiveButton updateRareButton(@NotNull final ReactiveButton button, @NotNull final LootFilterSettings lootFilterData) {
        return button.item(rareIcon)
                .title("Мин. редкость\nснаряжения")
                .description("&7Мин. редкость:\n" + lootFilterData.getMinimumRare().getColoredName())
                .hover("&7Позволяет &6игнорировать &7снаряжение,\n&6редкость &7которого меньше заданной\n\n\uE3FE &fПовысить\n\uE3FF &fПонизить")
                .hint("Изменить")
                .onLeftClick((player, i, reactiveButton) -> {
                    val next = ItemRare.findByPosition(lootFilterData.getMinimumRare().getPosition() + 1);
                    if (next == null) {
                        Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Данная редкость максимально возможная!");
                        return;
                    }

                    lootFilterData.setMinimumRare(next);
                    Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                    this.updateRareButton(reactiveButton, lootFilterData);
                }).onRightClick((player, i, reactiveButton) -> {
                    val next = ItemRare.findByPosition(lootFilterData.getMinimumRare().getPosition() - 1);
                    if (next == null || next == ItemRare.NONE) {
                        Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Данная редкость минимально возможная!");
                        return;
                    }

                    lootFilterData.setMinimumRare(next);
                    Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                    this.updateRareButton(reactiveButton, lootFilterData);
                });
    }

    private ReactiveButton updateItemButton(@NotNull final ReactiveButton button, @NotNull final LootFilterSettings lootFilterData) {
        return button.item(levelIcon)
                .title("Мин. уровень\nснаряжения")
                .description("&7Мин. уровень: &6" + lootFilterData.getMinimumItemLevel())
                .hover("&7Позволяет &6игнорировать снаряжение,\n&6уровень &7которого меньше заданного\n\n\uE3FE &fПовысить\n\uE3FF &fПонизить")
                .hint("Изменить")
                .onLeftClick((player, i, reactiveButton) -> {
                    val next = lootFilterData.getMinimumItemLevel() + 1;
                    if (next > LevelStorage.getInstance().getArmorLevel()) {
                        Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR,
                                "Максимальный уровень снаряжения - %d!".formatted(LevelStorage.getInstance().getArmorLevel()));
                        return;
                    }

                    lootFilterData.setMinimumItemLevel(next);
                    Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                    this.updateItemButton(reactiveButton, lootFilterData);
                }).onRightClick((player, i, reactiveButton) -> {
                    val next = lootFilterData.getMinimumItemLevel() - 1;
                    if (next < 1) {
                        Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Минимальный уровень снаряжения - 1!");
                        return;
                    }

                    lootFilterData.setMinimumItemLevel(next);
                    Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                    this.updateItemButton(reactiveButton, lootFilterData);
                });
    }

    private ReactiveButton updateGearButton(@NotNull final ReactiveButton button, @NotNull final LootFilterSettings lootFilterData) {
        button.item(gearIcon);
        button.title("Снаряжение");
        button.hover("&7Позволяет &6игнорировать &7конкретное снаряжение,\n&7которые Вы выберете в меню\n\n\uE3FE &fОткрыть меню");
        button.hint("Открыть");
        button.onLeftClick((player, i, reactiveButton) -> {
            val selection = Selection.builder()
                    .title("Выбор снаряжения")
                    .hint("Изменить")
                    .columns(3).rows(4).build();

            final List<ReactiveButton> storage = new ArrayList<>();

            final List<ItemObject> items = Arrays.stream(ItemObject.values())
                    .filter(itemObject -> itemObject.getItem().getImperiumType() == ItemType.ARMOR || itemObject.getItem().getImperiumType() == ItemType.WEAPON)
                    .collect(Collectors.toList());

            items.forEach(value -> {
                val item = value.getItem().create();
                if (item.getRare() == ItemRare.COMMON)
                    ModifierProvider.getInstance().randomizeItemRarity(item, LevelStorage.getInstance().getArmorLevel(), List.of(ItemRare.COMMON));
                ModifierProvider.getInstance().createHighestBaseModifiers(item);
                item.updateAttributes();

                final List<String> description = new ArrayList<>();
                description.add(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName());
                description.addAll(item.processDescriptionContainer().getAdvancedLore());

                val status = lootFilterData.getGearToIgnore().getOrDefault(value, 0);
                val readableStatus = status == 0 ? "Подбирать" : status == 1 ? "Подбирать с мод. направления" : "Игнорировать";

                storage.add(new ReactiveButton()
                        .title(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName())
                        .item(item.createNew())
                        .hover(description)
                        .description("&fСтатус: &6" + readableStatus)
                        .onLeftClick((player1, i1, reactiveButton1) -> {
                            val currentStatus = lootFilterData.getGearToIgnore().getOrDefault(value, 0);
                            val nextStatus = currentStatus == 2 ? 0 : currentStatus + 1;

                            lootFilterData.getGearToIgnore().put(value, nextStatus);
                            val currentReadableStatus = nextStatus == 0 ? "Подбирать" : nextStatus == 1 ? "Подбирать с мод. направления" : "Игнорировать";

                            reactiveButton1.setDescription("&fСтатус: &6" + currentReadableStatus);
                            Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                        })
                );
            });

            selection.setStorage(storage);
            selection.open(player);

            Fantasy.bukkitProvider().sound(player, AdvancedSounds.CLICK_1);
        }); return button;
    }

    private ReactiveButton updateAspectButton(@NotNull final ReactiveButton button, @NotNull final LootFilterSettings lootFilterData) {
        button.item(aspectIcon);
        button.title("Аспекты");
        button.description("&7Аспектов\n&7игнорируется: &6" + lootFilterData.getAspectsToIgnore().size());
        button.hover("&7Позволяет &6игнорировать &7конкретные аспекты,\n&7которые Вы выберете в меню\n\n\uE3FE &fОткрыть меню");
        button.hint("Открыть");
        button.onLeftClick((player, i, reactiveButton) -> {
            val selection = Selection.builder()
                    .title("Выбор аспектов")
                    .hint("Изменить")
                    .columns(3).rows(4).build();

            final List<ReactiveButton> storage = new ArrayList<>();

            final List<ItemObject> items = Arrays.stream(ItemObject.values())
                    .filter(itemObject -> itemObject.getItem().getImperiumType() == ItemType.ASPECT)
                    .collect(Collectors.toList());

            items.forEach(value -> {
                val item = value.getItem();
                final List<String> description = new ArrayList<>();
                description.add(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName());
                description.addAll(item.generateDescription());

                storage.add(new ReactiveButton()
                        .title(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName())
                        .item(item.createNew())
                        .hover(description)
                        .description("&fСтатус: &6" + (lootFilterData.getAspectsToIgnore().contains(value) ? "Игнорировать" : "Подбирать"))
                        .onLeftClick((player1, i1, reactiveButton1) -> {
                            if (lootFilterData.getAspectsToIgnore().contains(value)) {
                                lootFilterData.getAspectsToIgnore().remove(value);
                            } else lootFilterData.getAspectsToIgnore().add(value);

                            reactiveButton1.setDescription("&fСтатус: &6" + (lootFilterData.getAspectsToIgnore().contains(value) ? "Игнорировать" : "Подбирать"));
                            button.description("&7Аспектов\n&7игнорируется: &6" + lootFilterData.getAspectsToIgnore().size());
                            Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                        })
                );
            });

            selection.setStorage(storage);
            selection.open(player);

            Fantasy.bukkitProvider().sound(player, AdvancedSounds.CLICK_1);
        }); return button;
    }

}
