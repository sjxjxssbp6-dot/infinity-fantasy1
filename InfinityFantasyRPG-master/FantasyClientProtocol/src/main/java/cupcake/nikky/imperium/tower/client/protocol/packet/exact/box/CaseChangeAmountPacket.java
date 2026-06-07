package cupcake.nikky.imperium.tower.client.protocol.packet.exact.box;

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
public class CaseChangeAmountPacket extends ClientPacket {

    private int amount;

    @Override
    public void clientRead(ByteBuf data) {
        this.amount = data.readInt();
    }

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeInt(amount);
    }

    @Override
    public void clientWrite(ByteBuf data) {
        data.writeInt(amount);
    }

    @Override
    public void serverRead(ByteBuf data) {
        this.amount = data.readInt();
    }
}
