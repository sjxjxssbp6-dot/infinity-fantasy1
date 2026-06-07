package cupcake.nikky.imperium.tower.rng.exact.loot;

import cupcake.nikky.imperium.tower.client.protocol.GearType;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.ArmorItem;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSpear;
import cupcake.nikky.imperium.tower.item.generator.name.EquipDisplayNameGenerator;
import cupcake.nikky.imperium.tower.item.modifier.ModifierCreatorSettings;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.rng.Weight;
import cupcake.nikky.imperium.tower.rng.WeightPool;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.PotentialUtility;
import cupcake.nikky.imperium.tower.utility.TriFunction;
import dev.xdark.feder.TriConsumer;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import me.anfanik.steda.api.utility.Pair;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

public class LootWeightPool extends WeightPool<WeightItemWrapper> {

    @Setter @Accessors(fluent = true, chain = true)
    private TriFunction<Monster, User, Pair<ItemObject, Integer>, ItemStack> converter = (monster, killer, pair) -> {
        val item = pair.getFirst().getItem().create();
        val lootGeneratorSettings = item.getLootSettings().lootGeneratorSettings();

        if (lootGeneratorSettings.rarity() && item.getRare() == ItemRare.COMMON) {
            val commonChance = lootGeneratorSettings.commonChance();
            val rareChance = lootGeneratorSettings.rareChance();
            val epicChance = lootGeneratorSettings.epicChance();

            val level = Math.min(monster.getLevel(), lootGeneratorSettings.maxItemLevel());

            val currentRandom = MathUtility.randomInRange(1, 100);
            ItemRare rare;

            if (currentRandom <= commonChance) rare = ItemRare.COMMON;
            else if (currentRandom <= commonChance + rareChance) rare = ItemRare.RARE;
            else if (currentRandom <= commonChance + rareChance + epicChance) rare = ItemRare.EPIC;
            else rare = ItemRare.COMMON;

            ModifierProvider.getInstance().randomizeItemRarity(item, lootGeneratorSettings.maxItemLevel() == -1 ? item.getGeneralSettings().itemLevel() : level, Collections.singletonList(rare));
        }

        if (lootGeneratorSettings.maxItemLevel() != -1 && !lootGeneratorSettings.rarity()) {
            item.getGeneralSettings().itemLevel(Math.min(monster.getLevel(), lootGeneratorSettings.maxItemLevel()));
        }

        if (lootGeneratorSettings.potential()) {
            item.getGearSettings().potential(PotentialUtility.processPotential(monster.getLevel()));
        }

        var maxCorrupt = lootGeneratorSettings.corruptionSettings().maxCorrupt().applyAsInt(monster.getMonsterData());
        if (maxCorrupt > 0) {
            var minCorrupt = lootGeneratorSettings.corruptionSettings().minCorrupt().applyAsInt(monster.getMonsterData());
            var resultCorrupt = MathUtility.randomInRange(minCorrupt, maxCorrupt);

            item.getGearSettings().corrupted(resultCorrupt);
        }

        if (item.getImperiumType() == ItemType.WEAPON || item.getImperiumType() == ItemType.ARMOR) {
            if (item.checkInstance(BaseSpear.class))
                item.setName(EquipDisplayNameGenerator.getInstance().generate(monster.getMonsterType(), EquipDisplayNameGenerator.DisplayNameType.NEUTRAL) + " " + item.getName());
            else if (item.checkInstance(ArmorItem.class)) {
                val type = item.apply(ArmorItem.class).getGearType();
                if (type == GearType.LEGGINGS || type == GearType.BOOTS)
                    item.setName(EquipDisplayNameGenerator.getInstance().generate(monster.getMonsterType(), EquipDisplayNameGenerator.DisplayNameType.MANY) + " " + item.getName());
                else item.setName(EquipDisplayNameGenerator.getInstance().generate(monster.getMonsterType(), EquipDisplayNameGenerator.DisplayNameType.ONE) + " " + item.getName());
            } else item.setName(EquipDisplayNameGenerator.getInstance().generate(monster.getMonsterType(), EquipDisplayNameGenerator.DisplayNameType.ONE) + " " + item.getName());
        }

        if (lootGeneratorSettings.extendedModifiers()) {
            val itemRare = item.getRare();
            if (itemRare.highestThan(ItemRare.COMMON)) {
                ModifierProvider.getInstance().createExtendedModifiers(item, new ModifierCreatorSettings()
                        .registerPlace(ModifierPlace.PREFIX, MathUtility.randomInRange(1, itemRare.getModifierSize()))
                        .registerPlace(ModifierPlace.SUFFIX, MathUtility.randomInRange(1, itemRare.getModifierSize())),
                        lootGeneratorSettings.exaltedModifiers() && monster.getLevel() >= LevelStorage.getInstance().getStartShardEdgeLevel()
                );
            }
        }

        if (lootGeneratorSettings.directionModifiers()) {
            if (lootGeneratorSettings.canBeEmptyDirectionModifier()) {
                ModifierProvider.getInstance().createRandomizedDirectionModifiers(item, killer);
            } else ModifierProvider.getInstance().createRandomizedDirectionModifiers(item, false);
        }

        if (lootGeneratorSettings.baseModifiers()) {
            ModifierProvider.getInstance().createRandomizedBaseModifiers(item);
        }

        return item.createNew(MathUtility.randomInRange(1, pair.getSecond()), killer.getSpigot());
    };

    private final List<TriConsumer<User, Monster, WeightItemWrapper>> preProcesses = new ArrayList<>();

    public LootWeightPool(int initalSize, int repeatTime) {
        super(initalSize, repeatTime);
    }

    public LootWeightPool(int repeatTime) {
        super(repeatTime);
    }

    public static LootWeightPool newInstance(@Nullable final LootWeightPool lootWeightPool) {
        if (lootWeightPool == null)
            return null;

        val pool = new LootWeightPool(lootWeightPool.getInitialSize(), lootWeightPool.getRepeatTime());
        pool.apply(lootWeightPool.copyStorage());
        lootWeightPool.preProcesses.forEach(pool::registerNewPreProcess);
        return pool;
    }

    public static LootWeightPool newInstance(final int initialSize, final int repeatTime) {
        return new LootWeightPool(initialSize, repeatTime);
    }

    public static LootWeightPool newInstance(final int repeatTime) {
        return new LootWeightPool(repeatTime);
    }

    public LootWeightProcess newWeightInstance(@NotNull final ItemObject itemObject, final int amount, final int weight) {
        return new LootWeightProcess(this, new Weight<>(new WeightItemWrapper(itemObject, amount, weight)));
    }

    public LootWeightPool registerNewPreProcess(BiConsumer<User, WeightItemWrapper> preProcess) {
        this.preProcesses.add((user, monster, weightItemWrapper) -> preProcess.accept(user, weightItemWrapper));
        return this;
    }

    public LootWeightPool registerNewPreProcess(TriConsumer<User, Monster, WeightItemWrapper> preProcess) {
        this.preProcesses.add(preProcess);
        return this;
    }

    public List<ItemStack> generateItemPool(@NotNull final User owner, @NotNull final Monster monster, final int repeatTime) {
        this.finalizePool();

        final Map<ItemObject, Integer> result = new EnumMap<>(ItemObject.class);
        val storage = this.copyStorage();
        this.preProcesses.forEach(handler -> storage.forEach(weightItemWrapperWeight -> handler.accept(owner, monster, weightItemWrapperWeight.weightWrapper())));

        val successWeights = this.generatePool(storage, repeatTime);

        successWeights.forEach(weight -> {
            val wrapper = weight.weightWrapper();
            result.put(wrapper.getWrapped(), result.getOrDefault(wrapper.getWrapped(), 0) + wrapper.getAmount());
        });

        return result.keySet().stream()
                .filter(itemObject -> result.get(itemObject) > 0)
                .flatMap(itemObject -> {
                    final List<ItemStack> resultFlat = new ArrayList<>();
                    for (int i = 0; i < result.get(itemObject); i++) {
                        resultFlat.add(this.converter.apply(monster, owner, new Pair<>(itemObject, 1)));
                    }

                    return resultFlat.stream();
                })
                .toList();
    }

    @Override
    public int onSuccess(Weight<WeightItemWrapper> weight) {
        val wrapper = weight.weightWrapper();
        wrapper.setSuccessCounter(wrapper.getSuccessCounter() + 1);
        return super.onSuccess(weight);
    }

    @Override
    public int onFail(Weight<WeightItemWrapper> weight, Weight<WeightItemWrapper> winner) {
        val wrapper = weight.weightWrapper();
        wrapper.setFailCounter(wrapper.getFailCounter() + 1);
        return super.onFail(weight, winner);
    }

}
