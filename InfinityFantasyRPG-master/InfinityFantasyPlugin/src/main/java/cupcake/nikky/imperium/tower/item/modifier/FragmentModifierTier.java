package cupcake.nikky.imperium.tower.item.modifier;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true, chain = true)
public class FragmentModifierTier extends ModifierTier {

    @Setter
    private int dropQuantity;

    @Override
    public ModifierTier copy() {
        return new FragmentModifierTier().dropQuantity(dropQuantity).tier(tier()).weight(weight()).min(min()).max(max()).requireBaseLevel(requireBaseLevel());
    }

}
