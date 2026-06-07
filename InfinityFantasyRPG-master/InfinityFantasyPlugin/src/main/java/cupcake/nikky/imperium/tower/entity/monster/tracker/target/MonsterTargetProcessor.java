package cupcake.nikky.imperium.tower.entity.monster.tracker.target;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.battle.EntityBattleData;
import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.server.v1_12_R1.EntityLiving;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Objects;

@RequiredArgsConstructor
public class MonsterTargetProcessor {

    private final Monster monster;
    private final MonsterTargetTracker tracker;

    public void onGoalTargetUpdate(@Nullable final EntityLiving oldTarget, @Nullable final EntityLiving newTarget) {
        val isRemovedTarget = oldTarget != null && newTarget == null;
        if (isRemovedTarget) this.forgotTarget();
    }

    public void tryCatchTargetByAttackers() {
        val attackers = monster.getAttackers();
        val entity = monster.getSpigot();

        if (entity != null && !attackers.isEmpty()) {
            val location = entity.getLocation();
            val range = monster.getConduct().getRange() * monster.getConduct().getRange();

            val nextDamager = attackers.stream()
                    .filter(player -> player.getLocation().distanceSquared(location) < range)
                    .filter(player -> {
                        val user = player.<User>getBungeePlayer();
                        return user != null && user.isLoaded() && !user.isInvalidated() && !user.isShouldDeath();
                    })
                    .map(player -> monster.findBattleData(player.getUniqueId()).orElse(null))
                    .filter(Objects::nonNull)
                    .max(Comparator.comparing(EntityBattleData::getDamage)).orElse(null);
            this.tracker.goalTarget(nextDamager == null ? null : Bukkit.getPlayer(nextDamager.getUserUuid()));
        } else this.tracker.goalTarget(null);
    }

    public void forgotTarget() {
        if (!monster.isExist() || monster.isShouldDeath()) return;

        monster.setInvulnerability(20 * 4);
        tracker.setForgotTarget(true);

        monster.getAttackers().forEach(monster::removeAttacker);
        monster.getTargetBarData().unsubscribeAll();
        monster.clearBattleData();
        Fantasy.combatProvider().invalidateCombat(monster.getMonsterUniqueId());

        monster.onForgotTarget();
    }

}
