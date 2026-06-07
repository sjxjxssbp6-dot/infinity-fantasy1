package cupcake.nikky.imperium.tower.trade.npc.session;

import cupcake.nikky.imperium.tower.client.protocol.trade.TradeCurrency;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.wada.util.ModTransfer;

import java.util.function.Consumer;
import java.util.function.Function;

@Getter
@Setter
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class TradeContainer {

    private final int id;
    private final String uniqueId;
    private final ItemStack icon;
    private final TradeCurrency currency;
    private final int price;
    private final Function<Player, ImperiumItem> itemGenerator;
    private long whenReset;
    private int currentCount = 0;
    private int maxCount = -1;
    private Consumer<Player> onUpdate;

    public TradeContainer(final int id, @NotNull final String uniqueId, @NotNull final ItemStack item, final int price, @Nullable final Function<Player, ImperiumItem> itemGenerator) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.icon = item;
        this.currency = TradeCurrency.GRAN;
        this.price = price;
        this.itemGenerator = itemGenerator;
    }

    public static TradeContainer of(final int id, @NotNull final ImperiumItem item, final int price) {
        return new TradeContainer(id, item.getUniqueId(), item.createNew(), price, player -> item);
    }

    public static TradeContainer of(final int id, @NotNull final ImperiumItem item, @NotNull final TradeCurrency tradeCurrency, final int price) {
        return new TradeContainer(id, item.getUniqueId(), item.createNew(), tradeCurrency, price, player -> item);
    }

    public static TradeContainer of(final int id, @NotNull final String uniqueId, @NotNull final ItemStack icon, final int price) {
        return new TradeContainer(id, uniqueId, icon, price, null);
    }

    public static TradeContainer of(final int id, @NotNull final String uniqueId, @NotNull final ItemStack icon, final int price, @Nullable final Function<Player, ImperiumItem> itemGenerator) {
        return new TradeContainer(id, uniqueId, icon, price, itemGenerator);
    }

    public void write(@NotNull final ModTransfer transfer) {
        transfer.writeInt(id).writeItem(icon).writeString(currency.name()).writeInt(price).writeLong(whenReset).writeInt(currentCount).writeInt(maxCount);
    }

}
