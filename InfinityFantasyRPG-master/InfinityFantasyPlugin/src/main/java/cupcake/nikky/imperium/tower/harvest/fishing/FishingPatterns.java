package cupcake.nikky.imperium.tower.harvest.fishing;

import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.exact.gear.tool.ToolItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum FishingPatterns {

    LEVEL_1(FishingPattern.of(1, ItemObject.FISH_HARVEST_1)),
    LEVEL_2(FishingPattern.of(2, ItemObject.FISH_HARVEST_2)),
    LEVEL_3(FishingPattern.of(3, ItemObject.FISH_HARVEST_3)),
    LEVEL_4(FishingPattern.of(4, ItemObject.FISH_HARVEST_4)),
    LEVEL_5(FishingPattern.of(6, ItemObject.FISH_HARVEST_5));

    private final FishingPattern pattern;

    public static List<FishingPattern> findPattern(@NotNull final ToolItem fishingRodItem) {
        return Arrays.stream(values())
                .map(FishingPatterns::getPattern)
                .filter(pattern -> pattern.getToolLevelRequire() == fishingRodItem.getGeneralSettings().itemLevel() || pattern.getToolLevelRequire() == fishingRodItem.getGeneralSettings().itemLevel() - 1)
                .collect(Collectors.toList());
    }

}
