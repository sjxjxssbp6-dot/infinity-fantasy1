package cupcake.nikky.imperium.tower.tree.builder.position;

import cupcake.nikky.imperium.tower.tree.builder.position.pattern.SegmentPattern;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.*;
import lombok.experimental.Accessors;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.V3;

@Accessors(chain = true)
@NoArgsConstructor
public class TreePositionBuilder {

    @Setter(value = AccessLevel.PACKAGE)
    private V3 mainDirection;
    @Getter(value = AccessLevel.PACKAGE)
    private final Int2ObjectMap<Segment> positions = new Int2ObjectOpenHashMap<>();
    @Setter(value = AccessLevel.PACKAGE)
    private Segment lastSegment = new Segment(0, new int[0], new int[0], new V3(0, 0));
    @Getter(value = AccessLevel.PACKAGE) @Setter(value = AccessLevel.PACKAGE)
    private TreePositionBuilder lastBuilder;

    public TreePositionBuilder(@NotNull final V3 mainDirection) {
        this.mainDirection = mainDirection;
    }

    public static TreePositionBuilder of(@NotNull final V3 mainDirection) {
        return new TreePositionBuilder().setMainDirection(mainDirection);
    }

    public TreePositionBuilder next(@NotNull final SegmentPattern pattern, final int... id) {
        return pattern.apply(this, new V3(), id);
    }

    public TreePositionBuilder next(@NotNull final SegmentPattern pattern, @NotNull final V3 offset, final int... id) {
        return pattern.apply(this, offset, id);
    }

    public TreePositionBuilder nextWithReset(@NotNull final SegmentPattern pattern, final int resetSegmentId, final int... id) {
        return pattern.apply(this, new V3(), id).resetPoint(resetSegmentId);
    }

    public TreePositionBuilder nextWithReset(@NotNull final SegmentPattern pattern, @NotNull final V3 offset, final int resetSegmentId, final int... id) {
        return pattern.apply(this, offset, id).resetPoint(resetSegmentId);
    }

    public TreePositionBuilder next(final int... id) {
        for (int i : id) this.next(i, mainDirection);
        return this;
    }

    public TreePositionBuilder createLink(final int id1, final int id2) {
        positions.get(id1).addParent(id2);
        positions.get(id2).addParent(id1);
        positions.get(id1).addWadaParent(id2);
        return this;
    }

    public TreePositionBuilder next(final int id, @NotNull final V3 direction) {
        lastSegment.addParent(id);
        var parent = id == 0 ? new int[0] : new int[]{lastSegment.getId()};
        this.lastSegment = new Segment(id, parent, parent, direction.clone().add(lastSegment.getPosition()));
        this.positions.put(id, lastSegment);
        return this;
    }

    public TreePositionBuilder subSegment(@NotNull final V3 nearDirection) {
        return new TreePositionBuilder(nearDirection.clone()).setLastBuilder(this).setLastSegment(lastSegment);
    }

    public TreePositionBuilder applySegment() {
        lastBuilder.getPositions().putAll(positions);
        return lastBuilder;
    }

    public TreePositionBuilder resetPoint(@NotNull final V3 mainDirection) {
        return this.setMainDirection(mainDirection).setLastSegment(new Segment(0, new int[0], new int[0], new V3(0, 0)));
    }

    public TreePositionBuilder resetPoint(@NotNull final int segmentId) {
        var segment = positions.get(segmentId);
        if (segment == null) {
            Bukkit.getLogger().warning("Try to reset point from unknown segment %d".formatted(segmentId));
            return this;
        }

        return this.setMainDirection(mainDirection).setLastSegment(segment);
    }

    public Int2ObjectMap<Segment> generate() {
        return new Int2ObjectOpenHashMap<>(positions);
    }

}
