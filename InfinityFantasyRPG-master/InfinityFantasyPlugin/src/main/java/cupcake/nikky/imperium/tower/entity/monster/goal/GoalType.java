package cupcake.nikky.imperium.tower.entity.monster.goal;

import cupcake.nikky.imperium.tower.entity.monster.goal.exact.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GoalType {

    MELEE(new MeleeGoalUpdater()),
    MELEE_JUMP(new MeleeJumpGoalUpdater()),
    IMMOBILIZED(new ImmobilizedGoalUpdater()),
    ARROW(new ArrowGoalUpdater()),
    FIREBALL(new FireballGoalUpdater());

    private final GoalUpdater goalUpdater;

}
