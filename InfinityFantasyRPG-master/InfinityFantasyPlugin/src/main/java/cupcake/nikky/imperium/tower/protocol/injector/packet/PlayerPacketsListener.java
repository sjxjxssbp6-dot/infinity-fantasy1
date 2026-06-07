package cupcake.nikky.imperium.tower.protocol.injector.packet;

import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Packet;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.util.function.ThrowingConsumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class PlayerPacketsListener<T extends Packet<?>> {

    private final Map<String, ThrowingConsumer<PacketEvent<T>>> inboundHandlers = new ConcurrentHashMap<>();
    private final Map<String, ThrowingConsumer<PacketEvent<T>>> outboundHandlers = new ConcurrentHashMap<>();

    private final Class<? extends T> clazz;

    public void addInboundHandler(@NotNull final String name, @NotNull final ThrowingConsumer<PacketEvent<T>> handler) {
        this.inboundHandlers.put(name, handler);
    }

    public void addOutboundHandler(@NotNull final String name, @NotNull final ThrowingConsumer<PacketEvent<T>> handler) {
        this.inboundHandlers.put(name, handler);
    }

    public PacketEvent<?> callEvent(boolean inbound, EntityPlayer entityPlayer, Packet<?> packet) {
        if (packet.getClass() != clazz) return new PacketEvent<>(entityPlayer, packet);

        val packetEvent = new PacketEvent<T>(entityPlayer, clazz.cast(packet));
        if (inbound) inboundHandlers.values().forEach(handler -> {
            try {
                handler.accept(packetEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        else outboundHandlers.values().forEach(handler -> {
            try {
                handler.accept(packetEvent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return packetEvent;
    }

    public void removeHandler(@NotNull final String name) {
        this.inboundHandlers.remove(name);
        this.outboundHandlers.remove(name);
    }

}
