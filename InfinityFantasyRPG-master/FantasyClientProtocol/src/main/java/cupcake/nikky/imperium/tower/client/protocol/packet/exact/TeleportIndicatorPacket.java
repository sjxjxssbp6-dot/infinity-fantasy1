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
public class TeleportIndicatorPacket extends ClientPacket {

    private long millis;
    private TeleportState state;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarLong(data, millis);
        NetUtil.writeVarInt(data, state.ordinal());
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.millis = NetUtil.readVarLong(data);
        this.state = TeleportState.values()[NetUtil.readVarInt(data)];
    }

    public enum TeleportState {
        START, SKIP, DONE
    }

}
