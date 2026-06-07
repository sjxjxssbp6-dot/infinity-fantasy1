package cupcake.nikky.imperium.tower.protocol;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.CoreApi;
import ru.cristalix.core.network.Capability;
import ru.cristalix.core.network.CorePackage;
import ru.cristalix.core.realm.RealmId;
import ru.cristalix.core.realm.RealmInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PacketRegistry {

    public static <T extends CorePackage> void addListener(Class<T> var1, BiConsumer<RealmId, T> var2) {
        CoreApi.get().getSocketClient().addListener(var1, var2);
    }

    public static <T extends CorePackage> void forward(@NotNull final RealmId realmId, @NotNull final T packet) {
        CoreApi.get().getSocketClient().forward(realmId, packet);
    }

    public static <T extends CorePackage> void forward(@NotNull final RealmInfo realmInfo, @NotNull final T packet) {
        CoreApi.get().getSocketClient().forward(realmInfo.getRealmId(), packet);
    }

    public static <T extends CorePackage> void write(@NotNull final T packet) {
        CoreApi.get().getSocketClient().write(packet);
    }

    public static <T extends CorePackage> void write(@NotNull final String namespace, @NotNull final T packet) {
        CoreApi.get().getSocketClient().write(namespace, packet);
    }

    public static <T extends CorePackage> CompletableFuture<T> writeAndAwaitResponse(@NotNull final T corePackage) {
        return CoreApi.get().getSocketClient().writeAndAwaitResponse(corePackage);
    }

    public static <T extends CorePackage> CompletableFuture<T> writeAndAwaitResponse(@NotNull final String namespace, @NotNull final T corePackage) {
        return CoreApi.get().getSocketClient().writeAndAwaitResponse(namespace, corePackage);
    }

    public static <T extends CorePackage> CompletableFuture<T> awaitResponse(@NotNull final T corePackage) {
        return CoreApi.get().getSocketClient().awaitResponse(corePackage);
    }

    public static void registerCapabilities(@NotNull final List<Capability> capabilities) {
        CoreApi.get().getSocketClient().registerCapabilities(capabilities.toArray(new Capability[0]));
    }

    public static void registerCapabilities(@NotNull final Capability... capabilities) {
        CoreApi.get().getSocketClient().registerCapabilities(capabilities);
    }

}
