package cupcake.nikky.imperium.tower.entity.monster.loot;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.rng.exact.loot.WeightItemWrapper;
import cupcake.nikky.imperium.tower.user.User;
import dev.xdark.feder.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class LootStrategyBuilder {

    private Function<MonsterData, List<LootItemData>> creator = data -> new ArrayList<>();
    private Function<MonsterData, TriConsumer<User, Monster, WeightItemWrapper>> preProcess;
    private final List<LootStrategyBuilder> builders = new ArrayList<>();

    public static LootStrategyBuilder create() {
        return new LootStrategyBuilder();
    }

    public LootStrategyBuilder of(@NotNull final Function<MonsterData, List<LootItemData>> creator) {
        this.creator = creator;
        return this;
    }

    public LootStrategyBuilder preProcess(@NotNull final Function<MonsterData, TriConsumer<User, Monster, WeightItemWrapper>> preProcess) {
        this.preProcess = preProcess;
        return this;
    }

    public LootStrategyBuilder of(@NotNull final LootStrategyBuilder... builders) {
        this.builders.addAll(Arrays.asList(builders));
        return this;
    }

    public LootStrategy build(@NotNull final MonsterData monsterData) {
        var preProcesses = new ArrayList<TriConsumer<User, Monster, WeightItemWrapper>>();
        var lootItemData = new ArrayList<LootItemData>();

        builders.forEach(strategyBuilder -> {
            if (strategyBuilder.preProcess != null)
                preProcesses.add(strategyBuilder.preProcess.apply(monsterData));
            lootItemData.addAll(strategyBuilder.creator.apply(monsterData));
        });

        return new LootStrategy(monsterData, lootItemData, preProcesses);
    }

}
