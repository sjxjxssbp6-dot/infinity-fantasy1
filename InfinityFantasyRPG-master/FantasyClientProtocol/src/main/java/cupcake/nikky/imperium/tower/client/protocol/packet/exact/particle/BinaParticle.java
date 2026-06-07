package cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BinaParticle extends ClientPacket {

    private String hexDecimalColor;
    private double x;
    private double y;
    private double z;
    private int duration;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeUtf8(data, hexDecimalColor);
        data.writeDouble(x);
        data.writeDouble(y);
        data.writeDouble(z);
        data.writeInt(duration);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.hexDecimalColor = NetUtil.readUtf8(data);
        this.x = data.readDouble();
        this.y = data.readDouble();
        this.z = data.readDouble();
        this.duration = data.readInt();
    }
}
