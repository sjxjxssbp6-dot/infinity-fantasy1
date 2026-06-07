package cupcake.nikky.imperium.tower.entity.monster.tracker.target;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.battle.EntityBattleData;
import cupcake.nikky.imperium.tower.entity.monster.conduct.ConductType;
import cupcake.nikky.imperium.tower.entity.monster.goal.exact.ImmobilizedGoalUpdater;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.zone.ZoneList;
import lombok.Getter;
import lombok.val;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EntityTrackerEntry;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MonsterTargetTracker {

    private final Monster monster;
    @Getter
    private final MonsterTargetProcessor targetProcessor;

    private final AtomicReference<EntityLiving> goalTarget = new AtomicReference<>();
    private final AtomicBoolean forgotTarget = new AtomicBoolean();

    private final List<Location> fragmentLocations = List.of(
            ZoneList.BATTLE_GROUND_ZONE.spawnLocation("spawn1"),
            ZoneList.BATTLE_GROUND_ZONE.spawnLocation("spawn3"),
            ZoneList.BATTLE_GROUND_ZONE.spawnLocation("spawn6"),
            ZoneList.BATTLE_GROUND_ZONE.spawnLocation("spawn8"),
            ZoneList.EDGE_FRAGMENT_1.spawnLocation("default"),
            ZoneList.EDGE_FRAGMENT_2.spawnLocation("default"),
            ZoneList.EDGE_FRAGMENT_3.spawnLocation("default"),
            ZoneList.EDGE_FRAGMENT_4.spawnLocation("default"),
            ZoneList.EDGE_FRAGMENT_5.spawnLocation("default"),
            ZoneList.EDGE_FRAGMENT_6.spawnLocation("default"),
            ZoneList.EDGE_FRAGMENT_7.spawnLocation("default"),
            ZoneList.EDGE_FRAGMENT_8.spawnLocation("default")
    );

    public boolean isForgotTarget() {
        return forgotTarget.get();
    }

    public void setForgotTarget(boolean value) {
        this.forgotTarget.set(value);
    }

    public EntityLiving goalTarget() {
        return goalTarget.get();
    }

    public LivingEntity goalTargetBukkit() {
        val result = goalTarget.get();
        return result == null ? null : (LivingEntity) result.getBukkitEntity();
    }

    private void goalTarget(@Nullable final EntityLiving target) {
        if (!monster.isExist()) return;

        this.targetProcessor.onGoalTargetUpdate(goalTarget.get(), target);
        this.goalTarget.set(target);
    }

    public void goalTarget(@Nullable final LivingEntity livingEntity) {
        if (livingEntity == null) this.goalTarget((EntityLiving) null);
        else this.goalTarget(((CraftLivingEntity) livingEntity).getHandle());
    }

    public MonsterTargetTracker(@NotNull final Monster monster) {
        this.monster = monster;
        this.targetProcessor = new MonsterTargetProcessor(monster, this);
    }

    public void reset() {
        this.setForgotTarget(false);
    }

    public void update(@Nullable final EntityTrackerEntry trackerEntry) {
        if (!monster.isExist() || monster.isShouldDeath()) {
            if (this.goalTarget() != null)
                this.goalTarget((EntityLiving) null);
            return;
        }
        if (monster.getTargetTracker().isForgotTarget()) return;

        val spigot = monster.getSpigot();
        val conduct = monster.getConduct();
        val range = conduct.getRange();

        val target = this.goalTargetBukkit();
        val battleData = monster.getBattleData();

        switch (target) {
            case LivingEntity entity when entity instanceof Player player &&
                    (player.getBungeePlayer() == null || player.<User>getBungeePlayer().isInvalidated() || !player.<User>getBungeePlayer().isLoaded()) ->
                    this.goalTarget((EntityLiving) null);
            case LivingEntity entity when entity.getLocation().distanceSquared(spigot.getLocation()) > range * range * 2 * 2 -> this.goalTarget((EntityLiving) null);
            case LivingEntity entity when spigot.getLocation().distanceSquared(monster.getSpawnLocation()) > 18 * 18 -> this.goalTarget((EntityLiving) null);
            case LivingEntity entity when !battleData.isEmpty() && monster.findAttribute(ImperiumAttribute.MOVEMENT_SPEED).findValue() > 0 &&
                    !(monster.getGoalUpdater() instanceof ImmobilizedGoalUpdater) &&
                    battleData.values().stream()
                            .map(EntityBattleData::getLastReceiveDamage)
                            .allMatch(time -> time + 4_000 < Instant.now().toEpochMilli()) -> this.goalTarget((EntityLiving) null);
            case null -> {
                if (conduct != ConductType.NEUTRAL) {
                    if (trackerEntry == null) return;

                    val playerMap = trackerEntry.trackedPlayerMap;
                    if (playerMap.isEmpty()) return;

                    new ArrayList<>(playerMap.keySet()).stream()
                            .map(EntityPlayer::getBukkitEntity)
                            .filter(craftPlayer -> !craftPlayer.<User>getBungeePlayer().isShouldDeath())
                            .filter(player -> player.getGameMode() == GameMode.SURVIVAL)
                            .filter(player -> player.getLocation().distanceSquared(spigot.getLocation()) <= range * range)
                            .filter(player -> {
                                val user = player.<User>getBungeePlayer();
                                return user != null && user.isLoaded() && !user.isInvalidated();
                            })
                            .filter(player -> {
                                val playerLocation = player.getLocation();
                                return fragmentLocations.stream().noneMatch(location -> location.distanceSquared(playerLocation) < 1.5 * 1.5);
                            })
                            .filter(player -> {
                                User user = player.getBungeePlayer();
                                val beacon = user.getBeaconObject();
                                val playerLocation = player.getLocation();

                                return beacon == null || beacon.getBeacon().getRespawnLocation().distanceSquared(playerLocation) > 1.5 * 1.5;
                            })
                            .findAny().ifPresent(this::goalTarget);
                }
            }
            default -> {}
        }
    }

}
