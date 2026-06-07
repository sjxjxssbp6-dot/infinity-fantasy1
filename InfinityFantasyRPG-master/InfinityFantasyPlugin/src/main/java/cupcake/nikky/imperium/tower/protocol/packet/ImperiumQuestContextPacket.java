package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.client.protocol.QuestState;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.protocol.ImperiumPacket;
import cupcake.nikky.imperium.tower.quest.Quest;
import cupcake.nikky.imperium.tower.quest.reward.*;
import cupcake.nikky.imperium.tower.user.settings.exact.QuestSettings;
import cupcake.nikky.imperium.tower.utility.QuestUtility;
import lombok.val;
import me.anfanik.steda.api.utility.ItemBuilder;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.wada.util.ModTransfer;

import java.util.ArrayList;

public class ImperiumQuestContextPacket extends ImperiumPacket<QuestSettings> {

    private final Player player;
    private final Quest quest;
    private final boolean isSubscribed;
    private final NPCs npc;

    public ImperiumQuestContextPacket(@NotNull final Player player, @NotNull final QuestSettings client, @Nullable final Quest quest, final boolean isSubscribed, @Nullable final NPCs npc) {
        super(client);
        this.player = player;
        this.quest = quest;
        this.isSubscribed = isSubscribed;
        this.npc = npc;
    }

    @Override
    public void notice(int... flags) {
        for (int flag : flags) {
            if (flag == 0) { // full context
                this.registerContext();
            }
        }
    }

    private void registerContext() {
        if (quest == null) {
            Fantasy.runLater(() -> Bukkit.getLogger().warning("Attempt to send quest context packet with null quest"));
            return;
        }

        val currentState = QuestUtility.findCurrentQuestState(player, quest.getId());
        if (currentState == null) {
            Fantasy.runLater(() -> Bukkit.getLogger().warning("Attempt to send quest context packet with null quest state"));
            return;
        }

        val transfer = new ModTransfer();
        val description = new ArrayList<>(quest.getDescription());
        val goals = new ArrayList<String>();

        if (!getClient().isPassed(quest.getId())) {
            val requirements = getClient().findInProgress(quest.getId()).orElse(quest).getRequirements();

            if (!requirements.isEmpty()) {
                requirements.forEach(questRequirement -> questRequirement.goalDescription().forEach(line ->
                        goals.addAll(QuestUtility.convertDescription(String.join(" ", line), 50))));
            }
        }

        val idLine = quest.isVisibleQuestId() ? "&c#" + quest.getId() + " " : "";
        transfer.writeString(TextUtility.colorize(idLine + "&6" + quest.getTitle()));
        transfer.writeString(String.join("\n", TextUtility.colorize(description)));
        transfer.writeString(String.join("\n", TextUtility.colorize(goals)));
        transfer.writeInt(quest.getId()).writeInt(quest.getQuestRewards().size());
        quest.getQuestRewards().forEach(questReward -> {
            if (questReward instanceof ItemReward itemReward) {
                transfer.writeItem(itemReward.getBuildProcessor().catchItem().createNew(itemReward.getAmount()));
            } else if (questReward instanceof ExpReward expReward) {
                val exp = expReward.getAmount();
                transfer.writeItem(ItemBuilder.fromMaterial(Material.EXP_BOTTLE)
                        .setName("&c" + exp + " &6" + TextUtility.transformViaAmount(exp, "опыт", "опыта", "опыта"))
                        .build());
            } else if (questReward instanceof GranReward granReward) {
                val gran = granReward.getAmount();
                transfer.writeItem(ItemBuilder.fromMaterial(Material.EMERALD)
                        .nbtBuilder()
                            .setTag("rid", String.valueOf(1000))
                        .apply()
                        .setName("&c" + gran + " &6" + CurrencyType.GRAN.getTextTransformer().apply(gran))
                        .build());
            } else if (questReward instanceof FirmReward firmReward) {
                val firm = firmReward.getAmount();
                transfer.writeItem(ItemBuilder.fromMaterial(Material.EMERALD)
                        .nbtBuilder()
                        .setTag("rid", String.valueOf(1001))
                        .apply()
                        .setName("&c" + firm + " &6" + CurrencyType.FIRM.getTextTransformer().apply(firm))
                        .build());
            } else if (questReward instanceof FlowerReward flowerReward) {
                val flowers = flowerReward.getAmount();
                transfer.writeItem(ItemBuilder.fromMaterial(Material.EMERALD)
                        .nbtBuilder()
                        .setTag("rid", String.valueOf(1051))
                        .apply()
                        .setName("&c" + flowers + " &6" + CurrencyType.FLOWER.getTextTransformer().apply(flowers))
                        .build());
            } else if (questReward instanceof TreePointReward treePointReward) {
                transfer.writeItem(ItemBuilder.fromMaterial(Material.EMERALD)
                        .nbtBuilder()
                        .setTag("rid", String.valueOf(1053))
                        .apply()
                        .setName("&c1&r" + treePointReward.getPointType().getIcon())
                        .build());
            }
        });

        boolean canReceive = QuestUtility.canClick(quest, currentState, npc, isSubscribed);
        if (currentState == QuestState.NOT_RECEIVE && canReceive) canReceive = quest.canReceive(player);

        transfer.writeInt(currentState.getState()).writeInt(canReceive ? 1 : 0).send("imp:quest-cont", player);
    }

}
