package cupcake.nikky.imperium.tower.user;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import lombok.val;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import ru.cristalix.wada.util.ModTransfer;

public interface PacketUser extends SpigotUser, UserState {

    default void sendPacket(Packet<?> packet) {
        if (packet == null || this.isInvalidated() || !isLoaded()) return;
        ((CraftPlayer) getSpigot()).getHandle().playerConnection.sendPacket(packet);
    }

    default <T extends ClientPacket> void sendPacket(T packet) {
        if (packet == null || this.isInvalidated() || !isLoaded()) return;

        val transfer = new ModTransfer().writeString(packet.getClass().getName());
        packet.serverWrite(transfer.getBuffer());
        transfer.send("fantasy:packet", this.getSpigot());
    }

}
