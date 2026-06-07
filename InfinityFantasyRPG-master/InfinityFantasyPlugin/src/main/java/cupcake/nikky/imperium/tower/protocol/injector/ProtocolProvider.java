package cupcake.nikky.imperium.tower.protocol.injector;

import cupcake.nikky.imperium.tower.protocol.injector.listener.NetworkPacketHandler;
import cupcake.nikky.imperium.tower.protocol.injector.packet.PacketEvent;
import cupcake.nikky.imperium.tower.protocol.injector.packet.PacketListener;
import cupcake.nikky.imperium.tower.protocol.injector.packet.PlayerPacketsListener;
import lombok.val;
import net.minecraft.server.v1_12_R1.*;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.util.function.ThrowingConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProtocolProvider {

    private final Map<Class<?>, PlayerPacketsListener<?>> listeners = new ConcurrentHashMap<>();

    private final List<cupcake.nikky.imperium.tower.protocol.injector.packet.PacketListener> generalInListeners = new ArrayList<>();
    private final List<cupcake.nikky.imperium.tower.protocol.injector.packet.PacketListener> generalOutListeners = new ArrayList<>();

    public void addInboundListener(@NotNull final cupcake.nikky.imperium.tower.protocol.injector.packet.PacketListener listener) {
        generalInListeners.add(listener);
    }

    public void addOutboundListener(@NotNull final PacketListener listener) {
        generalOutListeners.add(listener);
    }

    @SuppressWarnings("all")
    private <T extends Packet<?>> PlayerPacketsListener<T> getPlayerPacketListener(@NotNull final Class<T> clazz) {
        return (PlayerPacketsListener<T>) this.listeners.computeIfAbsent(clazz, (key) -> new PlayerPacketsListener<T>(clazz));
    }

    public synchronized <T extends Packet<?>> void addInboundHandler(@NotNull final Class<T> clazz, @NotNull final String handlerName,
                                                                     @NotNull final ThrowingConsumer<PacketEvent<T>> handler) {
        this.getPlayerPacketListener(clazz).addInboundHandler(handlerName, handler);
    }

    public synchronized <T extends Packet<?>> void addOutboundHandler(@NotNull final Class<T> clazz, @NotNull final String handlerName,
                                                                      @NotNull final ThrowingConsumer<PacketEvent<T>> handler) {
        this.getPlayerPacketListener(clazz).addOutboundHandler(handlerName, handler);
    }

    public synchronized <T extends Packet<?>> void removeHandler(@NotNull final Class<T> clazz, @NotNull final String handlerName) {
        var listener = this.listeners.get(clazz);
        if (listener != null) {
            listener.removeHandler(handlerName);
        }
    }

    public boolean handlePacket(final boolean inbound, @NotNull final EntityPlayer entityPlayer, @NotNull final Packet<?> packet) {
        boolean cancel = false;
        for (val listener : (inbound ? this.generalInListeners : this.generalOutListeners)) {
            try {
                if (listener.accept(entityPlayer, packet)) {
                    cancel = true;
                }
            } catch (Exception e) {
                System.out.println(entityPlayer.getName() + " " + packet.getClass().getSimpleName());
                e.printStackTrace();
            }
        }
        PlayerPacketsListener<?> listener = this.listeners.get(packet.getClass());
        if (listener != null) {
            return !listener.callEvent(inbound, entityPlayer, packet).isCancelled();
        }
        return !cancel;
    }

    public void registerPacketInjector(@NotNull final EntityPlayer entityPlayer) {
        val channel = entityPlayer.playerConnection.networkManager.channel;
        if (channel == null) {
            System.err.println("Player " + entityPlayer + " channel is null!");
            return;
        }
        try {
            channel.pipeline().addBefore("packet_handler", "PacketInjector", new NetworkPacketHandler(entityPlayer));
        } catch (Exception e) {
            // Печатаем каналы
            System.out.println(channel.pipeline().names());
            e.printStackTrace();
        }
    }

}
