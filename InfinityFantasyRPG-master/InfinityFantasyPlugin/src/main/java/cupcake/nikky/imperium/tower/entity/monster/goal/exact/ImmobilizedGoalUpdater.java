package cupcake.nikky.imperium.tower.entity.monster.goal.exact;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.goal.*;
import net.minecraft.server.v1_12_R1.*;
import org.jetbrains.annotations.NotNull;

public class ImmobilizedGoalUpdater implements GoalUpdater {

    @Override
    public void update(@NotNull EntityInsentient craftEntity, Monster monster) {
        this.clearGoals(craftEntity);

        craftEntity.goalSelector.a(5, new FantasyPathfinderGoalReturnToSpawn(monster, craftEntity, monster.getSpawnLocation()));
        craftEntity.goalSelector.a(8, new FantasyPathfinderGoalRandomLookAround(craftEntity));
        craftEntity.goalSelector.a(7, new FantasyPathfinderGoalRandomLookAtTarget(monster, craftEntity));

    }

}
