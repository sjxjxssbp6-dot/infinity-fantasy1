package cupcake.nikky.imperium.tower.box;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.blood.BloodObject;
import cupcake.nikky.imperium.tower.box.builder.CaseDataBuilder;
import cupcake.nikky.imperium.tower.box.reward.CaseItemReward;
import cupcake.nikky.imperium.tower.box.reward.CaseMetadataReward;
import cupcake.nikky.imperium.tower.box.reward.CaseReward;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseCurrency;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseData;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseRarity;
import cupcake.nikky.imperium.tower.death.DeathObject;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.rng.Weight;
import cupcake.nikky.imperium.tower.rng.WeightPool;
import cupcake.nikky.imperium.tower.rng.exact.box.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.InventoryUtility;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public enum CaseType {

    GENERAL_ITEMS(
            CaseDataBuilder.of()
                    .setTitle("Кейс с предметами")
                    .setPrice(39, CaseCurrency.SPECIAL)
                    .applyReward(new WeightPool<CaseWrapper<?>>(1)
                            .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.MIXED_REAGENT_VERGE_MOD_ITEM, 100, 4, CaseRarity.COMMON, 0)))
                            .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.EPIC_REAGENT_VERGE_MOD_ITEM, 100, 4, CaseRarity.COMMON, 1)))
                            .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.SHARD_OF_ABYSS_MOD_ITEM, 50, 1, CaseRarity.EPIC, 2)))
                            .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.ABSOLUTE_DUST_MOD_ITEM, 75, 4, CaseRarity.RARE, 4)))
                            .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.ORB_OF_DISTORTION_MOD_ITEM, 75, 4, CaseRarity.RARE, 5)))
                            .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.FORM_OF_TYCHE_MOD_ITEM, 50, 6, CaseRarity.EPIC, 6)))
                            .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.STAR_OF_CORRUPT_MOD_ITEM, 100, 4, CaseRarity.COMMON, 7)))
                            .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.ESSENCE_OF_TYCHE_MOD_ITEM, 50, 1, CaseRarity.EPIC, 8)))
                            .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.ORB_OF_HEPHAESTUS_MOD_ITEM, 75, 4, CaseRarity.RARE, 9)))
                            .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.CLEANSING_TEAR_MOD_ITEM, 100, 4, CaseRarity.COMMON, 10))))
                    .build(),
            (player, weight) -> new CaseItemReward(weight),
            new CaseRequirement(
                    (player, caseRolls) -> InventoryUtility.getAmountOfEmptySlots(player.getInventory()) >= caseRolls,
                    (player, caseRolls) ->
                            new String[]{
                                    "Недостаточно места в инвентаре",
                                    "Необходимо %d %s".formatted(caseRolls, TextUtility.transformViaAmount(caseRolls, "пустой слот", "пустых слотов", "пустых слотов"))
                            }
            )
    ),
    STAR_ITEMS(CaseDataBuilder.of()
            .setTitle("Звездный кейс с предметами")
            .setPrice(99, CaseCurrency.SPECIAL)
            .applyReward(new WeightPool<CaseWrapper<?>>(1)
                    .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.DROP_OF_EDGE_MOD_ITEM, 35, 1, CaseRarity.EDGE, 0)))
                    .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.SHARD_OF_ABYSS_MOD_ITEM, 50, 3, CaseRarity.EPIC, 1)))
                    .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.FORM_OF_TYCHE_MOD_ITEM, 75, 8, CaseRarity.RARE, 3)))
                    .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.STAR_OF_CORRUPT_MOD_ITEM, 100, 8, CaseRarity.COMMON, 4)))
                    .apply(new Weight<>(new WeightCaseItemWrapper(ItemObject.ESSENCE_OF_TYCHE_MOD_ITEM, 50, 4, CaseRarity.EPIC, 5))))
            .build(),
            (player, weight) -> new CaseItemReward(weight),
            new CaseRequirement(
                    (player, caseRolls) -> InventoryUtility.getAmountOfEmptySlots(player.getInventory()) >= caseRolls,
                    (player, caseRolls) ->
                            new String[]{
                                    "Недостаточно места в инвентаре",
                                    "Необходимо %d %s".formatted(caseRolls, TextUtility.transformViaAmount(caseRolls, "пустой слот", "пустых слотов", "пустых слотов"))
                            }
            )),
    BASE_VISUAL(CaseDataBuilder.of()
            .setTitle("Базовый кейс с визуалом")
            .setPrice(199, CaseCurrency.SPECIAL)
            .applyAuraReward(AuraObject.values())
            .applyReward(new WeightCaseBloodWrapper(BloodObject.HEART_BLOOD, 75, CaseRarity.UNIQUE, 0))
            .applyReward(new WeightCaseBloodWrapper(BloodObject.WHITE_BLOOD, 75, CaseRarity.UNIQUE, 0))
            .applyReward(new WeightCaseBloodWrapper(BloodObject.MULTI_COLORED_BLOOD, 50, CaseRarity.EDGE, 0))
            .applyReward(new WeightCaseDeathWrapper(DeathObject.DARK_EXPLOSION, 50, CaseRarity.EDGE, 0))
            .applyReward(new WeightCaseDeathWrapper(DeathObject.PURPLE_EXPLOSION, 50, CaseRarity.EDGE, 0))
            .applyReward(new WeightCaseDeathWrapper(DeathObject.RED_LINE_UP, 50, CaseRarity.EDGE, 0))
            .build(),
            (player, weight) -> new CaseMetadataReward(weight, 79, CaseCurrency.SPECIAL),
            new CaseRequirement((player, caseRolls) -> true, (player, caseRolls) -> new String[]{}));

    private final CaseServerData caseServerData;
    private final BiFunction<Player, Weight<CaseWrapper<?>>, CaseReward> caseReward;
    private final CaseRequirement caseRequirement;

    public CaseData getCaseData() {
        return caseServerData.getCaseData();
    }

    public boolean tryRoll(@NotNull final Player player, final int caseRolls) {
        var result = caseRequirement.getHandler().apply(player, caseRolls);
        if (!result)
            Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, caseRequirement.getTextError().apply(player, caseRolls));

        return result;
    }

}
