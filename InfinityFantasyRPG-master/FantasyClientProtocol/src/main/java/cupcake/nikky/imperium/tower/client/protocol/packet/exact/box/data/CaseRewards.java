package cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data;

import io.netty.buffer.ByteBuf;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CaseRewards {

    private Map<Integer, CaseExactReward> exactItems = new HashMap<>();

    public void write(ByteBuf data) {
        exactItems.forEach((id, item) -> {
            data.writeInt(id);
            item.write(data);
        });
    }

    public CaseRewards read(ByteBuf data) {
        while (data.isReadable())
            this.exactItems.put(data.readInt(), new CaseExactReward().read(data));
        return this;
    }

}
