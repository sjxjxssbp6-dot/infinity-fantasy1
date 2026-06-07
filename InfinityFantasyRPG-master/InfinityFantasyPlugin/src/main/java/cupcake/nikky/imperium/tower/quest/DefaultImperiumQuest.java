package cupcake.nikky.imperium.tower.quest;

import com.google.common.reflect.TypeToken;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.quest.requirement.QuestRequirement;
import cupcake.nikky.imperium.tower.quest.reward.TreePointReward;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.exact.QuestSettings;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DefaultImperiumQuest extends ImperiumQuest {

    private final Type typeToken = new TypeToken<List<String>>(){}.getType();
    @Setter @Accessors(fluent = true)
    private Predicate<QuestSettings> receiveHandler;
    @Setter @Accessors(fluent = true)
    private Consumer<Player> onComplete;

    public DefaultImperiumQuest(final int id, @Nullable final NPCs storageNpc, @Nullable final NPCs passQuestNpc, @NotNull final String title,
                                @NotNull final List<String> description, final boolean daily) {
        super(id, storageNpc, passQuestNpc, title, description, daily);
    }

    public DefaultImperiumQuest(final int id, @NotNull final NPCs storageNpc, @NotNull final String title, @NotNull final List<String> description, final boolean daily) {
        super(id, storageNpc, storageNpc, title, description, daily);
    }

    @Override
    public boolean canReceive(@NotNull Player player) {
        val questSettings = player.<User>getBungeePlayer().findQuestSettings();

        if (receiveHandler != null) return receiveHandler.test(questSettings);
        return getId() == 1 || questSettings.isPassed(getId() - 1) && !questSettings.isPassed(getId()) && !questSettings.findInProgress(getId()).isPresent();
    }

    @Override
    public void onPassQuest(Player player) {
        super.onPassQuest(player);
        if (onComplete != null) onComplete.accept(player);
        if (getQuestRewards().stream().anyMatch(TreePointReward.class::isInstance)) {
            Fantasy.clientProvider().sendTreeIndicator(player.<User>getBungeePlayer());
        }
    }

    @Override
    public Quest applyTo(@NotNull final UUID playerUuid) {
        val instance = new DefaultImperiumQuest(getId(), getStorageNpc(), getPassQuestNpc(), getTitle().replace(" (*)", ""), getDescription(), isDaily());

        instance.setOwner(playerUuid);

        instance.addQuestRequirement(this.getRequirements().stream().map(QuestRequirement::clone).collect(Collectors.toList()));
        instance.addQuestReward(new ArrayList<>(this.getQuestRewards()));
        instance.receiveHandler(receiveHandler);
        instance.onComplete(onComplete);
        instance.setVisibleQuestId(isVisibleQuestId());
        instance.setReceiveRequirementText(getReceiveRequirementText());

        return instance;
    }

    @Override
    public String serialize() {
        return Fantasy.gson().toJson(getRequirements().stream().map(QuestRequirement::serialize).collect(Collectors.toList()), typeToken);
    }

    @Override
    public void deserialize(@Nullable final String serialized) {
        if (serialized == null || serialized.trim().equals("")) return;

        final List<String> serializedRequirements = Fantasy.gson().fromJson(serialized, typeToken);

        for (int i = 0; i < getRequirements().size(); i++) {
            if (serializedRequirements.size() - 1 < i) break;
            getRequirements().get(i).deserialize(serializedRequirements.get(i));
        }
    }

}
