package cupcake.nikky.imperium.tower.item.settings.exact.loot;

import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import cupcake.nikky.imperium.tower.rng.WeightApplier;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.Function;
import java.util.function.ToIntFunction;

@Accessors(fluent = true, chain = true)
@Getter
@Setter
public class LootSettings extends ItemSettings {

    private final LootGeneratorSettings lootGeneratorSettings = new LootGeneratorSettings();

    private int dropEntityLevel;
    private ToIntFunction<MonsterData> weightHandler = data -> 0;
    private Function<MonsterData, WeightApplier> weightApplier = monsterData -> null;
    private ToIntFunction<MonsterData> maxAmountHandler = data -> 0;

    public LootSettings(ImperiumItem item) {
        super(item);
    }
}
