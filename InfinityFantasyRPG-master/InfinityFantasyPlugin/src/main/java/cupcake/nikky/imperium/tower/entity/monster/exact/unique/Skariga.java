package cupcake.nikky.imperium.tower.entity.monster.exact.unique;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.InboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class Skariga extends FantasyMonster {

    private BukkitTask damageTask;

    public Skariga(@NotNull Location spawnLocation, @NotNull MonsterData monsterData, @NotNull MonsterType monsterType) {
        super(spawnLocation, monsterData, monsterType);
    }

    private InboundDamageCast createCast() {
        return new InboundDamageCast(this) {

            private boolean applied;

            @Override
            public boolean canApply() {
                if (applied) {
                    return false;
                } else if (getCurrentHealthPercent() <= 50) {
                    applied = true;
                    return true;
                }

                return false;
            }

            @Override
            public void apply() {
                val monsterLocation = getSpigot().getLocation();
                Fantasy.bukkitProvider().sound(monsterLocation, Sound.BLOCK_METAL_FALL);
                getAttackers().stream().filter(player -> player.getLocation().distanceSquared(monsterLocation) <= 6 * 6).forEach(player -> {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "На Вас был наложен &cстрах&f!");
                    Fantasy.buffService().appendBuff(player.getUniqueId(), BuffType.SKARIGA_DEBUFF);
                });
            }
        };
    }

    @Override
    public void onForgotTarget() {
        super.onForgotTarget();
        this.clearInboundCasts();
        this.appendCast(this.createCast());
    }

    private void createDamageRadius() {
        this.appendCast(this.createCast());

        damageTask = Fantasy.runTimer(() -> {
            val spigot = getSpigot();
            val monsterLocation = spigot.getLocation();

            val attackers = getAttackers().stream().filter(player -> player.getLocation().distanceSquared(monsterLocation) <= 6 * 6).toList();
            val damage = MathUtility.randomInRange(getMinimumFullDamage(), getMaximumFullDamage());

            Fantasy.getAttackExecutor().execute(() -> attackers.forEach(player -> {
                Fantasy.damageProvider().processDamage(player)
                        .setAttackInfluence(spigot)
                        .setDamage(InfluenceType.PHYSICAL, damage)
                        .apply();
            }));
            Fantasy.runLater(() -> attackers.forEach(player -> player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 2, 2))));
        }, 10);
    }

    private void cancelDamageRadius() {
        this.clearInboundCasts();

        if (damageTask != null && !damageTask.isCancelled()) {
            damageTask.cancel();
            damageTask = null;
        }
    }

    @Override
    public void onSpawn() {
        super.onSpawn();
        this.createDamageRadius();
    }

    @Override
    public void invalidateEntity() {
        super.invalidateEntity();
        this.cancelDamageRadius();
    }
}
