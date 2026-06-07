package cupcake.nikky.imperium.tower.achievement.selection;

import cupcake.nikky.imperium.tower.achievement.Achievements;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.func.mod.reactive.ReactiveButton;
import me.func.mod.ui.menu.selection.Selection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AchievementSelection {

    private final Player player;

    public void open() {
        val achievementSettings = player.<User>getBungeePlayer().findAchievementSettings();

        val builder = Selection.builder().rows(4).columns(3).hint("");
        val sortKey = new AtomicInteger(0);

        final List<ReactiveButton> storage = new ArrayList<>();

        for (Achievements achievement : Arrays.stream(Achievements.values()).sorted(Comparator.comparingInt(achievement ->
                achievementSettings.findByEnum(achievement).isAchieveDone() ? -1 - sortKey.getAndIncrement() : 1 + sortKey.getAndIncrement())).collect(Collectors.toList())) {
            val userData = achievementSettings.findByEnum(achievement);
            if (userData == null) continue;

            val isDone = userData.isAchieveDone();

            val button = new ReactiveButton();
            button.texture(FantasyImage.PAPER.toTexture());
            button.setTitle(achievement.getChecker().getTitle());
            button.setDescription((isDone ? "&aПолучено" : "&cНе получено"));

            val rewardDescription = achievement.getChecker().getRewardDescription();
            val achievementReward = "&fНаграды: &6" + (achievement.getChecker().isWithReward() ? achievement.getChecker().getRewardAmount() + "\uE104" :
                    rewardDescription == null || rewardDescription.isBlank() ? "отсутствуют" : rewardDescription);
            button.setHover("&6" + achievement.getChecker().getTitle() + "\n&6" + achievement.getChecker().getDescription() + "\n\n" + achievementReward);
            storage.add(button);
        }

        builder.storage(storage).build().open(player);
    }

}
