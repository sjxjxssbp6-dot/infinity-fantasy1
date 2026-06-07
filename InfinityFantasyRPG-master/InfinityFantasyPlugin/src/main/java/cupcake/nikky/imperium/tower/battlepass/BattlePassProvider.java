package cupcake.nikky.imperium.tower.battlepass;

import com.google.common.collect.ImmutableList;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.battlepass.quest.BattlePassQuest;
import cupcake.nikky.imperium.tower.battlepass.quest.BattlePassLinkedQuest;
import cupcake.nikky.imperium.tower.battlepass.quest.QuestLinkBuilder;
import cupcake.nikky.imperium.tower.battlepass.reward.*;
import cupcake.nikky.imperium.tower.item.ItemBuildProcessor;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.PotentialUtility;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;
import me.anfanik.steda.api.utility.ItemBuilder;
import me.func.mod.ui.battlepass.BattlePass;
import me.func.mod.ui.battlepass.BattlePassPageAdvanced;
import me.func.protocol.data.rare.DropRare;
import me.func.protocol.ui.battlepass.BattlePassUserData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class BattlePassProvider {

    private final List<BattlePassLinkedQuest> questLinks = new ArrayList<>();
    @Getter
    private int questCount;

    @Getter
    private final Int2ObjectOpenHashMap<BattlePassRewardList> baseRewards = new Int2ObjectOpenHashMap<>();
    @Getter
    private final Int2ObjectOpenHashMap<BattlePassRewardList> advancedRewards = new Int2ObjectOpenHashMap<>();

    public BattlePassProvider() {
        BattlePass.questResetActive(false);
    }

    public void initialize() {
        baseRewards.put(1, new BattlePassRewardList(1, false)
                .applyNext(new BattlePassRandomEquipRewardExecutor(ItemObject.FIRM_SWORD, 5))
                .applyNext(new BattlePassRandomEquipRewardExecutor(ItemObject.FIRM_BOOTS, 8))
                .applyNext(new BattlePassRandomEquipRewardExecutor(ItemObject.FIRM_HELMET, 13))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.COAL_HARVEST.getItem().createNew(25), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.EPIC_REAGENT_VERGE_MOD_ITEM.getItem().createNew(2)))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.MIXED_REAGENT_VERGE_MOD_ITEM.getItem().createNew(2)))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.IRON_HARVEST.getItem().createNew(25), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.ORB_OF_HEPHAESTUS_MOD_ITEM.getItem().createNew(5)))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.FORM_OF_TYCHE_MOD_ITEM.getItem().createNew(), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.GOLD_HARVEST.getItem().createNew(10), DropRare.RARE))
        );
        advancedRewards.put(1, new BattlePassRewardList(1, true)
                .applyNext(new BattlePassRandomEquipRewardExecutor(ItemObject.FIRM_BODY, 6))
                .applyNext(new BattlePassRandomEquipRewardExecutor(ItemObject.FIRM_LEGGINGS, 9))
                .applyNext(new BattlePassRandomEquipRewardExecutor(ItemObject.REKN_SWORD, 15))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.IRON_HARVEST.getItem().createNew(20), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.ABSOLUTE_DUST_MOD_ITEM.getItem().createNew(2), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.ORB_OF_DISTORTION_MOD_ITEM.getItem().createNew(2), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.GOLD_HARVEST.getItem().createNew(20), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.ORB_OF_HEPHAESTUS_MOD_ITEM.getItem().createNew(10), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.FORM_OF_TYCHE_MOD_ITEM.getItem().createNew(4), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.DIAMOND_HARVEST.getItem().createNew(10), DropRare.EPIC))
        );
        baseRewards.put(2, new BattlePassRewardList(2, false)
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.WAY_FRAGMENT_1.getItem().createNew(), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemBuildProcessor.of(ItemObject.PROJECTION_YI_BINA_SHARD.getItem())
                        .apply(imperiumItem -> imperiumItem.getGeneralSettings().itemLevel(61))
                        .catchItem().createNew(4), DropRare.EPIC))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.WAY_FRAGMENT_2.getItem().createNew(), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getItem().createNew(3), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.CLEANSING_TEAR_MOD_ITEM.getItem().createNew(2)))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.CATALYST_LIFE_MANA_MOD_ITEM.getItem().createNew(4), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.RESET_ORB.getItem().createNew(5)))
                .applyNext(new BattlePassItemRewardExecutor(ItemBuildProcessor.of(ItemObject.JUMP_ASPECT.getItem().create())
                        .apply(item -> item.getGearSettings().potential(PotentialUtility.processPotential(20)))
                        .catchItem().createNew(1), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getItem().createNew(6), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.ABSOLUTE_DUST_MOD_ITEM.getItem().createNew(4), DropRare.RARE))
        );
        advancedRewards.put(2, new BattlePassRewardList(2, true)
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.WAY_FRAGMENT_1.getItem().createNew(2), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemBuildProcessor.of(ItemObject.PROJECTION_YI_HOCHMA_SHARD.getItem())
                        .apply(imperiumItem -> imperiumItem.getGeneralSettings().itemLevel(61))
                        .catchItem().createNew(4), DropRare.EPIC))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.WAY_FRAGMENT_2.getItem().createNew(2), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.ESSENCE_OF_TYCHE_MOD_ITEM.getItem().createNew(5), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getItem().createNew(6), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.CATALYST_LIFE_MANA_MOD_ITEM.getItem().createNew(6), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.CATALYST_SPEED_MOD_ITEM.getItem().createNew(5), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.RESET_ORB.getItem().createNew(20)))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.SHARD_OF_ABYSS_MOD_ITEM.getItem().createNew(2), DropRare.EPIC))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.MASK_LOATHING.getItem().createNew(4), DropRare.RARE))
        );
        baseRewards.put(3, new BattlePassRewardList(3, false)
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.STACKED_DECK.getItem().createNew(10), DropRare.EPIC))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.ORB_OF_DISTORTION_MOD_ITEM.getItem().createNew(3), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.FORM_OF_TYCHE_MOD_ITEM.getItem().createNew(5), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.DIVINATION_CARD_15.getItem().createNew(40)))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.MASK_STRENGTHENING.getItem().createNew(4), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.CLEANSING_TEAR_MOD_ITEM.getItem().createNew(2)))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.CATALYST_SPEED_MOD_ITEM.getItem().createNew(4), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.MASK_MISERY.getItem().createNew(2), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.FRAGMENT_BATTLE_1.getItem().createNew()))
                .applyNext(new BattlePassAuraRewardExecutor(ItemBuilder.fromMaterial(Material.EMERALD)
                        .setName(AuraObject.DEFENCE_AURA.getFantasyAura().title())
                        .setLore("&7При наличии данной ауры", "&7Вы получите 49 личных фирм-монет", " ", "&8Посмотреть подробнее про ауру можно в меню аур")
                        .nbtBuilder().setTag("rid", "101").apply()
                        .build(), AuraObject.DEFENCE_AURA, 49))
        );
        advancedRewards.put(3, new BattlePassRewardList(3, true)
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.STACKED_DECK.getItem().createNew(20), DropRare.EPIC))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.MASK_WRATH.getItem().createNew(3), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.MASK_ENVY.getItem().createNew(3), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.CATALYST_CRIT_MOD_ITEM.getItem().createNew(10), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.ESSENCE_OF_TYCHE_MOD_ITEM.getItem().createNew(5), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getItem().createNew(6), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.CATALYST_ELEM_DMG_MOD_ITEM.getItem().createNew(10), DropRare.RARE))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.FRAGMENT_BATTLE_3.getItem().createNew()))
                .applyNext(new BattlePassItemRewardExecutor(ItemObject.DROP_OF_EDGE_MOD_ITEM.getItem().createNew(1), DropRare.LEGENDARY))
                .applyNext(new BattlePassPersonalizationRewardExecutor(
                        ItemBuilder.fromMaterial(Material.SKULL_ITEM).setName("Персонализация &6Катана \"Синергия\"")
                                .nbtBuilder()
                                .setTag("p13nModelId", "ae87b373-b92d-4f51-a00a-80ba47851f3b")
                                .apply().build(),
                        UUID.fromString("ae87b373-b92d-4f51-a00a-80ba47851f3b"))
                )
        );
        questLinks.add(QuestLinkBuilder.create()
                .nextKillAnyMonsters(10)
                .nextKillAnyMonsters(25)
                .nextKillAnyMonsters(50)
                .nextKillAnyMonsters(100)
                .nextKillAnyMonsters(200)
                .nextKillAnyMonsters(400)
                .nextKillAnyMonsters(600)
                .nextKillAnyMonsters(1000)
                .nextKillAnyMonsters(1600)
                .nextKillAnyMonsters(2000)
                .build());
        questLinks.add(QuestLinkBuilder.create()
                .nextKillAnyUsers(1)
                .nextKillAnyUsers(3)
                .nextKillAnyUsers(5)
                .nextKillAnyUsers(10)
                .nextKillAnyUsers(15)
                .nextKillAnyUsers(25)
                .nextKillAnyUsers(35)
                .nextKillAnyUsers(50)
                .nextKillAnyUsers(70)
                .nextKillAnyUsers(100)
                .build());
        questLinks.add(QuestLinkBuilder.create()
                .nextNPCQuest(1)
                .nextNPCQuest(3)
                .nextNPCQuest(4)
                .nextNPCQuest(6)
                .nextNPCQuest(10_000)
                .nextNPCQuest(8)
                .nextNPCQuest(9)
                .nextNPCQuest(10)
                .nextNPCQuest(11)
                .nextNPCQuest(13)
                .nextNPCQuest(14)
                .nextNPCQuest(15)
                .nextNPCQuest(16)
                .nextNPCQuest(17)
                .nextNPCQuest(18)
                .nextNPCQuest(19)
                .nextNPCQuest(20)
                .nextNPCQuest(21)
                .nextNPCQuest(22)
                .nextNPCQuest(23)
                .nextNPCQuest(24)
                .nextNPCQuest(25)
                .nextNPCQuest(26)
                .nextNPCQuest(27)
                .nextNPCQuest(28)
                .nextNPCQuest(29)
                .nextNPCQuest(30)
                .nextNPCQuest(31)
                .nextNPCQuest(32)
                .nextNPCQuest(33)
                .nextNPCQuest(34)
                .nextNPCQuest(35)
                .nextNPCQuest(36)
                .nextNPCQuest(37)
                .nextNPCQuest(38)
                .build());
        questLinks.add(QuestLinkBuilder.create()
                .nextLevelQuests(new int[][]{{2, 15, 1}, {16, 60, 2}, {61, 100, 1}})
                .build());
        // ~129 quests = ~1290exp = ~21lvl

        var count = 0;
        for (BattlePassLinkedQuest questLink : questLinks)
            count += questLink.count();

        this.questCount = count;
    }

    public List<BattlePassPageAdvanced> getPages(@NotNull final Player player) {
        return ImmutableList.<BattlePassPageAdvanced>builder()
                .add(new BattlePassPageAdvanced(35, 20, baseRewards.get(1).toIcons(player), advancedRewards.get(1).toIcons(player)),
                        new BattlePassPageAdvanced(35, 30, baseRewards.get(2).toIcons(player), advancedRewards.get(2).toIcons(player)),
                        new BattlePassPageAdvanced(35, 40, baseRewards.get(3).toIcons(player), advancedRewards.get(3).toIcons(player))
                )
                .build();
    }

    public List<BattlePassQuest> generateQuests(@NotNull final Player player) {
        var user = player.<User>getBungeePlayer();
        var battlePassSettings = user.findBattlePassSettings();

        final List<BattlePassQuest> result = new ArrayList<>();

        for (BattlePassLinkedQuest questLink : questLinks) {
            for (int i = 0; i < questLink.count(); i++) {
                var questFromChain = questLink.nextIndex(i);
                if (!battlePassSettings.getPassedQuests().contains(questFromChain.getId()) && !battlePassSettings.getActiveQuests().contains(questFromChain)) {
                    result.add(questFromChain);
                    break;
                }
            }
        }

        return result;
    }

    public void open(@NotNull final Player player) {
        var user = player.<User>getBungeePlayer();
        var battlePassSettings = user.findBattlePassSettings();

        var battlePassData = battlePassSettings.getLastData();

        BattlePass.timerEnd(player, Fantasy.currentSeason().seasonLeft(), Fantasy.currentSeason().seasonDuration());
        BattlePass.sales(player, battlePassSettings.getLastSales());
        BattlePass.INSTANCE.getBattlePasses().remove(player.getUniqueId());
        BattlePass.create(battlePassData);
        BattlePass.INSTANCE.send(player, battlePassData);
        BattlePass.INSTANCE.show(player, battlePassData, new BattlePassUserData(battlePassSettings.getCurrentExp(), battlePassSettings.isAdvanced(), 0));
    }

}
