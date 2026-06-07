package cupcake.nikky.imperium.tower.trade.player.request;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TradeRequestStorage {

    @Getter
    private static final TradeRequestStorage instance = new TradeRequestStorage();

    private final Cache<UUID, TradeRequestData> data = CacheBuilder.newBuilder()
            .concurrencyLevel(8)
            .expireAfterWrite(31, TimeUnit.SECONDS)
            .build();
    private final Cache<UUID, Boolean> requestDelay = CacheBuilder.newBuilder()
            .concurrencyLevel(8)
            .expireAfterWrite(3, TimeUnit.SECONDS)
            .build();

    public boolean isDelay(@NotNull final UUID uuid) {
        return requestDelay.getIfPresent(uuid) != null;
    }

    public void registerRequest(@NotNull final TradeRequestData tradeRequestData) {
        this.requestDelay.put(tradeRequestData.sender().getUniqueId(), true);
        this.data.put(tradeRequestData.sender().getUniqueId(), tradeRequestData);
    }

    public void invalidateRequest(@NotNull final UUID uuid) {
        this.data.invalidate(uuid);
        this.requestDelay.invalidate(uuid);
    }

    public TradeRequestData findRequest(@NotNull final UUID sender) {
        return this.data.getIfPresent(sender);
    }

}
