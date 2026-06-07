package cupcake.nikky.imperium.tower.client.protocol.packet.exact.box;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseRollPacket extends ClientPacket {

    private int[] winId;

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeInt(winId.length);
        for (int i : winId) data.writeInt(i);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.winId = new int[data.readInt()];

        int index = 0;
        while (data.isReadable()) this.winId[index++] = data.readInt();
    }

}
