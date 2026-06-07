package cupcake.nikky.imperium.tower.quest.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.quest.QuestBuilder;
import cupcake.nikky.imperium.tower.quest.requirement.exact.BreakBlockRequirement;
import cupcake.nikky.imperium.tower.quest.requirement.exact.FishingRequirement;
import cupcake.nikky.imperium.tower.quest.requirement.exact.KillEntityRequirement;
import cupcake.nikky.imperium.tower.quest.requirement.exact.TalkToNPCRequirement;
import cupcake.nikky.imperium.tower.quest.reward.ExpReward;
import cupcake.nikky.imperium.tower.quest.reward.GranReward;
import cupcake.nikky.imperium.tower.quest.reward.ItemReward;
import cupcake.nikky.imperium.tower.quest.reward.TreePointReward;
import cupcake.nikky.imperium.tower.tree.PointType;
import me.anfanik.steda.api.utility.Pair;
import org.bukkit.Material;

public class MiddleWorldFragmentsQuestInitializer extends QuestInitializer {

    public MiddleWorldFragmentsQuestInitializer() {
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 9
                .setId(10)
                .setStorageNpc(NPCs.ASCLEPIUS)
                .setOnComplete(player -> this.applyBeaconNotification(player, BeaconObjects.DARK_FRAGMENT))
                .setTitle("Темный осколок")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(9))
                .setDescription("""
                        Прими мои поздравления, начало пути положено.
                        Но дальше - больше, а конкретно следующий осколок.
                        Представь, Грань выпустила на волю Лунного ангела! Шутка.
                        Лично я ставлю на суррогат, но людям из того мира хватило подобного.
                        Он буквально стал иконой для всего их мира. Ужасное зрелище, в один момент обрести миллионы последователей.
                        Благо он был на поводке Грани. Скорее всего до сих пор на её поводке, но уже под личным присмотром.
                        Чем бы Грань ни было, но её же питомцы её и боятся.
                        Когда казалось, что суррогат лунного ангела вот-вот станет единственным королем того мира, на свет появились Темные оцелоты и Лунные пауки. Символично.
                        Благо, на осколке остались лишь самые слабые особи. Тебе они должны быть по силам.
                        """)
                .appendRequirement(
                        new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.MOON_SPIDER, 5), new Pair<>(MonsterType.SHADOW_OCELOT, 6)))
                .appendRewards(new ExpReward(1350), new GranReward(250), new ItemReward(ItemObject.MIXED_REAGENT_VERGE_MOD_ITEM.getUniqueId(), 2))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 11
                .setId(11)
                .setStorageNpc(NPCs.ASCLEPIUS)
                .setOnComplete(player -> this.applyBeaconNotification(player, BeaconObjects.DRAGON_FRAGMENT))
                .setTitle("Потерянная у псевдо-дракона")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(10))
                .setDescription("""
                        Те, кто когда-то рождался бок о бок с драконами - развязали войну.
                        Грань лишь добавила перчинки к их пиршеству.
                        А потом выдернула кусок пространства вместе с сильнейшим драконом.
                        Красавец, ты его еще увидишь. Жаль он ушел в вечность.
                        Альпы и Пауки сноходцы лишь побочный продукт. А вот о главном я расскажу тебе позже.
                        Скажем так - личное задание. Проверим твои способности.
                        У меня, к слову, сестрица есть, Хала звать её. Любит по осколкам прохаживаться.
                        И в ближайшее время она будет как раз в месте твоего поручения.
                        Она будет в курсе твоего поручения - тебе надо всего-лишь показаться на глаза. Подтверди же - просто, а?
                        """)
                .appendRequirement(
                        new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.DREAM_WALKER_SPIDER, 6), new Pair<>(MonsterType.ALP, 6)),
                        new TalkToNPCRequirement().appendTalkGoals(NPCs.RIN))
                .appendRewards(new ExpReward(1800), new GranReward(250), new ItemReward(ItemObject.FISHING_ROD.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 13
                .setId(12)
                .setStorageNpc(NPCs.ASCLEPIUS)
                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(10))
                .setTitle("Скаррига")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(10))
                .setDescription("""
                        Ах да, главный продукт Грани на том осколке - это Скаррига.
                        Насколько мне известно, он засел на самом верху башни.
                        Думаю, нам всем будет лучше, если ты оборвешь существование данного монстра.
                        """)
                .appendRequirement(
                        new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.SKARIGA, 1)))
                .appendRewards(new ExpReward(3_000), new GranReward(1000),
                        new ItemReward(ItemObject.DIVINATION_CARD_23.getUniqueId(), 1),
                        new ItemReward(ItemObject.JUMP_ASPECT.getUniqueId(), 1),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 3),
                        new TreePointReward(PointType.CHARACTER))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 13
                .setId(13)
                .setStorageNpc(NPCs.ASCLEPIUS)
                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(11))
                .setOnComplete(player -> this.applyBeaconNotification(player, BeaconObjects.HERMIT_FRAGMENT))
                .setTitle("Обитель отшельника")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(11))
                .setDescription("""
                        Один из немногих осколков, что попал к нам с его жителями.
                        Немного искареженными, но все-таки коренными жителями.
                        Но ничего, земля примет всех, как говорят Беглецы.
                        """)
                .appendRequirement(
                        new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.IRON_SKELETON, 7), new Pair<>(MonsterType.INFECTED_MLENIA_RESIDENT, 7))
                )
                .appendRewards(new ExpReward(2100), new GranReward(250))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 15
                .setId(14)
                .setStorageNpc(NPCs.ASCLEPIUS)
                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(12))
                .setTitle("Отшельник Ильф")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(12))
                .setDescription("""
                        По поводу твоего дела, помимо прочего, исполни мою просьбу - проведай местного отшельника.
                        Уж не знаю как он до сих пор среди монстров умудряется жить.
                        """)
                .appendRequirement(
                        new BreakBlockRequirement().appendGoal(new Pair<>(Material.GOLD_ORE, 5)).appendTitles(new Pair<>(Material.GOLD_ORE, "Золотую руду")),
                        new FishingRequirement().setGoal(5),
                        new TalkToNPCRequirement().appendTalkGoals(NPCs.HERMIT_ILF)
                )
                .appendRewards(new ExpReward(2250), new GranReward(250), new ItemReward(ItemObject.ABSOLUTE_DUST_MOD_ITEM.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 15
                .setId(15)
                .setStorageNpc(NPCs.ASCLEPIUS)
                .setToPassNpc(NPCs.METSA)
                .setOnComplete(player -> this.applyBeaconNotification(player, BeaconObjects.ENSLAVED_CASTLE_FRAGMENT))
                .setTitle("Порабощенный замок")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(14))
                .setDescription("""
                        Порождения Грани уже успели исказить величавый замок.
                        Но помехой тебе это стать не должно.
                        Это ведь, считай, даже не середина пути.
                        И далее твоим “гидом” станет Метса.
                        Думаю, найдешь её без моих подсказок.
                        """)
                .appendRequirement(
                        new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.DISCIPLE_OF_LICH, 6), new Pair<>(MonsterType.CHAOS_SKELETON, 8)))
                .appendRewards(new ExpReward(2400), new GranReward(250), new ItemReward(ItemObject.FORM_OF_TYCHE_MOD_ITEM.getUniqueId(), 1))
                .build());
    }

}
