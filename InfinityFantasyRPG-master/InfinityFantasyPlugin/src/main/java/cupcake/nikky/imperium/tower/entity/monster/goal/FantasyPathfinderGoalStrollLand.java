package cupcake.nikky.imperium.tower.entity.monster.goal;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import lombok.val;
import net.minecraft.server.v1_12_R1.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FantasyPathfinderGoalStrollLand extends PathfinderGoal {

    private final Monster monster;
    private final EntityCreature entity;
    private final int movementDelay;

    private double x;
    private double y;
    private double z;
    private int currentMovementDelay;

    public FantasyPathfinderGoalStrollLand(@NotNull final Monster monster, @NotNull final EntityCreature entity) {
        this.monster = monster;
        this.entity = entity;
        this.movementDelay = 120;
        this.a(1);
    }

    @Override
    public boolean a() {
        if (--currentMovementDelay > 0 || entity.bW() >= 100) {
            return false;
        }

        if (monster.getTargetTracker().isForgotTarget()) return false;

        val vector = this.findVector();
        if (vector == null) return false;

        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
        this.currentMovementDelay = movementDelay;

        return true;
    }

    @Override
    public boolean b() {
        return !this.entity.getNavigation().o();
    }

    @Override
    public void startExecuting() {
        this.entity.getNavigation().a(this.x, this.y, this.z, 1D);
    }

    private Vec3D findVector() {
        if (entity.isInWater()) {
            val vector = RandomPositionGenerator.b(entity, 15, 7);
            if (vector != null)
                return vector;
        }

        return RandomPositionGenerator.a(entity, 10, 7);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FantasyPathfinderGoalStrollLand that = (FantasyPathfinderGoalStrollLand) o;
        return Objects.equals(monster.getMonsterUniqueId(), that.monster.getMonsterUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(monster.getMonsterUniqueId());
    }
}
