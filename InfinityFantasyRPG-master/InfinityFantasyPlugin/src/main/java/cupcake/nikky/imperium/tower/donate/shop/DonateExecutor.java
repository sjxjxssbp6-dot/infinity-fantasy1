package cupcake.nikky.imperium.tower.donate.shop;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.invoice.IInvoiceService;
import ru.cristalix.core.invoice.Invoice;
import ru.cristalix.core.invoice.InvoiceResult;
import ru.cristalix.core.invoice.TransactionCategory;

import java.util.concurrent.CompletableFuture;

public class DonateExecutor {

    public CompletableFuture<InvoiceResult> execute(@NotNull final Player player, @NotNull final TransactionCategory category,
                                                    @NotNull final String description, final int price, final boolean allowBonuses) {
        return IInvoiceService.get().bill(player.getUniqueId(), Invoice.builder()
                .category(category)
                .allowBonuses(allowBonuses)
                .price(price)
                .description(description)
                .build()
        );
    }

}
