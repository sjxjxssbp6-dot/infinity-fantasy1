package cupcake.nikky.imperium.tower.quest;

import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.quest.reward.QuestReward;
import cupcake.nikky.imperium.tower.quest.requirement.QuestRequirement;
import cupcake.nikky.imperium.tower.user.settings.exact.QuestSettings;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class QuestBuilder {

    private int id;
    private String title, receiveRequirementText;
    private NPCs storage, pass;
    private boolean daily = false, visibleQuestId = true;
    private Predicate<QuestSettings> receiveHandler;
    private Consumer<Player> onComplete;
    private final List<String> description = new ArrayList<>();
    private final List<QuestReward> questRewards = new ArrayList<>();
    private final List<QuestRequirement> requirements = new ArrayList<>();

    public static QuestBuilder newBuilder() {
        return new QuestBuilder();
    }

    public QuestBuilder setId(final int id) {
        this.id = id;
        return this;
    }

    public QuestBuilder setDaily(final boolean daily) {
        this.daily = daily;
        return this;
    }

    public QuestBuilder setVisibleQuestId(final boolean visibleQuestId) {
        this.visibleQuestId = visibleQuestId;
        return this;
    }

    public QuestBuilder setTitle(@NotNull final String title) {
        this.title = title;
        return this;
    }

    public QuestBuilder setReceiveRequirementText(@NotNull final String receiveRequirementText) {
        this.receiveRequirementText = receiveRequirementText;
        return this;
    }

    public QuestBuilder setStorageNpc(@Nullable final NPCs npc) {
        this.storage = npc;
        return this;
    }

    public QuestBuilder setToPassNpc(@Nullable final NPCs npc) {
        this.pass = npc;
        return this;
    }

    public QuestBuilder setDescription(@NotNull final Collection<String> description) {
        this.description.clear();
        this.description.addAll(description);
        return this;
    }

    public QuestBuilder setDescription(@NotNull final String description) {
        this.description.clear();
        this.description.add(description.replace("\n", " "));
        return this;
    }

    public QuestBuilder appendDescription(@NotNull final String... description) {
        this.description.addAll(Arrays.asList(description));
        return this;
    }

    public QuestBuilder appendRewards(@NotNull final QuestReward... rewards) {
        this.questRewards.addAll(Arrays.asList(rewards));
        return this;
    }

    public QuestBuilder appendRequirement(@NotNull final QuestRequirement... requirements) {
        this.requirements.addAll(Arrays.asList(requirements));
        return this;
    }

    public QuestBuilder setReceiveHandler(@NotNull final Predicate<QuestSettings> receiveHandler) {
        this.receiveHandler = receiveHandler;
        return this;
    }

    public QuestBuilder setOnComplete(@NotNull final Consumer<Player> onComplete) {
        this.onComplete = onComplete;
        return this;
    }

    public Quest build() {
        val quest = new DefaultImperiumQuest(id, storage, pass == null ? storage == null ? pass : storage : pass, title, description, daily);
        quest.addQuestReward(questRewards);
        quest.addQuestRequirement(requirements);
        quest.receiveHandler(receiveHandler);
        quest.onComplete(onComplete);
        quest.setVisibleQuestId(visibleQuestId);
        quest.setReceiveRequirementText(receiveRequirementText);
        return quest;
    }

}
