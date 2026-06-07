package cupcake.nikky.imperium.tower.tree;

import cupcake.nikky.imperium.tower.tree.builder.position.Segment;
import cupcake.nikky.imperium.tower.tree.builder.position.SegmentDirection;
import cupcake.nikky.imperium.tower.tree.builder.position.TreePositionBuilder;
import cupcake.nikky.imperium.tower.tree.builder.position.pattern.SegmentPatterns;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import ru.cristalix.wada.util.V3;

public class TreeCharacterPositionGenerator {

    public Int2ObjectMap<Segment> generate() {
        return TreePositionBuilder.of(SegmentDirection.RIGHT.getVector())
                .next(0, new V3(0, 0))
                .next(SegmentPatterns.BRANCH_3_TWISTED_DOWN_RIGHT, new V3(0, 100), 2, 3, 4)
                .resetPoint(0)
                .next(SegmentPatterns.BRANCH_3_TWISTED_UP_RIGHT, new V3(0, -100), 6, 7, 8)
                .resetPoint(0)
                .next(9, new V3(400, 0))
                .createLink(8, 9)
                .createLink(4, 9)
                .next(24, 25, 26, 27, 28)

                .resetPoint(24)
                .next(46, new V3(-300, -300))
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_DOWN_RIGHT_2, 10, 11, 12)
                .resetPoint(24)
                .next(47, new V3(-300, 300))
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_DOWN_RIGHT_1, 17, 18, 19)

                .resetPoint(26)
                .next(1, new V3(100, 100))
                .resetPoint(26)
                .next(5, new V3(100, -100))
                .resetPoint(28)

                .next(SegmentPatterns.BRANCH_7_CIRCLE_RIGHT, 29, 30, 31, 32, 33, 34, 35)

                .subSegment(SegmentDirection.DOWN.getVector().multiply(2))
                    .next(36, 37, 38)

                    .nextWithReset(SegmentPatterns.BRANCH_7_CIRCLE_RIGHT, 49, 49, 50, 51, 52, 53, 54, 55)

                    .next(61, new V3(300, 0))
                    .resetPoint(38)
                    .next(56, 57, 58, 59, 60)
                    .resetPoint(57)

                    .next(69, new V3(100, 0))
                    .next(SegmentPatterns.BRANCH_3_TWISTED_UP_RIGHT, new V3(0, -50), 70, 71, 72)
                    .next(SegmentPatterns.BRANCH_3_TWISTED_DOWN_RIGHT, new V3(0, 50), 73, 74, 75)
                    .resetPoint(56)
                    .next(76, new V3(-100, 0))
                    .next(SegmentPatterns.BRANCH_3_TWISTED_UP_LEFT, new V3(0, -50), 77, 78, 79)
                    .next(SegmentPatterns.BRANCH_3_TWISTED_DOWN_LEFT, new V3(0, 50), 80, 81, 82)
                    .resetPoint(58)
                    .next(83, new V3(-100, 0))
                    .next(SegmentPatterns.BRANCH_3_TWISTED_UP_LEFT, new V3(0, -50), 84, 85, 86)
                    .next(SegmentPatterns.BRANCH_3_TWISTED_DOWN_LEFT, new V3(0, 50), 87, 88, 89)
                    .resetPoint(60)
                    //.next(90, 91, 92)
                    .next(SegmentPatterns.BRANCH_3_TWISTED_DOWN_LEFT, 62, 63, 64)

                    .subSegment(SegmentDirection.RIGHT.getVector().multiply(2))
                        .next(93, 94, 95, 65, 66).resetPoint(93)
                        .nextWithReset(SegmentPatterns.BRANCH_7_CIRCLE_DOWN, 95, 96, 97, 98, 99, 100, 101, 102)
                        .resetPoint(95)
                        .next(300, new V3(0, -100))
                        .next(SegmentPatterns.BRANCH_3_TWISTED_LEFT_UP, new V3(-50, 0), 301, 302, 303)
                        .next(SegmentPatterns.BRANCH_3_TWISTED_RIGHT_UP, new V3(50, 0), 304, 305, 306)
                        .resetPoint(65)
                        .nextWithReset(SegmentPatterns.BRANCH_7_CIRCLE_DOWN, 66, 106, 107, 108, 109, 110, 111, 112)
                        .next(113, new V3(200, -200))
                        .next(SegmentPatterns.BRANCH_3_OBLIQUELY_DOWN_RIGHT_1, 114, 115, 116)
                        .next(SegmentPatterns.BRANCH_3_OBLIQUELY_UP_RIGHT_1, 117, 118, 119)
                        .resetPoint(66)
                        .next(120, new V3(200, 200))
                        .next(SegmentPatterns.BRANCH_3_OBLIQUELY_DOWN_RIGHT_2, 121, 122, 123)
                        .next(SegmentPatterns.BRANCH_3_OBLIQUELY_UP_RIGHT_2, 124, 125, 126)
                    .applySegment()
                .applySegment()

                .createLink(58, 94)

                .subSegment(SegmentDirection.UP.getVector().multiply(2))
                    .next(127, 128, 129)
                    //.next(137, 138, 139)
                    .resetPoint(129)

                    .nextWithReset(SegmentPatterns.BRANCH_7_CIRCLE_RIGHT, 140, 140, 141, 142, 143, 144, 145, 146)

                    .next(152, new V3(300, 0)).resetPoint(129)
                    .next(147, 148, 149, 150, 151)
                    .resetPoint(149)

                    .next(153, new V3(100, 0))
                    .next(SegmentPatterns.BRANCH_3_TWISTED_UP_RIGHT, new V3(0, -50), 154, 155, 156)
                    .next(SegmentPatterns.BRANCH_3_TWISTED_DOWN_RIGHT, new V3(0, 50), 157, 158, 159)
                    .resetPoint(147)
                    .next(160, new V3(-100, 0))
                    .next(SegmentPatterns.BRANCH_3_TWISTED_UP_LEFT, new V3(0, -50), 161, 162, 163)
                    .next(SegmentPatterns.BRANCH_3_TWISTED_DOWN_LEFT, new V3(0, 50), 164, 165, 166)
                    .resetPoint(149)
                    .next(174, new V3(-100, 0))
                    .next(SegmentPatterns.BRANCH_3_TWISTED_UP_LEFT, new V3(0, -50), 175, 176, 177)
                    .next(SegmentPatterns.BRANCH_3_TWISTED_DOWN_LEFT, new V3(0, 50), 178, 179, 180)
                    .resetPoint(151)
                    .next(SegmentPatterns.BRANCH_3_TWISTED_UP_LEFT, 167, 168, 169)

                    .subSegment(SegmentDirection.RIGHT.getVector().multiply(2))
                        .next(184, 185, 186, 170, 171).resetPoint(184)
                        .nextWithReset(SegmentPatterns.BRANCH_7_CIRCLE_UP, 170, 187, 188, 189, 190, 191, 192, 193)
                        .nextWithReset(SegmentPatterns.BRANCH_7_CIRCLE_UP, 186, 197, 198, 199, 200, 201, 202, 203)
                        .resetPoint(186)
                        .next(307, new V3(0, 100))
                        .nextWithReset(SegmentPatterns.BRANCH_3_TWISTED_LEFT_DOWN, new V3(-50, 0), 307, 308, 309, 310)
                        .next(SegmentPatterns.BRANCH_3_TWISTED_RIGHT_DOWN, new V3(50, 0), 311, 312, 313)
                        .resetPoint(171)
                        .next(204, new V3(200, -200))
                        .next(SegmentPatterns.BRANCH_3_OBLIQUELY_DOWN_RIGHT_1, 205, 206, 207)
                        .next(SegmentPatterns.BRANCH_3_OBLIQUELY_UP_RIGHT_1, 208, 209, 210)
                        .resetPoint(171)
                        .next(211, new V3(200, 200))
                        .next(SegmentPatterns.BRANCH_3_OBLIQUELY_DOWN_RIGHT_2, 212, 213, 214)
                        .next(SegmentPatterns.BRANCH_3_OBLIQUELY_UP_RIGHT_2, 215, 216, 217)
                    .applySegment()
                .applySegment()

                .createLink(149, 185)

                .resetPoint(29)
                .next(218, new V3(900, 0))
                .next(219, new V3(0, 400))
                .resetPoint(218)
                .next(220, new V3(0, -400))
                .nextWithReset(SegmentPatterns.BRANCH_3_TWISTED_LEFT_UP, new V3(-150, -400), 219, 221, 222, 223)
                .nextWithReset(SegmentPatterns.BRANCH_3_TWISTED_LEFT_DOWN, new V3(-150, 400), 220, 224, 225, 226)
                .nextWithReset(SegmentPatterns.BRANCH_7_CIRCLE_RIGHT, 219, 227, 228, 229, 230, 231, 232, 233)
                .next(SegmentPatterns.BRANCH_7_CIRCLE_RIGHT, 234, 235, 236, 237, 238, 239, 240)
                .createLink(171, 223).createLink(66, 226)
                .createLink(220, 171).createLink(219, 66)

                .resetPoint(218)
                .next(241, new V3(-200, -200))
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_DOWN_RIGHT_2, 242, 243, 244)
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_UP_RIGHT_2, 245, 246, 247)
                .resetPoint(218)
                .next(248, new V3(-200, 200))
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_DOWN_RIGHT_1, 249, 250, 251)
                .next(SegmentPatterns.BRANCH_3_OBLIQUELY_UP_RIGHT_1, 252, 253, 254)

                .resetPoint(218)
                .next(SegmentPatterns.BRANCH_7_CIRCLE_RIGHT, new V3(300, 0), 255, 256, 257, 258, 259, 260, 261)

//                .resetPoint(186)
//                .next(262, new V3(0, 100))

                .createLink(5, 128)
                .createLink(1, 37)
                .createLink(61, 248)
                .createLink(152, 241)

                .generate();
    }

}
