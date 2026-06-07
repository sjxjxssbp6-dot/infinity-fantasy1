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
public class EntityIceExplosionParticlePacket extends ClientPacket {

    private double radius;
    private int entityId;

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeDouble(radius);
        data.writeInt(entityId);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.radius = data.readDouble();
        this.entityId = data.readInt();
    }

}
