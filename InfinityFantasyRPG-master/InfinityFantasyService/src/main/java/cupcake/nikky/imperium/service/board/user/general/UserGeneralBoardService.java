package cupcake.nikky.imperium.service.board.user.general;

import cupcake.nikky.imperium.service.Fantasy;
import cupcake.nikky.imperium.service.board.user.TopBoardService;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.packet.UserGeneralBoardPacket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.realm.RealmId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserGeneralBoardService implements TopBoardService<Double> {

    @Getter
    private static final UserGeneralBoardService instance = new UserGeneralBoardService();

    @Getter private final Map<UUID, Double> cachedData = new ConcurrentHashMap<>(128, 0.75F, 4);
    @Getter private final Map<UUID, String> cachedNames = new ConcurrentHashMap<>(128, 0.75F, 4);

    public void clearAll() {
        this.cachedData.clear();
        this.cachedNames.clear();
    }

    public void applyName(@NotNull final UUID uuid, @NotNull final String name) {
        this.cachedNames.put(uuid, name);
    }

    public void applyData(@NotNull final UUID uuid, @NotNull final Double data) {
        this.cachedData.put(uuid, data);
    }

    public void sendPacket(@NotNull final RealmId realmId) {
        val packet = new UserGeneralBoardPacket();

        packet.setNames(new HashMap<>(UserGeneralBoardService.getInstance().getCachedNames()));
        packet.setData(new HashMap<>(UserGeneralBoardService.getInstance().getCachedData()));

        PacketRegistry.forward(realmId, packet);
    }

    public void sendPacket() {
        val packet = new UserGeneralBoardPacket();

        packet.setNames(new HashMap<>(UserGeneralBoardService.getInstance().getCachedNames()));
        packet.setData(new HashMap<>(UserGeneralBoardService.getInstance().getCachedData()));

        PacketRegistry.write(Fantasy.gameServer().nameSpace(), packet);
    }

}