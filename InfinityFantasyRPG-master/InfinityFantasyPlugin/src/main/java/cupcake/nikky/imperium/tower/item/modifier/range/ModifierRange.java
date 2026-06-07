package cupcake.nikky.imperium.tower.item.modifier.range;

import cupcake.nikky.imperium.tower.item.modifier.ModifierTier;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModifierRange {

    private final ModifierTier[] tiers;

    public ModifierRange(@NotNull final ModifierTier[] tiers) {
        this.tiers = tiers;
    }

    public static ModifierRange of(@NotNull final ModifierTier... tiers) {
        return new ModifierRange(tiers);
    }

    public List<ModifierTier> getTiers() {
        return Arrays.stream(tiers).map(ModifierTier::copy).collect(Collectors.toList());
    }

    public int size() {
        return tiers.length;
    }

    public int sizeWithoutExalted() {
        return (int) Arrays.stream(tiers).filter(modifierTier -> !modifierTier.exalted()).count();
    }

    public ModifierTier findByNumericTier(final int tier) {
        return Arrays.stream(tiers).filter(Objects::nonNull).filter(modifierTier -> modifierTier.tier() == tier).findAny().orElse(null);
    }

    public ModifierTier findExaltedTier() {
        return Arrays.stream(tiers).filter(ModifierTier::exalted).findAny().orElse(null);
    }

    public ModifierRange copyWithTiers(final int numericTier) {
        return ModifierRange.of(getTiers().stream().filter(modifierTier -> modifierTier.tier() <= numericTier).toArray(ModifierTier[]::new));
    }

}
