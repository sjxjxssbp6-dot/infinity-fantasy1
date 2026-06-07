package cupcake.nikky.imperium.tower.quest.service;

import cupcake.nikky.imperium.tower.quest.Quest;
import cupcake.nikky.imperium.tower.quest.reward.TreePointReward;
import cupcake.nikky.imperium.tower.tree.PointType;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class ImperiumQuestService {

    private final Int2ObjectMap<Quest> allQuests = new Int2ObjectOpenHashMap<>();

    private final Map<PointType, List<Quest>> pointQuests = Map.of(
            PointType.CHARACTER, new ArrayList<>(),
            PointType.EDGE, new ArrayList<>()
    );

    public Collection<Quest> findAllQuests() {
        return new ArrayList<>(allQuests.values());
    }

    public Collection<Quest> findPointQuests(@NotNull final PointType pointType) {
        return new ArrayList<>(pointQuests.get(pointType));
    }

    public void registerNewQuest(@NotNull final Quest quest) {
        allQuests.put(quest.getId(), quest);
        quest.getQuestRewards().stream()
                .filter(TreePointReward.class::isInstance)
                .forEach(questReward -> pointQuests.get(((TreePointReward) questReward).getPointType()).add(quest));
    }

    public Optional<Quest> findQuest(final int id) {
        return Optional.ofNullable(allQuests.get(id));
    }

}
