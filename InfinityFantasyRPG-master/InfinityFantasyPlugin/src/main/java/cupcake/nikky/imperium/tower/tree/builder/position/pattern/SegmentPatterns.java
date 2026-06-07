package cupcake.nikky.imperium.tower.tree.builder.position.pattern;

import cupcake.nikky.imperium.tower.tree.builder.position.SegmentDirection;
import cupcake.nikky.imperium.tower.tree.builder.position.TreePositionBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.V3;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SegmentPatterns {

    public static SegmentPattern BRANCH_5_UP_LEFT = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.UP.getVector())
                    .next(id[0], new V3(0, -100).add(offset))
                    .next(id[1], new V3(0, -100))
                    .next(id[2], new V3(-60, -70))
                    .next(id[3], new V3(-60, 80))
                    .next(id[4], new V3(0, 100))
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_5_DOWN_LEFT = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.DOWN.getVector())
                    .next(id[0], new V3(0, 100).add(offset))
                    .next(id[1], new V3(0, 100))
                    .next(id[2], new V3(-60, 70))
                    .next(id[3], new V3(-60, -80))
                    .next(id[4], new V3(0, -100))
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_5_LEFT_UP = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.LEFT.getVector())
                    .next(id[0], new V3(-100, 0).add(offset))
                    .next(id[1], new V3(-100, 0))
                    .next(id[2], new V3(-80, 50))
                    .next(id[3], new V3(80, 60))
                    .next(id[4], new V3(100, 0))
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_5_RIGHT_UP = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.RIGHT.getVector())
                    .next(id[0], new V3(100, 0).add(offset))
                    .next(id[1], new V3(100, 0))
                    .next(id[2], new V3(80, 50))
                    .next(id[3], new V3(-80, 60))
                    .next(id[4], new V3(-100, 0))
                    .applySegment();
        }
    };

    public static SegmentPattern BRANCH_3_OBLIQUELY_DOWN_RIGHT_1 = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.DOWN.getVector())
                    .next(id[0], new V3(0, 100).add(offset))
                    .next(id[1], new V3(80, -20))
                    .next(id[2], new V3(20, -80))
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_3_OBLIQUELY_UP_RIGHT_1 = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.RIGHT.getVector())
                    .next(id[0], new V3(-100, 0).add(offset))
                    .next(id[1], new V3(20, -80))
                    .next(id[2], new V3(80, -20))
                    .applySegment();
        }
    };

    public static SegmentPattern BRANCH_3_OBLIQUELY_DOWN_RIGHT_2 = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.DOWN.getVector())
                    .next(id[0], new V3(100, 0).add(offset))
                    .next(id[1], new V3(-20, -80))
                    .next(id[2], new V3(-80, -20))
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_3_OBLIQUELY_UP_RIGHT_2 = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.RIGHT.getVector())
                    .next(id[0], new V3(0, 100).add(offset))
                    .next(id[1], new V3(-80, -20))
                    .next(id[2], new V3(-20, -80))
                    .applySegment();
        }
    };

    public static SegmentPattern BRANCH_7_CIRCLE_DOWN = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.DOWN.getVector())
                    .next(id[0], new V3(0, 100).add(offset))
                        .subSegment(SegmentDirection.RIGHT.getVector())
                            .next(id[1], new V3(150, 100))
                            .next(id[2], new V3(30, 100))
                            .next(id[3], new V3(-30, 100))
                        .applySegment()
                        .subSegment(SegmentDirection.LEFT.getVector())
                            .next(id[4], new V3(-150, 100))
                            .next(id[5], new V3(-30, 100))
                            .next(id[6], new V3(30, 100))
                        .applySegment()
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_7_CIRCLE_UP = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.UP.getVector())
                    .next(id[0], new V3(0, -100).add(offset))
                    .subSegment(SegmentDirection.RIGHT.getVector())
                    .next(id[1], new V3(-150, -100))
                    .next(id[2], new V3(-30, -100))
                    .next(id[3], new V3(30, -100))
                    .applySegment()
                    .subSegment(SegmentDirection.LEFT.getVector())
                    .next(id[4], new V3(150, -100))
                    .next(id[5], new V3(30, -100))
                    .next(id[6], new V3(-30, -100))
                    .applySegment()
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_7_CIRCLE_LEFT = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.LEFT.getVector())
                    .next(id[0], new V3(-100, 0).add(offset))
                    .subSegment(SegmentDirection.DOWN.getVector())
                    .next(id[1], new V3(-100, 150))
                    .next(id[2], new V3(-100, 30))
                    .next(id[3], new V3(-100, -30))
                    .applySegment()
                    .subSegment(SegmentDirection.UP.getVector())
                    .next(id[4], new V3(-100, -150))
                    .next(id[5], new V3(-100, -30))
                    .next(id[6], new V3(-100, 30))
                    .applySegment()
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_7_CIRCLE_RIGHT = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.RIGHT.getVector())
                    .next(id[0], new V3(100, 0).add(offset))
                    .subSegment(SegmentDirection.UP.getVector())
                    .next(id[1], new V3(100, -150))
                    .next(id[2], new V3(100, -30))
                    .next(id[3], new V3(100, 30))
                    .applySegment()
                    .subSegment(SegmentDirection.DOWN.getVector())
                    .next(id[4], new V3(100, 150))
                    .next(id[5], new V3(100, 30))
                    .next(id[6], new V3(100, -30))
                    .applySegment()
                    .applySegment();
        }
    };

    public static SegmentPattern BRANCH_3_TWISTED_LEFT_UP = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.UP.getVector())
                    .next(id[0], SegmentDirection.UP.sum(offset))
                    .next(id[1], SegmentDirection.UP.sum(new V3(-30, 0)))
                    .next(id[2], SegmentDirection.UP.sum(new V3(30, 0)))
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_3_TWISTED_RIGHT_UP = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.UP.getVector())
                    .next(id[0], SegmentDirection.UP.sum(offset))
                    .next(id[1], SegmentDirection.UP.sum(new V3(30, 0)))
                    .next(id[2], SegmentDirection.UP.sum(new V3(-30, 0)))
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_3_TWISTED_LEFT_DOWN = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.DOWN.getVector())
                    .next(id[0], SegmentDirection.DOWN.sum(offset))
                    .next(id[1], SegmentDirection.DOWN.sum(new V3(-30, 0)))
                    .next(id[2], SegmentDirection.DOWN.sum(new V3(30, 0)))
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_3_TWISTED_RIGHT_DOWN = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.DOWN.getVector())
                    .next(id[0], SegmentDirection.DOWN.sum(offset))
                    .next(id[1], SegmentDirection.DOWN.sum(new V3(30, 0)))
                    .next(id[2], SegmentDirection.DOWN.sum(new V3(-30, 0)))
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_3_TWISTED_UP_LEFT = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.LEFT.getVector())
                    .next(id[0], SegmentDirection.LEFT.sum(offset))
                    .next(id[1], SegmentDirection.LEFT.sum(new V3(0, -30)))
                    .next(id[2], SegmentDirection.LEFT.sum(new V3(0, 30)))
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_3_TWISTED_DOWN_LEFT = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.LEFT.getVector())
                    .next(id[0], SegmentDirection.LEFT.sum(offset))
                    .next(id[1], SegmentDirection.LEFT.sum(new V3(0, 30)))
                    .next(id[2], SegmentDirection.LEFT.sum(new V3(0, -30)))
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_3_TWISTED_UP_RIGHT = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.RIGHT.getVector())
                    .next(id[0], SegmentDirection.RIGHT.sum(offset))
                    .next(id[1], SegmentDirection.RIGHT.sum(new V3(0, -30)))
                    .next(id[2], SegmentDirection.RIGHT.sum(new V3(0, 30)))
                    .applySegment();
        }
    };
    public static SegmentPattern BRANCH_3_TWISTED_DOWN_RIGHT = new SegmentPattern() {
        @Override
        public TreePositionBuilder apply(@NotNull TreePositionBuilder mainBuilder, @NotNull V3 offset, int[] id) {
            return mainBuilder.subSegment(SegmentDirection.RIGHT.getVector())
                    .next(id[0], SegmentDirection.RIGHT.sum(offset))
                    .next(id[1], SegmentDirection.RIGHT.sum(new V3(0, 30)))
                    .next(id[2], SegmentDirection.RIGHT.sum(new V3(0, -30)))
                    .applySegment();
        }
    };

}
