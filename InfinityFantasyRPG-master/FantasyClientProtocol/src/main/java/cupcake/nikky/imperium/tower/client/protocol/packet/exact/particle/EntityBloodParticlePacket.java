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
public class EntityBloodParticlePacket extends ClientPacket {

    private int entityId;
    private BloodType bloodType;

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeInt(entityId);
        data.writeInt(bloodType.ordinal());
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.entityId = data.readInt();
        this.bloodType = BloodType.values()[data.readInt()];
    }

}
