package cupcake.nikky.imperium.tower.client.protocol.packet.exact.debug;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttributeDebugPacket extends ClientPacket {

    private List<AttributeClient> attributes = new ArrayList<>();

    @Override
    public void serverWrite(ByteBuf data) {
        attributes.forEach(attributeClient -> attributeClient.write(data));
    }

    @Override
    public void clientRead(ByteBuf data) {
        while (data.isReadable()) {
            this.attributes.add(new AttributeClient().read(data));
        }
    }
}
