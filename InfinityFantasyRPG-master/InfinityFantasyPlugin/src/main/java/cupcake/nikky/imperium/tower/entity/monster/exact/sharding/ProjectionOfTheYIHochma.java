package cupcake.nikky.imperium.tower.entity.monster.exact.sharding;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.InboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.item.Icons;
import cupcake.nikky.imperium.tower.protocol.board.TimeType;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.EntityUtility;
import lombok.val;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.Wada;
import ru.cristalix.wada.module.zone.IQuadZone;
import ru.cristalix.wada.util.Color;

import java.util.*;
import java.util.stream.Collectors;

public class ProjectionOfTheYIHochma extends BreakThroughMonster {

    private static final int MAX_ZONE_LIVING = 20 * 3;

    private boolean debuff;
    private boolean rage;

    private int zoneTimer = -1;
    private IQuadZone lastZone;

    private BukkitTask mechanicTask;

    public ProjectionOfTheYIHochma(@NotNull final Location spawnLocation, @NotNull final MonsterData monsterData) {
        super(spawnLocation, monsterData, MonsterType.PROJECTION_OF_THE_YOUNG_INDIVIDUAL_HOCHMA, "PR_HOCHMA_", TimeType.HOCHMA,
        null, Icons.AWAKENED_AXE, Icons.AWAKENED_AXE);
    }

    private void startTask() {
        mechanicTask = Fantasy.runTimer(() -> {
            val health = EntityUtility.getHealthPercentage(this);
            if (health > 80D) return;

            if (zoneTimer != -1 && !Fantasy.buffService().findById(getMonsterUniqueId()).map(entity -> entity.find(BuffType.HOCHMA_STABILITY_BUFF).isPresent()).orElse(false)) {
                val sessionMembers = getSession().findAll();
                this.removeLastZone();
                sessionMembers.forEach(player -> {
                    Fantasy.particleProvider().sendPurpleExplosion(player, player.getLocation(), sessionMembers);
                    sessionMembers.forEach(player2 -> Fantasy.bukkitProvider().sound(player2, player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE));
                    player.<User>getBungeePlayer().changeHealthPercent(-200);
                });
                return;
            }

            if (lastZone == null && zoneTimer == -1 || zoneTimer >= MAX_ZONE_LIVING) {
                if (zoneTimer == -1) {
                    Fantasy.buffService().appendBuff(getMonsterUniqueId(), BuffType.HOCHMA_STABILITY_BUFF);
                }

                this.removeLastZone();
                val location = this.generateSphereLocations(getSpigot().getLocation());
                if (location == null) return;

                getSession().findAll().forEach(player -> {
                    Fantasy.bukkitProvider().sound(player, location, Sound.BLOCK_END_PORTAL_SPAWN);
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fПоявляется зона для обновления стабильности Хохмы!");
                });

                lastZone = Wada.get().getZoneManager().zoneBuilder()
                        .size(1.5)
                        .color(Color.YELLOW)
                        .location(location)
                        .onCollisionStateChange((player, state) -> {
                            this.removeLastZone();
                            Fantasy.buffService().appendBuff(getMonsterUniqueId(), BuffType.HOCHMA_STABILITY_BUFF);
                            getSession().findAll().forEach(sessionPlayer -> {
                                Fantasy.bukkitProvider().sound(sessionPlayer, player.getLocation(), Sound.BLOCK_PORTAL_TRAVEL);
                                Fantasy.bukkitProvider().sendMessage(sessionPlayer, ChatType.SYSTEM, "&fСтабильность Хохмы обновлена!");
                            });
                        })
                        .build();

                lastZone.addWhoCanView(getSession().findAll());
                Wada.get().getZoneManager().addZone(lastZone);
                zoneTimer = 0;
            } else {
                zoneTimer += 5;
            }

        }, 5);
    }

    private void removeLastZone() {
        if (lastZone == null) return;
        Wada.get().getZoneManager().removeZone(lastZone);
        lastZone = null;
    }

    private void cancelTask() {
        if (mechanicTask == null || mechanicTask.isCancelled()) return;
        mechanicTask.cancel();
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
                getMonster().getAttributes().register(ImperiumAttribute.CHANCE_TO_SHOCK_ON_HIT, 100D);
                getMonster().getAttributes().register(ImperiumAttribute.CHANCE_TO_CAUSE_BLEEDING, 100D);
                getSession().findAll().forEach(player -> {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fХохма усиляется: атаки накладывают &cшок &fи &cкровотечение&f!");
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
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fХохма усиляется: атаки наносят на &c50% больше урона&f!");
                });
            }
        });
    }

    @Override
    public void onSpawn() {
        super.onSpawn();
        this.createCasts().forEach(this::appendCast);
        this.startTask();
    }

    @Override
    public void invalidateEntity() {
        super.invalidateEntity();
        this.cancelTask();
    }

    private Location generateSphereLocations(@NotNull final Location center) {
        val offsets = Arrays.stream(new double[][]{
                {2, 0}, {0, 2}, {-2, 0}, {0, -2}, {2, 2}, {2, -2}, {-2, 2}, {-2, -2},
                {3, 0}, {0, 3}, {-3, 0}, {0, -3}, {3, 3}, {3, -3}, {-3, 3}, {-3, -3},
        }).collect(Collectors.toList());

        val playerLocations = this.getSession().findAll().stream().map(Entity::getLocation).toList();

        Collections.shuffle(offsets);

        for (double[] offset : offsets) {
            for (int multiply = 1; multiply <= 2; multiply++) {
                val location = center.clone().add(offset[0] * multiply, 0, offset[1] * multiply);
                if (playerLocations.stream().anyMatch(playerLocation -> playerLocation.distanceSquared(location) <= 1.5)) continue;

                val blockType = location.toBlockLocation().getBlock().getType();

                if (blockType == Material.AIR || blockType == Material.STATIONARY_WATER)
                    return location.toCenterLocation().add(0, -.5, 0);
            }
        }

        return null;
    }

}
