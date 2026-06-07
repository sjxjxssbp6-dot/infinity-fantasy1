package cupcake.nikky.imperium.tower.tree;

import cupcake.nikky.imperium.tower.tree.builder.position.Segment;
import cupcake.nikky.imperium.tower.tree.builder.position.SegmentDirection;
import cupcake.nikky.imperium.tower.tree.builder.position.TreePositionBuilder;
import cupcake.nikky.imperium.tower.tree.builder.position.pattern.SegmentPatterns;
import ru.cristalix.wada.util.V3;

import java.util.Map;

public class TreeEdgePositionGenerator {

    public Map<Integer, Segment> generate() {
        return TreePositionBuilder.of(SegmentDirection.LEFT.getVector())
                .nextWithReset(SegmentPatterns.BRANCH_7_CIRCLE_LEFT, 1003, 1000, 1001, 1002, 1003, 1004, 1005, 1006)
                .next(SegmentPatterns.BRANCH_3_TWISTED_UP_LEFT, 1007, 1008, 1009)
                .resetPoint(1006)
                .next(SegmentPatterns.BRANCH_3_TWISTED_DOWN_LEFT, 1010, 1011, 1012)
                .createLink(1011, 1008)

                .resetPoint(1008)
                .nextWithReset(SegmentPatterns.BRANCH_7_CIRCLE_DOWN, 1016, 1013, 1014, 1015, 1016, 1017, 1018, 1019)
                .next(1020, new V3(200, 200))
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_UP_RIGHT_2, 1021, 1022, 1023)
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_DOWN_RIGHT_2, 1024, 1025, 1026)
                .resetPoint(1019)
                .next(1027, new V3(-200, 200))
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_UP_RIGHT_1, 1028, 1029, 1030)
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_DOWN_RIGHT_1, 1031, 1032, 1033)
                .createLink(1016, 1019)

                .resetPoint(1011)
                .nextWithReset(SegmentPatterns.BRANCH_7_CIRCLE_UP, 1037, 1034, 1035, 1036, 1037, 1038, 1039, 1040)
                .next(1041, new V3(-200, -200))
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_UP_RIGHT_2, 1042, 1043, 1044)
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_DOWN_RIGHT_2, 1045, 1046, 1047)
                .resetPoint(1040)
                .next(1048, new V3(200, -200))
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_UP_RIGHT_1, 1049, 1050, 1051)
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_DOWN_RIGHT_1, 1052, 1053, 1054)
                .createLink(1037, 1040)

                .resetPoint(1008)
                .nextWithReset(SegmentPatterns.BRANCH_7_CIRCLE_LEFT, new V3(-200, -120), 1055, 1055, 1056, 1057, 1058, 1059, 1060, 1061)
                .createLink(1055, 1011).createLink(1055, 1008)
                .resetPoint(1055)
                .next(1062, new V3(-300, 0))
                .next(1063, new V3(-100, 100)).resetPoint(1062)
                .next(1064, new V3(-100, -100))

                .generate();
    }

}
