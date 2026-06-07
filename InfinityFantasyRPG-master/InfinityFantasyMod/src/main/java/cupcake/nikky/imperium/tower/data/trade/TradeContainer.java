package cupcake.nikky.imperium.tower.data.trade;

import cupcake.nikky.imperium.tower.client.protocol.trade.TradeCurrency;
import dev.xdark.clientapi.item.ItemStack;
import lombok.*;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.util.ModTransfer;

@Getter
@Setter
@Accessors(fluent = true)
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class TradeContainer {

    private final int id;
    private final ItemStack itemStack;
    private final TradeCurrency tradeCurrency;
    private final int price;
    private long whenReset;
    private int currentCount = 0;
    private int maxCount = -1;

    public void writeClick() {
        new ModTransfer().writeInt(id).send("imp:trade-buy");
    }

    public static TradeContainer read(@NotNull final ModTransfer transfer) {
        val id = transfer.readInt();
        val item = transfer.readItem();
        val currency = TradeCurrency.valueOf(transfer.readString());
        val price = transfer.readInt();
        val whenReset = transfer.readLong();
        val currentCount = transfer.readInt();
        val maxCount = transfer.readInt();

        return TradeContainer.of(id, item, currency, price, whenReset, currentCount, maxCount);
    }

}
