package cupcake.nikky.imperium.tower.utility;

import com.google.common.collect.ImmutableList;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.client.protocol.QuestState;
import cupcake.nikky.imperium.tower.quest.Quest;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.exact.QuestSettings;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuestUtility {

    public static QuestState findCurrentQuestState(@NotNull final QuestSettings questedPlayer, final int questId) {
        val isPassed = questedPlayer.isPassed(questId);
        if (isPassed) return QuestState.PASS;

        val received = questedPlayer.findInProgress(questId);
        val completed = received.isPresent() && received.get().isCompleted();

        if (received.isPresent() && completed) {
            return QuestState.COMPLETE_WITHOUT_PASS;
        } else if (received.isPresent()) {
            return QuestState.RECEIVE;
        }

        return QuestState.NOT_RECEIVE;
    }

    public static boolean canClick(@NotNull final Quest quest, @NotNull final QuestState state, @Nullable final NPCs npc, final boolean subscribed) {
        if (npc == null && subscribed) return true;

        if (state == QuestState.NOT_RECEIVE) {
            return npc != null && quest.getStorageNpc() == npc;
        } else if (state == QuestState.COMPLETE_WITHOUT_PASS) {
            return npc != null && quest.getPassQuestNpc() == npc;
        }

        return true;
    }

    public static QuestState findCurrentQuestState(@NotNull final Player player, final int questId) {
        val questedPlayer = player.<User>getBungeePlayer().findQuestSettings();
        if (questedPlayer == null) return null;
        return findCurrentQuestState(questedPlayer, questId);
    }

    public static Quest findCurrentPlayerQuest(@NotNull final Player player, final int questId) {
        val questedPlayer = player.<User>getBungeePlayer().findQuestSettings();
        if (questedPlayer == null) return Fantasy.questService().findQuest(questId).orElse(null);
        return findCurrentPlayerQuest(questedPlayer, questId);
    }

    public static Quest findCurrentPlayerQuest(@NotNull final QuestSettings questedPlayer, final int questId) {
        val isPassed = questedPlayer.isPassed(questId);
        if (isPassed) return Fantasy.questService().findQuest(questId).orElse(null);

        return questedPlayer.findInProgress(questId).orElse(Fantasy.questService().findQuest(questId).orElse(null));
    }

    public static ItemStack generateQuestIcon(@NotNull final Player player, @Nullable final Quest quest, final boolean receivable, final boolean visible) {
        if (!visible) {
            return new ItemStack(Material.AIR);
        }

        val questedPlayer = player.<User>getBungeePlayer().findQuestSettings();
        if (questedPlayer == null || quest == null) return new ItemStack(Material.AIR);

        val questState = findCurrentQuestState(questedPlayer, quest.getId());
        val idLine = quest.isVisibleQuestId() ? "&6#" + quest.getId() + " " : "";

        switch (questState) {
            case COMPLETE_WITHOUT_PASS: return Fantasy.newItemBuilder(Material.BOOK)
                        .nbtBuilder().setTag("rid", "3").apply()
                        .setName(idLine + "&r" + quest.getTitle())
                        .build();

            case PASS: return Fantasy.newItemBuilder(Material.BOOK)
                    .nbtBuilder().setTag("rid", "4").apply()
                    .setName(idLine + "&a" + quest.getTitle())
                    .build();

            case RECEIVE: return Fantasy.newItemBuilder(Material.BOOK)
                        .nbtBuilder().setTag("rid", "1").apply()
                        .setName(idLine + "&6" + quest.getTitle())
                        .build();
            default: {
                if (quest.canReceive(player)) {
                    val lore = new ArrayList<String>();
                    if (!receivable) lore.add("&7Можно взять данное задание у " + quest.getStorageNpc().getMask().getData().getName());

                    return Fantasy.newItemBuilder(Material.BOOK)
                            .nbtBuilder().setTag("rid", "2").apply()
                            .setName(idLine + "&c" + quest.getTitle())
                            .setLore(lore)
                            .build();
                } else {
                    List<String> lore = new ArrayList<>();
                    lore.add("&cЗадание недоступно");
                    lore.add(" ");
                    lore.add("&6Требования:");
                    lore.addAll(Arrays.asList(quest.getReceiveRequirementText().split("\n")));

                    return Fantasy.newItemBuilder(Material.BOOK)
                            .nbtBuilder().setTag("rid", "5").apply()
                            .setName(idLine + "&4" + quest.getTitle())
                            .setLore(lore)
                            .build();
                }
            }
        }
    }

    public static List<String> convertDescription(@NotNull final String description, final int limit) {
        val result = new ArrayList<String>();
        val justifiedLine = new StringBuilder();
        val words = description.split(" ");

        String lastColor = null;

        for (int i = 0; i < words.length; i++) {
            val word = words[i];

            if (word.startsWith("&") || word.startsWith("§")) lastColor = "§" + word.toCharArray()[1];

            justifiedLine.append(words[i]).append(' ');
            if (i + 1 == words.length || getLineLength(justifiedLine.toString()) + getLineLength(words[i + 1]) > limit) {
                justifiedLine.deleteCharAt(justifiedLine.length() - 1);
                result.add(justifiedLine.toString());
                justifiedLine.setLength(0);
                if (lastColor != null) justifiedLine.append(lastColor);
            }
        }

        return ImmutableList.copyOf(result);
    }

    private static int getLineLength(@NotNull final String line) {
        boolean isColor = false;
        int result = 0;

        for (char c : line.toCharArray()) {
            if (isColor) {
                isColor = false;
                continue;
            }

            if (c == '&') {
                isColor = true;
                continue;
            }

            result += 1;
        }

        return result;
    }


}
