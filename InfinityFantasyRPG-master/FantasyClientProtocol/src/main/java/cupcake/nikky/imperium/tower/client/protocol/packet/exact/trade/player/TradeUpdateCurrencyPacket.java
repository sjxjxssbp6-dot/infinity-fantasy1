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
public class TradeUpdateCurrencyPacket extends ClientPacket {

    private int gran;
    private int firm;

    @Override
    public void clientWrite(ByteBuf data) {
        data.writeInt(gran);
        data.writeInt(firm);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.gran = data.readInt();
        this.firm = data.readInt();
    }

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeInt(gran);
        data.writeInt(firm);
    }

    @Override
    public void serverRead(ByteBuf data) {
        this.gran = data.readInt();
        this.firm = data.readInt();
    }

}
