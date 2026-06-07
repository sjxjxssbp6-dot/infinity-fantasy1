package cupcake.nikky.imperium.tower.donate.shop.sample.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.donate.shop.DonateExecutor;
import cupcake.nikky.imperium.tower.donate.shop.sample.DonateSample;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.donate.DonateData;
import cupcake.nikky.imperium.tower.protocol.donate.DonateType;
import cupcake.nikky.imperium.tower.protocol.packet.UserDonateLogPacket;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.invoice.TransactionCategory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DonateCurrencySample implements DonateSample<Integer> {

    private final Map<Integer, Integer> prices = new HashMap<>();

    public DonateCurrencySample() {
        prices.put(500, 500);
        prices.put(1_100, 1_000);
        prices.put(3_500, 3_000);
    }

    @Override
    public Map<Integer, Integer> findPrices() {
        return prices;
    }

    @Override
    public Consumer<Player> createProcess(@NotNull final DonateData donateData, @NotNull final Integer specialCurrency, final int price) {
        if (price == 0) return this.process(donateData, specialCurrency);
        return player -> new DonateExecutor().execute(player, TransactionCategory.GAME_CURRENCY, "currency-" + specialCurrency, price, true).thenAccept(invoiceResult -> {
            if (!invoiceResult.isSuccess()) {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, ChatColor.RED + "Ошибка во время выполнения операции: " + invoiceResult.getErrorMessage());
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                return;
            }

            process(donateData, specialCurrency).accept(player);
        });
    }

    public Consumer<Player> process(@NotNull final DonateData donateData, final int specialCurrency) {
        return player -> {
            player.<User>getBungeePlayer().getCurrencyHandler().depositCurrency(CurrencyType.FIRM, specialCurrency);

            PacketRegistry.write(Fantasy.gameServer().nameSpace(), new UserDonateLogPacket(player.getUniqueId(), DonateType.SPECIAL_CURRENCY, specialCurrency, Instant.now().toEpochMilli()));
            Fantasy.clientProvider().notification(player, "&aВы успешно приобрели: ", "&6" + specialCurrency + " &aФирм-монет");
            player.<User>getBungeePlayer().findDonateSettings().forceUpdateInDataBase();
        };
    }

}