package cupcake.nikky.imperium.tower.protocol.session;

import cupcake.nikky.imperium.tower.protocol.game.GameServer;
import cupcake.nikky.imperium.tower.protocol.packet.connection.session.SessionState;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class ConnectionSession {

    @Getter private final UUID uuid;
    private final AtomicReference<SessionState> sessionState = new AtomicReference<>(SessionState.DISCONNECTED);
    private final AtomicReference<GameServer> gameServer = new AtomicReference<>();
    private final AtomicLong lastUpdate = new AtomicLong();
    private final AtomicBoolean blocked = new AtomicBoolean(false);

    public ConnectionSession(@NotNull final UUID uuid, @NotNull final SessionState state, @NotNull final GameServer gameServer) {
        this.uuid = uuid;
        this.sessionState.set(state);
        this.gameServer.set(gameServer);
        this.lastUpdate.set(Instant.now().toEpochMilli());
    }

    public boolean isBlocked() {
        return blocked.get();
    }

    public void setBlocked(final boolean state) {
        for (;;) {
            var old = this.blocked.get();
            if (this.blocked.compareAndSet(old, state)) break;
        }
    }

    public long getLastUpdate() {
        return lastUpdate.get();
    }

    public void setLastUpdate(final long lastUpdate) {
        for (;;) {
            var old = this.lastUpdate.get();
            if (this.lastUpdate.compareAndSet(old, lastUpdate)) break;
        }
    }

    public void setSessionState(@NotNull final SessionState state) {
        for (;;) {
            var old = sessionState.get();
            if (this.sessionState.compareAndSet(old, state)) break;
        }
    }

    public void setGameServer(GameServer gameServer) {
        for (;;) {
            var old = this.gameServer.get();
            if (this.gameServer.compareAndSet(old, gameServer)) break;
        }
    }

    public GameServer getGameServer() {
        return gameServer.get();
    }

    public SessionState getSessionState() {
        return sessionState.get();
    }

}
