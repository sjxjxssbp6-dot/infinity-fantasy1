package cupcake.nikky.imperium.tower.rng.exact.loot;

import cupcake.nikky.imperium.tower.rng.Weight;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class LootWeightProcess {

    private final LootWeightPool pool;
    private final Weight<WeightItemWrapper> weight;

    public LootWeightProcess handle(@NotNull final Consumer<Weight<WeightItemWrapper>> handler) {
        handler.accept(weight);
        return this;
    }

    public LootWeightProcess cancelSizing() {
        return this.handle(weight -> weight.weightWrapper().setCancelPoolSizing(true));
    }

    public LootWeightProcess setupUnique() {
        weight.weightWrapper().setUniquePerSession(true);
        return this;
    }

    public LootWeightProcess setupFlatWeight() {
        weight.onSuccess(winner -> weight.weightWrapper().getWeight());
        return this;
    }

    public LootWeightProcess setupFlatLimit(final int limit) {
        return this.handle(weight -> weight.onSuccess(winner -> weight.weightWrapper().getSuccessCounter() == limit ? 0 : weight.weightWrapper().getWeight()));
    }

    public LootWeightPool apply() {
        pool.apply(weight);
        return pool;
    }

}
