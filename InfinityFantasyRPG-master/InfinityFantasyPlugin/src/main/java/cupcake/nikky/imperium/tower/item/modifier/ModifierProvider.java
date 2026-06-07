package cupcake.nikky.imperium.tower.item.modifier;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.exact.fragment.FragmentItem;
import cupcake.nikky.imperium.tower.item.exact.modification.rule.StarRule;
import cupcake.nikky.imperium.tower.rng.Weight;
import cupcake.nikky.imperium.tower.rng.WeightPool;
import cupcake.nikky.imperium.tower.rng.exact.modifier.WeightModifierWrapper;
import cupcake.nikky.imperium.tower.user.User;
import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModifierProvider {

    @Getter private static final ModifierProvider instance = new ModifierProvider();

    public void createExtendedModifiers(@NotNull final ImperiumItem item, @NotNull final ModifierCreatorSettings settings) {
        this.createExtendedModifiers(item, settings, false);
    }

    public void createExtendedModifiers(@NotNull final ImperiumItem item, @NotNull final ModifierCreatorSettings settings, final boolean exaltedModifiers) {
        val created = item.getModifierSettings().findSpecificByPlace(ModifierPlace.SUFFIX, ModifierPlace.PREFIX);
        val possible = item.getModifierSettings().findPossibleByPlace(ModifierPlace.SUFFIX, ModifierPlace.PREFIX);
        possible.removeIf(modifier ->
                created.stream().anyMatch(specificModifier -> specificModifier.getModifier().getId().equals(modifier.getId())) ||
                        !modifier.getItemTarget().stream().allMatch(itemFilter -> itemFilter.isSuitedItem(item)));

        val chance = (exaltedModifiers && item.getRare().highestThanOrEqual(ItemRare.EPIC)) ? MathUtility.randomInRange(0D, 100D) : -1D;

        settings.getStorage().forEach((place, amount) -> {
            if (amount <= 0) return;

            val modifiersByPlace = possible.stream().filter(modifier -> modifier.getPlace() == place).collect(Collectors.toList());
            val generatedPool = this.createWeightPool(modifiersByPlace, amount, item.getGeneralSettings().itemLevel(), item.checkInstance(FragmentItem.class)).generatePool();

            val exaltedId = (chance == -1D || chance > 1D) ? "" : generatedPool.get(MathUtility.randomInRange(0, generatedPool.size() - 1)).weightWrapper().getWrapped().getId();

            generatedPool.forEach(weight -> {
                val wrapper = weight.weightWrapper();
                val tier = wrapper.getTier();
                val modifier = wrapper.getWrapped();

                if (item.getModifierSettings().findSpecificById(modifier.getId()) != null) return;

                val finalTier = modifier.getId().equals(exaltedId) && item.getRare() != ItemRare.EXALTED ?
                        Optional.ofNullable(modifier.getRange().findExaltedTier()).map(ModifierTier::tier).orElse(tier) : tier;
                item.getModifierSettings().registerSpecificModifier(modifier.process().type(ModifierType.EXTENDED).apply(finalTier));

                if (modifier.getId().equals(exaltedId)) {
                    item.setRare(ItemRare.EXALTED);
                }
            });
        });
    }

    public void createRandomizedExtendedModifiers(@NotNull final ImperiumItem item) {
        this.createRandomizedExtendedModifiers(item, false);
    }

    public void createRandomizedExtendedModifiers(@NotNull final ImperiumItem item, final boolean exalted) {
        val min = item.getRare() == ItemRare.COMMON ? 0 : 1;
        if (min <= 0) return;

        val prefixAvailable = item.getRare().getModifierSize() - item.getModifierSettings().findSpecificByPlace(ModifierPlace.PREFIX).size();
        val suffixAvailable = item.getRare().getModifierSize() - item.getModifierSettings().findSpecificByPlace(ModifierPlace.SUFFIX).size();

        this.createExtendedModifiers(item, new ModifierCreatorSettings()
                .registerPlace(ModifierPlace.PREFIX, MathUtility.randomInRange(min, prefixAvailable))
                .registerPlace(ModifierPlace.SUFFIX, MathUtility.randomInRange(min, suffixAvailable)),
                exalted
        );
    }

    public void createRandomizedDirectionModifiers(@NotNull final ImperiumItem item) {
        this.createRandomizedDirectionModifiers(item, true, size -> size * 10);
    }

    public void createRandomizedDirectionModifiers(@NotNull final ImperiumItem item, final boolean canBeEmpty) {
        this.createRandomizedDirectionModifiers(item, canBeEmpty, size -> size * 10);
    }

    public void createRandomizedDirectionModifiers(@NotNull final ImperiumItem item, @NotNull final User user) {
        if (!user.findFragmentSettings().isInFragment()) {
            this.createRandomizedDirectionModifiers(item, true);
            return;
        }
        this.createRandomizedDirectionModifiers(item, true, size -> {
            var corruption = (double) user.findFragmentSettings().getFragmentCorruption();
            var maxCorruption = (double) StarRule.FRAGMENT_STAR_RULE.corruptLimit();

            return (int) Math.round(size * (10 - (corruption / maxCorruption) * 5));
        });
    }

    public void createRandomizedDirectionModifiers(@NotNull final ImperiumItem item, final boolean canBeEmpty, Int2IntFunction randomApplier) {
        val possible = item.getModifierSettings().findPossibleByPlace(ModifierPlace.DIRECTION).stream()
                .filter(modifier -> modifier.getItemTarget().isEmpty() || modifier.getItemTarget().stream().allMatch(itemFilter -> itemFilter.isSuitedItem(item)))
                .collect(Collectors.toList());
        val exists = item.getModifierSettings().findSpecificByPlace(ModifierPlace.DIRECTION).stream().map(SpecificModifier::getModifierId).toList();
        possible.removeIf(modifier -> exists.contains(modifier.getId()));

        val weightPool = this.createWeightPool(possible, 1, item.getGeneralSettings().itemLevel(), item.checkInstance(FragmentItem.class));
        val size = canBeEmpty ? randomApplier.apply(weightPool.findTotalSize()) : weightPool.findTotalSize();
        weightPool.setInitialSize(size);

        weightPool.generatePool().forEach(weight -> {
            val wrapper = weight.weightWrapper();
            val tier = wrapper.getTier();
            val modifier = wrapper.getWrapped();

            item.getModifierSettings().registerSpecificModifier(modifier.process().type(ModifierType.DIRECTION).apply(tier));
        });
    }

    public void createDirectionModifier(@NotNull final ImperiumItem item, @NotNull final Modifier modifier, final int tier) {
        item.getModifierSettings().registerSpecificModifier(modifier.process().type(ModifierType.DIRECTION).apply(tier));
    }

    public void createRandomizedBaseModifiers(@NotNull final ImperiumItem item) {
        this.createBaseModifiers(item, 0);
    }

    public void createLowestBaseModifiers(@NotNull final ImperiumItem item) {
        this.createBaseModifiers(item, 1);
    }

    public void createAverageBaseModifiers(@NotNull final ImperiumItem item) {
        this.createBaseModifiers(item, 2);
    }

    public void createHighestBaseModifiers(@NotNull final ImperiumItem item) {
        this.createBaseModifiers(item, 3);
    }

    public void createBaseModifiers(@NotNull final ImperiumItem item, final int tierState) {
        item.getModifierSettings().findPossibleByPlace(ModifierPlace.BASE)
                .forEach(modifier -> {
                    val itemLevel = item.getGeneralSettings().itemLevel();
                    if (modifier.getRange().findByNumericTier(itemLevel) == null) return;

                    switch (tierState) {
                        case 0: {
                            item.getModifierSettings().registerSpecificModifier(modifier.process().type(ModifierType.BASE).apply(itemLevel));
                            break;
                        }
                        case 1: {
                            item.getModifierSettings().registerSpecificModifier(modifier.process().type(ModifierType.BASE).applyLowest(itemLevel));
                            break;
                        }
                        case 2: {
                            item.getModifierSettings().registerSpecificModifier(modifier.process().type(ModifierType.BASE).applyAverage(itemLevel));
                            break;
                        }
                        case 3: {
                            item.getModifierSettings().registerSpecificModifier(modifier.process().type(ModifierType.BASE).applyHighest(itemLevel));
                            break;
                        }
                    }
                });
    }

    public void randomizeItemRarity(@NotNull final ImperiumItem item, final int itemLevel, @NotNull final List<ItemRare> rares) {
        if (rares.size() > 1)
            Collections.shuffle(rares);

        item.setRare(rares.get(MathUtility.randomInRange(1, rares.size()) - 1));
        item.getGeneralSettings().itemLevel(itemLevel);
    }

    public void applyCraftModifier(@NotNull final ImperiumItem item, @NotNull final Modifier modifier, final int tier) {
        item.getModifierSettings().removeSpecificModifierByType(ModifierType.CRAFTED);
        item.getModifierSettings().registerSpecificModifier(modifier.process().type(ModifierType.CRAFTED).apply(tier));
    }

    public void clearModifiers(@NotNull final ImperiumItem item, @NotNull final ModifierType... types) {
        for (ModifierType type : types)
            item.getModifierSettings().removeSpecificModifierByType(type);
    }

    public void removeModifier(@NotNull final ImperiumItem item, @NotNull final ModifierPlace place, final int amount) {
        int removed = 0;
        val list = item.getModifierSettings().findSpecificByPlace(place);

        Collections.shuffle(list);

        for (SpecificModifier specificModifier : list) {
            if (removed++ >= amount) break;

            item.getModifierSettings().removeSpecificModifier(specificModifier.getModifierId());
        }
    }

    public WeightPool<WeightModifierWrapper> createWeightPool(@NotNull final Collection<Modifier> modifiers, final int repeatTime, final int baseItemLevel, final boolean way) {
        return this.createWeightPool(modifiers, 0, repeatTime, baseItemLevel, way);
    }

    public WeightPool<WeightModifierWrapper> createWeightPool(@NotNull final Collection<Modifier> modifiers, final int initialSize, final int repeatTime, final int baseItemLevel, final boolean way) {
        val pool = new WeightPool<WeightModifierWrapper>(initialSize, repeatTime);
        final Set<String> usedId = new HashSet<>();

        modifiers.forEach(modifier -> modifier.getRange().getTiers().stream()
                .filter(modifierTier -> modifierTier.requireBaseLevel() <= baseItemLevel && !modifierTier.exalted())
                .sorted(Comparator.comparingInt(modifierTier -> -modifierTier.tier()))
                .forEach(modifierTier -> {
                    if (way) {
                        if (usedId.contains(modifier.getId())) return;
                        usedId.add(modifier.getId());
                    }

                    pool.apply(new Weight<>(new WeightModifierWrapper(modifier, modifierTier.weight(), false, modifierTier.tier()))
                            .onSuccess(winner -> 0).onFail(winner -> {
                                if (winner != null && winner.weightWrapper().getWrapped().getId().equals(modifier.getId())) return 0;
                                return modifierTier.weight();
                            })
                    );
                })
        );

        return pool;
    }

}
