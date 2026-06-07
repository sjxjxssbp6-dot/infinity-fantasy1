package cupcake.nikky.imperium.tower.item.modifier.description;

import cupcake.nikky.imperium.tower.item.modifier.ModifierTier;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class ModifierDescription {

    @Getter
    private final String descriptionFormat;

    public String generateDescription(@NotNull final String valueColor, @NotNull final String descriptionColor, @NotNull final SpecificModifier specificModifier) {
        return this.generateDescription(valueColor, descriptionColor, specificModifier.getKey(), 0);
    }

    public String generateDescription(@NotNull final String valueColor, @NotNull final String descriptionColor, final double value, final double amplitude) {
        return getDescriptionFormat().formatted(valueColor, value, descriptionColor);
    }

    public String generateSubCraftDescription(@NotNull final String valueColor, @NotNull final String descriptionColor, @NotNull final ModifierTier modifierTier) {
        return getDescriptionFormat().formatted(valueColor, 0D, descriptionColor)
                .replace(".0", "")
                .replace("0", "%.1f-%.1f".formatted(modifierTier.min(), modifierTier.max()));
    }

    public String generateCraftDescription(@NotNull final String valueColor, @NotNull final String descriptionColor) {
        return getDescriptionFormat().formatted(valueColor, 0D, descriptionColor)
                .replace("0", "#")
                .replace(".0", ".#");
    }

    public String generateAdvancedDescription(@NotNull final String valueColor, @NotNull final String descriptionColor, @NotNull final SpecificModifier specificModifier) {
        var tier = specificModifier.getModifier().getRange().findByNumericTier(specificModifier.findTier());
        var advancedValue = tier.min() == tier.max() ? descriptionColor : "&8(%.1f-%.1f) %s".formatted(tier.min(), tier.max(), descriptionColor);
        return getDescriptionFormat().formatted(valueColor, specificModifier.getKey(), advancedValue);
    }

}
