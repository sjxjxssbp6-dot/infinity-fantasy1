package cupcake.nikky.imperium.tower.data.quest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuestListStorage {

    @Getter
    private static final QuestListStorage instance = new QuestListStorage();

    private final Map<Integer, QuestListData> quests = new HashMap<>();
    @Setter @Getter
    private boolean updated;

    public void addQuest(final int id, @NotNull final QuestListData quest) {
        this.quests.put(id, quest);
    }

    public Map<Integer, QuestListData> getQuests() {
        return new HashMap<>(quests);
    }

    public void clear() {
        this.quests.clear();
    }

}
