package cupcake.nikky.imperium.tower.client.protocol.packet.exact;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StorageClickPacket extends ClientPacket {

    private int page;

    @Override
    public void clientWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, page);
    }

    @Override
    public void serverRead(ByteBuf data) {
        this.page = NetUtil.readVarInt(data);
    }

}
