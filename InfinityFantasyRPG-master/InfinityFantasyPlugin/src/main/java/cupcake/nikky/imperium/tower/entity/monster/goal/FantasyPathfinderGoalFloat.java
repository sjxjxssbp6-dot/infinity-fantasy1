package cupcake.nikky.imperium.tower.entity.monster.goal;

import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FantasyPathfinderGoalFloat extends PathfinderGoal {

    private final EntityInsentient entityinsentient;

    public FantasyPathfinderGoalFloat(@NotNull final EntityInsentient entityinsentient) {
        this.entityinsentient = entityinsentient;
    }

    public boolean a() {
        return this.entityinsentient.isInWater() && !this.entityinsentient.isNoAI();
    }

    public void update() {
        this.e();
    }

    public void e() {
        if (this.entityinsentient.getRandom().nextFloat() < 0.8F) {
            this.entityinsentient.getControllerJump().a();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FantasyPathfinderGoalFloat that = (FantasyPathfinderGoalFloat) o;
        return Objects.equals(entityinsentient.getUniqueID(), that.entityinsentient.getUniqueID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(entityinsentient.getUniqueID());
    }

}
