package cupcake.nikky.imperium.service.board.user.time;

import cupcake.nikky.imperium.service.Fantasy;
import cupcake.nikky.imperium.service.board.user.TopBoardService;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.board.PlayerTimeData;
import cupcake.nikky.imperium.tower.protocol.packet.time.UserTimeBoardPacket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.realm.RealmId;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserTopHochmaBoardService implements TopBoardService<PlayerTimeData> {

    @Getter
    private static final UserTopHochmaBoardService instance = new UserTopHochmaBoardService();

    @Getter private final Map<UUID, PlayerTimeData> cachedData = new ConcurrentHashMap<>(128, 0.75F, 4);
    @Getter private final Map<UUID, String> cachedNames = new ConcurrentHashMap<>(128, 0.75F, 4);

    public void clearAll() {
        this.cachedData.clear();
        this.cachedNames.clear();
    }

    public void applyName(@NotNull final UUID uuid, @NotNull final String name) {
        this.cachedNames.put(uuid, name);
    }

    public void applyData(@NotNull final UUID uuid, @NotNull final PlayerTimeData data) {
        this.cachedData.put(uuid, data);
    }

    public void sendPacket(@NotNull final RealmId realmId) {
        val packet = new UserTimeBoardPacket();

        packet.setNames(new HashMap<>(UserTopHochmaBoardService.getInstance().getCachedNames()));
        packet.setData(new HashMap<>(UserTopHochmaBoardService.getInstance().getCachedData()));

        PacketRegistry.forward(realmId, packet);
    }

    public void sendPacket() {
        val packet = new UserTimeBoardPacket();

        packet.setNames(new HashMap<>(UserTopHochmaBoardService.getInstance().getCachedNames()));
        packet.setData(new HashMap<>(UserTopHochmaBoardService.getInstance().getCachedData()));

        PacketRegistry.write(Fantasy.gameServer().nameSpace(), packet);
    }

}
