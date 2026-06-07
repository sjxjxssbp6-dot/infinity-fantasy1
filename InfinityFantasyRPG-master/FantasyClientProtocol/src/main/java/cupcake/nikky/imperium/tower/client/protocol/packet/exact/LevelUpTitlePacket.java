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
public class LevelUpTitlePacket extends ClientPacket {

    private int level;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, level);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.level = NetUtil.readVarInt(data);
    }

}
