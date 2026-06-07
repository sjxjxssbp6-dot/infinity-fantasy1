package cupcake.nikky.imperium.tower.entity.monster.listener;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.achievement.AchievementType;
import cupcake.nikky.imperium.tower.achievement.Achievements;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.DeathType;
import cupcake.nikky.imperium.tower.death.DeathObject;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.entity.monster.event.AsyncFantasyMonsterDeath;
import cupcake.nikky.imperium.tower.edge.EdgeCoreList;
import cupcake.nikky.imperium.tower.entity.monster.loot.other.GranLoot;
import cupcake.nikky.imperium.tower.entity.monster.modifier.ModifierCollection;
import cupcake.nikky.imperium.tower.entity.monster.shard.ShardingMonster;
import cupcake.nikky.imperium.tower.fraction.FractionProvider;
import cupcake.nikky.imperium.tower.statistic.Statistics;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import me.anfanik.steda.api.utility.MathUtility;
import me.anfanik.steda.api.utility.Pair;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class MonsterListener implements DefaultListener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDeath(@NotNull final AsyncFantasyMonsterDeath event) {
        val monster = event.getMonster();
        if (monster == null) return;

        monster.getTargetTracker().goalTarget(null);

        final LivingEntity killer = event.getLastDamageContainer() == null ? null : event.getLastDamageContainer().getAttackInfluence();
        if (killer == null) return;

        val killerUser = killer instanceof Player player ? player.<User>getBungeePlayer() : null;
        if (killerUser == null) return;

        val monsterSpigot = monster.getSpigot();
        if (monsterSpigot == null) return;

        if (monster.getMonsterType() == MonsterType.WARRIOR_SYNERGY) {
            FractionProvider.getInstance().setBuffFraction(killerUser.getFractionType());
        }

        val originalAttackers = event.getAttackers();

        val newAttackers = originalAttackers.stream()
                .filter(Objects::nonNull)
                .filter(OfflinePlayer::isOnline)
                .map(Player::<User>getBungeePlayer)
                .collect(Collectors.toList());

        newAttackers.forEach(user -> {
            val player = user.getSpigot();
            if (player == null) return;

            user.findIncubatorSettings().findAllIncubators().forEach(incubator -> incubator.onKill(player, monster));
        });

        monster.getDropExpLoot().apply(newAttackers, monster);
        val currencyReward = 20 * Math.pow(1.02, monster.getLevel()) *
                (monster.findAttribute(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS).findValue() + killerUser.findAttribute(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS).findValue() - 1);
        new GranLoot((int) Math.round(currencyReward * MathUtility.randomInRange(.80, 1.30))).apply(newAttackers, monster);

        val lootWeightPool = monster.getMonsterData().getLootWeightPool();
        double repeatTime = lootWeightPool.getRepeatTime();
        double increase = 1;

        if (monster instanceof ShardingMonster shardingMonster)
            increase += shardingMonster.getIncreaseItemQuantity() / 100D;
        if (killerUser.findFragmentSettings().isInFragment())
            increase += killerUser.findFragmentSettings().getFragmentItemQuantity() / 100D;

        if (!monster.getMonsterData().isPandoraEntity()) {
            increase += monster.findAttribute(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS).findValue() - 1;
        }

        increase += killerUser.findAttribute(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS).findValue() - 1;

        val loot = lootWeightPool.generateItemPool(killerUser, monster, (int) Math.round(repeatTime * increase));

        val world = monsterSpigot.getWorld();

        val filterHandler = killerUser.findLootFilterSettings().getItemFilterHandler();
        val items = loot.stream().filter(itemStack -> {
            val imperiumItem = Fantasy.itemProvider().find(itemStack);
            if (imperiumItem == null) return false;

            val isValid = filterHandler.isValidToRemove(imperiumItem);
            if (isValid) {
                filterHandler.process(null, imperiumItem, itemStack.getAmount(), true);
                return false;
            }

            return true;
        }).map(itemStack -> {
            val location = monsterSpigot.getLocation().clone();
            val random = ((CraftWorld) world).getHandle().random;
            val xs = random.nextFloat() * 0.7F - 0.35;
            val ys = random.nextFloat() * 0.7F - 0.35;
            val zs = random.nextFloat() * 0.7F - 0.35;
            CraftWorld.randomLocationWithinBlock(location, xs, ys, zs);
            return new Pair<>(itemStack, location);
        });

        Fantasy.runLater(() -> items.forEach(paired -> {
            val itemStack = paired.getFirst();
            val location = paired.getSecond();
            val amount = itemStack.getAmount();

            itemStack.setAmount(1);

            for (int i = 0; i < amount; i++) {
                val item = Fantasy.bukkitProvider().spawnItem(location, itemStack);
                item.setCanMobPickup(false);
            }
        }));

        val killerSpigot = killerUser.getSpigot();
        if (killerSpigot == null) return;

        EdgeCoreList.generate(killerSpigot, monsterSpigot);

        newAttackers.forEach(user -> {
            val monsterKillStat = user.findStatisticSettings().findStatistic(Statistics.MONSTER_KILL);
            monsterKillStat.add(1);

            val achievementSettings = user.findAchievementSettings();
            Achievements.findByType(AchievementType.MONSTER).stream()
                    .map(achievementSettings::findByEnum)
                    .map(achievement -> achievement.addProgress(monster.getMonsterType().name())).forEach(achievementProcess ->
                            achievementProcess.ifGoalJustDone(achievement1 -> {
                                val title = achievement1.getType().getChecker().getTitle();
                                Fantasy.clientProvider().notification(Bukkit.getPlayer(user.getUniqueId()), "&r\uE129 &aНовое достижение!", "&6" + title);
                            }));
        });

        val modifierCollection = monster.getModifierEngine().getCurrentModifiers();
        val currentDeathType = killerUser.getCurrentDeathType();
        if ((modifierCollection == ModifierCollection.EPIC || modifierCollection == ModifierCollection.UNIQUE) && currentDeathType != DeathType.NONE) {
            var location = monsterSpigot.getLocation();
            Fantasy.particleProvider().sendLocationDeath(killerSpigot, location, currentDeathType);
            DeathObject.findByType(currentDeathType).getDeathSound().playSound(location);
        }

        monster.onDeath(killerSpigot);

        val monsterName = modifierCollection.getNameColor() + monster.getMonsterData().getName();
        Fantasy.bukkitProvider().sendMessageToExactChat(ChatType.SYSTEM, "&6Вы убили " + monsterName + " &c" + monster.getLevel() + " &6уровня",
                Collections.singletonList(killerSpigot));

        Fantasy.bukkitProvider().sendMessageToExactChat(ChatType.SYSTEM, "&6Вы участвовали в убийстве " + monsterName + " &c" + monster.getLevel() + " &6уровня",
                newAttackers.stream().filter(user -> !user.getUniqueId().equals(killerUser.getUniqueId())).map(User::getSpigot).collect(Collectors.toList()));
    }

}
