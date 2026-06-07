package cupcake.nikky.imperium.tower.user.settings.exact;

import com.google.common.reflect.TypeToken;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import cupcake.nikky.imperium.tower.quest.Quest;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import lombok.val;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

public class QuestSettings extends SavableUserSettings {

    private final Set<Integer> passedQuests = new CopyOnWriteArraySet<>();
    private final List<Quest> inProgressQuests = new CopyOnWriteArrayList<>();

    public QuestSettings(User user) {
        super(user);
    }

    public List<Integer> getPassedQuests() {
        return new ArrayList<>(passedQuests);
    }

    public void invalidatePassedQuest(final Integer id) {
        passedQuests.remove(id);
    }

    public boolean isPassed(final int id) {
        return passedQuests.contains(id);
    }

    public Optional<Quest> findInProgress(final int id) {
        return inProgressQuests.stream().filter(quest -> quest.getId() == id).findAny();
    }

    public List<Quest> findAllInProgressQuests() {
        return new ArrayList<>(inProgressQuests);
    }

    public List<Integer> findAllPassedAndInProgressQuestIDs() {
        val list = inProgressQuests.stream().map(Quest::getId).collect(Collectors.toList());
        list.addAll(passedQuests);
        return list;
    }

    public void applyNewQuest(final int questId) {
        this.applyNewQuest(questId, true);
    }

    public void applyNewQuest(final int questId, final boolean withRegistryPacket) {
        if(isPassed(questId) || findInProgress(questId).isPresent()) return;

        Fantasy.questService().findQuest(questId).ifPresent(quest -> {
            val currentQuest = quest.applyTo(getUser().getUniqueId());
            this.inProgressQuests.add(currentQuest);

            if (withRegistryPacket) quest.createRegisterPacket(Bukkit.getPlayer(getUser().getUniqueId()));
        });
    }

    public void passQuest(final int questId) {
        if(isPassed(questId)) return;

        this.findInProgress(questId).ifPresent(quest -> {
            this.inProgressQuests.remove(quest);
            this.passedQuests.add(questId);

            quest.createUnregisterPacket(Bukkit.getPlayer(getUser().getUniqueId()));
        });
    }

    public void invalidateQuestInProgress(final int questId) {
        this.findInProgress(questId).ifPresent(quest -> {
            this.inProgressQuests.remove(quest);
            quest.createUnregisterPacket(Bukkit.getPlayer(getUser().getUniqueId()));
        });
    }

    public String serializePassed() {
        return Fantasy.gson().toJson(passedQuests.stream().mapToInt(i -> i).toArray());
    }

    public String serializeInProgress() {
        val tokenQuest = new TypeToken<Map<String, String>>(){}.getType();
        val tokenPlayer = new TypeToken<List<String>>(){}.getType();

        final List<String> quests = new ArrayList<>();

        findAllInProgressQuests().forEach(quest -> {
            final Map<String, String> data = new HashMap<>();

            data.put("id", String.valueOf(quest.getId()));
            data.put("serialized", quest.serialize());

            quests.add(Fantasy.gson().toJson(data, tokenQuest));
        });

        return Fantasy.gson().toJson(quests, tokenPlayer);
    }

    public void deserialize(@Nullable final String passed, @Nullable final String inProgress) {
        if (passed != null && !passed.trim().isEmpty()) {
            int[] data = Fantasy.gson().fromJson(passed, int[].class);

            for (int datum : data)
                this.passedQuests.add(datum);
        }

        if (inProgress != null && !inProgress.trim().isEmpty()) {
            val tokenQuest = new TypeToken<Map<String, String>>(){}.getType();
            val tokenPlayer = new TypeToken<List<String>>(){}.getType();

            final List<String> quests = Fantasy.gson().fromJson(inProgress, tokenPlayer);

            quests.forEach(serializedQuest -> {
                final Map<String, String> questData = Fantasy.gson().fromJson(serializedQuest, tokenQuest);
                val questId = NumberUtility.parseInteger(questData.get("id"));
                val serializedData = questData.get("serialized");

                this.applyNewQuest(questId);
                this.findInProgress(questId).ifPresent(quest -> quest.deserialize(serializedData));
            });
        }
    }

    @Override
    public List<Bson> createUpdates() {
        val dailyQuests = this.getPassedQuests().stream()
                .map(id -> Fantasy.questService().findQuest(id).orElse(null))
                .filter(quest -> quest != null && quest.isDaily())
                .map(Quest::getId)
                .toList();

        return Arrays.asList(Updates.set("questPassData", this.serializePassed()),
                Updates.set("questProgressData", this.serializeInProgress()),
                Updates.set("dailyQuests", dailyQuests));
    }

    @Override
    public void readDocument(Document document) {
        if (document.isEmpty()) return;

        val serializedPassQuests = document.get("questPassData", "");
        val serializedInProgressQuests = document.get("questProgressData", "");
        final List<Integer> dailyQuests = document.getList("dailyQuests", Integer.class, new ArrayList<>());

        this.deserialize(serializedPassQuests, serializedInProgressQuests);
        this.getPassedQuests().stream().map(id -> Fantasy.questService().findQuest(id).orElse(null)).filter(quest -> quest != null && quest.isDaily()).forEach(quest -> {
            if (!dailyQuests.contains(quest.getId())) {
                this.invalidatePassedQuest(quest.getId());
            }
        });
    }

}
