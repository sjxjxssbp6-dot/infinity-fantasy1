package cupcake.nikky.imperium.tower.rng.exact.modifier;

import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.rng.WeightApplier;
import cupcake.nikky.imperium.tower.rng.WeightWrapper;
import lombok.Getter;

@Getter
public class WeightModifierWrapper extends WeightWrapper<Modifier> {

    private final int tier;

    public WeightModifierWrapper(Modifier wrapped, WeightApplier weight, boolean cancelPoolSizing, final int tier) {
        super(wrapped, 0, weight, cancelPoolSizing);

        this.tier = tier;
    }

    public WeightModifierWrapper(Modifier wrapped, int weightInt, WeightApplier weight, boolean cancelPoolSizing, final int tier) {
        super(wrapped, weightInt, weight, cancelPoolSizing);

        this.tier = tier;
    }

    public WeightModifierWrapper(Modifier wrapped, int weight, boolean cancelPoolSizing, final int tier) {
        super(wrapped, weight, null, cancelPoolSizing);

        this.tier = tier;
    }

    @Override
    public WeightWrapper<Modifier> copy() {
        return new WeightModifierWrapper(getWrapped(), getWeight(), getWeightApplier(), isCancelPoolSizing(), getTier());
    }

}
