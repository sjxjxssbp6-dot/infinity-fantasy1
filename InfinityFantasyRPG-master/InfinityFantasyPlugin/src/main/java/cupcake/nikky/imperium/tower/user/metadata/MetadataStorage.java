package cupcake.nikky.imperium.tower.user.metadata;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MetadataStorage {

    private final Map<MetadataType, Integer> data = new ConcurrentHashMap<>(16, 0.75F, 4);

    public void registerType(@NotNull final MetadataType type) {
        this.data.put(type, 1);
    }

    public void registerType(@NotNull final MetadataType type, final int data) {
        this.data.put(type, data);
    }

    public int findExactData(@NotNull final MetadataType type) {
        return this.data.getOrDefault(type, 0);
    }

    public boolean containsType(@NotNull final MetadataType type) {
        return this.data.getOrDefault(type, 0) != 0;
    }

    public void invalidateType(@NotNull final MetadataType type) {
        this.data.remove(type);
    }

}
