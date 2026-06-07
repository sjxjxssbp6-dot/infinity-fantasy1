package cupcake.nikky.imperium.tower.client.protocol.packet.exact.pandora;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PandoraChestRemovePacket extends ClientPacket {

    private UUID uuid;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeId(uuid, data);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.uuid = NetUtil.readId(data);
    }

}
