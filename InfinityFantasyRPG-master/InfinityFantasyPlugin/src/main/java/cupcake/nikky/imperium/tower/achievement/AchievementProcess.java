package cupcake.nikky.imperium.tower.achievement;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@RequiredArgsConstructor
public final class AchievementProcess {

    private final Achievement achievement;
    private final boolean keyUnknown;
    private final boolean increasedProgress, alreadyDoneGoal, justDoneGoal;

    public AchievementProcess ifIncreasedProgress(@NotNull final Consumer<Achievement> achievementConsumer) {
        if (keyUnknown) return this;

        if (increasedProgress)
            achievementConsumer.accept(achievement);

        return this;
    }

    public AchievementProcess ifGoalAlreadyDone(@NotNull final Consumer<Achievement> achievementConsumer) {
        if (keyUnknown) return this;

        if (alreadyDoneGoal)
            achievementConsumer.accept(achievement);

        return this;
    }

    public AchievementProcess ifGoalJustDone(@NotNull final Consumer<Achievement> achievementConsumer) {
        if (keyUnknown) return this;

        if (justDoneGoal)
            achievementConsumer.accept(achievement);

        return this;
    }

}
