package cupcake.nikky.imperium.tower.data.pandora;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PandoraStorage {

    @Getter private static final PandoraStorage instance = new PandoraStorage();
    private final List<PandoraChest> data = new ArrayList<>();

    public void invalidatePandora(@NotNull final UUID uuid) {
        data.removeIf(pandoraChest -> pandoraChest.getUuid().equals(uuid));
    }

    public void addChest(@NotNull final UUID uuid, final double x, final  double y, final double z) {
        this.data.add(new PandoraChest(uuid, x, y, z));
    }

    public PandoraChest findPandora(final double x, final double y, final double z) {
        return data.stream().filter(pandoraChest -> pandoraChest.equal(x, y, z)).findAny().orElse(null);
    }

    public PandoraChest findPandora(@NotNull final UUID uuid) {
        return data.stream().filter(pandoraChest -> pandoraChest.getUuid().equals(uuid)).findAny().orElse(null);
    }

    public void clear() {
        this.data.clear();
    }

}
