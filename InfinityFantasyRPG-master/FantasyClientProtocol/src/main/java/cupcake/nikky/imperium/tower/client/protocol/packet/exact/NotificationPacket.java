package cupcake.nikky.imperium.tower.client.protocol.packet.exact;

import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class NotificationPacket extends ClientPacket {

    private SystemNotificationType type;
    private String[] text;

    public NotificationPacket(@NotNull final SystemNotificationType type, @NotNull final String... lines) {
        this.type = type;
        this.text = lines;
    }

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, type.getType());
        NetUtil.writeVarInt(data, text.length);

        for (String line : text) {
            NetUtil.writeUtf8(data, line);
        }
    }

    @Override
    public void clientRead(ByteBuf data) {
        type = SystemNotificationType.findByType(NetUtil.readVarInt(data));
        this.text = new String[NetUtil.readVarInt(data)];

        for (int i = 0; i < text.length; i++) {
            this.text[i] = NetUtil.readUtf8(data);
        }
    }

}
