package cupcake.nikky.imperium.tower.client.protocol.packet.exact;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PremiumPacket extends ClientPacket {

    private boolean isSubscribed;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, isSubscribed ? 1 : 0);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.isSubscribed = NetUtil.readVarInt(data) == 1;
    }

}
