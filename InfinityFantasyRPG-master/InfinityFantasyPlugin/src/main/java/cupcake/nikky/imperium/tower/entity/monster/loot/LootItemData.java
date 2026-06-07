package cupcake.nikky.imperium.tower.entity.monster.loot;

import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.rng.WeightApplier;
import org.jetbrains.annotations.NotNull;

public record LootItemData(@NotNull ItemObject itemObject, int weight, int maxAmount, @NotNull WeightApplier weightApplier) {

}
