package cupcake.nikky.imperium.tower.client.protocol.packet.exact;

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
public class ManaBarPacket extends ClientPacket {

    private boolean blocked;
    private int currentMana;
    private int maxMana;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, blocked ? 1 : 0);
        NetUtil.writeVarInt(data, currentMana);
        NetUtil.writeVarInt(data, maxMana);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.blocked = NetUtil.readVarInt(data) == 1;
        this.currentMana = NetUtil.readVarInt(data);
        this.maxMana = NetUtil.readVarInt(data);
    }

}
