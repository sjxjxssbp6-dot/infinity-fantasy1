package cupcake.nikky.imperium.tower.item.modifier;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

@RequiredArgsConstructor
public class ModifierCreator {

    private final Modifier modifier;

    @Accessors(fluent = true, chain = true)
    @Setter
    private ModifierType type;

    public SpecificModifier apply(final int tier) {
        val modifierTier = modifier.getRange().findByNumericTier(tier);
        if (modifierTier == null) throw new RuntimeException("Attempt to find an invalid tier of modifier");

        return new SpecificModifier(type, modifier, modifier.getId(), modifierTier.generate());
    }

    public SpecificModifier applyAverage(final int tier) {
        val modifierTier = modifier.getRange().findByNumericTier(tier);
        if (modifierTier == null) throw new RuntimeException("Attempt to find an invalid tier of modifier");

        return new SpecificModifier(type, modifier, modifier.getId(), Math.round((modifierTier.min() + modifierTier.max()) / 2));
    }

    public SpecificModifier applyLowest(final int tier) {
        val modifierTier = modifier.getRange().findByNumericTier(tier);
        if (modifierTier == null) throw new RuntimeException("Attempt to find an invalid tier of modifier");

        return new SpecificModifier(type, modifier, modifier.getId(), modifierTier.min());
    }

    public SpecificModifier applyHighest(final int tier) {
        val modifierTier = modifier.getRange().findByNumericTier(tier);
        if (modifierTier == null) throw new RuntimeException("Attempt to find an invalid tier of modifier");

        return new SpecificModifier(type, modifier, modifier.getId(), modifierTier.max());
    }

}
