package cupcake.nikky.imperium.tower.protocol.injector.packet;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Packet;

public interface PacketListener {

    boolean accept(EntityPlayer entityPlayer, Packet<?> packet);

}
