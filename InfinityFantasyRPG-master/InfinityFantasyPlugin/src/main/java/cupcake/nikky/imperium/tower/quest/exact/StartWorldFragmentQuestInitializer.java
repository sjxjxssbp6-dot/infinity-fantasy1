package cupcake.nikky.imperium.tower.quest.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.item.ItemBuildProcessor;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.quest.QuestBuilder;
import cupcake.nikky.imperium.tower.quest.requirement.exact.BlockInteractRequirement;
import cupcake.nikky.imperium.tower.quest.requirement.exact.BreakBlockRequirement;
import cupcake.nikky.imperium.tower.quest.requirement.exact.KillEntityRequirement;
import cupcake.nikky.imperium.tower.quest.requirement.exact.TalkToNPCRequirement;
import cupcake.nikky.imperium.tower.quest.reward.ExpReward;
import cupcake.nikky.imperium.tower.quest.reward.GranReward;
import cupcake.nikky.imperium.tower.quest.reward.ItemReward;
import cupcake.nikky.imperium.tower.quest.reward.TreePointReward;
import cupcake.nikky.imperium.tower.tree.PointType;
import cupcake.nikky.imperium.tower.user.User;
import me.anfanik.steda.api.utility.Pair;
import org.bukkit.Location;
import org.bukkit.Material;

public class StartWorldFragmentQuestInitializer extends QuestInitializer {

    public StartWorldFragmentQuestInitializer() {
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder() // iLevel = 1
                .setId(1)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setOnComplete(player -> player.<User>getBungeePlayer().findQuestSettings().applyNewQuest(3000, true))
                .setTitle("До конца #1")
                .setDescription("""
                        Прибытие очередного нарушителя на наши безжизненные земли - это повод устроить пир!
                        Да у тебя преступных шевронов альфа-вида больше, чем моих зубов!
                        Ох-хох, только не неси мне очередной бред про потерю памяти.
                        Перво-наперво, мы находимся на осколке Беглецов - сюда отправляют таких как ты.
                        Да-да, я принял к сведению, что ты у нас с покалеченной головой.
                        Осколки представляют собой клочок земли на которой проживают самые разные существа.
                        И сразу радостная новость - с наших осколков еще никто не выбирался!
                        """)
                .appendRewards(new ExpReward(150))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
                .setId(2)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setTitle("До конца #2")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(1))
                .setOnComplete(player -> {
                    Fantasy.clientProvider().notification(player, "&aМеню телепортов всегда с Вами!", "&7Нажмите &6[TELEPORT]&7, чтобы открыть его");
                    Fantasy.clientProvider().notification(player, "&aДрево искажений всегда с Вами!", "&7Нажмите &6[TREE]&7, чтобы открыть его");
                })
                .setDescription("""
                        Почему так? Дак дохли как мухи, представляешь?
                        Тут всего-то пройти сквозь десяток осколков, убить пару десятков монстров и дело в шляпе!
                        Ладно, с лирикой кончили. Тебя будет ждать местный путеводитель по первым осколкам.
                        Но для начала другие дела. Одно из них - наше знаменитое Древо искажения.
                        Это один из наших способов усиления. И, естественно, покажись на глаза нашему старику.
                        И потом обратно ко мне, я еще не всё тебе рассказал. И отдал.
                        """)
                .appendRequirement(new TalkToNPCRequirement().appendTalkGoals(NPCs.TREE, NPCs.ELDER/*,NPCs.TRADER_2, NPCs.TRADER_5*/))
                .appendRewards(new ExpReward(150))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
                .setId(3)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setToPassNpc(NPCs.FRAGMENT_PATHFINDER)
                .setTitle("Отправка")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(2))
                .setDescription("""
                        Отлично. Надеюсь, ты был вежлив со стариком - тебе к нему придется возвращаться на протяжении всего пути.
                        Да, мы тоже повязаны правилами. Не обессудь. От меня держи наш личный подарок каждому новоприбывшему хулигану.
                        Хотя тебя назвать так язык не поворачивается. И можешь отправляться к Следопыту Осколков.
                        """)
                .appendRewards(new ItemReward(ItemBuildProcessor.of(ItemObject.FIRM_SWORD.getUniqueId())
                        .apply(item -> {
                            item.getGeneralSettings().itemLevel(1).requiredLevel(1);
                            item.getDropSettings().droppable(false);
                            ModifierProvider.getInstance().createLowestBaseModifiers(item);
                        }), 1))
                .appendRequirement(new TalkToNPCRequirement().appendTalkGoals(NPCs.FRAGMENT_PATHFINDER))
                .appendRewards(new ExpReward(150))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()
                .setId(4)
                .setStorageNpc(NPCs.FRAGMENT_PATHFINDER)
                .setOnComplete(player -> this.applyBeaconNotification(player, BeaconObjects.DEAD_FRAGMENT))
                .setTitle("Мертвый осколок")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(3))
                .setDescription("""
                        Грань поглотила уже множество миров. И продолжает поглощать.
                        Ты же, путник, попал на перекресток этих самых миров.
                        Отсюда видно обитателей мертвого осколка. Догадаешься почему мертвого?
                        Нежить и волки - ничего необычного.
                        В прямом смысла этого слова - у Грани явно какие-то непростые отношения с нежитью.
                        Но их частота не отменяет их опасности.
                        """)
                .appendRequirement(
                        new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.GHOUL, 3), new Pair<>(MonsterType.INFECTED_WOLF, 3)))
                .appendRewards(new ExpReward(1_000), new GranReward(200), new TreePointReward(PointType.CHARACTER), new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 3
                .setId(5)
                .setStorageNpc(NPCs.FRAGMENT_PATHFINDER)
                .setTitle("Крафт?")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(4))
                .setDescription("Уже заметил, что монстр монстру рознь? Дальше - хуже, да и простые обноски, что на тебе, уже не помогут. Наш верстак станет тебе отличным помощником. И не только он.")
                .appendRequirement(
                        new TalkToNPCRequirement().appendTalkGoals(NPCs.TRADER_1, NPCs.BATTLE_PASS),
                        new BlockInteractRequirement().appendGoal("верстаком", new Location(MapServiceProcess.GAME_WORLD, 7, 100, 20, 0, 0))
                )
                .appendRewards(new ExpReward(450), new GranReward(200), new ItemReward(ItemObject.PICKAXE.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 4
                .setId(6)
                .setStorageNpc(NPCs.FRAGMENT_PATHFINDER)
                .setOnComplete(player -> this.applyBeaconNotification(player, BeaconObjects.FRAGMENT_OF_ARACHNOPHOBIA))
                .setTitle("Мечта арахнофоба")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(5))
                .setDescription("""
                        Канрийская деревня была весьма мирным местом на протяжении многих столетий.
                        И ты, я уверен, не догадаешься почему. На протяжении всего времени они оставались деревней.
                        С точно выверенным количеством людишек. Лишних просто убивали, представь?
                        Никаких тебе несчастных случаев - коль лишним рожден, значит предназначение быть подарком Канрийскому божеству.
                        Возможно, Грань принесла спасение той деревне.
                        """)
                .appendRequirement(
                        new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.STILLBORN_SPIDER, 3), new Pair<>(MonsterType.CANRIAN_SPIDER, 4)),
                        new BreakBlockRequirement().appendGoal(new Pair<>(Material.COAL_ORE, 5)).appendTitles(new Pair<>(Material.COAL_ORE, "Угольную руду"))
                )
                .appendRewards(new ExpReward(600), new GranReward(200), new ItemReward(ItemObject.EPIC_REAGENT_VERGE_MOD_ITEM.getUniqueId(), 2))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 5
                .setId(7)
                .setStorageNpc(NPCs.FRAGMENT_PATHFINDER)
                .setTitle("Спутник?")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(6))
                .setDescription("""
                        Далее придется туго без средства передвижения.
                        Расстояние меньше не станет, спутником тебе нужно обзавестись.
                        Но да, Старец тебе расскажет обо всем. И к Парну зайти следует.
                        """)
                .appendRequirement(new TalkToNPCRequirement().appendTalkGoals(NPCs.TRADER_5, NPCs.TRADER_2, NPCs.PARN))
                .appendRewards(new ExpReward(325), new GranReward(200))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 5
                .setId(10_000).setVisibleQuestId(false)
                .setStorageNpc(NPCs.PARN)
                .setTitle("Спутник!")
                .setOnComplete(player -> Fantasy.clientProvider().notification(player,
                        "&7Вам стал доступен Ваш &6спутник&7!",
                        "&7Нажмите &6[MOUNT_USE]&7, чтобы его &6призвать"))
                .setReceiveHandler(questedPlayer -> questedPlayer.isPassed(6) || questedPlayer.findInProgress(7).isPresent())
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(6))
                .setDescription("""
                        С этого момента, мой милый друг, у тебя появится не менее милый, но уже твой, спутник.\s
                        """)
                .appendRewards(new ExpReward(325), new TreePointReward(PointType.CHARACTER))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 5
                .setId(8)
                .setStorageNpc(NPCs.FRAGMENT_PATHFINDER)
                .setOnComplete(player -> this.applyBeaconNotification(player, BeaconObjects.ADJACENT_FRAGMENT))
                .setTitle("Смежный осколок")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(7))
                .setDescription("""
                        В некотором странном понимании, Смежный осколок - чудо.
                        Огненный скелет и вожак волков на клочке пространства выполняют волю Грани.
                        Хотя что я тебе рассказываю, все равно не поймешь моего изумления.
                        Уверен, справишься. Хотя кого я обманываю - мне абсолютно без разницы.
                        Ведь мои поручения - буквально ничего, вот у Асклепия... Но оставим это на потом.
                        """)
                .appendRequirement(
                        new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.WOLF_LEADER, 3), new Pair<>(MonsterType.FIRE_ELEMENTAL, 4)),
                        new BreakBlockRequirement().appendGoal(new Pair<>(Material.IRON_ORE, 5)).appendTitles(new Pair<>(Material.IRON_ORE, "Железную руду")))
                .appendRewards(new ExpReward(900), new GranReward(200))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 7
                .setId(9)
                .setStorageNpc(NPCs.FRAGMENT_PATHFINDER)
                .setToPassNpc(NPCs.ASCLEPIUS)
                .setOnComplete(player -> this.applyBeaconNotification(player, BeaconObjects.ENDERMIT_FRAGMENT))
                .setTitle("Осколок эндермитов")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(8))
                .setDescription("""
                        Ко всем рано или поздно приходит праздник.
                        К следующему осколку он пришел в виде эндермитов.
                        Да-да, праздник. Высокоценный материал, который продавался на территории всего мира.
                        Конечно, больше его не существует. Грань забрала свое.
                        То, что подарила - вырвала в стократном объеме.
                        Этот осколок служит напоминанием безумия всех, кто коснулся Грани.
                        Если справишься, то твой путь будет лежать уже через Асклепия.
                        Далеко ходить не надо, он на нашем осколке.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.CANRIAN_ENDERMITE, 5), new Pair<>(MonsterType.ANDOR, 5)),
                        new TalkToNPCRequirement().appendTalkGoals(NPCs.TRADER_4))
                .appendRewards(new ExpReward(1200), new GranReward(200), new ItemReward(ItemObject.DIVINATION_CARD_15.getUniqueId(), 4))
                .build());
    }

}
