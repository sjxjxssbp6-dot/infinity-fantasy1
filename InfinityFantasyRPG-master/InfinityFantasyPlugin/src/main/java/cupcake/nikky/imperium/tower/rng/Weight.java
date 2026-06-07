package cupcake.nikky.imperium.tower.rng;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Accessors(fluent = true)
public class Weight<T extends WeightWrapper<?>> {

    private final UUID uuid = UUID.randomUUID();
    private final T weightWrapper;

    private Function<Weight<T>, Integer> onFail;
    private Function<Weight<T>, Integer> onSuccess;

    public Weight(@NotNull final T weightWrapper) {
        this.weightWrapper = weightWrapper;

        this.onFail = winner -> weightWrapper().getWeight();
        this.onSuccess = winner -> winner.weightWrapper().getWeight() / 2;
    }

    public Weight<T> onSuccess(@NotNull final Function<Weight<T>, Integer> function) {
        this.onSuccess = function;
        return this;
    }

    public Weight<T> onFail(@NotNull final Function<Weight<T>, Integer> function) {
        this.onFail = function;
        return this;
    }

    @SuppressWarnings("unchecked")
    public Weight<T> copy() {
        return new Weight<>((T) this.weightWrapper().copy()).onFail(this.onFail()).onSuccess(this.onSuccess());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weight<?> weight = (Weight<?>) o;
        return Objects.equals(uuid, weight.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
