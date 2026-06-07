package cupcake.nikky.imperium.tower.battlepass.quest;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.statistic.Statistics;
import cupcake.nikky.imperium.tower.user.User;
import lombok.NoArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@NoArgsConstructor(staticName = "create")
public class QuestLinkBuilder {

    private final Deque<BattlePassQuest> quests = new LinkedList<>();
    private static int lastId;

    /*
    * stage = [startLevel, endLevel, stepLevel]
    * */
    public QuestLinkBuilder nextLevelQuests(final int[][] stages) {
        for (int[] stage : stages) {
            var start = stage[0];
            var end = stage[1];
            var step = stage[2];

            for (int i = start; i <= end; i += step) {
                this.nextLevelQuest(i);
            }
        }

        return this;
    }

    public QuestLinkBuilder nextLevelQuest(final int requireUserLevel) {
        return this.nextWithUser(
                (user, exp) -> "Получите &6%d уровень&f. Награда: &6%d опыта".formatted(requireUserLevel, exp),
                user -> user.getLevel() >= requireUserLevel
        );
    }

    public QuestLinkBuilder nextKillAnyMonsters(final int requirement) {
        return this.nextWithUser(
                (user, exp) -> "Убейте &6%d монстров&f. Награда: &6%d опыта. &fПрогресс: &6%d&f/&6%d".formatted(requirement, exp, user.findStatisticSettings().findStatistic(Statistics.MONSTER_KILL).data(), requirement),
                user -> user.findStatisticSettings().findStatistic(Statistics.MONSTER_KILL).data() >= requirement
        );
    }

    public QuestLinkBuilder nextKillAnyUsers(final int requirement) {
        return this.nextWithUser(
                (user, exp) -> "Убейте &6%d игроков&f. Награда: &6%d опыта. &fПрогресс: &6%d&f/&6%d".formatted(requirement, exp, user.findStatisticSettings().findStatistic(Statistics.USER_KILL).data(), requirement),
                user -> user.findStatisticSettings().findStatistic(Statistics.USER_KILL).data() >= requirement
        );
    }

    public QuestLinkBuilder nextNPCQuest(final int questID) {
        return this.nextWithUser(
                (user, exp) -> {
                    var quest = Fantasy.questService().findQuest(questID).orElse(null);
                    if (quest == null) return "&cUnknown";

                    var npc = quest.getStorageNpc();
                    return "&fВыполнить задание: &6%s &8(НПС: %s, координаты (x z): %.0f %.0f)&f. Награда: &6%d опыта"
                            .formatted(quest.getTitle(),
                                    ChatColor.stripColor(npc.getMask().getData().getName()),
                                    npc.getSpawnLocations().getFirst().getX(),
                                    npc.getSpawnLocations().getFirst().getZ(),
                                    exp
                            );
                },
                user -> user.findQuestSettings().isPassed(questID)
        );
    }

    public QuestLinkBuilder nextWithPlayer(@NotNull final BiFunction<Player, Integer, String> description, @NotNull final Predicate<Player> isDone) {
        this.quests.addLast(new BattlePassQuest(lastId++, 10, description, isDone));
        return this;
    }

    public QuestLinkBuilder nextWithUser(@NotNull final BiFunction<User, Integer, String> description, @NotNull final Predicate<User> isDone) {
        this.quests.addLast(new BattlePassQuest(lastId++, 10, (player, exp) -> description.apply(player.getBungeePlayer(), exp), player -> isDone.test(player.getBungeePlayer())));
        return this;
    }

    public BattlePassLinkedQuest build() {
        return new BattlePassLinkedQuest(quests.toArray(new BattlePassQuest[0]));
    }

}
