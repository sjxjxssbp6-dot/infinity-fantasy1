package cupcake.nikky.imperium.tower.statistic;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Accessors(fluent = true)
public class StatisticType<T> {

    @Getter private final int index;

    private final BiConsumer<StatisticType<T>, String> converter;
    private final BiConsumer<StatisticType<T>, T> addFunction;
    private final Function<T, Boolean> emptyCheckerFunction;

    private final AtomicReference<T> data = new AtomicReference<>();

    public StatisticType(final int index, @NotNull final T zero, @NotNull final Function<T, Boolean> emptyCheckerFunction, @NotNull final BiConsumer<StatisticType<T>, String> converter) {
        this(index, zero, emptyCheckerFunction, converter, (tStatisticType, t) -> {});
    }

    public StatisticType(final int index, @NotNull final T zero, @NotNull final Function<T, Boolean> emptyCheckerFunction,
                         @NotNull final BiConsumer<StatisticType<T>, String> converter, @NotNull final BiConsumer<StatisticType<T>, T> addFunction) {
        this.index = index;
        this.converter = converter;
        this.addFunction = addFunction;
        this.emptyCheckerFunction = emptyCheckerFunction;
        this.data(zero);
        Statistics.register(this);
    }

    public StatisticType<T> convert(@NotNull final String data) {
        this.converter.accept(this, data);
        return this;
    }

    public StatisticType<T> copy() {
        return new StatisticType<>(index, data(), emptyCheckerFunction, converter, addFunction).data(this.data());
    }

    public StatisticType<T> add(T t) {
        this.addFunction.accept(this, t);
        return this;
    }

    public AtomicReference<T> dataReference() {
        return data;
    }

    public T data() {
        return data.get();
    }

    public StatisticType<T> data(@NotNull final T data) {
        for (;;) {
            var old = this.data();
            if (this.data.compareAndSet(old, data)) break;
        }

        return this;
    }

    public boolean isEmpty() {
        var currentData = data();
        return currentData == null || emptyCheckerFunction.apply(currentData);
    }

    public String serialize() {
        return "%d:%s".formatted(index(), data().toString());
    }

}
