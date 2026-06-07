package cupcake.nikky.imperium.tower.trade.player;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TradePlayerProvider {

    @Getter
    private static final TradePlayerProvider instance = new TradePlayerProvider();

    private final Set<TradePlayerSession> sessions = new HashSet<>();

    public @Nullable TradePlayerData findTradeData(@NotNull final UUID uuid) {
        val session = TradePlayerProvider.getInstance().findSession(uuid);
        if (session == null) return null;
        return session.findData(uuid);
    }

    public boolean isTradable(@NotNull final UUID... uuids) {
        for (UUID uuid : uuids) {
            if (this.findSession(uuid) != null) return false;
        }

        return true;
    }

    public @Nullable TradePlayerSession findSession(@NotNull final UUID trader) {
        for (TradePlayerSession session : sessions) {
            if (session.isTrader(trader)) return session;
        }

        return null;
    }

    public void registerSession(@NotNull final Player firstTrader, @NotNull final Player secondTrader) {
        val firstTradeData = new TradePlayerData(firstTrader.getBungeePlayer());
        val secondTradeData = new TradePlayerData(secondTrader.getBungeePlayer());
        this.sessions.add(new TradePlayerSession(firstTradeData, secondTradeData, new TradePlayerUpdater(firstTradeData, secondTradeData)));
    }

    public void invalidateSession(@NotNull final UUID initialUuid) {
        val currentSession = this.findSession(initialUuid);
        if (currentSession == null) return;

        this.sessions.remove(currentSession);
        currentSession.closeSession(initialUuid);
    }

}
