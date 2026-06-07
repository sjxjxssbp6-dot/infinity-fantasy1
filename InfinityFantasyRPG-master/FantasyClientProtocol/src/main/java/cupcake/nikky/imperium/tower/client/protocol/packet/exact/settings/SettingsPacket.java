package cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SettingsPacket extends ClientPacket {

    private Object2BooleanMap<SettingsType> settings;

    @Override
    public void serverRead(ByteBuf data) {
        this.settings = new Object2BooleanOpenHashMap<>();
        while(data.isReadable()) {
            val type = SettingsType.valueOf(NetUtil.readUtf8(data));
            val value = NetUtil.readVarInt(data) == 1;

            settings.put(type, value);
        }
    }

    @Override
    public void serverWrite(ByteBuf data) {
        if (settings == null) return;
        settings.forEach((settingsType, aBoolean) -> {
            NetUtil.writeUtf8(data, settingsType.name());
            NetUtil.writeVarInt(data, aBoolean ? 1 : 0);
        });
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.settings = new Object2BooleanOpenHashMap<>();
        while(data.isReadable()) {
            val type = SettingsType.valueOf(NetUtil.readUtf8(data));
            val value = NetUtil.readVarInt(data) == 1;

            settings.put(type, value);
        }
    }

    @Override
    public void clientWrite(ByteBuf data) {
        if (settings == null) return;
        settings.forEach((settingsType, aBoolean) -> {
            NetUtil.writeUtf8(data, settingsType.name());
            NetUtil.writeVarInt(data, aBoolean ? 1 : 0);
        });
    }
}
