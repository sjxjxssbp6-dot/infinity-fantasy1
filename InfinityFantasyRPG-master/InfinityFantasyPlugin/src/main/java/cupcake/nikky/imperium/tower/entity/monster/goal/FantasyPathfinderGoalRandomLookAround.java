package cupcake.nikky.imperium.tower.entity.monster.goal;

import me.anfanik.steda.api.utility.MathUtility;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.PathfinderGoal;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FantasyPathfinderGoalRandomLookAround extends PathfinderGoal {

    private final EntityInsentient entity;
    private double deltaX;
    private double deltaY;
    private double deltaZ;
    private int d;

    private int delay = 100;

    public FantasyPathfinderGoalRandomLookAround(@NotNull final EntityInsentient entity) {
        this.entity = entity;

        this.a(3);
    }

    @Override
    public boolean a() {
        if (--delay > 0) return false;
        delay = 100;
        return true;
    }

    @Override
    public boolean b() {
        return this.d >= 0;
    }

    @Override
    public void startExecuting() {
        double var1 = 6.283185307179586 * MathUtility.randomInRange(.01D, .99D);
        this.deltaX = Math.cos(var1);
        this.deltaY = MathUtility.randomInRange(-.5, .5);
        this.deltaZ = Math.sin(var1);
        this.d = 20 + MathUtility.randomInRange(0, 20);
    }

    @Override
    public void updateTask() {
        --this.d;
        this.entity.getControllerLook().a(
                this.entity.locX + this.deltaX,
                this.entity.locY + this.entity.getHeadHeight() + this.deltaY,
                this.entity.locZ + this.deltaZ,
                this.entity.O(),
                this.entity.N());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FantasyPathfinderGoalRandomLookAround that = (FantasyPathfinderGoalRandomLookAround) o;
        return Objects.equals(entity.getUniqueID(), that.entity.getUniqueID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity.getUniqueID());
    }
}
