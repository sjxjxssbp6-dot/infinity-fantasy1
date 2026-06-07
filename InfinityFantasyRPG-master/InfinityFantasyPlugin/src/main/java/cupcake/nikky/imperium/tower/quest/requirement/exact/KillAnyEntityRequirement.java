package cupcake.nikky.imperium.tower.quest.requirement.exact;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.quest.requirement.QuestRequirement;
import lombok.val;
import org.bukkit.entity.Player;

import java.util.*;

public class KillAnyEntityRequirement extends QuestRequirement {

    private int amount;
    private int minLevel;

    @Override
    public KillAnyEntityRequirement clone() {
        val goal = new KillAnyEntityRequirement();
        goal.amount = amount;
        goal.minLevel = minLevel;
        goal.getData().putAll(getData());
        goal.setDone(goal.isDone());
        return goal;
    }

    public final KillAnyEntityRequirement setupGoal(final int amount, final int minLevel) {
        this.amount = amount;
        this.minLevel = minLevel;

        return this;
    }

    @Override
    public List<String> goalDescription() {
        final List<String> result = new ArrayList<>();

        var currentAmount = getData().getOrDefault("currentAmount", 0);

        val isDone = currentAmount >= amount;
        val levelLine = minLevel == -1 ? "" : " &7(&6" + minLevel + "+ &7уровня)";

        result.add("&7Убить монстров" + levelLine + "&7: " + (isDone ? "&2" : "&6") + currentAmount + "&7/" + (isDone ? "&2" : "&6") + amount);

        return result;
    }

    public void onKill(Player killer, Monster monster, boolean notification) {
        if (this.isDone()) return;

        if (monster.getLevel() < minLevel) return;

        val current = getData().getOrDefault("currentAmount", 0);

        if (Objects.equals(current, amount)) return;

        getData().put("currentAmount", Math.min(current + 1, amount));

        if (notification && Objects.equals(getData().get("currentAmount"), amount)) {
            sendNotification(killer);
        }

        this.checkDone();
    }

    @Override
    protected void checkDone() {
        setDone(amount <= getData().getOrDefault("currentAmount", 0));
    }

}
