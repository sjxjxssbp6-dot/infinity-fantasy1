package cupcake.nikky.imperium.tower.quest.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.quest.QuestBuilder;
import cupcake.nikky.imperium.tower.quest.requirement.exact.KillEntityRequirement;
import cupcake.nikky.imperium.tower.quest.requirement.exact.TalkToNPCRequirement;
import cupcake.nikky.imperium.tower.quest.reward.ExpReward;
import cupcake.nikky.imperium.tower.quest.reward.GranReward;
import cupcake.nikky.imperium.tower.quest.reward.ItemReward;
import cupcake.nikky.imperium.tower.quest.reward.TreePointReward;
import cupcake.nikky.imperium.tower.tree.PointType;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import me.anfanik.steda.api.utility.Pair;

public class EndWorldFragmentsQuestInitializer extends QuestInitializer {

    public EndWorldFragmentsQuestInitializer() {
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 17
                .setId(16)
                .setStorageNpc(NPCs.METSA)
                .setOnComplete(player -> this.applyBeaconNotification(player, BeaconObjects.MOUNTAIN_FRAGMENT))
                .setTitle("Горный осколок")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(15))
                .setDescription("""
                        Вроде как на осколке царит красота, но представь насколько красивее там было до появления Грани?
                        Нет ничего человечного у неё... Хотя нет, есть. Сколько же людей было ею поглощено?
                        Но, как обычно с этими всеми осколками - пейзаж красив, а внутри гниль. Так и там, личи да стихийные скелеты.
                        """)
                .appendRequirement(
                        new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.ELEMENTAL_SKELETON, 8), new Pair<>(MonsterType.LICH, 8)))
                .appendRewards(new ExpReward(2700), new GranReward(300), new ItemReward(ItemObject.DIVINATION_CARD_18.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 19
                .setId(17)
                .setStorageNpc(NPCs.METSA)
                .setOnComplete(player -> this.applyBeaconNotification(player, BeaconObjects.BINA_FRAGMENT))
                .setTitle("Осколок Бина")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(16))
                .setDescription("""
                        Не будем о грустном. Поговорим о печальном. С Гранью же получился казус. Для неё казус, точнее говоря, а для нас трагедия.
                        И не одна, но тебе пока что стоит знать только про Бину. Этот осколок буквально далекий отголосок обмена ударами между Гранью и её дитем.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.ABYSS_SKELETON, 9), new Pair<>(MonsterType.FOLLOWER_OF_BINA, 9)))
                .appendRewards(new ExpReward(3000), new GranReward(300), new ItemReward(ItemObject.DIVINATION_CARD_9.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 21
                .setId(18)
                .setStorageNpc(NPCs.METSA)
                .setOnComplete(player -> {
                    this.applyBeaconNotification(player, BeaconObjects.GOLEM_FRAGMENT);
                    this.applyBeaconNotification(player, BeaconObjects.CORRUPTED_FRAGMENT);
                })
                .setTitle("Осколок големов")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(17))
                .setDescription("""
                        И возможно, ты сможешь увидеть это дитя. Где-то там, где время встало, а пространство дрожит от силы монстров, существую семена.
                        Мы называем их семенами. И место то мы зовем Путями.
                        Сейчас тебе рано даже смотреть в ту сторону, но придет время, я уверена, ты отправишься туда в поисках ответов.
                        Прорастая, эти семена создают трансфер для проекции силы монстров за гранью силы.
                        Но кто знает, возможно есть семены, которые могут пробить путь и для истинной сути детей Грани?
                        Надеюсь, нам не суждено об этом узнать.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(new Pair<>(MonsterType.CHARMED_GOLEM, 9), new Pair<>(MonsterType.ELEMENTAL_GOLEM, 10)))
                .appendRewards(new ExpReward(3300), new GranReward(300), new ItemReward(ItemObject.EPIC_REAGENT_VERGE_MOD_ITEM.getUniqueId(), 10))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 23
                .setId(19)
                .setStorageNpc(NPCs.METSA)
                .setToPassNpc(NPCs.HISSA)
                .setOnComplete(player -> {
                    this.applyBeaconNotification(player, BeaconObjects.DECAYED_FRAGMENT);
                    this.applyBeaconNotification(player, BeaconObjects.DISTORTED_FRAGMENT);
                })
                .setTitle("Оскверненный и сгнивший осколки")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(18))
                .setDescription("""
                        Судьбы оставшихся осколков нам неизвестны. Тебе предстоит провести самостоятельную разведку, ликвидацию и продвижение вглубь.
                        Но есть шанс, что чем-то тебе сможет помочь Хисса. Она любит ставить себе сложные задачи. По крайней мере любила.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.CORRUPTED_PIG, 10), new Pair<>(MonsterType.DARK_ENDERMIT, 10),
                        new Pair<>(MonsterType.DISTORTED_VEX, 11), new Pair<>(MonsterType.DECAYED_SHELL, 11)
                ))
                .appendRewards(new ExpReward(3900), new GranReward(600), new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 27
                .setId(20)
                .setStorageNpc(NPCs.HISSA)
                .setOnComplete(player -> {
                    this.applyBeaconNotification(player, BeaconObjects.ETERNAL_FRAGMENT);
                    this.applyBeaconNotification(player, BeaconObjects.WOLF_FRAGMENT);
                })
                .setTitle("Искаженный и вечный осколки")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(19))
                .setDescription("""
                        Нет-нет! Так не пойдет. Я тратила время, силы, рисковала жизнью, а ты тут видите ли особенный чем-то?
                        На, смотри. Видишь? Искаженные паук и разбойник. Вечные стражник и дитя. Больше от меня ничего не получишь.
                        Хотя нет, постой. Есть еще и Морта. Нет, не одна из старых Парок, а лишь её искажение, но тебе и этого хватит. Теперь можешь идти.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.DISTORT_OF_MORTA, 1),
                        new Pair<>(MonsterType.DISTORTED_ILLAGER, 11), new Pair<>(MonsterType.DISTORTED_SPIDER, 12),
                        new Pair<>(MonsterType.ETERNAL_GUARDIAN, 12), new Pair<>(MonsterType.ETERNAL_BABY, 12)
                ))
                .appendRewards(new ExpReward(4500), new GranReward(1000),
                        new ItemReward(ItemObject.DIVINATION_CARD_28.getUniqueId(), 1),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1),
                        new ItemReward(ItemObject.ABSOLUTE_DUST_MOD_ITEM.getUniqueId(), 5),
                        new TreePointReward(PointType.CHARACTER)
                )
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 31
                .setId(21)
                .setStorageNpc(NPCs.HISSA)
                .setOnComplete(player -> {
                    this.applyBeaconNotification(player, BeaconObjects.FREE_FRAGMENT);
                    this.applyBeaconNotification(player, BeaconObjects.SOUL_FRAGMENT);
                })
                .setTitle("Волчий и вольный осколки")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(20))
                .setDescription("""
                        Смотри-смотри, не верти носом. Читай: пепельный волк, морк, вольная душа и последователь Фатума.
                        И не шарахайся от Фатума - были бы это не осколки, а цельные миры, то да, тогда бы и я не лезла. А так, раз дают - бери.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.ASHEN_WOLF, 13), new Pair<>(MonsterType.MORK, 13),
                        new Pair<>(MonsterType.FREE_SOUL, 13), new Pair<>(MonsterType.FOLLOWER_OF_FATUM, 14)
                ))
                .appendRewards(new ExpReward(5100), new GranReward(600), new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 35
                .setId(22)
                .setStorageNpc(NPCs.HISSA)
                .setOnComplete(player -> {
                    this.applyBeaconNotification(player, BeaconObjects.WATCHER_FRAGMENT);
                    this.applyBeaconNotification(player, BeaconObjects.KETER_AND_CHESED_FRAGMENT);
                })
                .setTitle("Осколки духов и созерцателей")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(21))
                .setDescription("""
                        А ты весьма крепок. Знаешь, на вид и не скажешь. Значит и с душами погибших проблем не будет. Верно? И с подарками Грани тоже.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.SOUL_OF_BANDIT_WARRIOR, 14), new Pair<>(MonsterType.SOUL_OF_BANDIT_ARCHER, 14),
                        new Pair<>(MonsterType.EDGE_WATCHER, 15), new Pair<>(MonsterType.ABSORBED_BY_EDGE, 15)
                ))
                .appendRewards(new ExpReward(5700), new GranReward(600), new ItemReward(ItemObject.ORB_OF_DISTORTION_MOD_ITEM.getUniqueId(), 2))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 39
                .setId(23)
                .setStorageNpc(NPCs.HISSA)
                .setToPassNpc(NPCs.GLEM)
                .setOnComplete(player -> {
                    this.applyBeaconNotification(player, BeaconObjects.CENTURION_FRAGMENT);
                    this.applyBeaconNotification(player, BeaconObjects.FOREST_FRAGMENT);
                })
                .setTitle("Осколки милости и сотника")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(22))
                .setDescription("""
                        Еще одни дети Грани: Кетер и Хесед. Их силу, к счастью, мы пока не видели.
                        Не сказать, что мы впечатлены их сторонниками - Грань с предателями поступает весьма скверно.
                        Забирает силы и выкидывает вместе с разбитым миром в никуда.
                        Но, можно сказать, перекрестку повезло. Иначе мы бы даже и не знали об их существовании.
                        Если у тебя останутся силы и желание - зайдешь к Глему.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.FOLLOWER_OF_KETHER, 15), new Pair<>(MonsterType.PRIEST_OF_CHESED, 16),
                        new Pair<>(MonsterType.EDGE_OCELOT, 16), new Pair<>(MonsterType.ETERNAL_CENTURION, 16)
                ))
                .appendRewards(new ExpReward(6300), new GranReward(600), new ItemReward(ItemObject.ABSOLUTE_DUST_MOD_ITEM.getUniqueId(), 3))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 43
                .setId(24)
                .setStorageNpc(NPCs.GLEM)
                .setOnComplete(player -> {
                    this.applyBeaconNotification(player, BeaconObjects.FIELDS_FRAGMENT);
                    this.applyBeaconNotification(player, BeaconObjects.MERNIGA_FRAGMENT);
                })
                .setTitle("Леса и поля")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(23))
                .setDescription("""
                        Хоть эти осколки и рядом, но история их смерти противоположна друг другу.
                        Одни остались защищать свои земли на все оставшиеся века. Другие остались нести смерть.
                        И тоже на века. Но да ладно, меньше лирики, воин! Ты же у нас уже воин, не так ли? Или еще нет?
                        Так или иначе, твой путь близок к своему завершению.
                        Почему так? Тогда задайся вопросом, почему это место еще существует, коль где-то впереди есть дверька с надписью “Выход”.
                        Ладно-ладно, выход есть. Был, есть и будет всегда.
                        Избавиться от Грани как явления. Ну как, посильная задача?
                        Нет, коль хочешь пробовать - вперед. Я расскажу тебе обо всем, в отличии от нашей Хиссы.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.FOREST_GOLEM, 1),
                        new Pair<>(MonsterType.FOREST_GUARDIAN, 17), new Pair<>(MonsterType.FOREST_VILLAGER, 17),
                        new Pair<>(MonsterType.EDGE_WARRIOR, 17), new Pair<>(MonsterType.WANDERER_EDGE, 18)
                ))
                .appendRewards(new ExpReward(6900), new GranReward(600),
                        new ItemReward(ItemObject.DIVINATION_CARD_27.getUniqueId(), 1),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1),
                        new ItemReward(ItemObject.ESSENCE_OF_TYCHE_MOD_ITEM.getUniqueId(), 1),
                        new TreePointReward(PointType.CHARACTER))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 47
                .setId(25)
                .setStorageNpc(NPCs.GLEM)
                .setOnComplete(player -> {
                    this.applyBeaconNotification(player, BeaconObjects.SHADOW_FRAGMENT);
                    this.applyBeaconNotification(player, BeaconObjects.WAR_FRAGMENT);
                })
                .setTitle("Осколки Мерниги и теней")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(24))
                .setDescription("""
                        Здесь тебе надо быть действительно аккуратным.
                        Мернига славится своей атакой на расстоянии - потревожишь её и имеешь шанс уже не уйти.
                        Благо в своих слугах она не так искусна, как детки Грани. С тенями проще. Хоть они и явление Хеседа.
                        Ты же уже наслышан, да? Мертвые осколки никогда не принесут с собой полную силу неудачников.
                        Вот когда какой-нибудь Хесед заглянет к нам - тогда да, стоит подумать над своим местом смерти.
                        Я вот себе присмотрел Горный осколок.
                        Чего тебе не нравится? Нет, если уверен в своих силах - можешь меня и не слушать, пожалуйста.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.MERNIGA, 18), new Pair<>(MonsterType.SERVANT_OF_MERNIGA, 18),
                        new Pair<>(MonsterType.DISTORTED_COW, 19), new Pair<>(MonsterType.SHADOW_OF_CHESED, 19)
                ))
                .appendRewards(new ExpReward(7500), new GranReward(600),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1),
                        new ItemReward(ItemObject.DIVINATION_CARD_15.getUniqueId(), 2))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 51
                .setId(26)
                .setStorageNpc(NPCs.GLEM)
                .setOnComplete(player -> {
                    this.applyBeaconNotification(player, BeaconObjects.CHAOS_FRAGMENT);
                    this.applyBeaconNotification(player, BeaconObjects.CHESED_FRAGMENT);
                })
                .setTitle("Осколки войны и хаоса")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(25))
                .setDescription("""
                        Мне искренне кажется, что ты еще не готов услышать полную историю.
                        Но возможно стоит тебе рассказать еще одну грань безумия истории нашего мира.
                        На осколке войны тебя встретят остатки двух сторон конфликта. Казалось бы - чего нового, да?
                        Да, возможно я не прав и на самом деле истинных сторон конфликта все три. Но вернемся к сути.
                        Одну из них ты знаешь - дитё Грани Хеседа. Его истинные сторонники становятся Хаосидами.
                        И до чего же похожее именование имеют сторонники второй стороны - Хаоса, а именно Хаоситы.
                        Да-да, если Грань можно считать вторым слоем реальности, то Хаос - это то, что присутствует везде.
                        Даже в тебе. Я бы даже сказал - УЖЕ в тебе. Дальше - больше. Осколок хаоса состоит полностью из сторонников Хаоса.
                        Не живется им в мире. Хотя, стоит признать, незванных гостей много кто не любит. Вполне вероятно, Грань из таких.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.WARRIOR_OF_CHESED, 19), new Pair<>(MonsterType.CHAOS_ENTITY, 20),
                        new Pair<>(MonsterType.LEADER_OF_CHAOS, 20), new Pair<>(MonsterType.DISTORTED_BY_CHAOS, 20)
                ))
                .appendRewards(new ExpReward(8100), new GranReward(600), new TreePointReward(PointType.CHARACTER), new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 55
                .setId(27)
                .setStorageNpc(NPCs.GLEM)
                .setToPassNpc(NPCs.FRAGMENT_GUARDIAN)
                .setOnComplete(player -> this.applyBeaconNotification(player, BeaconObjects.COLD_FRAGMENT))
                .setTitle("Длань Хеседы и Медведи")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(26))
                .setDescription("""
                        И так, друг и воин, перед тобой остались три осколка, после которых ты убедишься в моих словах - выйти отсюда нельзя, жизнь тлен и вообще, как ты сюда попал?
                        Хотя ладно, за мелкие проступки сюда никого не кидают. Смеха ради - мы даже не знаем, кто стоит за перемещениями в пространстве.
                        Может быть узнаешь ты, поживем-увидим. И так, да, осколки. Длань Хеседы - ничего нового.
                        Видимо, в тот момент стадия конфликта совсем обострилась, на осколке никого кроме них не осталось.
                        Кроме Длани Хеседы и, конечно же, Грани. Ты же уже заметил, что используешь против Грани её же оружие?
                        Но да ладно. Медвежий осколок, видимо, был поглощен Гранью без сопротивлений, голой энергией. Конечно, тебе от этого легче не будет. На этом, пожалуй, всё.
                        За последний остров в ответе у нас один человек - он тебя встречал в самом начале. Ладно-ладно, Страж осколков. Как разберешься со всеми делами - бегом к нему.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.HAND_OF_CHESED, 21), new Pair<>(MonsterType.GAZE_OF_CHESED, 21),
                        new Pair<>(MonsterType.EDGE_BEAR, 21), new Pair<>(MonsterType.MINE_EXILE, 22)
                ))
                .appendRewards(new ExpReward(8700), new GranReward(600), new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1))
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 59
                .setId(28)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setOnComplete(player -> this.applyBeaconNotification(player, BeaconObjects.MINE))
                .setTitle("Шахтерский осколок")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(27))
                .setDescription("""
                        Давно не виделись, мой больной на голову товарищ. Твоё последнее препятствие - последний осколок.
                        Цельный шахтерский осколок. Одно чудо даже сохранило часть силы. Им является Искаженный шахтер.
                        С ним у тебя могут возникнуть проблемы, но я верю - ты справишься.
                        Честно-честно верю. Посмотри в мои глаза.
                        До выполнения моего поручения, к твоему счастью, доступ к полигонам выдан не будет.
                        Это и твоя, и моя необходимость.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.MINER, 22),
                        new Pair<>(MonsterType.ALIEN_MINER, 22),
                        new Pair<>(MonsterType.DISTORTED_MINER, 1)
                ), new TalkToNPCRequirement().appendTalkGoals(NPCs.DAILY_QUESTS))
                .appendRewards(new ExpReward(18000), new GranReward(1000),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1),
                        new ItemReward(ItemObject.DIVINATION_CARD_24.getUniqueId(), 1),
                        new ItemReward(ItemObject.WAY_FRAGMENT_1.getUniqueId(), 2),
                        new TreePointReward(PointType.CHARACTER)
                )
                .build());

        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 59
                .setId(29)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setTitle("Фракция силы")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(28))
                .setDescription("""
                        Перед тем, как мы продолжим, зайди к фракционеру.
                        Я не настаиваю, но выбор фракции силы - не самая последняя вещь, которую тебе стоит сделать.
                        На одном из осколов весьма специфичные условия и именно там нужны носители подобной силы.
                        """)
                .appendRequirement(new TalkToNPCRequirement().appendTalkGoals(NPCs.FRACTION_NPC))
                .appendRewards(new ExpReward(10000), new GranReward(1000),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 1),
                        new ItemReward(ItemObject.WAY_FRAGMENT_1.getUniqueId(), 1)
                )
                .build());

        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 61+
                .setId(30)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setTitle("Пути")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(28))
                .setDescription("""
                        Да, заданий от меня больше не будет. Будут только просьбы. Можешь считать, что...
                        Показал свои способности и волю к свободе! Но и только.
                        Возможно уже кто-то растрепал тебе, но не суть важно - как такового выхода мы не имеем.
                        Никогда не имели. И вряд ли будем иметь. Не думал же ты, что мы тут сидим от прекрасных видов на Бездну?
                        Но есть у нас одна вещь, которая может приоткрыть дверь.
                        И за этой дверью, возможно, окажется не свирепый тигр, способный растерзать даже тебя, а простой портал в земли, которые еще не объяли Грань и Хаос.
                        Этой вещью, собственно, мы называем Пути. Это такой артефакт - он сможет перенести тебя очень далеко отсюда.
                        Очень, очень далеко. Но ты спросишь, если есть возможность, то почему мы всё ещё сидим здесь?
                        И знаешь, хотел бы я как глашатай вещать, что отсюда пути нет.
                        Но вдруг найдется тот, кому будет по силам сделать то, что не смог никто до этого, верно? Чего его отпугивать сразу.
                        Так вот, побывав на той стороне, стало понятно, что мы не готовы к дальнейшему пути. И вряд ли будем готовы.
                        Страх сковал нас и не дает двинуться. Безумие, что творилось там, до сих пор отзывается дрожью в руках.
                        Знаешь, впервые тебя увидев я даже обрадовался. Значит, кто-то на той стороне всё ещё жив и даже несет “закон”, ха!
                        Но ближе к сути, да. Мы подарим тебе один артефакт. И ты взглянешь на всё своими глазами. Но! Если ты найдешь отсюда путь - дай нам знать.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.FUGITIVE_SOUL, 30),
                        new Pair<>(MonsterType.FUGITIVE_SHELL, 30)
                ), new TalkToNPCRequirement().appendTalkGoals(NPCs.TRADER_6))
                .appendRewards(new ExpReward(140000), new GranReward(3000),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 2)
                )
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 61+
                .setId(31)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setTitle("Глубже?")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(29))
                .setDescription("""
                        Ты вернулся. И я не чувствую в тебе страха, это хорошо. Даже отлично. Там...
                        Чтобы это ни было, но, возможно оно и вправду таит ответы...
                        Или хотя бы выход из этого богом забытого места. И если артефакт Пути на некогда наш осколок мы смогли сделать сами, то остальные...
                        Как бы это глупо не звучало, но их найти ты сможешь только на той стороне.
                        Чем дальше ты будешь переноситься от нас, тем сильнее Грань будет влиять на тебя.
                        Но до тех пор пока твоя голова холодна - тебе не стоит опасаться поводка Грани.
                        Хоть ты используешь её силу, хоть в тебе и есть толика Хаоса, но это твоя сила.
                        Или правильнее будет сказать, подвластная только тебе сила. Ладно, это всё лирика.
                        Если с первого Пути меня не особо интересуют обитатели, то вот последующие осколки...
                        Я буду только рад. Но я не могу заставлять тебя идти далее, об этом я уже говорил.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.JUNIOR_VIRAN, 40),
                        new Pair<>(MonsterType.SENIOR_VIRAN, 40)
                ))
                .appendRewards(new ExpReward(300000), new GranReward(3000),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 2),
                        new TreePointReward(PointType.CHARACTER)
                )
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 61+
                .setId(32)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setTitle("Вираны")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(30))
                .setDescription("""
                        Виран... Интересные особи, даже разделать их - та еще задача. Про их ликвидацию вообще молчу.
                        Но гораздо интереснее то, что где-то есть одноименный развитый мир.
                        Приходил к нам один такой герой. Правда, есть нюанс.
                        Смотрит он наверное уже тысячный свой сон.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.STILLBIRTH_SPIDER, 50),
                        new Pair<>(MonsterType.CANRIAN_FUGITIVE, 50)
                ))
                .appendRewards(new ExpReward(350000), new GranReward(3000),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 2)
                )
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 61+
                .setId(33)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setTitle("Копия копии?")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(31))
                .setDescription("""
                        А эти... Особи похожи на наших. Тех, что с Канрийского осколка.
                        Сильнее правда в десяток тысяч раз, но не суть.
                        Возможно, первичное впечатление было правдивым и те далекие места не более чем параллельный мир?
                        Веришь в такое?
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.EDGE_WOLF, 75),
                        new Pair<>(MonsterType.EDGE_ELEMENTALIST, 75)
                ))
                .appendRewards(new ExpReward(500000), new GranReward(3000),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 2),
                        new TreePointReward(PointType.CHARACTER)
                )
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 61+
                .setId(34)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setTitle("Копия копии")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(32))
                .setDescription("""
                        И снова совпадения.
                        И смотря на тебя, полагаю плотность силы всё возрастает.
                        У меня весьма скверное предчувствие, но ты волен делать то, чего желаешь, конечно же.
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.CHAOS_INFUSED_ENEDERMITE, 75),
                        new Pair<>(MonsterType.NEWBORN_BY_CHAOS, 75)
                ))
                .appendRewards(new ExpReward(750000), new GranReward(3000),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 2)
                )
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 61+
                .setId(35)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setTitle("Влияние Хаоса")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(33))
                .setDescription("""
                        Жаль, что они не обладают разумом, но влияние Хаоса на них весьма... специфичное. А разум...
                        Будь с такой энергетической структурой человек и я бы засомневался - точно ли это монстра Хаоса?
                        Разумный, что владеет Хаосом! Звучит не смешно, а страшно. Хотя, смотря на тебя...
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.CHAOS_BLOODHOUND, 75),
                        new Pair<>(MonsterType.EDGE_PILLAR, 75)
                ))
                .appendRewards(new ExpReward(750000), new GranReward(3000),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 2),
                        new TreePointReward(PointType.CHARACTER)
                )
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 61+
                .setId(36)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setTitle("Войны сил")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(34))
                .setDescription("""
                        Чем больше ты приносишь мне этого “добра”, тем непонятнее картина.
                        Слишком много влияния Хаоса. Такое чувство, что если у нас доминирует Грань, то там, наоборот, Хаос.
                        Хотя точнее будет говорить не про нашу деревню, а про две вселенные.
                        Но если здесь мы можем развиваться, то там нам останется только удобрить землю. Мертвую землю. Или искаженную?
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.AWAKENED_BEAR, 75),
                        new Pair<>(MonsterType.AWAKENED_SKELET, 75)
                ))
                .appendRewards(new ExpReward(750000), new GranReward(3000),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 2)
                )
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 61+
                .setId(37)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setTitle("Слияние")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(35))
                .setDescription("""
                        Пробуждение-пробуждение... Весьма специфично, но это выглядит очень даже гармонично.
                        Инь-Янь, только Хаос и Грань слились в едином организме. И даже друг друга не топят в энергии и дисбалансе.
                        Это заставляет задуматься над их природой, но.... Кому не все-равно, да?
                        """)
                .appendRequirement(new KillEntityRequirement().appendGoal(
                        new Pair<>(MonsterType.CHAOS_MLENIA, 75),
                        new Pair<>(MonsterType.PRIEST_OF_CHAOS, 75)
                ))
                .appendRewards(new ExpReward(750000), new GranReward(3000),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 2),
                        new TreePointReward(PointType.CHARACTER)
                )
                .build());
        Fantasy.questService().registerNewQuest(QuestBuilder.newBuilder()  // iLevel = 61+
                .setId(38)
                .setStorageNpc(NPCs.FRAGMENT_GUARDIAN)
                .setTitle("Самый сильный")
                .setReceiveRequirementText("&cНеобходимо выполнить задание: &6" + this.findQuestTitle(36))
                .setDescription("""
                        Говоришь, дальше пусто... Что же. Ты сделал то, что не могли сделать мы.
                        И мы все всё-равно остаемся с пустыми руками. Весьма гадкое ощущение.
                        Хотя, остается маленькая надежда на семена с последнего пути.
                        Возможно они откроют нам секрет как пробиться сквозь пространство и время еще дальше.
                        Возможно, где-то там, все же есть та заветная дверь...
                        """)
                .appendRequirement(new KillEntityRequirement()
                        .appendGoal(MonsterType.PROJECTION_OF_THE_YOUNG_INDIVIDUAL_BINA, 1, 76)
                        .appendGoal(MonsterType.PROJECTION_OF_THE_YOUNG_INDIVIDUAL_HOCHMA, 1, 76)
                )
                .appendRewards(new ExpReward(1000000), new GranReward(3000),
                        new ItemReward(ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getUniqueId(), 5),
                        new TreePointReward(PointType.EDGE)
                )
                .build());
    }

}
