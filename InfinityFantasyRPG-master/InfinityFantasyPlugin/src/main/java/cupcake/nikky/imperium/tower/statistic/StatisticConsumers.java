package cupcake.nikky.imperium.tower.statistic;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.BiConsumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StatisticConsumers {

    public static final BiConsumer<StatisticType<Integer>, Integer> INTEGER_ADD = (type, value) -> {
        for (;;) {
            var old = type.data();
            if (type.dataReference().compareAndSet(old, old + value)) break;
        }
    };

}
