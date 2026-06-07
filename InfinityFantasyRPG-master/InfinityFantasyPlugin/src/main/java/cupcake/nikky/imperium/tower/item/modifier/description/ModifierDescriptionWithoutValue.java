package cupcake.nikky.imperium.tower.item.modifier.description;

import cupcake.nikky.imperium.tower.item.modifier.ModifierTier;
import cupcake.nikky.imperium.tower.item.modifier.SpecificModifier;
import org.jetbrains.annotations.NotNull;

public class ModifierDescriptionWithoutValue extends ModifierDescription {

    public ModifierDescriptionWithoutValue(String descriptionFormat) {
        super(descriptionFormat);
    }

    @Override
    public String generateDescription(@NotNull final String valueColor, @NotNull final String descriptionColor, final double value, final double amplitude) {
        return getDescriptionFormat().formatted(descriptionColor);
    }

    @Override
    public String generateSubCraftDescription(@NotNull final String valueColor, @NotNull final String descriptionColor, @NotNull final ModifierTier modifierTier) {
        return getDescriptionFormat().formatted(descriptionColor);
    }

    @Override
    public String generateCraftDescription(@NotNull final String valueColor, @NotNull final String descriptionColor) {
        return getDescriptionFormat().formatted(descriptionColor);
    }

    @Override
    public String generateAdvancedDescription(@NotNull final String valueColor, @NotNull final String descriptionColor, @NotNull final SpecificModifier specificModifier) {
        return getDescriptionFormat().formatted(descriptionColor);
    }

}
