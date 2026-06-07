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
@NoArgsConstructor
@AllArgsConstructor
public class TreePointIndicatorPacket extends ClientPacket {

    private int count;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, count);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.count = NetUtil.readVarInt(data);
    }

}
