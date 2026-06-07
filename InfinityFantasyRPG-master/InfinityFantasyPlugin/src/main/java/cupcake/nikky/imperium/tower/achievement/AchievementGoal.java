package cupcake.nikky.imperium.tower.achievement;

import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class AchievementGoal {

    private final Map<String, Integer> goals;
    @Getter private final String title;
    @Getter private final String description;
    @Getter private final String rewardDescription;
    @Getter private final int rewardAmount;
    @Getter private final boolean withReward;
    @Getter private final int treePointReward;

    public Map<String, Integer> getGoals() {
        return new HashMap<>(goals);
    }

    public Achievement applyTo(@NotNull final Achievement achievement) {
        getGoals().forEach(achievement::setupGoal);
        return achievement;
    }

    @SuppressWarnings("unchecked")
    public static class GoalBuilder<T extends GoalBuilder<T>> {

        private final Map<String, Integer> goals = new HashMap<>();
        private String title;
        private String description;
        private String rewardDescription;
        private int rewardAmount;
        private boolean withReward;
        private int treePointReward;

        public T addGoal(@NotNull final String key, final int goal) {
            this.goals.put(key, goal);
            return (T) this;
        }

        public T setTitle(@NotNull final String title) {
            this.title = title;
            return (T) this;
        }

        public T withReward(final boolean flag) {
            this.withReward = flag;
            return (T) this;
        }

        public T setDescription(@NotNull final String description) {
            this.description = description;
            return (T) this;
        }

        public T setRewardDescription(@NotNull final String rewardDescription) {
            this.rewardDescription = rewardDescription;
            return (T) this;
        }

        public T setReward(final int rewardAmount) {
            this.rewardAmount = rewardAmount;
            return (T) this;
        }

        public T setTreePointReward(final int treePointReward) {
            this.treePointReward = treePointReward;
            return (T) this;
        }

        public AchievementGoal build() {
            return new AchievementGoal(goals, title, description, rewardDescription, rewardAmount, withReward, treePointReward);
        }

    }

    public static class MonsterGoalBuilder extends GoalBuilder<MonsterGoalBuilder> {

        public MonsterGoalBuilder addGoal(@NotNull final MonsterType key, int goal) {
            return super.addGoal(key.name(), goal);
        }

    }

}
