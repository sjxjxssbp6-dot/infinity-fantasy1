package cupcake.nikky.imperium.tower.quest.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.quest.QuestBuilder;
import cupcake.nikky.imperium.tower.quest.requirement.exact.BreakBlockRequirement;
import cupcake.nikky.imperium.tower.quest.requirement.exact.FishingRequirement;
import cupcake.nikky.imperium.tower.quest.requirement.exact.KillEntityRequirement;
import cupcake.nikky.imperium.tower.quest.reward.ExpReward;
import cupcake.nikky.imperium.tower.quest.reward.FirmReward;
import cupcake.nikky.imperium.tower.quest.reward.GranReward;
import cupcake.nikky.imperium.tower.quest.reward.ItemReward;
import me.anfanik.steda.api.utility.Pair;
import org.bukkit.Material;

public class DailyQuestInitializer extends QuestInitializer {

    public DailyQuestInitializer() {
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
                .setId(49)
                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(28))
                .setStorageNpc(NPCs.DAILY_QUESTS).setDaily(true).setVisibleQuestId(false)
                .setTitle("Ежедневный сбор")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(28))
                .appendDescription("Вам необходимо добыть 15 алмазной руды и словить 15 рыб.")
                .appendRequirement(
                        new BreakBlockRequirement().appendGoal(new Pair<>(Material.DIAMOND_ORE, 15)).appendTitles(new Pair<>(Material.DIAMOND_ORE, "Алмазную руду")),
                        new FishingRequirement().setGoal(15)
                )
                .appendRewards(new ExpReward(100_000), new FirmReward(1), new GranReward(2500),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
                .setId(50)
                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(31))
                .setStorageNpc(NPCs.DAILY_QUESTS).setDaily(true).setVisibleQuestId(false)
                .setTitle("Очистка местности #1")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(31))
                .appendDescription("Вам необходимо ликвидировать Канрийского беглеца.")
                .appendRequirement(new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.CANRIAN_FUGITIVE, 20)))
                .appendRewards(new ExpReward(100_000), new FirmReward(1), new GranReward(2_500),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1))
                .build());

        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
                .setId(51)
                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(32))
                .setStorageNpc(NPCs.DAILY_QUESTS).setDaily(true).setVisibleQuestId(false)
                .setTitle("Очистка местности #2")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(32))
                .appendDescription("Вам необходимо ликвидировать Элементалиста Грани.")
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.EDGE_ELEMENTALIST, 20))
                )
                .appendRewards(new ExpReward(100_000), new FirmReward(1), new GranReward(2500),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
                .setId(52)
                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(33))
                .setStorageNpc(NPCs.DAILY_QUESTS).setDaily(true).setVisibleQuestId(false)
                .setTitle("Очистка местности #3")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(33))
                .appendDescription("Вам необходимо ликвидировать Новорожденного Хаосом.")
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.NEWBORN_BY_CHAOS, 25))
                )
                .appendRewards(new ExpReward(100_000), new FirmReward(1), new GranReward(2500),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
                .setId(53)
                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(34))
                .setStorageNpc(NPCs.DAILY_QUESTS).setDaily(true).setVisibleQuestId(false)
                .setTitle("Очистка местности #4")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(34))
                .appendDescription("Вам необходимо ликвидировать Столп Грани.")
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.EDGE_PILLAR, 25))
                )
                .appendRewards(new ExpReward(100_000), new FirmReward(1), new GranReward(2500),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 2))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
                .setId(54)
                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(35))
                .setStorageNpc(NPCs.DAILY_QUESTS).setDaily(true).setVisibleQuestId(false)
                .setTitle("Очистка местности #5")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(35))
                .appendDescription("Вам необходимо ликвидировать Пробужденного скелета.")
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.AWAKENED_SKELET, 25))
                )
                .appendRewards(new ExpReward(100_000), new FirmReward(2), new GranReward(2500),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 2))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
                .setId(55)
                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(36))
                .setStorageNpc(NPCs.DAILY_QUESTS).setDaily(true).setVisibleQuestId(false)
                .setTitle("Очистка местности #6")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(36))
                .appendDescription("Вам необходимо ликвидировать Жрицу Хаоса")
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.PRIEST_OF_CHAOS, 25))
                )
                .appendRewards(new ExpReward(100_000), new FirmReward(2), new GranReward(2500),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 2))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
                .setId(56)
                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(37))
                .setStorageNpc(NPCs.DAILY_QUESTS).setDaily(true).setVisibleQuestId(false)
                .setTitle("Очистка местности #7")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(37))
                .appendDescription("Вам необходимо ликвидировать Духа Поля Битвы 32 уровня")
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.BATTLE_GROUND_IRON_GOLEM, 25))
                )
                .appendRewards(new ExpReward(100_000), new FirmReward(2), new GranReward(3000),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 3))
                .build());
    }

}
