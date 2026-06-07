package cupcake.nikky.imperium.tower.protocol.injector.listener;

import cupcake.nikky.imperium.tower.Fantasy;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.AllArgsConstructor;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Packet;

@AllArgsConstructor
public class NetworkPacketHandler extends ChannelDuplexHandler {

    private final EntityPlayer entityPlayer;

    @Override
    public void channelRead(ChannelHandlerContext arg0, Object arg1) throws Exception {
        if (Fantasy.protocolProvider().handlePacket(true, this.entityPlayer, (Packet<?>) arg1)) {
            super.channelRead(arg0, arg1);
        }
    }

    @Override
    public void write(ChannelHandlerContext arg0, Object arg1, ChannelPromise arg2) throws Exception {
        if (Fantasy.protocolProvider().handlePacket(false, this.entityPlayer, (Packet<?>) arg1)) {
            super.write(arg0, arg1, arg2);
        }
    }
}
