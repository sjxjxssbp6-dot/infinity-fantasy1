package cupcake.nikky.imperium.tower.item.exact.modification.drop;

import cupcake.nikky.imperium.tower.item.exact.modification.ModificationItem;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public enum ModificationDropApplier {

    CATALYST(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> totalSize + (data.getLevel() - level) * 2);
        item.getLootSettings().dropEntityLevel(level);
        item.getLootSettings().maxAmountHandler(data -> 1);
    }),
    STAR_OF_CORRUPT(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> 7 * totalSize + Math.max(data.getLevel() - level, 0) * 4);
        item.getLootSettings().dropEntityLevel(1);
        item.getLootSettings().maxAmountHandler(data -> data.getLevel() >= level ? 2 : 1);
    }),
    MIXED_REAGENT_VERGE(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> 5 * totalSize + Math.max(data.getLevel() - level, 0));
        item.getLootSettings().dropEntityLevel(2);
        item.getLootSettings().maxAmountHandler(data -> data.getLevel() >= level ? 2 : 1);
    }),
    EPIC_REAGENT_VERGE(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> 5 * totalSize + Math.max(data.getLevel() - level, 0) * 2);
        item.getLootSettings().dropEntityLevel(12);
        item.getLootSettings().maxAmountHandler(data -> data.getLevel() >= level ? 2 : 1);
    }),
    ORB_OF_HEPHAESTUS(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> 3 * totalSize + Math.max(data.getLevel() - level, 0) * 2);
        item.getLootSettings().dropEntityLevel(12);
        item.getLootSettings().maxAmountHandler(data -> 1);
    }),
    ORB_OF_PANDORA(item -> item.getLootSettings().dropEntityLevel(LevelStorage.getInstance().getStartShardEdgeLevel())),
    EXALTED_ORB(item -> item.getLootSettings().dropEntityLevel(LevelStorage.getInstance().getStartShardEdgeLevel())),
    ORB_OF_DISTORTION(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> 2 * totalSize + Math.max(data.getLevel() - level, 0) * 2);
        item.getLootSettings().dropEntityLevel(35);
        item.getLootSettings().maxAmountHandler(data -> 1);
    }),
    ABSOLUTE_DUST(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> 2 * totalSize + Math.max(data.getLevel() - level, 0) * 2);
        item.getLootSettings().dropEntityLevel(35);
        item.getLootSettings().maxAmountHandler(data -> 1);
    }),
    STONE_OF_UNIQUE(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> totalSize + Math.max(data.getLevel() - level, 0) * 2);
        item.getLootSettings().dropEntityLevel(18);
        item.getLootSettings().maxAmountHandler(data -> 1);
    }),
    FORM_OF_TYCHE(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> 2 * totalSize + Math.max(data.getLevel() - level, 0));
        item.getLootSettings().dropEntityLevel(35);
        item.getLootSettings().maxAmountHandler(data -> 1);
    }),
    CLEANSING_TEAR(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> 5 * totalSize + Math.max(data.getLevel() - level, 0));
        item.getLootSettings().dropEntityLevel(16);
        item.getLootSettings().maxAmountHandler(data -> 1);
    }),
    ESSENCE_OF_TYCHE(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> totalSize / 2 + Math.max(data.getLevel() - level, 0));
        item.getLootSettings().dropEntityLevel(35);
        item.getLootSettings().maxAmountHandler(data -> 1);
    }),
    SHARD_OF_ABYSS(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> totalSize / 4 + Math.max(data.getLevel() - level, 0));
        item.getLootSettings().dropEntityLevel(level);
        item.getLootSettings().maxAmountHandler(data -> 1);
    }),
    DROP_OF_EDGE(item -> {
        var level = LevelStorage.getInstance().getStartShardEdgeLevel();
        item.getLootSettings().weightApplier(data -> totalSize -> totalSize / 8 + Math.max(data.getLevel() - level, 0));
        item.getLootSettings().dropEntityLevel(level);
        item.getLootSettings().maxAmountHandler(data -> 1);
    });

    @Getter
    private final Consumer<ModificationItem> initHandler;

}
