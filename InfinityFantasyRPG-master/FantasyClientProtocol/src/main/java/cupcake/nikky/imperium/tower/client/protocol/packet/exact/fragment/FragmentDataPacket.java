package cupcake.nikky.imperium.tower.client.protocol.packet.exact.fragment;

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
public class FragmentDataPacket extends ClientPacket {

    private long time;
    private int respawns;

    @Override
    public void clientRead(ByteBuf data) {
        this.time = data.readLong();
        this.respawns = data.readInt();
    }

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeLong(time);
        data.writeInt(respawns);
    }

}
