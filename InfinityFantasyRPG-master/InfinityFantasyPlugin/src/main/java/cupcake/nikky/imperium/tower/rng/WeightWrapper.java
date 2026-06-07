package cupcake.nikky.imperium.tower.rng;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class WeightWrapper<T> {

    private final UUID uuid;
    @Getter
    private final T wrapped;
    @Getter @Setter
    private int weight;
    @Getter @Setter
    private WeightApplier weightApplier;
    @Getter @Setter
    private boolean cancelPoolSizing;

    public WeightWrapper(@NotNull final UUID uuid, @NotNull final T wrapped, final int weight, @Nullable final WeightApplier weightApplier, final boolean cancelPoolSizing) {
        this.uuid = uuid;
        this.wrapped = wrapped;
        this.weight = weight;
        this.weightApplier = weightApplier;
        this.cancelPoolSizing = cancelPoolSizing;
    }

    public WeightWrapper(@NotNull final T wrapped, final int weight, @Nullable final WeightApplier weightApplier, final boolean cancelPoolSizing) {
        this.wrapped = wrapped;
        this.weight = weight;
        this.weightApplier = weightApplier;
        this.cancelPoolSizing = cancelPoolSizing;
        this.uuid = UUID.randomUUID();
    }

    public WeightWrapper(@NotNull final T wrapped, final int weight, final boolean cancelPoolSizing) {
        this.wrapped = wrapped;
        this.weight = weight;
        this.cancelPoolSizing = cancelPoolSizing;
        this.uuid = UUID.randomUUID();
    }

    public WeightWrapper<T> copy() {
        return new WeightWrapper<>(uuid, wrapped, weight, weightApplier, cancelPoolSizing);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeightWrapper<?> that = (WeightWrapper<?>) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
