package cupcake.nikky.imperium.tower.data.analyze;

import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyzeContainer;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyzeType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemAnalyzeStorage {

    @Getter
    private static final ItemAnalyzeStorage instance = new ItemAnalyzeStorage();

    private final List<ItemAnalyzeContainer> containers = new ArrayList<>();
    @Getter @Setter private boolean shouldClear = false;

    public List<ItemAnalyzeContainer> findAll(@NotNull final ItemAnalyzeType type) {
        return containers.stream().filter(container -> container.getType() == type).collect(Collectors.toList());
    }

    public void registerContainer(@NotNull final ItemAnalyzeContainer container) {
        this.containers.add(container);
    }

    public boolean isEmpty() {
        return containers.isEmpty();
    }

    public void clear() {
        this.containers.clear();
    }

}
