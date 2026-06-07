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
public class EntitySparksParticlePacket extends ClientPacket {

    private int entityId;
    private double radius;

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeInt(entityId);
        data.writeDouble(radius);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.entityId = data.readInt();
        this.radius = data.readDouble();
    }

}
