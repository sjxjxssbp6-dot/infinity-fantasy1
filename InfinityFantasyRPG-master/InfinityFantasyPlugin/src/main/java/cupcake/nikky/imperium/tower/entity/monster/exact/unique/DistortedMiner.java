package cupcake.nikky.imperium.tower.entity.monster.exact.unique;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.InboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.OutboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.knockback.KnockBackApplier;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DistortedMiner extends FantasyMonster {

    private final KnockBackApplier outKnockBack = new KnockBackApplier(-2);
    private final KnockBackApplier inKnockBack = new KnockBackApplier(.4);

    private final List<Monster> minions = new ArrayList<>();
    private boolean done = false;

    public DistortedMiner(@NotNull Location spawnLocation, @NotNull MonsterData monsterData, @NotNull MonsterType monsterType) {
        super(spawnLocation, monsterData, monsterType,
                new ItemStack[]{new ItemStack(Material.CHAINMAIL_BOOTS), null, new ItemStack(Material.GOLD_CHESTPLATE), null},
                new ItemStack(Material.GOLD_PICKAXE),
                new ItemStack(Material.DIAMOND_PICKAXE));

        this.appendCast(new OutboundDamageCast(this) {

            @Override
            public boolean canApply() {
                return true;
            }

            @Override
            public void apply(LivingEntity entity) {
                Fantasy.runLater(() -> entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 20, false, false)));
            }

        });

        this.appendCast(new InboundDamageCast(this) {

            @Override
            public boolean canApply() {
                if (!done && getCurrentHealthPercent() <= 50) {
                    done = true;
                    return true;
                }

                return false;
            }

            @Override
            public void apply() {
                val location = getSpigot().getLocation();
                val attackers = getAttackers();

                Collections.shuffle(attackers);

                for (int i = 0; i < 3; i++) {
                    val minion = Fantasy.monsterProvider().createInstance(MonsterType.MISGUIDED_MINER, location);
                    minions.add(minion);
                }

                Fantasy.runLater(() -> minions.forEach(minion -> {
                    minion.naturallySpawn(false);

                    if (!attackers.isEmpty())
                        minion.getTargetTracker().goalTarget(attackers.get(MathUtility.randomInRange(0, attackers.size() - 1)));
                }));
            }
        });

        Fantasy.runTimer(() -> {
            if (!this.isExist() || this.isShouldDeath()) return;
            getAttackers().forEach(player -> {
                val spigot = getSpigot();
                outKnockBack.apply(spigot.getLocation().getDirection().setY(.4), player);
                Fantasy.damageProvider().processDamage(player).setAttackInfluence(spigot).setDamage(InfluenceType.CHAOS, MathUtility.randomInRange(getMinimumFullDamage(), getMaximumFullDamage())).apply();

                Fantasy.combatProvider().registerCombat(player.getUniqueId(), getMonsterUniqueId());
            });
        }, 20 * 5);
    }

    @Override
    public void onForgotTarget() {
        super.onForgotTarget();

        minions.forEach(Monster::invalidateEntity);
        minions.clear();
        done = false;
    }

    @Override
    public void onAsyncTick() {
        super.onAsyncTick();

        if (!this.isExist()) return;

        if (isMinionAlive()) this.setInvulnerability(20);

        if (getTargetTracker().goalTarget() == null) {
            val spigot = this.getSpigot();
            if (spigot == null) return;

            val location = spigot.getLocation();

            Bukkit.getOnlinePlayers().stream()
                    .filter(player -> player.getWorld().getName().equals(spigot.getWorld().getName()))
                    .filter(player -> player.getLocation().distanceSquared(location) <= 8 * 8)
                    .findAny()
                    .ifPresent(player -> getTargetTracker().goalTarget(player));
        }
    }

    @Override
    public void onSpawn() {
        super.onSpawn();
        ((ZombieVillager) getSpigot()).setBaby(false);
    }

    @Override
    public double onInboundDamage(@NotNull LivingEntity entity, double damage) {
        if (isMinionAlive()) {
            return super.onInboundDamage(entity, 0);
        }

        inKnockBack.apply(getSpigot().getLocation().getDirection(), entity);
        return super.onInboundDamage(entity, damage);
    }

    @Override
    public void invalidateEntity() {
        super.invalidateEntity();

        minions.forEach(Monster::invalidateEntity);
        minions.clear();
        done = false;
    }

    private boolean isMinionAlive() {
        return minions.stream().anyMatch(Monster::isExist);
    }

}
