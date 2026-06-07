package cupcake.nikky.imperium.tower.trade.npc.repository;

import cupcake.nikky.imperium.tower.trade.npc.PlayerTradeData;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class TradeNpcRepository {

    private final Map<UUID, PlayerTradeData> data = new ConcurrentHashMap<>(32, 0.75F, 2);

    public void registerNew(@NotNull final UUID uuid, @NotNull final PlayerTradeData data) {
        this.data.put(uuid, data);
    }

    public void invalidate(@NotNull final UUID uuid) {
        this.data.remove(uuid);
    }

    public PlayerTradeData findData(@NotNull final UUID uuid) {
        return this.data.get(uuid);
    }

    public Collection<PlayerTradeData> findAll() {
        return this.data.values();
    }

}
