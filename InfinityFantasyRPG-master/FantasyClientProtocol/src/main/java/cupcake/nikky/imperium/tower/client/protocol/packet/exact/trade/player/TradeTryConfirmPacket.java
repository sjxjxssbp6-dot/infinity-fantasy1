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
public class TradeTryConfirmPacket extends ClientPacket {

    private boolean accepted;

    @Override
    public void clientRead(ByteBuf data) {
        this.accepted = data.readBoolean();
    }

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeBoolean(accepted);
    }

}
