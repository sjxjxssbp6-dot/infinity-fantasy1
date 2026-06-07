package cupcake.nikky.imperium.tower.client.protocol.packet.exact;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class BattleDurationPacket extends ClientPacket {

    private int duration;
    private boolean remove;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, duration);
        NetUtil.writeVarInt(data, remove ? 1 : 0);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.duration = NetUtil.readVarInt(data);
        this.remove = NetUtil.readVarInt(data) == 1;
    }

}
