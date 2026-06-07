package cupcake.nikky.imperium.tower.data.quest;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuestStorage {

    @Getter
    private static final QuestStorage instance = new QuestStorage();

    private final List<QuestData> quests = new ArrayList<>();

    public void registerQuest(@NotNull final QuestData data) {
        this.quests.add(data);
    }

    public void unregisterQuest(final int id) {
        val quest = this.findById(id);
        this.quests.remove(quest);
    }

    public QuestData findById(final int id) {
        for (QuestData datum : quests) {
            if (datum.getId() == id) return datum;
        }

        return null;
    }

    public List<QuestData> getQuests() {
        return new ArrayList<>(quests);
    }

    public void clear() {
        this.quests.clear();
    }

}
