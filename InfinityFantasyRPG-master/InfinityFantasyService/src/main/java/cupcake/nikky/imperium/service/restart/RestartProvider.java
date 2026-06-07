package cupcake.nikky.imperium.service.restart;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.game.GameStorage;
import cupcake.nikky.imperium.tower.protocol.packet.GameScheduledReloadPacket;
import cupcake.nikky.imperium.tower.protocol.utility.RealTimeUtility;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.realm.RealmId;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class RestartProvider {

    private final Cache<RealmId, Boolean> restarted = CacheBuilder.newBuilder()
            .concurrencyLevel(16)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    private AtomicBoolean sync = new AtomicBoolean();

    public boolean isSync() {
        return sync.get();
    }

    public void setSync(final boolean value) {
        for (;;) {
            var oldValue = sync.get();
            if (sync.compareAndSet(oldValue, value)) break;
        }
    }

    public void initialize(@NotNull final JavaPlugin javaPlugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(javaPlugin, () -> {
            RealTimeUtility.catchDateTime();

            val hour = RealTimeUtility.hour();
            val minute = RealTimeUtility.minute();

            val counter = new AtomicInteger();

            GameStorage.getInstance().findAll().stream().filter(gameServer -> !Optional.ofNullable(restarted.getIfPresent(gameServer.toRealm())).orElse(false)).forEach(gameServer -> {
                val expectedMinute = this.isSync() ? 0 : gameServer.getRealmId() / 3 * 3;

                if (hour == 4 && minute == expectedMinute) {
                    this.restarted.put(gameServer.toRealm(), true);
                    PacketRegistry.forward(gameServer.toRealm(), new GameScheduledReloadPacket());
                    counter.incrementAndGet();
                }
            });

            if (counter.get() > 0) Bukkit.getLogger().info("Reloaded " + counter.get() + " server" + (counter.get() > 1 ? "s." : "."));
        }, 20L * 30L, 20L * 30L);
    }

}
