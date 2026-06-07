package cupcake.nikky.imperium.tower.quest.session;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.client.protocol.QuestState;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.protocol.packet.ImperiumQuestContextPacket;
import cupcake.nikky.imperium.tower.protocol.packet.QuestListPacket;
import cupcake.nikky.imperium.tower.quest.Quest;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.metadata.MetadataType;
import cupcake.nikky.imperium.tower.utility.QuestUtility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class QuestSession {

    private final Player player;
    private final List<Integer> quests;

    @Setter
    @Accessors(fluent = true, chain = true)
    private BiFunction<Player, Quest, Boolean> canReceive, canView;
    @Setter
    @Getter
    @Accessors(fluent = true, chain = true)
    private NPCs npcType;

    public void openContext() {
        val icons = this.generateIcons();
        val showQuest = this.findQuestToShow(icons.keySet());

        if (icons.size() == 0) {
            Fantasy.clientProvider().notification(player, SystemNotificationType.WARN, "Этот НПС не имеет заданий,", "которые бы Вам подходили");
        } else {
            this.updateIcons(icons);
            this.tryOpenContext(showQuest == -1 ? quests.get(0) : showQuest);
        }
    }

    public void updateIcons() {
        this.updateIcons(this.generateIcons());
    }

    public void updateIcons(@NotNull final Map<Integer, ItemStack> icons) {
        new QuestListPacket(player, icons).notice(0);
    }

    public void updateCurrentContext() {
        val user = Fantasy.userService().findById(player.getUniqueId()).orElse(null);
        if (user == null) return;

        val questedPlayer = user.findQuestSettings();

        val donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();
        if (donateData == null) return;

        val questId = user.getMetadataStorage().findExactData(MetadataType.QUEST_CONTEXT);

        val playerQuest = QuestUtility.findCurrentPlayerQuest(questedPlayer, questId);

        new ImperiumQuestContextPacket(player, questedPlayer, playerQuest, donateData.isSubscribed(), npcType).notice(0);
    }

    public boolean tryOpenContext(final int questId) {
        val user = player.<User>getBungeePlayer();
        if (user == null) return false;

        val questedPlayer = user.findQuestSettings();

        val donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();
        if (donateData == null) return false;

        val playerQuest = QuestUtility.findCurrentPlayerQuest(questedPlayer, questId);
        val metadataStorage = user.getMetadataStorage();

        if (metadataStorage.findExactData(MetadataType.QUEST_CONTEXT) != questId) {
            new ImperiumQuestContextPacket(player, questedPlayer, playerQuest, donateData.isSubscribed(), npcType).notice(0);
            metadataStorage.registerType(MetadataType.QUEST_CONTEXT, questId);
            return true;
        }

        return false;
    }

    public Map<Integer, ItemStack> generateIcons() {
        val user = player.<User>getBungeePlayer();
        val questedPlayer = user.findQuestSettings();
        val donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();

        if (questedPlayer == null || donateData == null) {
            return new HashMap<>();
        }

        final Map<Integer, ItemStack> questItems = new HashMap<>();

        for (int questId : quests) {
            val quest = Fantasy.questService().findQuest(questId).orElse(null);
            if (quest == null) continue;

            val currentPlayerQuest = QuestUtility.findCurrentPlayerQuest(questedPlayer, questId);
            val currentQuestState = QuestUtility.findCurrentQuestState(questedPlayer, questId);

            if (!canView.apply(player, quest) || !QuestUtility.canClick(currentPlayerQuest, currentQuestState, npcType, donateData.isSubscribed()) && npcType != null) continue;

            questItems.put(questId, QuestUtility.generateQuestIcon(player, currentPlayerQuest, canReceive.apply(player, quest), canView.apply(player, quest)));
        }

        return questItems;
    }

    public int findQuestToShow(final Set<Integer> quests) {
        val user = player.<User>getBungeePlayer();
        val questedPlayer = user.findQuestSettings();

        for (int quest : quests.stream().sorted().collect(Collectors.toList())) {
            val currentQuestState = QuestUtility.findCurrentQuestState(questedPlayer, quest);
            val currentPlayerQuest = QuestUtility.findCurrentPlayerQuest(questedPlayer, quest);

            if (currentQuestState == QuestState.NOT_RECEIVE && currentPlayerQuest.canReceive(player) ||
                    currentQuestState == QuestState.RECEIVE ||
                    currentQuestState == QuestState.COMPLETE_WITHOUT_PASS) {
                return quest;
            }
        }

        return -1;
    }

}
