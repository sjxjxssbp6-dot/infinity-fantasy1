package cupcake.nikky.imperium.tower.quest.requirement.exact;

import cupcake.nikky.imperium.tower.quest.requirement.QuestRequirement;
import lombok.val;
import org.bukkit.entity.Player;

import java.util.*;

public class FishingRequirement extends QuestRequirement {

    private static final String KEY = "fishes";
    private int goalFishes;

    public FishingRequirement setGoal(final int goal) {
        this.goalFishes = goal;
        return this;
    }

    @Override
    public FishingRequirement clone() {
        val goal = new FishingRequirement();
        goal.setGoal(goalFishes);
        goal.getData().putAll(getData());
        goal.setDone(goal.isDone());
        return goal;
    }

    @Override
    public List<String> goalDescription() {
        final List<String> result = new ArrayList<>();

        var amount = getData().getOrDefault(KEY, 0);

        val isDone = amount >= goalFishes;
        val color = isDone ? "&2" : "&6";

        result.add("&7Поймать рыб: " + color + amount + "&7/" + color + goalFishes);

        return result;
    }

    @Override
    protected void checkDone() {
        setDone(getData().getOrDefault(KEY, 0) >= goalFishes);
    }

    public boolean onFish(Player player, boolean notification) {
        if (!this.isDone()) {
            val current = getData().getOrDefault(KEY, 0);

            if (current >= goalFishes) return false;

            getData().put(KEY, Math.min(current + 1, goalFishes));

            if (notification) {
                sendNotification(player);
            }

            this.checkDone();
            return true;
        }

        return false;
    }

}
