package cupcake.nikky.imperium.tower.entity.monster.loot;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.rng.exact.loot.LootWeightPool;
import cupcake.nikky.imperium.tower.rng.exact.loot.WeightItemWrapper;
import cupcake.nikky.imperium.tower.user.User;
import dev.xdark.feder.TriConsumer;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record LootStrategy(@NotNull MonsterData monsterData, @NotNull List<LootItemData> data, @Nullable List<TriConsumer<User, Monster, WeightItemWrapper>> preProcess) {

    public LootWeightPool invoke() {
        val pool = Optional.ofNullable(monsterData().getLootWeightPool()).orElse(LootWeightPool.newInstance(1));

        if (preProcess() != null)
            preProcess().forEach(pool::registerNewPreProcess);

        data.forEach(lootItemData -> {
            val itemObject = lootItemData.itemObject();
            val type = itemObject.getItem().getImperiumType();
            pool.newWeightInstance(itemObject, lootItemData.maxAmount(), lootItemData.weight())
                    .handle(weight -> {
                        weight.weightWrapper().setCancelPoolSizing(type == ItemType.ARMOR || type == ItemType.WEAPON);
                        weight.weightWrapper().setWeightApplier(lootItemData.weightApplier());
                    })
                    .apply();
        });

        pool.finalizePool();
        pool.setInitialSize((int) Math.round(pool.findTotalSize() * 2.85));

        return pool;
    }

}
