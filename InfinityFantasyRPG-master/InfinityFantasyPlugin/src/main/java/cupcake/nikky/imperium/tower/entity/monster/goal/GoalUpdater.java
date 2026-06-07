package cupcake.nikky.imperium.tower.entity.monster.goal;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import net.minecraft.server.v1_12_R1.EntityInsentient;

public interface GoalUpdater {

    default void clearGoals(final EntityInsentient craftEntity) {
        try {
            craftEntity.goalSelector.b.clear();
            craftEntity.goalSelector.c.clear();
            craftEntity.targetSelector.b.clear();
            craftEntity.targetSelector.c.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void update(final EntityInsentient craftEntity, final Monster monster);

}
