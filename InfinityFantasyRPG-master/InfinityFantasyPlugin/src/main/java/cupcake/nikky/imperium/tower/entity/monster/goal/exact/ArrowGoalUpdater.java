package cupcake.nikky.imperium.tower.entity.monster.goal.exact;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.goal.*;
import lombok.val;
import net.minecraft.server.v1_12_R1.*;

public class ArrowGoalUpdater implements GoalUpdater {

    @Override
    public void update(EntityInsentient craftEntity, Monster monster) {
        this.clearGoals(craftEntity);

        if (!(craftEntity instanceof EntitySkeletonAbstract)) {
            new MeleeGoalUpdater().update(craftEntity, monster);
            return;
        }

        val skeleton = (EntitySkeletonAbstract) craftEntity;

        craftEntity.goalSelector.a(0, new FantasyPathfinderGoalFloat(craftEntity));
        craftEntity.goalSelector.a(3, new FantasyPathfinderGoalBowShoot<>(monster, skeleton, 1.0D, 1, 7));
        craftEntity.goalSelector.a(5, new FantasyPathfinderGoalReturnToSpawn(monster, craftEntity, monster.getSpawnLocation()));
        craftEntity.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction((EntityCreature) craftEntity, 1D));
        craftEntity.goalSelector.a(7, new FantasyPathfinderGoalStrollLand(monster, (EntityCreature) craftEntity));
        craftEntity.goalSelector.a(8, new FantasyPathfinderGoalRandomLookAround(craftEntity));
    }

}
