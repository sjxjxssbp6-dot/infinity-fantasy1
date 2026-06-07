package cupcake.nikky.imperium.tower.entity.monster.goal;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import lombok.val;
import net.minecraft.server.v1_12_R1.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FantasyPathfinderGoalBowShoot<T extends EntityMonster & IRangedEntity> extends PathfinderGoal {

    private final Monster monster;
    private final T entity;
    private int attackSpeed;
    private final double speedMultiplier;
    private int c;
    private final float distanceToAttackSquared;
    private int e = -1;
    private int navigationCooldown;
    private boolean g;
    private boolean h;
    private int i = -1;

    private EntityLiving target;
    private int targetLookup;
    private int targetUpdater;

    public FantasyPathfinderGoalBowShoot(@NotNull final Monster monster, T entity, double speedMultiplier, int var4, float distanceToAttack) {
        this.monster = monster;
        this.entity = entity;
        attackSpeed = monster.getMonsterData().getAttackSpeed();
        this.speedMultiplier = speedMultiplier;
        this.c = var4;
        this.distanceToAttackSquared = distanceToAttack * distanceToAttack;

        this.a(3);
    }

    @Override
    public boolean a() {
        if (targetLookup++ < 3) return target != null;
        if (monster.getTargetTracker().isForgotTarget()) return false;

        targetLookup = 0;
        target = this.monster.getTargetTracker().goalTarget();
        return target != null;
    }

    @Override
    public boolean b() {
        return (this.a() || !this.entity.getNavigation().o());
    }

    @Override
    public void startExecuting() {
        this.entity.p(true);
    }

    @Override
    public void onTaskReset() {
        this.entity.p(false);
        this.navigationCooldown = 0;
        this.e = -1;
        this.entity.cN();
    }

    @Override
    public void updateTask() {
        if (targetUpdater++ >= 3) {
            target = this.monster.getTargetTracker().goalTarget();
            targetUpdater = 0;
        }

        if (target == null) return;

        if (targetUpdater % 3 == 0) {
            updateAttackSpeed();
        }

        double distanceToTarget = this.entity.d(target.locX, target.getBoundingBox().b, target.locZ);
        boolean canSeeWithRaytrace = this.entity.getEntitySenses().a(target);
        boolean isNavigationInCooldown = this.navigationCooldown > 0;

        if (canSeeWithRaytrace != isNavigationInCooldown) {
            this.navigationCooldown = 0;
        }

        if (canSeeWithRaytrace) {
            ++this.navigationCooldown;
        } else {
            --this.navigationCooldown;
        }

        if (distanceToTarget <= this.distanceToAttackSquared && this.navigationCooldown >= 20) {
            this.entity.getNavigation().p();
            ++this.i;
        } else {
            this.entity.getNavigation().a(target, this.speedMultiplier);
            this.i = -1;
        }

        if (this.i >= this.attackSpeed) {
            if (this.entity.getRandom().nextFloat() < 0.3) {
                this.g = !this.g;
            }

            if (this.entity.getRandom().nextFloat() < 0.3) {
                this.h = !this.h;
            }

            this.i = 0;
        }

        if (this.i > -1) {
            if (distanceToTarget > (this.distanceToAttackSquared * 0.75F)) {
                this.h = false;
            } else if (distanceToTarget < (this.distanceToAttackSquared * 0.25F)) {
                this.h = true;
            }

            this.entity.getControllerMove().a(this.h ? -0.5F : 0.5F, this.g ? 0.5F : -0.5F);
            this.entity.a(target, 30.0F, 30.0F);
        } else {
            this.entity.getControllerLook().a(target, 30.0F, 30.0F);
        }

        if (this.entity.isHandRaised()) {
            if (!canSeeWithRaytrace && this.navigationCooldown < -60) {
                this.entity.cN();
            } else if (canSeeWithRaytrace) {
                int var6 = this.entity.cL();
                if (var6 >= this.attackSpeed) {
                    this.entity.cN();
                    this.entity.a(target, ItemBow.b(var6));
                    this.e = this.c;
                }
            }
        } else if (--this.e <= 0 && this.navigationCooldown >= -60) {
            this.entity.c(EnumHand.MAIN_HAND);
        }

    }

    private void updateAttackSpeed() {
        val attackSpeed = monster.getMonsterData().getAttackSpeed();
        val attackSpeedMultiplier = 1 - monster.findAttribute(ImperiumAttribute.ATTACK_SPEED).findValue() / 100D;

        val value = attackSpeed + attackSpeed * attackSpeedMultiplier;
        this.attackSpeed = (int) Math.round(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FantasyPathfinderGoalBowShoot<?> that = (FantasyPathfinderGoalBowShoot<?>) o;
        return Objects.equals(monster.getMonsterUniqueId(), that.monster.getMonsterUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(monster.getMonsterUniqueId());
    }
}
