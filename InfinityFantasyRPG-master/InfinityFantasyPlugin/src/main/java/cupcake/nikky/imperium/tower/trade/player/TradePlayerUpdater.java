package cupcake.nikky.imperium.tower.trade.player;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player.TradePlayerNamePacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player.TradeStatePacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player.TradeUpdateCurrencyPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player.TradeUpdateItemsPacket;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import lombok.val;
import me.func.mod.Anime;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.permissions.IPermissionService;

import java.util.UUID;
import java.util.stream.Collectors;

public record TradePlayerUpdater(@NotNull TradePlayerData firstTrader, @NotNull TradePlayerData secondTrader) {

    public void sendTraderName(@NotNull final UUID uuid) {
        val oppositeTrader = firstTrader.getUser().getUniqueId().equals(uuid) ? secondTrader : firstTrader;
        val currentTrader = oppositeTrader.equals(firstTrader) ? secondTrader : firstTrader;

        IPermissionService.get().getPermissionContext(oppositeTrader.getUser().getUniqueId()).whenComplete((context, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }

            val tradePlayerNamePacket = new TradePlayerNamePacket(context.toDisplayName());
            currentTrader.getUser().sendPacket(tradePlayerNamePacket);
        });
    }

    public void sendCurrencyUpdate() {
        val firstTraderPacket = new TradeUpdateCurrencyPacket(firstTrader.getGran(), firstTrader.getFirm());
        secondTrader.getUser().sendPacket(firstTraderPacket);

        val secondTraderPacket = new TradeUpdateCurrencyPacket(secondTrader.getGran(), secondTrader.getFirm());
        firstTrader.getUser().sendPacket(secondTraderPacket);
    }

    public void sendItemsUpdate() {
        val firstPlayer = firstTrader.getUser().getSpigot();
        val secondPlayer = secondTrader.getUser().getSpigot();
        if (firstPlayer == null || secondPlayer == null) return;

        val firstTraderPacket = new TradeUpdateItemsPacket(firstTrader.getSlots().stream().map(slot -> firstPlayer.getInventory().getItem(slot)).collect(Collectors.toList()));
        secondTrader.getUser().sendPacket(firstTraderPacket);

        val secondTraderPacket = new TradeUpdateItemsPacket(secondTrader.getSlots().stream().map(slot -> secondPlayer.getInventory().getItem(slot)).collect(Collectors.toList()));
        firstTrader.getUser().sendPacket(secondTraderPacket);
    }

    public void sendClose(@NotNull final TradePlayerData tradeData) {
        val packet = new TradeStatePacket(true);
        tradeData.getUser().sendPacket(packet);
    }

    public void sendOpen() {
        val packet = new TradeStatePacket(false);
        val firstTraderPlayer = firstTrader.getUser().getSpigot();
        val secondTraderPlayer = secondTrader.getUser().getSpigot();
        val allExists = firstTraderPlayer != null && secondTraderPlayer != null;

        if (!allExists) {
            if (firstTraderPlayer != null) {
                Fantasy.bukkitProvider().sendMessage(firstTraderPlayer, ChatType.SYSTEM, "Игрок оффлайн");
                Fantasy.bukkitProvider().sound(firstTraderPlayer, AdvancedSounds.CONFUSION);
            }
            if (secondTraderPlayer != null) {
                Fantasy.bukkitProvider().sendMessage(secondTraderPlayer, ChatType.SYSTEM, "Игрок оффлайн");
                Fantasy.bukkitProvider().sound(secondTraderPlayer, AdvancedSounds.CONFUSION);
            }
            return;
        }

        firstTraderPlayer.closeInventory();
        secondTraderPlayer.closeInventory();

        firstTrader.getUser().sendPacket(packet);
        secondTrader.getUser().sendPacket(packet);
    }

}
