package cupcake.nikky.imperium.tower.quest.requirement.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.quest.requirement.QuestRequirement;
import lombok.val;
import me.anfanik.steda.api.utility.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CollectItemRequirement extends QuestRequirement {

    private final Map<String, Integer> goals = new HashMap<>();

    @Override
    public CollectItemRequirement clone() {
        val goal = new CollectItemRequirement();
        goal.goals.putAll(goals);
        goal.getData().putAll(getData());
        goal.setDone(goal.isDone());
        return goal;
    }

    @SafeVarargs
    public final CollectItemRequirement appendGoal(@NotNull final Pair<String, Integer>... pairs) {

        for (Pair<String, Integer> pair : pairs) {
            this.goals.put(pair.getFirst(), pair.getSecond());
        }

        return this;
    }

    public void onCollect(Player player, ItemStack itemStack, boolean notification) {
        if (this.isDone()) return;

        val item = Fantasy.itemProvider().find(itemStack);
        if (item == null) return;

        if (goals.containsKey(item.getUniqueId())) {
            val need = goals.get(item.getUniqueId());
            val current = getData().getOrDefault(item.getUniqueId(), 0);

            if (Objects.equals(need, current)) return;

            getData().put(item.getUniqueId(), Math.min(current + itemStack.getAmount(), need));

            if (notification) {
                sendNotification(player);
            }

            this.checkDone();
        }
    }

    @Override
    public List<String> goalDescription() {
        final List<String> result = new ArrayList<>();

        goals.keySet().forEach(id -> {
            var amount = getData().getOrDefault(id, 0);

            val isDone = amount >= goals.get(id);

            val item = Fantasy.itemProvider().find(id);
            if (item == null) return;

            result.add("&7Получить " + item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName() +
                    "&7: " + (isDone ? "&2" : "&6") + amount + "&7/" + (isDone ? "&2" : "&6") + goals.get(id));
        });

        return result;
    }

    @Override
    protected void checkDone() {
        boolean tempDone = true;

        for (String id : goals.keySet()) {
            if (goals.get(id) > getData().getOrDefault(id, 0)) {
                tempDone = false;
                break;
            }
        }

        setDone(tempDone);
    }

}
