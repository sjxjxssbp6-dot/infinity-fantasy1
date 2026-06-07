package cupcake.nikky.imperium.tower.quest.requirement.exact;

import cupcake.nikky.imperium.tower.quest.requirement.QuestRequirement;
import lombok.Getter;
import lombok.val;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.Wada;

import java.util.*;

public class BlockInteractRequirement extends QuestRequirement {

    /* key - location of block, value - title */
    @Getter
    private final Map<Location, String> goals = new HashMap<>();

    @Override
    public BlockInteractRequirement clone() {
        val goal = new BlockInteractRequirement();
        goal.goals.putAll(goals);
        goal.getData().putAll(getData());
        goal.setDone(goal.isDone());
        return goal;
    }

    public final BlockInteractRequirement appendGoal(@NotNull final String title, @NotNull final Location blockLocation) {
        this.goals.put(blockLocation, title);
        return this;
    }

    public void onInteract(Player player, Block block, boolean notification) {
        if (this.isDone()) return;

        val location = block.getLocation();
        location.setYaw(0);
        location.setPitch(0);

        if (goals.containsKey(location)) {
            val title = goals.get(location);
            val current = getData().getOrDefault(title, 0);

            if (Objects.equals(1, current)) return;

            getData().put(title, 1);
            Wada.get().getBlocksOutlineModule().clear(location, player);

            if (notification) {
                sendNotification(player);
            }

            this.checkDone();
        }
    }

    @Override
    public List<String> goalDescription() {
        final List<String> result = new ArrayList<>();

        goals.keySet().forEach(location -> {
            var title = goals.get(location);
            var amount = getData().getOrDefault(title, 0);

            val isDone = amount >= 1;

            result.add("&7Взаимодействие с " + title + "&7: " + (isDone ? "&2" : "&6") + amount + "&7/" + (isDone ? "&2" : "&6") + 1);
        });

        return result;
    }

    @Override
    protected void checkDone() {
        boolean tempDone = true;

        for (String title : goals.values()) {
            if (getData().getOrDefault(title, 0) != 1) {
                tempDone = false;
                break;
            }
        }

        setDone(tempDone);
    }

}
