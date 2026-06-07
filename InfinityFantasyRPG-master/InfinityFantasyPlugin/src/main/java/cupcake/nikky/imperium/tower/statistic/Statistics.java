package cupcake.nikky.imperium.tower.statistic;

import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Statistics {

    private static final Int2ObjectMap<StatisticType<?>> registry = new Int2ObjectOpenHashMap<>();

    public static final StatisticType<Integer> DEATHS_BY_MONSTER = new StatisticType<>(0, 0, data -> data <= 0,
            (type, data) -> type.data(NumberUtility.parseInteger(data)),
            StatisticConsumers.INTEGER_ADD);

    public static final StatisticType<Integer> USER_KILL = new StatisticType<>(1, 0, data -> data <= 0,
            (type, data) -> type.data(NumberUtility.parseInteger(data)),
            StatisticConsumers.INTEGER_ADD);

    public static final StatisticType<Integer> DEATHS_BY_USER = new StatisticType<>(2, 0, data -> data <= 0,
            (type, data) -> type.data(NumberUtility.parseInteger(data)),
            StatisticConsumers.INTEGER_ADD);

    public static final StatisticType<Integer> MONSTER_KILL = new StatisticType<>(3, 0, data -> data <= 0,
            (type, data) -> type.data(NumberUtility.parseInteger(data)),
            StatisticConsumers.INTEGER_ADD);

    public static void register(@NotNull final StatisticType<?> statisticType) {
        registry.put(statisticType.index(), statisticType);
    }

    public static StatisticType<?> get(final int index) {
        return registry.get(index);
    }

}
