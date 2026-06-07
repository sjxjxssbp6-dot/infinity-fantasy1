package cupcake.nikky.imperium.tower.quest;

import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.quest.reward.QuestReward;
import cupcake.nikky.imperium.tower.quest.generator.ClientQuestGoal;
import cupcake.nikky.imperium.tower.quest.requirement.QuestRequirement;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.QuestUtility;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import me.func.mod.conversation.ModTransfer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public interface Quest {

    int getId();

    boolean isVisibleQuestId();

    NPCs getStorageNpc();

    NPCs getPassQuestNpc();

    ClientQuestGoal getClientQuestGoal();

    String getTitle();

    String getReceiveRequirementText();

    List<String> getDescription();

    List<QuestReward> getQuestRewards();

    List<QuestRequirement> getRequirements();

    default <T extends QuestRequirement> List<T> findRequirementsByClass(Class<? extends T> clazz) {
        return getRequirements().stream()
                .filter(Objects::nonNull)
                .filter(questRequirement -> questRequirement.getClass() == clazz)
                .map(clazz::cast)
                .collect(Collectors.toList());
    }

    boolean isDaily();

    UUID getOwner();

    void setOwner(UUID uuid);

    boolean tryGiveReward(Player player);

    /*
        Achieved all goals and ready to pass the quest to NPC
     */
    boolean isCompleted();

    /*
        Receive quest from npc
     */
    boolean canReceive(Player player);

    void onPassQuest(Player player);

    /*
        Receive quest from npc
     */
    void onAccept(User user);

    /*
        Drop quest when it has already got
     */
    void onDecline(User user);

    /*
        Drop quest by server
     */
    void dropQuest(User user);

    Quest applyTo(UUID playerUuid);

    default void createRegisterPacket(@NotNull final Player... players) {
        val transfer = new ModTransfer();
        val idLine = isVisibleQuestId() ? "&6#" + getId() + " " : "";
        transfer.varInt(getId()).string(TextUtility.colorize(idLine + "&f" + getTitle()));

        List<String> description = new ArrayList<>();

        boolean isDone = true;

        for (QuestRequirement requirement : getRequirements()) {
            if (!requirement.isDone()) {
                isDone = false;
                break;
            }
        }

        if (isDone) {
            val npc = getPassQuestNpc();

            val isSoloNpc = npc.getSpawnLocations().size() == 1;
            val npcInfo = (isSoloNpc ? String.format(" &7(x=%.0f z=%.0f)", npc.getSpawnLocations().get(0).getX(), npc.getSpawnLocations().get(0).getZ()) : "");

            description.add("&aМожно сдать задание: " + npc.getMask().getData().getName() + npcInfo);
        } else {
            getRequirements().forEach(questRequirement -> {
                for (String s : questRequirement.goalDescription()) {
                    description.addAll(QuestUtility.convertDescription(s, 40));
                }
            });
        }

        transfer.varInt(description.size());

        TextUtility.colorize(description).forEach(data -> {
            transfer.string(String.join("\n", data));
        });

        transfer.send("imp:quest-register", players);
    }

    default void createUpdatePacket(@NotNull final Player... players) {
        val transfer = new ModTransfer();
        transfer.varInt(getId());

        List<String> description = new ArrayList<>();

        boolean isDone = true;

        for (QuestRequirement requirement : getRequirements()) {
            if (!requirement.isDone()) {
                isDone = false;
                break;
            }
        }

        if (isDone) {
            val npc = getPassQuestNpc();

            val isSoloNpc = npc.getSpawnLocations().size() == 1;
            val npcInfo = (isSoloNpc ? String.format(" &7(x=%.0f z=%.0f)", npc.getSpawnLocations().get(0).getX(), npc.getSpawnLocations().get(0).getZ()) : "");

            description.add("&aМожно сдать задание: " + getPassQuestNpc().getMask().getData().getName() + npcInfo);
        } else {
            getRequirements().forEach(questRequirement -> {
                for (String s : questRequirement.goalDescription()) {
                    description.addAll(QuestUtility.convertDescription(s, 38));
                }
            });
        }

        transfer.varInt(description.size());

        TextUtility.colorize(description).forEach(data -> {
            transfer.string(String.join("\n", data));
        });

        transfer.send("imp:quest-update", players);
    }

    default void createUnregisterPacket(@NotNull final Player... players) {
        new ModTransfer().varInt(getId()).send("imp:quest-unregister", players);
    }

    String serialize();

    void deserialize(String serialized);

}
