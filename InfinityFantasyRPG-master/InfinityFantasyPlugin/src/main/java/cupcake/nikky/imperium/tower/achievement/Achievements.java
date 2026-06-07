package cupcake.nikky.imperium.tower.achievement;

import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.tree.PointType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum Achievements {

    PR_BINA_61(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 61 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 61 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_62(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 62 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 62 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_63(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 63 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 63 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_64(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 64 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 64 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_65(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 65 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 65 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_66(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 66 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 66 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_67(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 67 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 67 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_68(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 68 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 68 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_69(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 69 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 69 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_70(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 70 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 70 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_71(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 71 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 71 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_72(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 72 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 72 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_73(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 73 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 73 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_74(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 74 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 74 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_75(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 75 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 75 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_BINA_76(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Бина, 76 уровень")
            .setDescription("Убейте проекцию младшей особи Бина 76 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_61(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 61 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 61 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_62(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 62 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 62 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_63(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 63 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 63 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_64(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 64 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 64 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_65(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 65 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 65 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_66(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 66 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 66 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_67(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 67 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 67 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_68(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 68 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 68 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_69(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 69 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 69 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_70(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 70 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 70 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_71(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 71 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 71 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_72(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 72 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 72 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_73(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 73 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 73 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_74(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 74 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 74 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_75(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 75 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 75 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    PR_HOCHMA_76(new AchievementGoal.GoalBuilder<>()
            .addGoal("death", 1)
            .setTitle("Проекция Хохма, 76 уровень")
            .setDescription("Убейте проекцию младшей особи Хохма 76 уровня")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    SKARIGA(new AchievementGoal.MonsterGoalBuilder()
            .addGoal(MonsterType.SKARIGA, 1)
            .setTitle("Ликвидация Скариги")
            .setDescription("Убейте Скаригу")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    DISTORT_OF_MORTA(new AchievementGoal.MonsterGoalBuilder()
            .addGoal(MonsterType.DISTORT_OF_MORTA, 1)
            .setTitle("Ликвидация Искажения Морты")
            .setDescription("Убейте Искажение Морты")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    FOREST_GOLEM(new AchievementGoal.MonsterGoalBuilder()
            .addGoal(MonsterType.FOREST_GOLEM, 1)
            .setTitle("Ликвидация Голема леса")
            .setDescription("Убейте Голема леса")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    BATTLE_GROUND_MAGMA_CUBE(new AchievementGoal.MonsterGoalBuilder()
            .addGoal(MonsterType.BATTLE_GROUND_MAGMA_CUBE, 3)
            .setTitle("Ликвидация Наследия Поля Битвы")
            .setDescription("Убейте Наследие Поля Битвы")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    DISTORTED_MINER(new AchievementGoal.MonsterGoalBuilder()
            .addGoal(MonsterType.DISTORTED_MINER, 1)
            .setTitle("Ликвидация Искаженного шахтера")
            .setDescription("Убейте Искаженного шахтера")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    WARRIOR_SYNERGY(new AchievementGoal.MonsterGoalBuilder()
            .addGoal(MonsterType.WARRIOR_SYNERGY, 1)
            .setTitle("Ликвидация Воина синергии")
            .setDescription("Убейте Воина синергии")
            .withReward(false)
            .setRewardDescription("1 искажение Грани &r" + PointType.EDGE.getIcon())
            .setTreePointReward(1)
            .build(), AchievementType.MONSTER),
    ;

    private final AchievementGoal checker;
    private final AchievementType achievementType;

    public String getUniqueId() {
        return this.name();
    }

    public static Collection<Achievements> findByType(@NotNull final AchievementType type) {
        List<Achievements> result = new ArrayList<>();

        for (Achievements value : values()) {
            if (value.getAchievementType() == type) result.add(value);
        }

        return result;
    }

    public static Achievements findById(@NotNull final String uniqueId) {
        try {
            return valueOf(uniqueId.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public static Achievement createInstance(@Nullable final Achievements achievement) {
        if (achievement == null) return null;
        return achievement.getChecker().applyTo(new Achievement(achievement.name(), achievement));
    }

}
