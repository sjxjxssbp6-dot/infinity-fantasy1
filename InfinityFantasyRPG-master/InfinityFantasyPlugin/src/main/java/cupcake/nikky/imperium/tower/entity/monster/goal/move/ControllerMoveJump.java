package cupcake.nikky.imperium.tower.entity.monster.goal.move;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import net.minecraft.server.v1_12_R1.ControllerMove;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import net.minecraft.server.v1_12_R1.GenericAttributes;
import org.jetbrains.annotations.NotNull;

public class ControllerMoveJump extends ControllerMove {

    private final Monster monster;
    private final EntityInsentient entity;
    private int jumpCooldown;
    public float i;
    public int currentJumpCooldown;

    public ControllerMoveJump(@NotNull final Monster monster, EntityInsentient entity, final int jumpCooldown) {
        super(entity);
        this.monster = monster;
        this.entity = entity;
        this.jumpCooldown = jumpCooldown;
        this.i = 180.0F * entity.yaw / 3.1415927F;
    }

    public void a(float f) {
        this.i = f;
    }

    public void a(double d0) {
        this.e = d0;
        this.h = Operation.MOVE_TO;
    }

    public void a() {
        this.entity.yaw = this.a(this.entity.yaw, this.i, 90.0F);
        this.entity.aP = this.entity.yaw;
        this.entity.aN = this.entity.yaw;
        if (this.h != Operation.MOVE_TO) {
            this.withMove.setMoveForwarding(0.0F);
        } else {
            this.h = Operation.WAIT;
            if (this.entity.onGround) {
                this.entity.k((float)(this.e * this.entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
                if (this.currentJumpCooldown-- <= 0) {
                    this.currentJumpCooldown = this.jumpCooldown;

                    this.entity.getControllerJump().a();
                } else {
                    this.entity.be = 0.0F;
                    this.entity.bg = 0.0F;
                    this.entity.k(0.0F);
                }
            } else {
                this.entity.k((float)(this.e * this.entity.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
            }
        }

    }
}
