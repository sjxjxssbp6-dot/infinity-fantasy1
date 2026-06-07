package cupcake.nikky.imperium.tower.quest.requirement.exact;

import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.quest.requirement.QuestRequirement;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TalkToNPCRequirement extends QuestRequirement {

    private final List<NPCs> goals = new ArrayList<>();

    @Override
    public TalkToNPCRequirement clone() {
        val goal = new TalkToNPCRequirement();
        goal.goals.addAll(goals);
        goal.getData().putAll(getData());
        goal.setDone(goal.isDone());
        return goal;
    }

    public TalkToNPCRequirement appendTalkGoals(@NotNull final NPCs... goals) {
        this.goals.addAll(Arrays.asList(goals));
        return this;
    }

    public void onTalk(Player player, NPCs npc, boolean notification) {
        if (this.isDone() || !goals.contains(npc) || this.findState(npc) == 1) return;
        val key = npc.name();
        getData().put(key, 1);

        if (notification) {
            sendNotification(player);
        }

        this.checkDone();
    }

    public int findState(NPCs npc) {
        return getData().getOrDefault(npc.name(), goals.contains(npc) ? 0 : -1);
    }

    @Override
    public List<String> goalDescription() {
        final List<String> result = new ArrayList<>();

        goals.forEach(npc -> {
            val talked = getData().getOrDefault(npc.name(), 0);
            val isDone = talked == 1;

            val isSoloNpc = npc.getSpawnLocations().size() == 1;
            val npcInfo = (isSoloNpc ? String.format(" &7(x=%.0f z=%.0f)", npc.getSpawnLocations().get(0).getX(), npc.getSpawnLocations().get(0).getZ()) : "");

            result.add("&7Поговорить с: " + (isDone ? "&2" : "&6") + TextUtility.uncolorize(npc.getMask().getData().getName()) + (isDone ? "" : npcInfo));
        });

        return result;
    }

    @Override
    protected void checkDone() {
        boolean tempDone = true;

        for (NPCs npc : goals) {
            if (getData().getOrDefault(npc.name(), 0) == 0) {
                tempDone = false;
                break;
            }
        }

        setDone(tempDone);
    }



}
