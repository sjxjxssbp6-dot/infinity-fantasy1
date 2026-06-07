package cupcake.nikky.imperium.tower.entity.monster.exact.sharding;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.buff.exact.Bleeding;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.FantasyMonster;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.Sessional;
import cupcake.nikky.imperium.tower.entity.monster.battle.EntityBattleData;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.entity.monster.shard.ShardingMonster;
import cupcake.nikky.imperium.tower.protocol.board.TimeType;
import cupcake.nikky.imperium.tower.session.Session;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.metadata.MetadataType;
import cupcake.nikky.imperium.tower.user.time.TimeData;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.core.util.UtilTime;
import ru.cristalix.wada.module.border.WorldBorder;

import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;

public abstract class BreakThroughMonster extends FantasyMonster implements Sessional, ShardingMonster {

    private BukkitTask borderTask;

    @Getter @Setter
    private Session session;
    @Getter @Setter
    private WorldBorder worldBorder;

    @Getter @Setter private double increaseItemQuantity;
    @Getter @Setter private double increaseExperience;
    @Getter @Setter private double increaseDamage;
    @Getter @Setter private double increaseHealth;
    @Getter @Setter private int corrupt;

    private final String achievementKey;
    private final TimeType timeType;

    public BreakThroughMonster(@NotNull final Location spawnLocation,
                               @NotNull final MonsterData monsterData,
                               @NotNull final MonsterType monsterType,
                               @NotNull final String achievementKey,
                               @NotNull final TimeType timeType) {
        super(spawnLocation, monsterData, monsterType);
        this.achievementKey = achievementKey;
        this.timeType = timeType;
    }

    public BreakThroughMonster(@NotNull final Location spawnLocation,
                               @NotNull final MonsterData monsterData,
                               @NotNull final MonsterType monsterType,
                               @NotNull final String achievementKey,
                               @NotNull final TimeType timeType,
                               @Nullable final ItemStack[] armor,
                               @Nullable final ItemStack itemInMainHand,
                               @Nullable final ItemStack itemInOffHand) {
        super(spawnLocation, monsterData, monsterType, armor, itemInMainHand, itemInOffHand);
        this.achievementKey = achievementKey;
        this.timeType = timeType;
    }

    @Override
    public void onSpawn() {
        super.onSpawn();

        borderTask = Fantasy.runTimer(() -> {
            if (worldBorder == null) return;

            val outsidePlayers = worldBorder.getPlayersOutside().stream().map(Entity::getUniqueId).toList();

            getSession().findAll().forEach(player -> {
                if (outsidePlayers.contains(player.getUniqueId())) {
                    if (Fantasy.buffService().findById(player.getUniqueId()).map(buffedEntity -> buffedEntity.find(BuffType.BLEEDING).isPresent()).orElse(false)) return;
                    var bleeding = Fantasy.buffService().appendBuff(
                            player.getUniqueId(),
                            BuffType.BLEEDING,
                            Fantasy.userService().findById(player.getUniqueId()).map(user -> user.findAttribute(ImperiumAttribute.HEALTH).findValue()).orElse(0D) * 5 / 100,
                            3
                    );
                    if (bleeding != null) ((Bleeding) bleeding).setInfluence(getSpigot());
                }
            });
        }, 5);
    }

    @Override
    public void onDeath(LivingEntity killer) {
        val time = getBattleData().values().stream().max(Comparator.comparingLong(EntityBattleData::getStartBattle)).map(EntityBattleData::getStartBattle);
        val currentTime = Instant.now().toEpochMilli();
        val finalTime = (int) (currentTime - time.orElse(currentTime));

        getAttackers().forEach(player -> {
            if (player != null && player.isOnline()) {
                val user = player.<User>getBungeePlayer();
                val achievementSettings = user.findAchievementSettings();

                val achievement = achievementSettings.findById(achievementKey + getLevel());
                if (achievement != null) {
                    achievement.addProgress("death").ifGoalJustDone(achievement1 ->
                            Fantasy.clientProvider().notification(player, "&r\uE129 &aНовое достижение!", "&6" + achievement.getType().getChecker().getTitle()));
                }

                var dps = findBattleData(user.getUniqueId()).map(EntityBattleData::getDamagePerSecond).orElse(0D);
                if (finalTime > 1 && finalTime < 1_000 * 60 * 3) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6Время затраченное на ликвидацию: &c" + UtilTime.makeStr(finalTime));
                    user.findTimeData(timeType).tryUpdate(getLevel(), finalTime, getCorrupt());
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM,
                            "&6Количество очков: &c%.2f".formatted(new TimeData(timeType, getLevel(), finalTime, getCorrupt()).getScore()));
                } else {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cВремя затраченное на ликвидацию превысило лимит (3 минуты)");
                }

                if (dps > 0D) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6Ваш урон в секунду за бой: &c%.0f".formatted(dps));
                }
            }
        });

        super.onDeath(killer);
    }

    @Override
    public void invalidateEntity() {
        super.invalidateEntity();

        if (worldBorder != null) {
            worldBorder.removeBorder();
        }

        if (borderTask != null && !borderTask.isCancelled()) {
            borderTask.cancel();
        }

        if (getSession() == null) return;
        getSession().findAll().stream()
                .filter(Objects::nonNull)
                .map(player2 -> Fantasy.userService().findById(player2.getUniqueId()).orElse(null))
                .filter(Objects::nonNull)
                .forEach(user -> user.getMetadataStorage().registerType(MetadataType.SESSION_MONSTER, user.getMetadataStorage().findExactData(MetadataType.SESSION_MONSTER) - 1));
    }

}
