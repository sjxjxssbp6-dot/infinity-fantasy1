package cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TradeStatePacket extends ClientPacket {

    private boolean closed;

    @Override
    public void clientWrite(ByteBuf data) {
        data.writeBoolean(closed);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.closed = data.readBoolean();
    }

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeBoolean(closed);
    }

    @Override
    public void serverRead(ByteBuf data) {
        this.closed = data.readBoolean();
    }
}
