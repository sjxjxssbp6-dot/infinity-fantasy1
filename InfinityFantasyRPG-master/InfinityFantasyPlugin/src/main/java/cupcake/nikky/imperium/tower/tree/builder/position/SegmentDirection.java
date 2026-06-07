package cupcake.nikky.imperium.tower.tree.builder.position;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.V3;

@RequiredArgsConstructor
public enum SegmentDirection {

    UP(new V3(0, -100)),
    LEFT(new V3(-100, 0)),
    RIGHT(new V3(100, 0)),
    DOWN(new V3(0, 100));

    private final V3 vector;

    public V3 sum(@NotNull final SegmentDirection segmentDirection) {
        return getVector().clone().add(segmentDirection.getVector());
    }

    public V3 sum(@NotNull final V3 position) {
        return getVector().clone().add(position);
    }

    public V3 getVector() {
        return vector.clone();
    }
}
