package cupcake.nikky.imperium.tower.entity.monster.goal;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FantasyPathfinderGoalRandomLookAtTarget extends PathfinderGoal {

    private final EntityInsentient entity;
    private final Monster monster;
    private EntityLiving target;
    private int targetLookup;

    public FantasyPathfinderGoalRandomLookAtTarget(@NotNull final Monster monster, @NotNull final EntityInsentient entity) {
        this.entity = entity;
        this.monster = monster;

        this.a(3);
    }

    @Override
    public boolean a() {
        if (targetLookup++ < 5) return false;

        targetLookup = 0;
        target = monster.getTargetTracker().goalTarget();
        return target != null;
    }

    @Override
    public boolean b() {
        return target != null;
    }

    @Override
    public void updateTask() {
        if (target == null) return;
        this.entity.getControllerLook().a(target.locX, target.locY + target.getHeadHeight(), target.locZ, this.entity.O(), this.entity.N());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FantasyPathfinderGoalRandomLookAtTarget that = (FantasyPathfinderGoalRandomLookAtTarget) o;
        return Objects.equals(entity.getUniqueID(), that.entity.getUniqueID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity.getUniqueID());
    }
}
