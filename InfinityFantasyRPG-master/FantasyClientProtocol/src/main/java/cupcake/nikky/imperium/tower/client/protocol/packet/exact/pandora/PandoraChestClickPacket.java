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
public class PandoraChestClickPacket extends ClientPacket {

    private UUID uuid;

    @Override
    public void clientWrite(ByteBuf data) {
        NetUtil.writeId(data, uuid);
    }

    @Override
    public void serverRead(ByteBuf data) {
        this.uuid = NetUtil.readId(data);
    }
}
