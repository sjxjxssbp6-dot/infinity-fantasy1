package cupcake.nikky.imperium.tower.entity.monster.loot;

import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.rng.WeightApplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class LootContainer {

    private final MonsterData monsterData;

    private final Map<ItemObject, Integer> weights = new EnumMap<>(ItemObject.class);
    private final Map<ItemObject, WeightApplier> weightsAppliers = new EnumMap<>(ItemObject.class);
    private final Map<ItemObject, Integer> amounts = new EnumMap<>(ItemObject.class);

}
