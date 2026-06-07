package cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeTryClickItemPacket extends ClientPacket {

    private int slot;
    private boolean accepted;

    @Override
    public void clientWrite(ByteBuf data) {
        data.writeInt(slot);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.slot = data.readInt();
        this.accepted = data.readBoolean();
    }

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeInt(slot);
        data.writeBoolean(accepted);
    }

    @Override
    public void serverRead(ByteBuf data) {
        this.slot = data.readInt();
    }
}
