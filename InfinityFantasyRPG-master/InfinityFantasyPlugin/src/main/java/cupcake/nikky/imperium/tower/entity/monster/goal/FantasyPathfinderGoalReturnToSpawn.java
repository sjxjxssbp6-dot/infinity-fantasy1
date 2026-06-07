package cupcake.nikky.imperium.tower.entity.monster.goal;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.PathEntity;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FantasyPathfinderGoalReturnToSpawn extends PathfinderGoal {

    private final Monster monster;
    private final EntityInsentient entityInsentient;
    private final Location targetLocation;

    private PathEntity pathEntity;

    private int cooldown = 5;
    private boolean isTaskDone;
    private int time;

    public FantasyPathfinderGoalReturnToSpawn(@NotNull final Monster monster, @NotNull final EntityInsentient entityInsentient, @NotNull final Location targetLocation) {
        this.monster = monster;
        this.entityInsentient = entityInsentient;
        this.targetLocation = targetLocation;

        this.a(1);
    }

    /* Can start executing */
    @Override
    public boolean a() {
        if (!monster.isExist() || monster.isShouldDeath()) return false;

        if (monster.getTargetTracker().isForgotTarget()) {
            this.pathEntity = this.entityInsentient.getNavigation().a(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());
            if (pathEntity != null) {
                return true;
            } else {
                monster.getSpigot().teleport(targetLocation);
                monster.getTargetTracker().reset();
            }
        }

        return false;
    }

    @Override
    public boolean b() {
        return monster.getTargetTracker().isForgotTarget() || !this.entityInsentient.getNavigation().o();
    }

    @Override
    public void startExecuting() {
        if (!entityInsentient.getNavigation().a(pathEntity, 1D)) {
            isTaskDone = true;
            monster.getSpigot().teleport(targetLocation);
            monster.getTargetTracker().reset();
        }
    }

    @Override
    public void onTaskReset() {
        entityInsentient.getNavigation().p();

        if (entityInsentient.getDistanceSq(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()) > 2D) {
            monster.getSpigot().teleport(targetLocation);
            monster.getTargetTracker().reset();
        }
    }

    @Override
    public void updateTask() {
        if (isTaskDone) return;

        if (time >= 4 * 3) {
            isTaskDone = true;
            cooldown = 20;
            monster.getSpigot().teleport(targetLocation);
            monster.getTargetTracker().reset();
            return;
        }

        entityInsentient.getControllerLook().a(
                targetLocation.getX(),
                targetLocation.getY() + entityInsentient.getHeadHeight(),
                targetLocation.getZ(),
                30.0F,
                30.0F);

        if (--cooldown <= 0) {
            time += 1;
            if (entityInsentient.getDistanceSq(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ()) <= 2) {
                isTaskDone = true;
                cooldown = 20;
                monster.getTargetTracker().reset();
                this.entityInsentient.getNavigation().p();
                this.pathEntity = null;
            } else cooldown = 5;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FantasyPathfinderGoalReturnToSpawn that = (FantasyPathfinderGoalReturnToSpawn) o;
        return Objects.equals(monster.getMonsterUniqueId(), that.monster.getMonsterUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(monster.getMonsterUniqueId());
    }
}
