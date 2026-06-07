package cupcake.nikky.imperium.tower.entity.monster.loot;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.fragment.FragmentItem;
import cupcake.nikky.imperium.tower.item.exact.mask.MaskItem;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.rng.WeightApplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum LootStrategies {

    EQUIP(LootStrategyBuilder.create().of(monsterData -> {
        WeightApplier weightApplier = totalSize -> totalSize * 30;
        final List<LootItemData> result = new ArrayList<>(Arrays.asList(new LootItemData(ItemObject.FIRM_SWORD, 0, 1, weightApplier),
                new LootItemData(ItemObject.FIRM_DAGGER, 0, 1, weightApplier),
                new LootItemData(ItemObject.FIRM_SPEAR, 0, 1, weightApplier),
                new LootItemData(ItemObject.FIRM_AXE, 0, 1, weightApplier),
                new LootItemData(ItemObject.FIRM_HELMET, 0, 1, weightApplier),
                new LootItemData(ItemObject.FIRM_BODY, 0, 1, weightApplier),
                new LootItemData(ItemObject.FIRM_LEGGINGS, 0, 1, weightApplier),
                new LootItemData(ItemObject.FIRM_BOOTS, 0, 1, weightApplier))
        );

        if (monsterData.getLevel() >= 15) {
            result.addAll(List.of(
                    new LootItemData(ItemObject.REKN_SWORD, 0, 1, weightApplier),
                    new LootItemData(ItemObject.REKN_DAGGER, 0, 1, weightApplier),
                    new LootItemData(ItemObject.REKN_SPEAR, 0, 1, weightApplier),
                    new LootItemData(ItemObject.REKN_AXE, 0, 1, weightApplier),
                    new LootItemData(ItemObject.REKN_HELMET, 0, 1, weightApplier),
                    new LootItemData(ItemObject.REKN_BODY, 0, 1, weightApplier),
                    new LootItemData(ItemObject.REKN_LEGGINGS, 0, 1, weightApplier),
                    new LootItemData(ItemObject.REKN_BOOTS, 0, 1, weightApplier)
            ));
        }

        if (monsterData.getLevel() >= 35) {
            result.addAll(List.of(new LootItemData(ItemObject.SALT_SWORD, 0, 1, weightApplier),
                    new LootItemData(ItemObject.SALT_DAGGER, 0, 1, weightApplier),
                    new LootItemData(ItemObject.SALT_SPEAR, 0, 1, weightApplier),
                    new LootItemData(ItemObject.SALT_AXE, 0, 1, weightApplier),
                    new LootItemData(ItemObject.SALT_HELMET, 0, 1, weightApplier),
                    new LootItemData(ItemObject.SALT_BODY, 0, 1, weightApplier),
                    new LootItemData(ItemObject.SALT_LEGGINGS, 0, 1, weightApplier),
                    new LootItemData(ItemObject.SALT_BOOTS, 0, 1, weightApplier)));
        }

        if (monsterData.getLevel() >= 50) {
            result.addAll(List.of(new LootItemData(ItemObject.TARN_SWORD, 0, 1, weightApplier),
                    new LootItemData(ItemObject.TARN_DAGGER, 0, 1, weightApplier),
                    new LootItemData(ItemObject.TARN_SPEAR, 0, 1, weightApplier),
                    new LootItemData(ItemObject.TARN_AXE, 0, 1, weightApplier),
                    new LootItemData(ItemObject.TARN_HELMET, 0, 1, weightApplier),
                    new LootItemData(ItemObject.TARN_BODY, 0, 1, weightApplier),
                    new LootItemData(ItemObject.TARN_LEGGINGS, 0, 1, weightApplier),
                    new LootItemData(ItemObject.TARN_BOOTS, 0, 1, weightApplier)));
        }

        if (monsterData.getLevel() >= 60) {
            result.addAll(List.of(new LootItemData(ItemObject.KELN_SWORD, 0, 1, weightApplier),
                    new LootItemData(ItemObject.KELN_DAGGER, 0, 1, weightApplier),
                    new LootItemData(ItemObject.KELN_SPEAR, 0, 1, weightApplier),
                    new LootItemData(ItemObject.KELN_AXE, 0, 1, weightApplier),
                    new LootItemData(ItemObject.KELN_HELMET, 0, 1, weightApplier),
                    new LootItemData(ItemObject.KELN_BODY, 0, 1, weightApplier),
                    new LootItemData(ItemObject.KELN_LEGGINGS, 0, 1, weightApplier),
                    new LootItemData(ItemObject.KELN_BOOTS, 0, 1, weightApplier)));
        }

        return result;
    })
    ),
    INCUBATOR(LootStrategyBuilder.create().of(monsterData -> Stream.of(ItemObject.values())
            .filter(itemObject -> itemObject.getItem().getImperiumType() == ItemType.INCUBATOR)
            .filter(itemObject -> itemObject.getItem().getGeneralSettings().itemLevel() <= monsterData.getLevel())
            .map(itemObject -> new LootItemData(itemObject, 0, 1, totalSize -> totalSize * 2))
            .collect(Collectors.toList()))
    ),
    PANDORA_ADDITIONAL(LootStrategyBuilder.create().of(data -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        return List.of(new LootItemData(ItemObject.ORB_OF_PANDORA_MOD_ITEM, 0, 1, totalSize -> totalSize / 2 + Math.max(data.getLevel() - level, 0)));
    })),
    EXALTED_ORB_ADDITIONAL(LootStrategyBuilder.create().of(data -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        return List.of(new LootItemData(ItemObject.EXALTED_ORB_MOD_ITEM, 0, 1, totalSize -> totalSize / 3 + Math.max(data.getLevel() - level, 0)));
    })),
    FRACTION_DROP(LootStrategyBuilder.create().of(data -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();

        if (data.isUnique()) {
            return List.of(
                    new LootItemData(ItemObject.SYNERGY_BOOTS, 0, 1, totalSize -> totalSize / 2),
                    new LootItemData(ItemObject.ORB_OF_PANDORA_MOD_ITEM, 0, 4, totalSize -> totalSize + Math.max(data.getLevel() - level, 0)),
                    new LootItemData(ItemObject.EXALTED_ORB_MOD_ITEM, 0, 4, totalSize -> totalSize + Math.max(data.getLevel() - level, 0))
            );
        }

        return List.of(
                new LootItemData(ItemObject.SYNERGY_BOOTS, 0, 1, totalSize -> totalSize / 8),
                new LootItemData(ItemObject.ORB_OF_PANDORA_MOD_ITEM, 0, 1, totalSize -> totalSize / 2 + Math.max(data.getLevel() - level, 0)),
                new LootItemData(ItemObject.EXALTED_ORB_MOD_ITEM, 0, 1, totalSize -> totalSize / 3 + Math.max(data.getLevel() - level, 0))
        );
    })),
    ASPECT(LootStrategyBuilder.create().of(monsterData -> Stream.of(ItemObject.values())
            .filter(itemObject -> itemObject.getItem().getImperiumType() == ItemType.ASPECT)
            .filter(itemObject -> monsterData.getLevel() >= LevelStorage.getInstance().getStartShardEdgeLevel())
            .map(itemObject -> new LootItemData(itemObject, 0, 1,
                    totalSize -> totalSize + Math.max(monsterData.getLevel() - LevelStorage.getInstance().getStartShardEdgeLevel(), 0) * 4)
            )
            .collect(Collectors.toList()))
    ),
    MASK(LootStrategyBuilder.create().of(monsterData -> Stream.of(ItemObject.values())
                    .filter(itemObject -> itemObject.getItem().checkInstance(MaskItem.class))
                    .map(itemObject -> new LootItemData(itemObject, 0, 1,
                            totalSize -> totalSize * 500)
                    )
                    .collect(Collectors.toList()))
            .preProcess(monsterData -> (user, monster, weightItemWrapper) -> {
                if (weightItemWrapper.getWrapped().getItem().checkInstance(MaskItem.class)) {
                    val chanceToDropMask = user.findAttribute(ImperiumAttribute.INCREASED_MASK_DROP_CHANCE).findValue() / 100D;
                    val maskCount = (int) user.findAttribute(ImperiumAttribute.ADDITIONAL_MASK_DROP).findValue();
                    weightItemWrapper.setAmount(weightItemWrapper.getAmount() + maskCount);

                    val chanceToDoubleMask = user.findAttribute(ImperiumAttribute.INCREASE_MASK_DROP_AMOUNT).findValue();
                    if (MathUtility.randomInRange(1, 100) <= chanceToDoubleMask) {
                        weightItemWrapper.setAmount(weightItemWrapper.getAmount() * 2);
                    }

                    weightItemWrapper.setWeight(weightItemWrapper.getWeight() + (int) Math.round(weightItemWrapper.getWeight() * chanceToDropMask));
                }
            })
    ),
    GENERAL(LootStrategyBuilder.create().of(monsterData -> Stream.of(ItemObject.values())
            .map(itemObject -> {
                val item = itemObject.getItem();
                val dropLevel = item.getLootSettings().dropEntityLevel();
                val currentWeight = item.getLootSettings().weightHandler().applyAsInt(monsterData);
                val currentWeightApplier = item.getLootSettings().weightApplier().apply(monsterData);
                if (dropLevel == 0 || dropLevel > monsterData.getLevel() || currentWeight <= 0 && currentWeightApplier == null) return null;
                return new LootItemData(itemObject, currentWeight, item.getLootSettings().maxAmountHandler().applyAsInt(monsterData), currentWeightApplier);
            }).filter(Objects::nonNull).collect(Collectors.toList()))
            .preProcess(monsterData -> (user, monster, weightItemWrapper) -> {
                if (weightItemWrapper.getWrapped().getItem().getImperiumType() == ItemType.MATERIAL) {
                    val chancePenalty = Math.max(1 - (5D * (user.getLevel() - (monsterData.getLevel() + 1))) / 100D, 0);
                    weightItemWrapper.setWeight((int) Math.round(weightItemWrapper.getWeight() * chancePenalty));
                }
            })
    ),
    UNIQUE_ITEMS_CHANCE_PREPROCESS(LootStrategyBuilder.create().preProcess(monsterData -> (user, monster, weightItemWrapper) -> {
        if (weightItemWrapper.getWrapped().getItem().getRare() == ItemRare.UNIQUE) {
            val multiplier = monster.findAttribute(ImperiumAttribute.MONSTER_CHANCE_TO_DROP_RELICT_ITEMS).findValue();
            weightItemWrapper.setWeight((int) Math.round(weightItemWrapper.getWeight() * multiplier));
        }
    })),
    WAY_FRAGMENT_CHANCE_PREPROCESS(LootStrategyBuilder.create().preProcess(monsterData -> (user, monster, weightItemWrapper) -> {
        if (weightItemWrapper.getWrapped().getItem().checkInstance(FragmentItem.class)) {
            val multiplier = 1 + monster.findAttribute(ImperiumAttribute.INCREASE_DROP_CHANCE_WAY_FRAGMENT).findValue() / 100D;
            weightItemWrapper.setWeight((int) Math.round(weightItemWrapper.getWeight() * multiplier));
        }
    })),
    INCUBATOR_CHANCE_PREPROCESS(LootStrategyBuilder.create().preProcess(monsterData -> (user, monster, weightItemWrapper) -> {
        if (weightItemWrapper.getWrapped().getItem().getImperiumType() == ItemType.INCUBATOR) {
            val multiplier = 1 + monster.findAttribute(ImperiumAttribute.INCREASE_DROP_CHANCE_INCUBATOR).findValue() / 100D;
            weightItemWrapper.setWeight((int) Math.round(weightItemWrapper.getWeight() * multiplier));
        }
    }));

    private final LootStrategyBuilder builder;

}
