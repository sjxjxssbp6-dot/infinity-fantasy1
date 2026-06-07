package cupcake.nikky.imperium.tower.tree.builder.position.pattern;

import cupcake.nikky.imperium.tower.tree.builder.position.TreePositionBuilder;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.V3;

public abstract class SegmentPattern {

    public abstract TreePositionBuilder apply(@NotNull final TreePositionBuilder mainBuilder, @NotNull final V3 offset, final int[] id);

}
