package cupcake.nikky.imperium.tower.bestiary;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attribute.process.AttributeFinderSettings;
import cupcake.nikky.imperium.tower.attribute.process.AttributeProcess;
import cupcake.nikky.imperium.tower.client.protocol.GearType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.conduct.ConductType;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterDataHandler;
import cupcake.nikky.imperium.tower.entity.monster.goal.GoalType;
import cupcake.nikky.imperium.tower.entity.monster.statistic.MonsterStatistic;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.ArmorItem;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSpear;
import cupcake.nikky.imperium.tower.item.generator.name.EquipDisplayNameGenerator;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import me.func.mod.reactive.ReactiveButton;
import me.func.mod.ui.menu.selection.Selection;
import me.func.protocol.data.color.GlowColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.item.Items;

import java.util.*;

@RequiredArgsConstructor()
public final class BestiarySelection {

    private final Player player;
    private static final Map<MonsterType, Selection> cachedDrop = new HashMap<>();

    public void open() {
        val user = Fantasy.userService().findById(player.getUniqueId()).orElse(null);
        if (user == null) return;

        val selectionBuilder = Selection.builder().title("&7Бестиарий монстров | Список").rows(4).columns(3).hint("Подробнее");

        Arrays.stream(MonsterType.values())
                .sorted(Comparator.comparingInt(type -> type.getMonsterData().getLevel()))
                .forEach(type -> {
                    val data = type.getMonsterData();
                    val dataHandler = new MonsterDataHandler(data);

                    final List<String> hover = new ArrayList<>();
                    val attributes = dataHandler.applyMap();
                    dataHandler.applyLoot();

                    hover.add("&6" + data.getName());
                    val isNeutral = (data.getConduct() == ConductType.NEUTRAL || data.getGoalType() == GoalType.IMMOBILIZED) &&
                            type != MonsterType.PROJECTION_OF_THE_YOUNG_INDIVIDUAL_BINA && type != MonsterType.PROJECTION_OF_THE_YOUNG_INDIVIDUAL_HOCHMA;
                    hover.add(isNeutral ? "&aНейтральный" : "&cАгрессивный");

                    if (attributes.find(ImperiumAttribute.MAX_PHYSICAL_DAMAGE).getValue() >= 1) {
                        hover.add(String.format("&fФизический урон: &7%.0f-%.0f",
                                attributes.find(ImperiumAttribute.MIN_PHYSICAL_DAMAGE).getValue(),
                                attributes.find(ImperiumAttribute.MAX_PHYSICAL_DAMAGE).getValue())
                        );
                    }
                    if (attributes.find(ImperiumAttribute.MAX_FIRE_DAMAGE).getValue() >= 1) {
                        hover.add(String.format("&fУрон огнем: &c%.0f-%.0f",
                                attributes.find(ImperiumAttribute.MIN_FIRE_DAMAGE).getValue(),
                                attributes.find(ImperiumAttribute.MAX_FIRE_DAMAGE).getValue())
                        );
                    }
                    if (attributes.find(ImperiumAttribute.MAX_COLD_DAMAGE).getValue() >= 1) {
                        hover.add(String.format("&fУрон холодом: &b%.0f-%.0f",
                                attributes.find(ImperiumAttribute.MIN_COLD_DAMAGE).getValue(),
                                attributes.find(ImperiumAttribute.MAX_COLD_DAMAGE).getValue())
                        );
                    }
                    if (attributes.find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).getValue() >= 1) {
                        hover.add(String.format("&fУрон молнией: &e%.0f-%.0f",
                                attributes.find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).getValue(),
                                attributes.find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).getValue())
                        );
                    }
                    if (attributes.find(ImperiumAttribute.MAX_CHAOS_DAMAGE).getValue() >= 1) {
                        hover.add(String.format("&fУрон хаосом: &5%.0f-%.0f",
                                attributes.find(ImperiumAttribute.MIN_CHAOS_DAMAGE).getValue(),
                                attributes.find(ImperiumAttribute.MAX_CHAOS_DAMAGE).getValue())
                        );
                    }

                    attributes.getAll().stream()
                            .filter(pair -> pair.getFirst() != ImperiumAttribute.HEALTH)
                            .filter(pair -> pair.getFirst() != ImperiumAttribute.FIRE_RESISTANCE_MAXIMUM)
                            .filter(pair -> pair.getFirst() != ImperiumAttribute.COLD_RESISTANCE_MAXIMUM)
                            .filter(pair -> pair.getFirst() != ImperiumAttribute.LIGHTNING_RESISTANCE_MAXIMUM)
                            .filter(pair -> pair.getFirst() != ImperiumAttribute.CHAOS_RESISTANCE_MAXIMUM)
                            .sorted(Comparator.comparingInt(pair -> pair.getFirst().getPosition()))
                            .forEach(pair -> {
                                val value = pair.getSecond().getValue();
                                val attribute = (ImperiumAttribute) pair.getFirst();

                                if (attribute.getDescriptionText().equals("unknown")) return;

                                if (Math.ceil(value) != 0D) {
                                    var percent = AttributeProcess.of(AttributeFinderSettings.builder().value(value).attribute(attribute).build())
                                            .findValueWithLimit();
                                    if (attribute == ImperiumAttribute.MOVEMENT_SPEED && percent < 0)
                                        percent = 0D;
                                    if (Math.ceil(percent) != 0D)
                                        hover.add(attribute.generateWithValue(percent));
                                }
                            });

                    hover.add(data.getAttacksDescription());

                    var hasExtraLevel = type == MonsterType.PROJECTION_OF_THE_YOUNG_INDIVIDUAL_BINA || type == MonsterType.PROJECTION_OF_THE_YOUNG_INDIVIDUAL_HOCHMA || type == MonsterType.MASK_ENTITY;

                    selectionBuilder.storage(new ReactiveButton()
                            .title((data.isUnique() ? "&6" : "&f") + data.getName())
                            .description("&7Уровень: &e%d%s".formatted(data.getLevel(), hasExtraLevel ? "+" : ""))
                            .hover(TextUtility.colorize(hover))
                            .item(Items.builder().type(Material.MONSTER_EGG).meta(SpawnEggMeta.class, spawnEggMeta -> spawnEggMeta.setSpawnedType(data.getEntityType())).build())
                            .onLeftClick((player1, i, reactiveButton) -> {
                                if (cachedDrop.get(type) != null) {
                                    cachedDrop.get(type).open(player1);
                                } else {
                                    val selection = this.createDropSelection(data, type);
                                    cachedDrop.put(type, selection);
                                    selection.open(player1);
                                }
                            }));
        });

        selectionBuilder.build().open(player);
    }

    private Selection createDropSelection(@NotNull final MonsterData data, @NotNull final MonsterType type) {
        val lootWeightPool = data.getLootWeightPool();
        lootWeightPool.finalizePool();

        val selectionBuilder = Selection.builder().hint("").title("&7Бестиарий монстров | Предметы: " + data.getName()).rows(4).columns(3);

        val storage = lootWeightPool.copyStorage();
        final double totalWeight = lootWeightPool.getInitialSize() == 0 ? lootWeightPool.findTotalSize() : lootWeightPool.getInitialSize();

        storage.stream().sorted(Comparator.comparingDouble(weight -> -weight.weightWrapper().getWeight() / totalWeight * 100D)).forEach(weight -> {
            val item = weight.weightWrapper().getWrapped().getItem().create();

            if (item.getImperiumType() == ItemType.ARMOR || item.getImperiumType() == ItemType.WEAPON) {
                if (item.getRare() == ItemRare.COMMON) {
                    var displayNameGenerator = EquipDisplayNameGenerator.DisplayNameType.NEUTRAL;
                    if (item.checkInstance(ArmorItem.class)) {
                        val gearType = item.apply(ArmorItem.class).getGearType();
                        if (gearType == GearType.LEGGINGS || gearType == GearType.BOOTS)
                            displayNameGenerator = EquipDisplayNameGenerator.DisplayNameType.MANY;
                        else displayNameGenerator = EquipDisplayNameGenerator.DisplayNameType.ONE;
                    } else if (item.checkInstance(BaseSpear.class)) {
                        displayNameGenerator = EquipDisplayNameGenerator.DisplayNameType.NEUTRAL;
                    } else displayNameGenerator = EquipDisplayNameGenerator.DisplayNameType.ONE;

                    item.setName(EquipDisplayNameGenerator.getInstance().generate(type, displayNameGenerator) + " " + item.getName());

                    ModifierProvider.getInstance().randomizeItemRarity(item, Math.min(data.getLevel(), LevelStorage.getInstance().getArmorLevel()), Collections.singletonList(ItemRare.COMMON));
                }

                ModifierProvider.getInstance().createAverageBaseModifiers(item);
            }

            if (item.getImperiumType() == ItemType.SHARD_BREAKTHROUGH) {
                item.getGeneralSettings().itemLevel(Math.min(data.getLevel(), MonsterStatistic.getInstance().findHigherMonsterLevel()));
            }

            item.updateAttributes();

            final List<String> itemHover = new ArrayList<>();

            itemHover.add(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName());
            itemHover.addAll(item.processDescriptionContainer().getAdvancedLore());

            val lootGeneratorSettings = item.getLootSettings().lootGeneratorSettings();

            if (item.getRare() == ItemRare.COMMON && lootGeneratorSettings.rarity()) {
                itemHover.add(" ");
                itemHover.add("&fШанс редкости, " + ItemRare.COMMON.getColoredName() + "&f: &6" + lootGeneratorSettings.commonChance() + "%");
                itemHover.add("&fШанс редкости, " + ItemRare.RARE.getColoredName() + "&f: &6" + lootGeneratorSettings.rareChance() + "%");
                itemHover.add("&fШанс редкости, " + ItemRare.EPIC.getColoredName() + "&f: &6" + lootGeneratorSettings.epicChance() + "%");
                if (data.getLevel() >= LevelStorage.getInstance().getStartShardEdgeLevel() && lootGeneratorSettings.exaltedModifiers())
                    itemHover.add("&fШанс редкости, " + ItemRare.EXALTED.getColoredName() + "&f: &60.001%");
            }

            val amount = weight.weightWrapper().getAmount();
            val textAmount = 1 == amount ? amount : "1-" + amount;

            selectionBuilder.storage(new ReactiveButton()
                    .title(item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName())
                    .hover(itemHover)
                    .description(
                            "&7Количество: &e" + textAmount,
                            String.format("&7Шанс (&cвес&7): &e%.4f%% &7(&c%d&7)", weight.weightWrapper().getWeight() / totalWeight * 100D, weight.weightWrapper().getWeight()))
                    .item(item.createNew(1)));
        });

        return storage.isEmpty() ?
                selectionBuilder.columns(3).rows(3)
                        .storage(new ReactiveButton().material(Material.AIR),
                                new ReactiveButton().material(Material.AIR),
                                new ReactiveButton().material(Material.AIR),
                                new ReactiveButton().material(Material.AIR),
                                new ReactiveButton().color(GlowColor.RED_DARK).material(Material.BARRIER).title("&cПусто").description("&eПредметы отсутствуют"),
                                new ReactiveButton().material(Material.AIR),
                                new ReactiveButton().material(Material.AIR),
                                new ReactiveButton().material(Material.AIR),
                                new ReactiveButton().material(Material.AIR))
                        .build()
                : selectionBuilder.build();
    }

}
