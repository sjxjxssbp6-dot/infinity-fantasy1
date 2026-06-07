package cupcake.nikky.imperium.tower.quest.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.quest.QuestBuilder;
import cupcake.nikky.imperium.tower.quest.requirement.exact.*;
import cupcake.nikky.imperium.tower.quest.reward.FlowerReward;
import me.anfanik.steda.api.utility.Pair;
import org.bukkit.Material;

public class EventQuestInitializer extends QuestInitializer {

    public EventQuestInitializer() {
//        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
//                .setId(3000)
//                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(1))
//                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(1))
//                .setStorageNpc(NPCs.SPRING_EVENT).setVisibleQuestId(false)
//                .setTitle("Весенняя прогулка")
//                .appendDescription("Смотри какой цветочек! Вообще-то все дарят мне такую красоту, но я готова поделиться! Но просто так не дам, нужно что-то придумать...",
//                        "Что-то не слишком сложное для тебя... Хм! Ну да, ты же умеешь только разрушать и убивать.")
//                .appendRequirement(new KillAnyEntityRequirement().setupGoal(10, -1), new TalkToNPCRequirement().appendTalkGoals(NPCs.TRADER_5))
//                .appendRewards(new FlowerReward(3))
//                .build());
//        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
//                .setId(3001)
//                .setStorageNpc(NPCs.SPRING_EVENT).setVisibleQuestId(false)
//                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(3000))
//                .setTitle("Весенний сбор")
//                .appendDescription("А я тут вспомнила - ты же наверняка еще собирательством занимаешься! Но нет-нет-нет, сейчас у меня другое желание!")
//                .appendRequirement(new CollectItemRequirement().appendGoal(
//                        new Pair<>(ItemObject.DEFENSE_HELMET.getUniqueId(), 1),
//                        new Pair<>(ItemObject.DEFENSE_BODY.getUniqueId(), 1),
//                        new Pair<>(ItemObject.DEFENSE_LEGGINGS.getUniqueId(), 1),
//                        new Pair<>(ItemObject.DEFENSE_BOOTS.getUniqueId(), 1)
//                ))
//                .appendRewards(new FlowerReward(3))
//                .build());
//        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
//                .setId(3002)
//                .setStorageNpc(NPCs.SPRING_EVENT).setVisibleQuestId(false)
//                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(3001))
//                .setTitle("Весенняя добыча угля")
//                .appendDescription("Как же приятно однако платить за свои желания цветочками! Хотя я бы предпочла, чтобы цветочки дарили мне. И именно дарили!",
//                        "А ты ведь помнишь, да? Я знаю-знаю! Собирательством занимаешься! Меняю уголек на конфетки! Ой, на цветочки!")
//                .appendRequirement(new BreakBlockRequirement().appendGoal(new Pair<>(Material.COAL_ORE, 5)).appendTitles(new Pair<>(Material.COAL_ORE, "Угольную руду")))
//                .appendRewards(new FlowerReward(3))
//                .build());
//        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
//                .setId(3003)
//                .setStorageNpc(NPCs.SPRING_EVENT).setVisibleQuestId(false)
//                .setReceiveHandler(questSettings -> questSettings.isPassed(3001))
//                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(3001))
//                .setTitle("Весенняя добыча железа")
//                .appendDescription("А железо сможешь? Сможешь же, а?")
//                .appendRequirement(new BreakBlockRequirement().appendGoal(new Pair<>(Material.IRON_ORE, 5)).appendTitles(new Pair<>(Material.IRON_ORE, "Железную руду")))
//                .appendRewards(new FlowerReward(3))
//                .build());
//        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
//                .setId(3004)
//                .setStorageNpc(NPCs.SPRING_EVENT).setVisibleQuestId(false)
//                .setReceiveHandler(questSettings -> questSettings.isPassed(3001))
//                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(3001))
//                .setTitle("Весенняя добыча золота")
//                .appendDescription("Знаешь, а я ведь и золото люблю...")
//                .appendRequirement(new BreakBlockRequirement().appendGoal(new Pair<>(Material.GOLD_ORE, 5)).appendTitles(new Pair<>(Material.GOLD_ORE, "Золотую руду")))
//                .appendRewards(new FlowerReward(3))
//                .build());
//        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
//                .setId(3005)
//                .setStorageNpc(NPCs.SPRING_EVENT).setVisibleQuestId(false)
//                .setReceiveHandler(questSettings -> questSettings.isPassed(3001))
//                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(3001))
//                .setTitle("Весенняя добыча алмазов")
//                .appendDescription("А как насчет алмазов? Или ты в такую глубь не ходишь?")
//                .appendRequirement(new BreakBlockRequirement().appendGoal(new Pair<>(Material.DIAMOND_ORE, 5)).appendTitles(new Pair<>(Material.DIAMOND_ORE, "Алмазную руду")))
//                .appendRewards(new FlowerReward(3))
//                .build());
//        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
//                .setId(3006)
//                .setStorageNpc(NPCs.SPRING_EVENT).setVisibleQuestId(false)
//                .setReceiveHandler(questSettings -> questSettings.isPassed(3001))
//                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(3001))
//                .setTitle("Весенняя добыча изумрудов")
//                .appendDescription("А ты уже слышал про Поле Битвы? Говорят, там есть изумруды...")
//                .appendRequirement(new BreakBlockRequirement().appendGoal(new Pair<>(Material.EMERALD_ORE, 5)).appendTitles(new Pair<>(Material.EMERALD_ORE, "Изумрудную руду")))
//                .appendRewards(new FlowerReward(3))
//                .build());
//        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
//                .setId(3007)
//                .setStorageNpc(NPCs.SPRING_EVENT).setVisibleQuestId(false)
//                .setReceiveHandler(questSettings -> questSettings.isPassed(3001))
//                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(3001))
//                .setTitle("Весенняя ловля рыбы")
//                .appendDescription("А ты ведь не только рудой занимаешься, правда ведь? Я бы и от красивой рыбки не отказалась... Она правда вонять будет, но это не главное!",
//                        "Главное, что ты ради меня рыбку поймал! Ведь так?")
//                .appendRequirement(new FishingRequirement().setGoal(3))
//                .appendRewards(new FlowerReward(3))
//                .build());
//        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
//                .setId(3008)
//                .setStorageNpc(NPCs.SPRING_EVENT).setDaily(true).setVisibleQuestId(false)
//                .setReceiveHandler(questSettings -> questSettings.isPassed(3002))
//                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(3002))
//                .setTitle("Ежедневная весенняя чистка")
//                .appendDescription("Ладно-ладно, давай изменим подход! Так должно быть удобнее и тебе, и мне... Хотя я люблю подарки в любое время, не только весной!",
//                        "Кто же виноват, что нужны особые даты чтобы подарить мне красивый изумрудик! Но цветочки дам-дам, только остатки Грани подчисти ради меня.")
//                .appendRequirement(new KillAnyEntityRequirement().setupGoal(25, -1))
//                .appendRewards(new FlowerReward(10))
//                .build());
    }

}
