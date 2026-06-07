package cupcake.nikky.imperium.tower.entity.monster.goal.exact;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.goal.*;
import cupcake.nikky.imperium.tower.entity.monster.goal.navigation.FantasyNavigation;
import net.minecraft.server.v1_12_R1.*;
import org.jetbrains.annotations.NotNull;

public class MeleeGoalUpdater implements GoalUpdater {

    @Override
    public void update(@NotNull EntityInsentient craftEntity, Monster monster) {
        this.clearGoals(craftEntity);

        if (!(craftEntity instanceof EntityCreature)) return;

        craftEntity.navigation = new FantasyNavigation(craftEntity, craftEntity.getWorld());
        ((FantasyNavigation) craftEntity.navigation).setCanEnterDoors(true);
        ((FantasyNavigation) craftEntity.navigation).setCanSwim(true);
        ((FantasyNavigation) craftEntity.navigation).setCanOpenDoors(true);

        craftEntity.goalSelector.a(0, new FantasyPathfinderGoalFloat(craftEntity));
        craftEntity.goalSelector.a(2, new FantasyPathfinderGoalMeleeAttack((EntityCreature) craftEntity, monster, true));
        craftEntity.goalSelector.a(5, new FantasyPathfinderGoalReturnToSpawn(monster, craftEntity, monster.getSpawnLocation()));
        craftEntity.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction((EntityCreature) craftEntity, 1D));
        craftEntity.goalSelector.a(7, new FantasyPathfinderGoalStrollLand(monster, (EntityCreature) craftEntity));
        craftEntity.goalSelector.a(8, new FantasyPathfinderGoalRandomLookAround(craftEntity));
    }

}
