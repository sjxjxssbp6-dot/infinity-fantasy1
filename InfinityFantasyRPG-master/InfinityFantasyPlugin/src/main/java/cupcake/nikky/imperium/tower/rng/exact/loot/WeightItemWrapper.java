package cupcake.nikky.imperium.tower.rng.exact.loot;

import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.rng.WeightApplier;
import cupcake.nikky.imperium.tower.rng.WeightWrapper;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

@Getter
@Setter
public class WeightItemWrapper extends WeightWrapper<ItemObject> {

    private int amount;
    private int successCounter;
    private int failCounter;
    private boolean uniquePerSession;

    public WeightItemWrapper(ItemObject wrapped, int amount, WeightApplier weight) {
        this(wrapped, amount, weight, false);
    }

    public WeightItemWrapper(ItemObject wrapped, int amount, WeightApplier weight, boolean cancel) {
        super(wrapped, 0, weight, cancel);
        this.amount = amount;
    }

    public WeightItemWrapper(ItemObject wrapped, int amount, int weightInt, WeightApplier weight, boolean cancel) {
        super(wrapped, weightInt, weight, cancel);
        this.amount = amount;
    }

    public WeightItemWrapper(ItemObject wrapped, int amount, int weight) {
        this(wrapped, amount, weight, false);
    }

    public WeightItemWrapper(ItemObject wrapped, int amount, int weight, boolean cancel) {
        super(wrapped, weight, null, cancel);
        this.amount = amount;
    }

    @Override
    public WeightItemWrapper copy() {
        val instance = new WeightItemWrapper(getWrapped(), getAmount(), getWeight(), getWeightApplier(), this.isCancelPoolSizing());
        instance.setSuccessCounter(getSuccessCounter());
        instance.setFailCounter(getFailCounter());
        return instance;
    }

}
