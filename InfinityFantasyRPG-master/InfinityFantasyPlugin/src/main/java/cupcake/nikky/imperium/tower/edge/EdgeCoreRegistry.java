package cupcake.nikky.imperium.tower.edge;

import cupcake.nikky.imperium.tower.Fantasy;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class EdgeCoreRegistry {

    @Getter
    private static final EdgeCoreRegistry instance = new EdgeCoreRegistry();

    private final List<EdgeCoreEffect> data = new ArrayList<>();

    public void addEdgeCore(@NotNull final EdgeCoreEffect edgeCoreEffect) {
        this.data.add(edgeCoreEffect);
    }

    public void invalidate(@NotNull final EdgeCoreEffect edgeCoreEffect) {
        this.data.remove(edgeCoreEffect);
        edgeCoreEffect.invalidate();
    }

    public void initialize() {
        Fantasy.runTimer(() -> new ArrayList<>(this.data).forEach(edgeCoreEffect -> {
            if (edgeCoreEffect.decreaseDuration() <= 0)
                this.invalidate(edgeCoreEffect);
        }), 20);
    }

}
