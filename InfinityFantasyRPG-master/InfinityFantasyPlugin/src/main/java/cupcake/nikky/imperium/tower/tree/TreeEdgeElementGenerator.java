package cupcake.nikky.imperium.tower.tree;

import cupcake.nikky.imperium.tower.tree.builder.element.TreeElement;
import cupcake.nikky.imperium.tower.tree.builder.element.TreeElementBuilder;
import cupcake.nikky.imperium.tower.tree.builder.position.Segment;
import cupcake.nikky.imperium.tower.tree.element.exact.edge.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.Color;

public class TreeEdgeElementGenerator {

    public Int2ObjectMap<TreeElement> generate(@NotNull final Int2ObjectMap<Segment> segments) {
        return TreeElementBuilder.of(segments, PointType.EDGE, NodeType.SMALL_EDGE)
                .with(1000, TreeIcon.EXPERIENCE, new AddBattleExperienceMultiplier(5), new IncreaseChanceDropWayFragmentElementProcess(5))
                .with(1001, TreeIcon.EXPERIENCE, new AddBattleExperienceMultiplier(5))
                .with(1002, TreeIcon.EXPERIENCE, new AddBattleExperienceMultiplier(5))
                .with(1003, TreeIcon.EXPERIENCE, new AddBattleExperienceMultiplier(5))
                .with(1004, TreeIcon.WAY_FRAGMENT, new IncreaseChanceDropWayFragmentElementProcess(15))
                .with(1005, TreeIcon.WAY_FRAGMENT, new IncreaseChanceDropWayFragmentElementProcess(15))
                .with(1006, TreeIcon.WAY_FRAGMENT, new IncreaseChanceDropWayFragmentElementProcess(15))

                .with(1007, TreeIcon.EDGE_CORE, new IncreaseDurationEdgeCoreElementProcess(15))
                .with(1008, TreeIcon.EDGE_CORE, new IncreaseDurationEdgeCoreElementProcess(15))
                .with(1009, TreeIcon.WAY_FRAGMENT, NodeType.BIG_EDGE, new AddToMaximumFragmentEventCountElementProcess(1))

                .with(1010, TreeIcon.EDGE_CORE, new IncreaseAmplifierEdgeCoreElementProcess(15))
                .with(1011, TreeIcon.EDGE_CORE, new IncreaseAmplifierEdgeCoreElementProcess(15))
                .with(1012, TreeIcon.WAY_FRAGMENT, NodeType.BIG_EDGE, new AddToMaximumFragmentEventCountElementProcess(1))

                .with(1013, TreeIcon.EDGE_CORE, new IncreaseAmplifierEdgeCoreElementProcess(5), new IncreaseChanceEdgeCoreElementProcess(5))
                .with(1014, TreeIcon.EDGE_CORE, new IncreaseChanceEdgeCoreElementProcess(10))
                .with(1015, TreeIcon.EDGE_CORE, new IncreaseChanceEdgeCoreElementProcess(10))
                .with(1016, TreeIcon.EDGE_CORE, NodeType.BIG_EDGE, new IncreaseChanceEdgeCoreElementProcess(10))
                .with(1017, TreeIcon.EDGE_CORE, new IncreaseAmplifierEdgeCoreElementProcess(10))
                .with(1018, TreeIcon.EDGE_CORE, new IncreaseAmplifierEdgeCoreElementProcess(10))
                .with(1019, TreeIcon.EDGE_CORE, NodeType.BIG_EDGE, new IncreaseAmplifierEdgeCoreElementProcess(10))

                .with(1020, TreeIcon.EXPERIENCE, new AddBattleExperienceMultiplier(10))
                .with(1021, TreeIcon.DURATION, new IncreaseFragmentDurationElementProcess(6))
                .with(1022, TreeIcon.DURATION, new IncreaseFragmentDurationElementProcess(6))
                .with(1023, TreeIcon.DURATION, NodeType.BIG_EDGE, new IncreaseFragmentDurationElementProcess(6))
                .with(1024, TreeIcon.INCUBATOR, new IncreaseDropChanceIncubatorElementProcess(10))
                .with(1025, TreeIcon.INCUBATOR, new IncreaseDropChanceIncubatorElementProcess(10))
                .with(1026, TreeIcon.INCUBATOR, NodeType.BIG_EDGE, new IncreaseDropChanceIncubatorElementProcess(10))

                .with(1027, TreeIcon.EXPERIENCE, new AddBattleExperienceMultiplier(10))
                .with(1028, TreeIcon.DURATION, new IncreaseFragmentDurationElementProcess(6))
                .with(1029, TreeIcon.DURATION, new IncreaseFragmentDurationElementProcess(6))
                .with(1030, TreeIcon.DURATION, NodeType.BIG_EDGE, new IncreaseFragmentDurationElementProcess(6))
                .with(1031, TreeIcon.EXPERIENCE, new AddBattleExperienceMultiplier(10))
                .with(1032, TreeIcon.EXPERIENCE, new AddBattleExperienceMultiplier(10))
                .with(1033, TreeIcon.EXPERIENCE, NodeType.BIG_EDGE, new AddBattleExperienceMultiplier(10))

                .with(1034, TreeIcon.MASK, new IncreaseChanceDropDoubleMaskElementProcess(25), new IncreaseMaskEntitySpawnChanceElementProcess(10))
                .with(1035, TreeIcon.MASK, new IncreaseMaskEntitySpawnChanceElementProcess(20))
                .with(1036, TreeIcon.MASK, new IncreaseMaskEntitySpawnChanceElementProcess(20))
                .with(1037, TreeIcon.MASK, NodeType.BIG_EDGE, new IncreaseMaskEntitySpawnChanceElementProcess(20))
                .with(1038, TreeIcon.MASK, new IncreaseChanceDropMaskElementProcess(10))
                .with(1039, TreeIcon.MASK, new IncreaseChanceDropMaskElementProcess(10))
                .with(1040, TreeIcon.MASK, NodeType.BIG_EDGE, new IncreaseChanceDropMaskElementProcess(10))

                .with(1041, TreeIcon.MASK, new AddMaskCountAndEntityLevelElementProcess(1))
                .with(1042, TreeIcon.MASK, new IncreaseChanceDropDoubleMaskElementProcess(5))
                .with(1043, TreeIcon.MASK, new IncreaseChanceDropDoubleMaskElementProcess(5))
                .with(1044, TreeIcon.MASK, NodeType.BIG_EDGE, new IncreaseChanceDropDoubleMaskElementProcess(5))
                .with(1045, TreeIcon.MASK, new IncreaseMaskEntitySpawnChanceElementProcess(20))
                .with(1046, TreeIcon.MASK, new IncreaseMaskEntitySpawnChanceElementProcess(20))
                .with(1047, TreeIcon.MASK, NodeType.BIG_EDGE, new IncreaseMaskEntitySpawnChanceElementProcess(20))

                .with(1048, TreeIcon.MASK, new AddMaskCountAndEntityLevelElementProcess(1))
                .with(1049, TreeIcon.MASK, new IncreaseChanceDropDoubleMaskElementProcess(5))
                .with(1050, TreeIcon.MASK, new IncreaseChanceDropDoubleMaskElementProcess(5))
                .with(1051, TreeIcon.MASK, NodeType.BIG_EDGE, new IncreaseChanceDropDoubleMaskElementProcess(5))
                .with(1052, TreeIcon.MASK, new IncreaseMaskEntitySpawnChanceElementProcess(20))
                .with(1053, TreeIcon.MASK, new IncreaseMaskEntitySpawnChanceElementProcess(20))
                .with(1054, TreeIcon.MASK, NodeType.BIG_EDGE, new IncreaseMaskEntitySpawnChanceElementProcess(20))

                .with(1055, TreeIcon.PANDORA, new IncreaseSpawnChancePandoraElementProcess(25))
                .with(1056, TreeIcon.PANDORA, new IncreaseSpawnChancePandoraElementProcess(35))
                .with(1057, TreeIcon.PANDORA, new IncreaseSpawnChancePandoraElementProcess(35))
                .with(1058, TreeIcon.PANDORA, NodeType.BIG_EDGE, new IncreaseSpawnChancePandoraElementProcess(35))
                .with(1059, TreeIcon.PANDORA, new IncreaseBaseItemsAmountInPandoraElementProcess(100))
                .with(1060, TreeIcon.PANDORA, new IncreaseBaseItemsAmountInPandoraElementProcess(100))
                .with(1061, TreeIcon.PANDORA, NodeType.BIG_EDGE, new IncreaseBaseItemsAmountInPandoraElementProcess(100))
                .with(1062, TreeIcon.PANDORA, new DecreaseBasePricePandoraElementProcess(10))
                .with(1063, TreeIcon.PANDORA, NodeType.BIG_EDGE, new RemoveEquipPandoraElementProcess())
                .with(1064, TreeIcon.PANDORA, NodeType.BIG_EDGE, new DoubleEquipPandoraElementProcess())

                .build();
    }

}
