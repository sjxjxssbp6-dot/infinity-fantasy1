package cupcake.nikky.imperium.tower.quest;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.quest.generator.ClientQuestGoalGenerator;
import cupcake.nikky.imperium.tower.quest.requirement.exact.BlockInteractRequirement;
import cupcake.nikky.imperium.tower.quest.reward.QuestReward;
import cupcake.nikky.imperium.tower.quest.generator.ClientQuestGoal;
import cupcake.nikky.imperium.tower.quest.requirement.QuestRequirement;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.Wada;
import ru.cristalix.wada.util.Color;

import java.util.*;

@Getter
@RequiredArgsConstructor
public abstract class ImperiumQuest implements Quest {

    private final int id;
    private final NPCs storageNpc, passQuestNpc;
    private final String title;
    private final List<String> description;
    private final boolean daily;
    private final List<QuestReward> questRewards = new ArrayList<>();
    private final List<QuestRequirement> requirements = new ArrayList<>();
    @Setter private String receiveRequirementText;

    private final ClientQuestGoal clientQuestGoal = new ClientQuestGoalGenerator(this);

    @Setter private UUID owner;
    @Setter @Getter private boolean visibleQuestId = true;

    protected void addQuestReward(@NotNull final Collection<QuestReward> questRewards) {
        this.questRewards.addAll(questRewards);
    }

    protected void addQuestRequirement(@NotNull final List<QuestRequirement> requirements) {
        this.requirements.addAll(requirements);
    }

    protected void addQuestRequirement(@NotNull final Collection<QuestRequirement> requirements) {
        this.requirements.addAll(requirements);
    }

    @Override
    public boolean tryGiveReward(@NotNull final Player player) {
        if (!isCompleted() || questRewards.isEmpty() || getQuestRewards().stream().anyMatch(questReward -> !questReward.canGive(player))) return false;
        getQuestRewards().forEach(questReward -> questReward.onReward(player));

        return true;
    }

    @Override
    public boolean canReceive(@NotNull final Player player) {
        val questSettings = player.<User>getBungeePlayer().findQuestSettings();
        // for linear quests; for others this method will be overridden
        return getId() == 1 | questSettings.isPassed(getId() - 1) && !questSettings.isPassed(getId()) && !questSettings.findInProgress(getId()).isPresent();
    }

    @Override
    public void onAccept(@NotNull final User user) {
        val questSettings = user.findQuestSettings();
        questSettings.applyNewQuest(this.getId());
        Fantasy.bukkitProvider().sound(user.getSpigot(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP);

        val player = user.getSpigot();

        findRequirementsByClass(BlockInteractRequirement.class).stream().filter(blockInteractRequirement -> !blockInteractRequirement.isDone()).forEach(requirement -> {
            requirement.getGoals().keySet().forEach(location -> Wada.get().getBlocksOutlineModule().show(location, Color.GREEN, player));
        });

        if (getId() == 2 && user.getLevel() == 1) {
            Fantasy.clientProvider().notification(user.getSpigot(), "&aВы можете открыть задания", "&aпрямо из своего инвентаря!", "&7Нажмите &6[QUESTS]&7, чтобы открыть меню");
        }
    }

    @Override
    public void onDecline(@NotNull final User user) {
        val questSettings = user.findQuestSettings();
        questSettings.findInProgress(this.getId()).ifPresent(quest -> {
            questSettings.invalidateQuestInProgress(quest.getId());
            Fantasy.bukkitProvider().sound(user.getSpigot(), Sound.BLOCK_NOTE_BASS);
        });
    }

    @Override
    public void onPassQuest(Player player) {
        Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
        val questSettings = player.<User>getBungeePlayer().findQuestSettings();

        questSettings.passQuest(getId());
    }

    @Override
    public boolean isCompleted() {
        return getRequirements().stream().allMatch(QuestRequirement::isDone);
    }

    @Override
    public void dropQuest(@NotNull final User user) {
        this.onDecline(user);
    }

    public String getTitle() {
        return title + (isDaily() ? " (*)" : "");
    }
}
