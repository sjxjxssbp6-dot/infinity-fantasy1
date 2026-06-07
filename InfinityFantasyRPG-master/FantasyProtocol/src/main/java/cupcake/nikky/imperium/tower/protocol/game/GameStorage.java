package cupcake.nikky.imperium.tower.protocol.game;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.realm.RealmId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GameStorage {

    @Getter
    private static final GameStorage instance = new GameStorage();

    private final Map<RealmId, GameServer> data = new ConcurrentHashMap<>();

    public void registerServer(@NotNull final GameServer gameServer) {
        this.data.put(gameServer.toRealm(), gameServer);
    }

    public void invalidateServer(@NotNull final RealmId realmId) {
        this.data.remove(realmId);
    }

    public GameServer findExactServer(@NotNull final RealmId realmId) {
        return this.data.get(realmId);
    }

    public GameServer findServer(@NotNull final RealmId realmId) {
        var server = this.data.get(realmId);
        if (server == null) {
            server = new GameServer(realmId, GameState.DISCONNECTED);
            this.data.put(realmId, server);
        }

        return server;
    }

    public List<GameServer> findAll() {
        return new ArrayList<>(data.values());
    }

}
