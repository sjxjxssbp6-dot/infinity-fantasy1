package cupcake.nikky.imperium.tower.entity.monster.goal;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import lombok.val;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class FantasyPathfinderGoalMeleeAttack extends PathfinderGoal {

    private final World world;
    private final EntityCreature entity;
    private final Monster monster;
    private int currentAttackCooldown;
    private int attackCooldown;
    private final double defaultDistanceToAttack;
    private final boolean e;

    private EntityLiving target;
    private int targetUpdater;
    private int targetLookup;

    private PathEntity pathEntity;
    private int h;
    private double i;
    private double j;
    private double k;

    public FantasyPathfinderGoalMeleeAttack(EntityCreature entityCreature, Monster monster, boolean bl) {
        this.entity = entityCreature;
        this.world = entityCreature.world;
        this.e = bl;
        this.attackCooldown = monster.getMonsterData().getAttackSpeed();
        this.monster = monster;
        this.defaultDistanceToAttack = monster.getMonsterData().getDistanceToAttack();

        this.a(3);
    }

    /* Can accept/handle goal */
    @Override
    public boolean a() {
        if (targetLookup++ < 3) return false;
        if (entity == null || monster == null) return false;
        if (monster.getTargetTracker().isForgotTarget()) return false;

        targetLookup = 0;
        target = this.monster.getTargetTracker().goalTarget();
        if (target == null) {
            return false;
        } else if (!target.getWorld().getWorld().getUID().equals(world.getWorld().getUID())) {
            return false;
        } else {
            this.pathEntity = this.entity.getNavigation().a(target);
            if (this.pathEntity != null) {
                return true;
            } else {
                return this.getDistanceToAttack(target) >= this.entity.d(target.locX, target.getBoundingBox().b, target.locZ);
            }
        }
    }

    /* Can continue handle goal */
    @Override
    public boolean b() {
        if (target == null) {
            return false;
        } else if (!this.e) {
            return !this.entity.getNavigation().o();
        } else if (!this.entity.f(new BlockPosition(target))) {
            return false;
        } else {
            return !(target instanceof EntityHuman entityHuman) || !entityHuman.isSpectator() && !((EntityHuman)target).z();
        }
    }

    /* On goal accept */
    @Override
    public void c() {
        this.entity.getNavigation().a(this.pathEntity, 1D);
        this.h = 0;
    }

    /* On stop this goal */
    @Override
    public void d() {
        val entityLiving = this.monster.getTargetTracker().goalTargetBukkit();
        if (entityLiving instanceof Player player && player.getGameMode() != GameMode.SURVIVAL) {
            monster.getTargetTracker().goalTarget(null);
        }

        this.entity.getNavigation().p();
    }

    /* Goal handler */
    @Override
    public void e() {
        if (targetUpdater++ >= 3) {
            target = this.monster.getTargetTracker().goalTarget();
            targetUpdater = 0;
        }

        if (target == null || !target.valid) return;

        this.entity.getControllerLook().a(target, 30.0F, 30.0F);

        double distanceSquared = this.entity.d(target.locX, target.getBoundingBox().b, target.locZ);
        --this.h;

        if ((this.e || this.entity.getEntitySenses().a(target)) && this.h <= 0 && (this.i == 0.0D && this.j == 0.0D && this.k == 0.0D || target.d(this.i, this.j, this.k) >= 1.0D || this.entity.getRandom().nextFloat() < 0.05F)) {
            this.i = target.locX;
            this.j = target.getBoundingBox().b;
            this.k = target.locZ;
            this.h = 4 + this.entity.getRandom().nextInt(7);
            if (distanceSquared > 1024.0D) {
                this.h += 10;
            } else if (distanceSquared > 256.0D) {
                this.h += 5;
            }

            if (!this.entity.getNavigation().a(target, 1D)) {
                this.h += 15;
            }
        }

        this.currentAttackCooldown = Math.max(this.currentAttackCooldown - 1, 0);
        this.tryAttack(target, distanceSquared);
    }

    public void tryAttack(EntityLiving entityLiving, double distance) {
        if (this.currentAttackCooldown <= 0 && distance <= this.getDistanceToAttack(entityLiving)) {
            this.updateAttackSpeed();
            this.currentAttackCooldown = attackCooldown;
            this.entity.a(EnumHand.MAIN_HAND);
            Bukkit.getPluginManager().callEvent(new EntityDamageByEntityEvent(entity.getBukkitEntity(), entityLiving.getBukkitEntity(),
                    EntityDamageEvent.DamageCause.ENTITY_ATTACK,
                    new EnumMap<>(Map.of(EntityDamageEvent.DamageModifier.BASE, 0D)),
                    new EnumMap<>(Map.of(EntityDamageEvent.DamageModifier.BASE, EntityDamageEvent.ZERO))));
        }

    }

    public double getDistanceToAttack(EntityLiving entityLiving) {
        if (defaultDistanceToAttack != -1) return defaultDistanceToAttack * defaultDistanceToAttack;

        val result = 2.4 - (this.entity.width - (1 - entityLiving.width)) / 2D;
        return result * result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FantasyPathfinderGoalMeleeAttack that = (FantasyPathfinderGoalMeleeAttack) o;
        return Objects.equals(monster.getMonsterUniqueId(), that.monster.getMonsterUniqueId());
    }

    private void updateAttackSpeed() {
        val attackSpeed = monster.getMonsterData().getAttackSpeed();
        val attackSpeedMultiplier = 1 - monster.findAttribute(ImperiumAttribute.ATTACK_SPEED).findValue() / 100D;

        val value = attackSpeed + attackSpeed * attackSpeedMultiplier;
        this.attackCooldown = (int) Math.round(value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(monster.getMonsterUniqueId());
    }
}
