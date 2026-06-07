package cupcake.nikky.imperium.tower.harvest.fishing;

import cupcake.nikky.imperium.tower.harvest.HarvestPattern;
import cupcake.nikky.imperium.tower.item.ItemObject;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class FishingPattern extends HarvestPattern {

    public FishingPattern(final int toolLevelRequire, ItemObject harvestItem) {
        super(toolLevelRequire, harvestItem);
    }

    public static FishingPattern of(final int toolLevelRequire, @NotNull final ItemObject harvestItem) {
        return new FishingPattern(toolLevelRequire, harvestItem);
    }

}
