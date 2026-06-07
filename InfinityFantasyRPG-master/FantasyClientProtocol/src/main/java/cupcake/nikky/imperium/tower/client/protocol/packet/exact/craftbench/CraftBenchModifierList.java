package cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CraftBenchModifierList extends ClientPacket {

    private Map<String, ModifierList> prefix = new HashMap<>();
    private Map<String, ModifierList> suffix = new HashMap<>();

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, prefix.size());
        prefix.forEach((title, modifierList) -> {
            NetUtil.writeUtf8(data, title);
            modifierList.write(data);
        });

        NetUtil.writeVarInt(data, suffix.size());
        suffix.forEach((title, modifierList) -> {
            NetUtil.writeUtf8(data, title);
            modifierList.write(data);
        });
    }

    @Override
    public void clientRead(ByteBuf data) {
        int size = NetUtil.readVarInt(data);
        for (int i = 0; i < size; i++) {
            val modifierList = new ModifierList();
            val title = NetUtil.readUtf8(data);

            modifierList.read(data);

            this.prefix.put(title, modifierList);
        }

        size = NetUtil.readVarInt(data);
        for (int i = 0; i < size; i++) {
            val modifierList = new ModifierList();
            val title = NetUtil.readUtf8(data);

            modifierList.read(data);

            this.suffix.put(title, modifierList);
        }
    }
}
