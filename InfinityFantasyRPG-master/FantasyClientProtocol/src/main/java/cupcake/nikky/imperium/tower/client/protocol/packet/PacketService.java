package cupcake.nikky.imperium.tower.client.protocol.packet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class PacketService {

    private final Map<Class<? extends ClientPacket>, List<Consumer<ClientPacket>>> listeners = new HashMap<>();
    private final Map<Class<? extends ClientPacket>, List<BiConsumer<UUID, ClientPacket>>> serverListeners = new HashMap<>();

    @Getter
    private static final PacketService instance = new PacketService();

    @SuppressWarnings("unchecked")
    public <T extends ClientPacket> void addListener(@NotNull final Class<? extends T> clazz, @NotNull final Consumer<? extends T> listener) {
        this.listeners.computeIfAbsent(clazz, ignore -> new ArrayList<>()).add((Consumer<ClientPacket>) listener);
    }

    @SuppressWarnings("unchecked")
    public <T extends ClientPacket> void addServerListener(@NotNull final Class<? extends T> clazz, @NotNull final BiConsumer<UUID, ? extends T> listener) {
        this.serverListeners.computeIfAbsent(clazz, ignore -> new ArrayList<>()).add((BiConsumer<UUID, ClientPacket>) listener);
    }

    public void invalidateAll(@NotNull final Class<? extends ClientPacket> clazz) {
        this.listeners.remove(clazz);
    }

    public void clear() {
        this.listeners.clear();
    }

    public <T extends ClientPacket> void handle(@NotNull final T instance) {
        Optional.ofNullable(this.listeners.get(instance.getClass())).ifPresent(listeners -> listeners.forEach(listener -> listener.accept(instance)));
    }

    public <T extends ClientPacket> void handle(@NotNull final UUID uuid, @NotNull final T instance) {
        Optional.ofNullable(this.serverListeners.get(instance.getClass())).ifPresent(listeners -> listeners.forEach(listener -> listener.accept(uuid, instance)));
    }

}
