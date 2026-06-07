package cupcake.nikky.imperium.tower.client.protocol.packet.exact.ailment;

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
public class AilmentPacket extends ClientPacket {

    private long whenReset;
    private AilmentType type;
    private int entityId;

    @Override
    public void serverWrite(ByteBuf data) {
        data.writeLong(whenReset);
        data.writeInt(type.ordinal());
        data.writeInt(entityId);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.whenReset = data.readLong();
        this.type = AilmentType.values()[data.readInt()];
        this.entityId = data.readInt();
    }
}
