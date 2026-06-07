package cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench;

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
public class CraftBenchTryCraft extends ClientPacket {

    private String title;
    private String subTitle;
    private boolean success;

    @Override
    public void clientWrite(ByteBuf data) {
        NetUtil.writeUtf8(data, title);
        NetUtil.writeUtf8(data, subTitle);
    }

    @Override
    public void serverRead(ByteBuf data) {
        this.title = NetUtil.readUtf8(data);
        this.subTitle = NetUtil.readUtf8(data);
    }

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeUtf8(data, title);
        NetUtil.writeUtf8(data, subTitle);
        NetUtil.writeVarInt(data, success ? 1 : 0);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.title = NetUtil.readUtf8(data);
        this.subTitle = NetUtil.readUtf8(data);
        this.success = NetUtil.readVarInt(data) == 1;
    }

}
