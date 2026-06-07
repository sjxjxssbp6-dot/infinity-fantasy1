package cupcake.nikky.imperium.tower.client.protocol.packet.exact;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryOverlayTextPacket extends ClientPacket {

    private String[] text;
    private String seasonUrl;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, text.length);

        for (String s : text) {
            NetUtil.writeUtf8(data, s);
        }

        NetUtil.writeUtf8(data, seasonUrl);
    }

    @Override
    public void clientRead(ByteBuf data) {
        val length = NetUtil.readVarInt(data);

        this.text = new String[length];

        for (int i = 0; i < length; i++) {
            this.text[i] = NetUtil.readUtf8(data);
        }

        this.seasonUrl = NetUtil.readUtf8(data);
    }

}
