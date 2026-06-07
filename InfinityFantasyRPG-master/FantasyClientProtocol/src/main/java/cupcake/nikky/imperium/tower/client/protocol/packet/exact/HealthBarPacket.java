package cupcake.nikky.imperium.tower.client.protocol.packet.exact;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthBarPacket extends ClientPacket {

    private int currentHealth;
    private int maxHealth;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, currentHealth);
        NetUtil.writeVarInt(data, maxHealth);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.currentHealth = NetUtil.readVarInt(data);
        this.maxHealth = NetUtil.readVarInt(data);
    }

}
