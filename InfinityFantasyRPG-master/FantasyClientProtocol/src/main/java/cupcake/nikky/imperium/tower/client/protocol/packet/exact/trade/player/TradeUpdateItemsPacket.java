package cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import io.netty.buffer.ByteBuf;
import lombok.*;
import ru.cristalix.wada.util.ModTransfer;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeUpdateItemsPacket extends ClientPacket {

    private List<Object> items;

    @Override
    public void serverWrite(ByteBuf data) {
        val transfer = new ModTransfer(data);
        transfer.writeInt(items == null ? 0 : items.size());
        if (items == null || items.isEmpty()) return;

        items.forEach(itemObject -> transfer.writeItem((org.bukkit.inventory.ItemStack) itemObject));
    }

    @Override
    public void clientRead(ByteBuf data) {
        val transfer = new ru.cristalix.enginex.util.ModTransfer(data);
        val size = transfer.readInt();
        this.items = new ArrayList<>();

        if (size == 0) return;

        for (int i = 0; i < size; i++) {
            items.add(transfer.readItem());
        }
    }

}
