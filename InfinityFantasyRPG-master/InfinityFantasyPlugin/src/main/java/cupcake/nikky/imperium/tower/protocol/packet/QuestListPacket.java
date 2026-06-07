package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.protocol.ImperiumPacket;
import cupcake.nikky.imperium.tower.utility.QuestUtility;
import lombok.val;
import me.func.mod.conversation.ModTransfer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class QuestListPacket extends ImperiumPacket<Player> {

    private final Map<Integer, ItemStack> quests;

    public QuestListPacket(@NotNull Player client, @NotNull final Map<Integer, ItemStack> quests) {
        super(client);
        this.quests = quests;
    }

    @Override
    public void notice(int... flags) {
        for (int flag : flags) {
            if (flag == 0) {
                this.sendUpdate();
            }
        }
    }

    public void sendUpdate() {
        val transfer = new ModTransfer();
        transfer.varInt(quests.size());

        quests.keySet().stream().sorted().forEach(id -> {
            val questState = QuestUtility.findCurrentQuestState(getClient(), id);
            if (questState == null) return;

            transfer.varInt(id).varInt(questState.getState()).varInt(QuestUtility.findCurrentPlayerQuest(getClient(), id).canReceive(getClient()) ? 1 : 0).item(quests.get(id));
        });

        transfer.send("imp:quest-list", getClient());
    }

}
