package cupcake.nikky.imperium.tower.entity.monster.exact.sharding;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageType;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.InboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.knockback.KnockBackApplier;
import cupcake.nikky.imperium.tower.protocol.board.TimeType;
import cupcake.nikky.imperium.tower.utility.EntityUtility;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectionOfTheYIBina extends BreakThroughMonster {

    private boolean debuff;
    private boolean rage;

    private final boolean[] pick = {false, false};
    private BukkitTask mechanicTask;

    private final List<BukkitTask> effectTasks = new ArrayList<>();

    private static final KnockBackApplier knockBackApplier = new KnockBackApplier(.3);

    public ProjectionOfTheYIBina(@NotNull final Location spawnLocation, @NotNull final MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.PROJECTION_OF_THE_YOUNG_INDIVIDUAL_BINA, "PR_BINA_", TimeType.BINA);
    }

    private Collection<InboundDamageCast> createCasts() {
        return List.of(new InboundDamageCast(this) {

            @Override
            public boolean canApply() {
                return !debuff && getCurrentHealthPercent() <= 40D;
            }

            @Override
            public void apply() {
                debuff = true;
                getMonster().getAttributes().register(ImperiumAttribute.CHANCE_TO_FREEZE_ON_HIT, 100D);
                getMonster().getAttributes().register(ImperiumAttribute.CHANCE_TO_POISON, 100D);
                getSession().findAll().forEach(player -> {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fБина усиляется: атаки накладывают &cзаморозку &fи &cотравление&f!");
                });
            }
        }, new InboundDamageCast(this) {

            @Override
            public boolean canApply() {
                return !rage && getCurrentHealthPercent() <= 10D;
            }

            @Override
            public void apply() {
                rage = true;

                getMonster().getAttributes().find(ImperiumAttribute.MIN_PHYSICAL_DAMAGE).applyBaseValue(getMonster().getAttributes().find(ImperiumAttribute.MIN_PHYSICAL_DAMAGE).getBaseValue() / 2);
                getMonster().getAttributes().find(ImperiumAttribute.MAX_PHYSICAL_DAMAGE).applyBaseValue(getMonster().getAttributes().find(ImperiumAttribute.MAX_PHYSICAL_DAMAGE).getBaseValue() / 2);

                getMonster().getAttributes().find(ImperiumAttribute.MIN_FIRE_DAMAGE).applyBaseValue(getMonster().getAttributes().find(ImperiumAttribute.MIN_FIRE_DAMAGE).getBaseValue() / 2);
                getMonster().getAttributes().find(ImperiumAttribute.MAX_FIRE_DAMAGE).applyBaseValue(getMonster().getAttributes().find(ImperiumAttribute.MAX_FIRE_DAMAGE).getBaseValue() / 2);
                getMonster().getAttributes().find(ImperiumAttribute.MIN_COLD_DAMAGE).applyBaseValue(getMonster().getAttributes().find(ImperiumAttribute.MIN_COLD_DAMAGE).getBaseValue() / 2);
                getMonster().getAttributes().find(ImperiumAttribute.MAX_COLD_DAMAGE).applyBaseValue(getMonster().getAttributes().find(ImperiumAttribute.MAX_COLD_DAMAGE).getBaseValue() / 2);
                getMonster().getAttributes().find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).applyBaseValue(getMonster().getAttributes().find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).getBaseValue() / 2);
                getMonster().getAttributes().find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).applyBaseValue(getMonster().getAttributes().find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).getBaseValue() / 2);
                getMonster().getAttributes().find(ImperiumAttribute.MIN_CHAOS_DAMAGE).applyBaseValue(getMonster().getAttributes().find(ImperiumAttribute.MIN_CHAOS_DAMAGE).getBaseValue() / 2);
                getMonster().getAttributes().find(ImperiumAttribute.MAX_CHAOS_DAMAGE).applyBaseValue(getMonster().getAttributes().find(ImperiumAttribute.MAX_CHAOS_DAMAGE).getBaseValue() / 2);

                getSession().findAll().forEach(player -> {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fБина усиляется: атаки наносят на &c50% больше урона&f!");
                });
            }
        });
    }

    private void startTask() {
        mechanicTask = Fantasy.runTimer(() -> {
            val health = EntityUtility.getHealthPercentage(this);
            if (health > 50D && health <= 90D && pick[0] || health > 90D) return;
            if (health <= 50D && pick[1]) return;

            if (!effectTasks.isEmpty()) return;

            if (!pick[0]) pick[0] = true;
            else if (!pick[1]) pick[1] = true;

            this.cancelEffect();

            val sessions = getSession().findAll();
            if (sessions.isEmpty()) return;

            val playerSpigot = getSpigot();
            val monsterLocation = playerSpigot.getLocation();
            val playerLocation = sessions.get(MathUtility.randomInRange(0, sessions.size() - 1)).getLocation();

            val locations = List.of(
                    monsterLocation.clone().add(playerLocation.toVector().subtract(monsterLocation.toVector()).multiply(-3)),
                    monsterLocation.clone().add(playerLocation.toVector().subtract(monsterLocation.toVector()).multiply(3))
            );

            for (int i = 0; i < locations.size(); i++) {
                val location = locations.get(i);
                this.sendEffect(location);

                this.effectTasks.add(Fantasy.runTimer(() -> getSession().findAll().forEach(player -> {
                    knockBackApplier.apply(player.getLocation().toVector().subtract(location.toVector()), player);
                    Fantasy.getAttackExecutor().execute(() ->
                            Fantasy.damageProvider().processDamage(player)
                                    .setAttackInfluence(getSpigot())
                                    .setDamageType(DamageType.OVER_TIME)
                                    .setDamage(InfluenceType.CHAOS, findAttribute(ImperiumAttribute.MAX_CHAOS_DAMAGE).findValue() / 15)
                                    .apply()
                    );
                }), 3 * i, 5));

                Fantasy.runLater(this::cancelEffect, 20 * 10);
            }
        }, 5);
    }

    private void sendEffect(@NotNull final Location location) {
        val sessionPlayers = getSession().findAll();
        if (!sessionPlayers.isEmpty()) {
            Fantasy.particleProvider().sendBinaEffect(sessionPlayers.getFirst(), 20 * 9, Color.VIOLET, location, sessionPlayers);
        }
    }

    private void cancelMechanic() {
        if (mechanicTask != null && !mechanicTask.isCancelled())
            mechanicTask.cancel();
    }

    private void cancelEffect() {
        effectTasks.removeIf(task -> {
            if (task.isCancelled()) return true;
            task.cancel();
            return true;
        });
    }

    @Override
    public void invalidateEntity() {
        super.invalidateEntity();
        this.cancelMechanic();
        this.cancelEffect();
    }

    @Override
    public void onSpawn() {
        super.onSpawn();

        this.createCasts().forEach(this::appendCast);
        this.startTask();
    }

}
