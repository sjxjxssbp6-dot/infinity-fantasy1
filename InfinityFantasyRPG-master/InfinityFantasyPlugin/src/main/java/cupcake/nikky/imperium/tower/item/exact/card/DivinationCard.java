package cupcake.nikky.imperium.tower.item.exact.card;

import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Getter
@Builder
@Accessors(fluent = true)
public class DivinationCard {

    private String id;
    private int weightMultiplier;
    private String title;
    private List<String> rewardDescription;
    @Builder.Default private Map<ItemObject, Integer> rewards = new HashMap<>();
    @Builder.Default private Supplier<Map<ItemObject, Integer>> taskFinder = HashMap::new;
    private int deckStack;
    private int entityLevelDrop;

    public DivinationCard applyReward(@NotNull final ItemObject itemObject, final int amount) {
        this.rewards.put(itemObject, amount);
        return this;
    }

    public DivinationCard taskFinder(@NotNull final Supplier<Map<ItemObject, Integer>> taskFinder) {
        this.taskFinder = taskFinder;
        return this;
    }

}
