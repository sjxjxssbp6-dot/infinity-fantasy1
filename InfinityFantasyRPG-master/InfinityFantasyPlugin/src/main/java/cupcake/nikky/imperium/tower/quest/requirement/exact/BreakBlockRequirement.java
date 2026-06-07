package cupcake.nikky.imperium.tower.quest.requirement.exact;

import cupcake.nikky.imperium.tower.quest.requirement.QuestRequirement;
import lombok.val;
import me.anfanik.steda.api.utility.Pair;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BreakBlockRequirement extends QuestRequirement {

    private final Map<Material, Integer> goals = new HashMap<>();
    private final Map<Material, String> titles = new HashMap<>();

    @Override
    public BreakBlockRequirement clone() {
        val goal = new BreakBlockRequirement();
        goal.goals.putAll(goals);
        goal.titles.putAll(titles);
        goal.getData().putAll(getData());
        goal.setDone(goal.isDone());
        return goal;
    }

    @SafeVarargs
    public final BreakBlockRequirement appendGoal(@NotNull final Pair<Material, Integer>... pairs) {
        for (Pair<Material, Integer> pair : pairs) {
            this.goals.put(pair.getFirst(), pair.getSecond());
        }
        return this;
    }

    @SafeVarargs
    public final BreakBlockRequirement appendTitles(@NotNull final Pair<Material, String>... pairs) {
        for (Pair<Material, String> pair : pairs) {
            this.titles.put(pair.getFirst(), pair.getSecond());
        }
        return this;
    }

    @Override
    public List<String> goalDescription() {
        final List<String> result = new ArrayList<>();

        goals.keySet().forEach(material -> {
            var amount = getData().getOrDefault(material.name(), 0);

            val isDone = amount >= goals.get(material);
            result.add("&7Сломать &6" + titles.get(material) + "&7: " + (isDone ? "&2" : "&6") + amount + "&7/" + (isDone ? "&2" : "&6") + goals.get(material));
        });

        return result;
    }

    @Override
    protected void checkDone() {
        boolean tempDone = true;

        for (Material material : goals.keySet()) {
            if (!getData().getOrDefault(material.name(), 0).equals(goals.get(material))) {
                tempDone = false;
                break;
            }
        }

        setDone(tempDone);
    }

    public boolean onBreak(Player player, Block block, boolean notification) {
        if (!this.isDone() && goals.containsKey(block.getType())) {
            val key = block.getType().name();
            val current = getData().getOrDefault(key, 0);
            val need = goals.get(block.getType());

            if (Objects.equals(current, need)) return false;

            getData().put(key, Math.min(current + 1, need));

            if (notification) {
                sendNotification(player);
            }

            this.checkDone();
            return true;
        }

        return false;
    }

}
