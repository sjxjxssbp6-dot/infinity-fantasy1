package cupcake.nikky.imperium.tower.tree;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.achievement.Achievement;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public enum PointType {

    CHARACTER('\uE1BD', (player, pointType) -> {
        val user = player.<User>getBungeePlayer();
        val pointsFromQuests = (int) Fantasy.questService().findPointQuests(pointType).stream().filter(quest -> user.findQuestSettings().isPassed(quest.getId())).count();
        return user.getLevelController().getLevel() - 1 + pointsFromQuests;
    }),
    EDGE('\uE1C7', (player, pointType) -> {
        val user = player.<User>getBungeePlayer();
        val achievementSettings = user.findAchievementSettings();
        var userHasPoints = 0;

        val pointsFromQuests = (int) Fantasy.questService().findPointQuests(pointType).stream().filter(quest -> user.findQuestSettings().isPassed(quest.getId())).count();

        if (achievementSettings != null) {
            userHasPoints = achievementSettings.getAchievements().stream()
                    .filter(Achievement::isAchieveDone)
                    .map(achievement -> achievement.getType().getChecker().getTreePointReward())
                    .reduce(Integer::sum).orElse(0);
        }

        return userHasPoints + pointsFromQuests;
    });

    private final char icon;
    private final BiFunction<Player, PointType, Integer> countAllPoints;

}
