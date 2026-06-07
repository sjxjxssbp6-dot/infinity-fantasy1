package cupcake.nikky.imperium.tower.entity.monster.goal.exact;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.goal.FantasyPathfinderGoalRandomLookAround;
import cupcake.nikky.imperium.tower.entity.monster.goal.FantasyPathfinderGoalReturnToSpawn;
import cupcake.nikky.imperium.tower.entity.monster.goal.GoalUpdater;
import net.minecraft.server.v1_12_R1.*;
import org.jetbrains.annotations.NotNull;

public class FireballGoalUpdater implements GoalUpdater {

    @Override
    public void update(EntityInsentient craftEntity, Monster monster) {
        this.clearGoals(craftEntity);

        if (!(craftEntity instanceof EntityBlaze)) {
            new MeleeGoalUpdater().update(craftEntity, monster);
            return;
        }

        craftEntity.navigation = new Navigation(craftEntity, craftEntity.getWorld());

        craftEntity.goalSelector.a(2, new PathfinderGoalFireball(monster, (EntityBlaze) craftEntity));
        craftEntity.goalSelector.a(5, new FantasyPathfinderGoalReturnToSpawn(monster, craftEntity, monster.getSpawnLocation()));
        craftEntity.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction((EntityCreature) craftEntity, 1D));
        craftEntity.goalSelector.a(8, new FantasyPathfinderGoalRandomLookAround(craftEntity));
    }

    static class PathfinderGoalFireball extends PathfinderGoal {

        private final Monster monster;
        private final EntityBlaze entity;
        private int b;
        private int attackCooldown;

        public PathfinderGoalFireball(@NotNull final Monster monster, @NotNull final EntityBlaze entity) {
            this.monster = monster;
            this.entity = entity;

            this.a(3);
        }

        @Override
        public boolean a() {
            var target = this.monster.getTargetTracker().goalTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void startExecuting() {
            this.b = 0;
        }

        @Override
        public void onTaskReset() {
            entity.a(false);
        }

        @Override
        public void updateTask() {
            EntityLiving target = this.monster.getTargetTracker().goalTarget();
            if (target == null) return;

            --this.attackCooldown;

            double var2 = this.entity.h(target);
            if (var2 < 8D * 8D) {
                double var4 = target.locX - this.entity.locX;
                double var6 = target.getBoundingBox().b + (target.length / 2.0F) - (this.entity.locY + (this.entity.length / 2.0F));
                double var8 = target.locZ - this.entity.locZ;
                if (this.attackCooldown <= 0) {
                    ++this.b;
                    if (this.b == 1) {
                        this.attackCooldown = 10;
                        this.entity.a(true);
                    } else if (this.b <= 4) {
                        this.attackCooldown = 2;
                    } else {
                        this.attackCooldown = 10;
                        this.b = 0;
                        this.entity.a(false);
                    }

                    if (this.b > 1) {
                        //float var10 = MathHelper.c(MathHelper.sqrt(var2)) * 0.5F;
                        this.entity.world.a(null, 1018, new BlockPosition((int)this.entity.locX, (int)this.entity.locY, (int)this.entity.locZ), 0);

                        for(int var11 = 0; var11 < 1; var11++) {
                            EntitySmallFireball var12 = new EntitySmallFireball(this.entity.world,
                                    this.entity,
                                    var4 /*+ this.entity.getRandom().nextGaussian() * var10*/,
                                    var6,
                                    var8 /*+ this.entity.getRandom().nextGaussian() * var10*/);
                            var12.locY = this.entity.locY + (this.entity.length / 2.0F) + 0.5D;
                            this.entity.world.addEntity(var12);
                        }
                    }
                }

                this.entity.getControllerLook().a(target, 10.0F, 10.0F);
            } else {
                this.entity.getNavigation().p();
                this.entity.getControllerMove().a(target.locX, target.locY, target.locZ, 1.0D);
            }
        }

    }

}
