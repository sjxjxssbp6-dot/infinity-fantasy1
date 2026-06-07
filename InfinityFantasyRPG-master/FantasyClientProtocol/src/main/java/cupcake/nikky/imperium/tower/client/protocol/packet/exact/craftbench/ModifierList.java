package cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench;

import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ModifierList {
    
    private List<ModifierData> data = new ArrayList<>();

    public void write(ByteBuf data) {
        val size = this.data.size();
        NetUtil.writeVarInt(data, size);
        if (size == 0) return;

        this.data.forEach(modifierData -> modifierData.write(data));
    }
    
    public void read(ByteBuf data) {
        val size = NetUtil.readVarInt(data);
        if (size == 0) return;

        for (int i = 0; i < size; i++) {
            val modifierData = new ModifierData();
            modifierData.read(data);
            this.data.add(modifierData);
        }
    }
    
}
