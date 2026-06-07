package cupcake.nikky.imperium.tower.client.protocol.packet.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
public class AttributePacket extends ClientPacket {

    private final Map<ImperiumAttribute, Double> values = new EnumMap<>(ImperiumAttribute.class);

    public void applyValue(@NotNull final ImperiumAttribute attribute, final double value) {
        this.values.put(attribute, value);
    }

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, values.size());
        values.forEach((attribute, aDouble) -> {
            NetUtil.writeVarInt(attribute.ordinal(), data);
            data.writeDouble(aDouble);
        });
    }

    @Override
    public void clientRead(ByteBuf data) {
        val size = NetUtil.readVarInt(data);
        for (int i = 0; i < size; i++) {
            val attribute = ImperiumAttribute.values()[NetUtil.readVarInt(data)];
            val value = data.readDouble();

            this.values.put(attribute, value);
        }
    }

}
