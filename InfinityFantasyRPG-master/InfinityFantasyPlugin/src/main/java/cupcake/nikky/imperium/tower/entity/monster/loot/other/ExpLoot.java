package cupcake.nikky.imperium.tower.entity.monster.loot.other;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.entity.monster.shard.ShardingMonster;
import cupcake.nikky.imperium.tower.entity.monster.statistic.MonsterStatistic;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.controller.ExpType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import me.anfanik.steda.api.utility.TextUtility;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

@Getter
@RequiredArgsConstructor
public class ExpLoot {

    private final double minExp;
    private final double maxExp;

    public void apply(@NotNull final Collection<User> users, @NotNull final Monster monster) {
        if (minExp == maxExp && minExp == 0) return;

        val higherDamageDealer = monster.getBattleData().values().stream().max(Comparator.comparingInt(data -> Math.round((float) data.getDamage()))).orElse(null);

        User receiver;
        double exp = MathUtility.randomInRange(minExp, maxExp);

        val multiplier = monster.findAttribute(ImperiumAttribute.MONSTER_INCREASE_EXP_DROP).findValue();
        exp += exp * multiplier;

        if (higherDamageDealer == null) {
            receiver = users.stream().max(Comparator.comparingInt(User::getLevel)).orElse(null);
        } else {
            receiver = users.stream().filter(user -> user.getUniqueId().equals(higherDamageDealer.getUserUuid())).findFirst().orElse(null);
        }

        if (receiver == null || receiver.getLevelController().isMaximumLevel()) return;

        val deltaLevel = Math.max(0, Math.min(receiver.getLevelController().getLevel() - 2, MonsterStatistic.getInstance().findHigherMonsterLevel()) - monster.getLevel());
        exp /= Math.pow(2, deltaLevel);

        if (receiver.findFragmentSettings().isInFragment())
            exp = exp * (1 + receiver.findFragmentSettings().getFragmentExperienceIncrease() / 100D);
        if (monster instanceof ShardingMonster shardingMonster)
            exp = exp * (1 + shardingMonster.getIncreaseExperience() / 100D);

        if (exp < 1D) return;

        exp = receiver.getLevelController().changeExp(exp, ExpType.BATTLE);
        Fantasy.bukkitProvider().sendMessageToExactChat(ChatType.SYSTEM, "&c+" + (int) exp + " &6опыта", Collections.singletonList(receiver.getSpigot()));
        Fantasy.clientProvider().cursorNotify(receiver, TextUtility.colorize("&2+&c" + (int) exp), "\uE1A6");
    }

}
