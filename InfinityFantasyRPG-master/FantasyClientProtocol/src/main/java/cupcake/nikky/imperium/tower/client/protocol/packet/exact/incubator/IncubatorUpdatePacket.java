package cupcake.nikky.imperium.tower.client.protocol.packet.exact.incubator;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;
import org.bukkit.inventory.ItemStack;
import ru.cristalix.wada.util.ModTransfer;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncubatorUpdatePacket extends ClientPacket {

    private List<IncubatorData> progress;

    @Override
    public void serverWrite(ByteBuf data) {
        val transfer = new ModTransfer(data);
        val size = progress.size();
        transfer.writeInt(size);

        if (size == 0) return;
        progress.forEach((incubatorData) -> transfer.writeUUID(incubatorData.getUuid())
                .writeInt(incubatorData.getCurrentProgress())
                .writeInt(incubatorData.getMaxProgress())
                .writeInt(incubatorData.getRequireLevel())
                .writeItem((ItemStack) incubatorData.getItemStack())
        );
    }

    @Override
    public void clientRead(ByteBuf data) {
        val transfer = new ru.cristalix.enginex.util.ModTransfer(data);
        progress = new ArrayList<>();

        val size = transfer.readInt();
        if (size == 0) return;

        for (int i = 0; i < size; i++) {
            progress.add(new IncubatorData(transfer.readUUID(), transfer.readInt(), transfer.readInt(), transfer.readInt(), transfer.readItem()));
        }
    }
}
