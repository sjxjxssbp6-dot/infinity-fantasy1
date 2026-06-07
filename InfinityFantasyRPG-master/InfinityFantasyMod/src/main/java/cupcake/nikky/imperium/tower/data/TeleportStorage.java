package cupcake.nikky.imperium.tower.data;

import cupcake.nikky.imperium.tower.hud.teleport.TeleportContainer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeleportStorage {

    @Getter
    private static final TeleportStorage instance = new TeleportStorage();

    private final Map<Integer, TeleportContainer> data = new HashMap<>();

    public void registerTeleport(final int id, @NotNull final TeleportContainer teleportContainer) {
        this.data.put(id, teleportContainer);
    }

    public Map<Integer, TeleportContainer> getData() {
        return new HashMap<>(data);
    }

    public void invalidateAll() {
        this.data.clear();
    }

}
