package cupcake.nikky.imperium.tower.data.quest;

import cupcake.nikky.imperium.tower.client.protocol.QuestState;
import dev.xdark.clientapi.item.ItemStack;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QuestContextStorage {

    @Getter
    private static final QuestContextStorage instance = new QuestContextStorage();

    private String title = "";
    private String description = "";
    private String goals = "";
    private int id = -1;
    private List<ItemStack> reward = new ArrayList<>();
    private QuestState questState = QuestState.NOT_RECEIVE;

    private int enabled;
    private int canReceive;

    public List<ItemStack> getReward() {
        return new ArrayList<>(reward);
    }

    public void addReward(@NotNull final ItemStack itemStack) {
        this.reward.add(itemStack);
    }

    public void clear() {
        this.reward.clear();
    }

}
