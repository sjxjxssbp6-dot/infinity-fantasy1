package cupcake.nikky.imperium.tower.user.death;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.ParticipantCallback;
import cupcake.nikky.imperium.tower.buff.BuffedEntity;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.DeathType;
import cupcake.nikky.imperium.tower.death.DeathObject;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.statistic.Statistics;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.controller.ExpType;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeathListener implements DefaultListener {

    @EventHandler
    public void onDeath(@NotNull final AsyncFantasyUserDeath event) {
        final List<UUID> inCombat = new ArrayList<>();

        LivingEntity lastAttacker = event.getLastDamageContainer() == null ? null : event.getLastDamageContainer().getAttackInfluence();
        ParticipantCallback lastAttackerCallback = new ParticipantCallback(lastAttacker);
        val user = event.getUser();
        val player = user.getSpigot();

        if (lastAttacker != null) inCombat.add(lastAttacker.getUniqueId());
        inCombat.add(user.getUniqueId());

        Fantasy.combatProvider().invalidateCombat(inCombat);
        Fantasy.clientProvider().hideAttackSpeed(user);

        Fantasy.buffService().findById(user.getUniqueId()).ifPresent(BuffedEntity::processDeath);

        if (!user.getTargetEntity().isEmpty()) {
            user.getTargetEntity().getTargetData().unsubscribe(user.getUniqueId());
        }

        lastAttackerCallback.ifUserPresent(attackerUser -> {
            var userKillStat = attackerUser.findStatisticSettings().findStatistic(Statistics.USER_KILL);
            userKillStat.add(1);

            var deathByUserStat = user.findStatisticSettings().findStatistic(Statistics.DEATHS_BY_USER);
            deathByUserStat.add(1);

            val currentDeathType = attackerUser.getCurrentDeathType();
            if (currentDeathType != DeathType.NONE) {
                var attackerSpigot = attackerUser.getSpigot();
                var location = attackerSpigot.getLocation();
                Fantasy.particleProvider().sendLocationDeath(attackerSpigot, location, currentDeathType);
                DeathObject.findByType(currentDeathType).getDeathSound().playSound(location);
            }
        }).ifMonsterPresent(monster -> {
            val type = user.findStatisticSettings().findStatistic(Statistics.DEATHS_BY_MONSTER);
            type.add(1);
        });

        lastAttackerCallback.ifMonsterPresent(monster -> {
            monster.removeAttacker(player);
            monster.getTargetTracker().getTargetProcessor().tryCatchTargetByAttackers();
        });

        val levelHandler = user.getLevelController();
        val requireLevel = LevelStorage.getInstance().getStartShardEdgeLevel();

        if (levelHandler.getLevel() < requireLevel || levelHandler.isMaximumLevel() || levelHandler.getExp() == 0D) return;

        lastAttackerCallback.ifMonsterPresent(lastMonsterAttacker -> {
            val donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();

            val subscribeMultiplier = (donateData != null && donateData.isSubscribed()) ? 2D : 1D;
            val multiplier = 10D / subscribeMultiplier / 100D;

            val delta = -LevelStorage.getInstance().getBasicRequirementToNextLevel(levelHandler.getLevel()) * multiplier;
            val viewDelta = Math.min(Math.abs(delta), levelHandler.getExp());

            levelHandler.changeExp(delta, ExpType.DEATH);
            Fantasy.bukkitProvider().sendMessage(player, ChatType.NOTIFICATION, String.format("&cВ связи с Вашей смертью было потеряно %d%% (%.0f) от общего опыта",
                    Math.round(multiplier * 100),
                    Math.abs(viewDelta)));
        });
    }

}
