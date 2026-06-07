package cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player;

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
public class TradePlayerNamePacket extends ClientPacket {

    private String name;

    @Override
    public void clientRead(ByteBuf data) {
        this.name = NetUtil.readUtf8(data);
    }

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeUtf8(data, name);
    }
}
