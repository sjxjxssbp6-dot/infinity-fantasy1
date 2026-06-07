package cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench;

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
public class CraftBenchSlotClick extends ClientPacket {

    private int slot;

    @Override
    public void clientWrite(ByteBuf data) {
        data.writeInt(slot);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.slot = data.readInt();
    }

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeInt(slot);
    }

    @Override
    public void serverRead(ByteBuf data) {
        this.slot = data.readInt();
    }

}
