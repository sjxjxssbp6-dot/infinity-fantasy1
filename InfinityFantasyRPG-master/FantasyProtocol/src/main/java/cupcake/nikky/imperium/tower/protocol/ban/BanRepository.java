package cupcake.nikky.imperium.tower.protocol.ban;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BanRepository {

    private final Cache<UUID, BanData> data = CacheBuilder.newBuilder()
            .expireAfterAccess(6, TimeUnit.HOURS)
            .build();

    public void register(@NotNull final BanData banData) {
        this.data.put(banData.getUuid(), banData);
    }

    public void invalidate(@NotNull final UUID uuid) {
        this.data.invalidate(uuid);
    }

    public @Nullable BanData find(@NotNull final UUID uuid) {
        return this.data.getIfPresent(uuid);
    }

}
