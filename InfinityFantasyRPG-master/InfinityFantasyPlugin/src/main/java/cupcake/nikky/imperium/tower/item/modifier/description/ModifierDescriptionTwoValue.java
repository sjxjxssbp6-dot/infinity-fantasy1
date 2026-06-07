package cupcake.nikky.imperium.tower.item.modifier.description;

import cupcake.nikky.imperium.tower.item.modifier.ModifierTier;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import org.jetbrains.annotations.NotNull;

public class ModifierDescriptionTwoValue extends ModifierDescription {

    public ModifierDescriptionTwoValue(String descriptionFormat) {
        super(descriptionFormat);
    }

    @Override
    public String generateDescription(@NotNull final String valueColor, @NotNull final String descriptionColor, @NotNull final SpecificModifier specificModifier) {
        var amplitude = specificModifier.getModifier().getRange().findByNumericTier(specificModifier.findTier()).amplitude();
        return this.generateDescription(valueColor, descriptionColor, specificModifier.getKey(), amplitude);
    }

    @Override
    public String generateDescription(@NotNull final String valueColor, @NotNull final String descriptionColor, final double value, final double amplitude) {
        return getDescriptionFormat().formatted(descriptionColor, valueColor, value - amplitude, value + amplitude, descriptionColor);
    }

    @Override
    public String generateSubCraftDescription(@NotNull final String valueColor, @NotNull final String descriptionColor, @NotNull final ModifierTier modifierTier) {
        return getDescriptionFormat().formatted(descriptionColor, valueColor, 0D, 0D, descriptionColor)
                .replace(".0", "")
                .replace("0", "(%.0f-%.0f)")
                .formatted(
                        modifierTier.min() - modifierTier.amplitude(), modifierTier.max() - modifierTier.amplitude(),
                        modifierTier.min() + modifierTier.amplitude(), modifierTier.max() + modifierTier.amplitude()
                );
    }

    @Override
    public String generateCraftDescription(@NotNull final String valueColor, @NotNull final String descriptionColor) {
        return getDescriptionFormat().formatted(descriptionColor, valueColor, 0D, 0D, descriptionColor)
                .replace("0", "#")
                .replace(".0", ".#");
    }

    @Override
    public String generateAdvancedDescription(@NotNull final String valueColor, @NotNull final String descriptionColor, @NotNull final SpecificModifier specificModifier) {
        var tier = specificModifier.getModifier().getRange().findByNumericTier(specificModifier.findTier());
        var advancedValue = tier.min() == tier.max() ? descriptionColor : "&8((%.1f-%.1f)-(%.1f-%.1f)) %s"
                .formatted(tier.min() - tier.amplitude(), tier.max() - tier.amplitude(), tier.min() + tier.amplitude(), tier.max() + tier.amplitude(), descriptionColor);
        var amplitude = tier.amplitude();

        return getDescriptionFormat().formatted(descriptionColor, valueColor, specificModifier.getKey() - amplitude, specificModifier.getKey() + amplitude, advancedValue);
    }

}
