package cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle;

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
public class LocationDeathParticlePacket extends ClientPacket {

    private double x;
    private double y;
    private double z;
    private DeathType deathType;

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeDouble(x);
        data.writeDouble(y);
        data.writeDouble(z);
        data.writeInt(deathType.ordinal());
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.x = data.readDouble();
        this.y = data.readDouble();
        this.z = data.readDouble();
        this.deathType = DeathType.values()[data.readInt()];
    }

}
