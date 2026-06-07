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
@NoArgsConstructor
@AllArgsConstructor
public class PandoraChestSpawnPacket extends ClientPacket {

    private UUID uuid;
    private double x;
    private double y;
    private double z;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeId(uuid, data);
        data.writeDouble(x);
        data.writeDouble(y);
        data.writeDouble(z);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.uuid = NetUtil.readId(data);
        this.x = data.readDouble();
        this.y = data.readDouble();
        this.z = data.readDouble();
    }
}
