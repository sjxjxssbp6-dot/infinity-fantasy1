package cupcake.nikky.imperium.tower.item.settings.exact.loot;

import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.function.ToIntFunction;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class CorruptionSettings {

    private ToIntFunction<MonsterData> minCorrupt = data -> 0;
    private ToIntFunction<MonsterData> maxCorrupt = data -> 0;

}
