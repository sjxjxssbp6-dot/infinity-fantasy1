package cupcake.nikky.imperium.tower.trade.player;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.trade.player.validate.ValidateSlotResult;
import cupcake.nikky.imperium.tower.utility.ImpInventoryUtility;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public record TradePlayerSession(UUID uuid, TradePlayerData firstTrader, TradePlayerData secondTrader, TradePlayerUpdater updater) {

    public TradePlayerSession(TradePlayerData firstTrader, TradePlayerData secondTrader, TradePlayerUpdater updater) {
        this(UUID.randomUUID(), firstTrader, secondTrader, updater);
    }

    public boolean isTrader(@NotNull final UUID uuid) {
        return this.firstTrader.getUser().getUniqueId().equals(uuid) || this.secondTrader.getUser().getUniqueId().equals(uuid);
    }

    public boolean isAllOnline() {
        return !this.firstTrader.getUser().isInvalidated() && !this.secondTrader.getUser().isInvalidated() &&
                this.firstTrader.getUser().getSpigot() != null && this.secondTrader.getUser().getSpigot() != null;
    }

    public boolean isEmpty() {
        return firstTrader.getFirm() == 0 && firstTrader.getGran() == 0 && firstTrader.getSlots().isEmpty() &&
                secondTrader.getFirm() == 0 && secondTrader.getGran() == 0 && secondTrader.getSlots().isEmpty();
    }

    public boolean isSomeConfirmed() {
        return firstTrader.isConfirm() || secondTrader.isConfirm();
    }

    public boolean isAllConfirmed() {
        return firstTrader.isConfirm() && secondTrader.isConfirm();
    }

    public void closeSession(@NotNull final UUID initialUuid) {
        val closed = this.findData(initialUuid);
        if (closed == null) return;

        val firstPlayer = firstTrader.getUser().getSpigot();
        val secondPlayer = secondTrader.getUser().getSpigot();

        if (!this.isAllConfirmed()) {
            if (closed.equals(firstTrader)) {
                this.updater().sendClose(secondTrader);
                if (secondPlayer != null) {
                    Fantasy.clientProvider().notification(secondPlayer, SystemNotificationType.ERROR, "Обмен был прерван", "противоположной стороной");
                    Fantasy.bukkitProvider().sound(secondPlayer, AdvancedSounds.CONFUSION);
                }
            } else {
                this.updater().sendClose(firstTrader);
                if (firstPlayer != null) {
                    Fantasy.clientProvider().notification(firstPlayer, SystemNotificationType.ERROR, "Обмен был прерван", "противоположной стороной");
                    Fantasy.bukkitProvider().sound(firstPlayer, AdvancedSounds.CONFUSION);
                }
            }
        }
    }

    public TradePlayerData findParticipant(@NotNull final TradePlayerData data) {
        return firstTrader.equals(data) ? secondTrader : firstTrader;
    }

    public TradePlayerData findData(@NotNull final UUID uuid) {
        if (firstTrader.getUser().getUniqueId().equals(uuid)) return firstTrader;
        if (secondTrader.getUser().getUniqueId().equals(uuid)) return secondTrader;
        return null;
    }

    public void tryProcessTrade() {
        if (!this.isAllConfirmed()) return;
        if (!this.secondTrader.validateCurrentCurrency() || !this.firstTrader.validateCurrentCurrency()) return;

        val firstTraderPlayer = this.firstTrader.getUser().getSpigot();
        val secondTraderPlayer = this.secondTrader.getUser().getSpigot();
        if (firstTraderPlayer == null || secondTraderPlayer == null) return;

        var resultFirst = this.secondTrader.validateCurrentSlots(firstTrader.convertCurrentSlots());
        if (resultFirst != ValidateSlotResult.ACCEPT) {
            Fantasy.bukkitProvider().sound(secondTraderPlayer, AdvancedSounds.CONFUSION);
            Fantasy.clientProvider().notification(secondTraderPlayer, SystemNotificationType.ERROR, resultFirst.getError());
        }

        var resultSecond = this.firstTrader.validateCurrentSlots(secondTrader.convertCurrentSlots());
        if (resultSecond != ValidateSlotResult.ACCEPT) {
            Fantasy.bukkitProvider().sound(firstTraderPlayer, AdvancedSounds.CONFUSION);
            Fantasy.clientProvider().notification(firstTraderPlayer, SystemNotificationType.ERROR, resultSecond.getError());
        }

        if (resultFirst != ValidateSlotResult.ACCEPT && resultSecond != ValidateSlotResult.ACCEPT) return;

        this.processCurrency();
        this.processItems(firstTraderPlayer, secondTraderPlayer);

        Fantasy.bukkitProvider().sound(firstTraderPlayer, AdvancedSounds.COINS_RECEIVE);
        Fantasy.bukkitProvider().sound(secondTraderPlayer, AdvancedSounds.COINS_RECEIVE);

        Fantasy.clientProvider().notification(firstTraderPlayer, SystemNotificationType.FINE, "Обмен был успешно совершен");
        Fantasy.clientProvider().notification(secondTraderPlayer, SystemNotificationType.FINE, "Обмен был успешно совершен");

        this.updater().sendClose(firstTrader);
        this.updater().sendClose(secondTrader);
    }

    private void processItems(@NotNull final Player firstTraderPlayer, @NotNull final Player secondTraderPlayer) {
        val air = new ItemStack(Material.AIR);

        val firstTraderItems = this.firstTrader.getSlots().stream().map(slot -> {
            val item = firstTraderPlayer.getInventory().getItem(slot);
            firstTraderPlayer.getInventory().setItem(slot, air);
            return item;
        }).toList();

        val secondTraderItems = this.secondTrader.getSlots().stream().map(slot -> {
            val item = secondTraderPlayer.getInventory().getItem(slot);
            secondTraderPlayer.getInventory().setItem(slot, air);
            return item;
        }).toList();

        firstTraderPlayer.updateInventory();
        secondTraderPlayer.updateInventory();

        secondTraderItems.forEach(itemStack -> {
            val item = Fantasy.itemProvider().find(itemStack);
            if (item == null) return;

            if (ImpInventoryUtility.canMoveToInventory(firstTraderPlayer.getInventory(), item, itemStack.getAmount())) {
                firstTraderPlayer.getInventory().addItem(itemStack);
            } else {
                Fantasy.bukkitProvider().spawnItem(firstTraderPlayer.getLocation(), firstTraderPlayer.getUniqueId(), item.createNew(itemStack.getAmount()));
            }
        });
        firstTraderItems.forEach(itemStack -> {
            val item = Fantasy.itemProvider().find(itemStack);
            if (item == null) return;

            if (ImpInventoryUtility.canMoveToInventory(secondTraderPlayer.getInventory(), item, itemStack.getAmount())) {
                secondTraderPlayer.getInventory().addItem(itemStack);
            } else {
                Fantasy.bukkitProvider().spawnItem(secondTraderPlayer.getLocation(), secondTraderPlayer.getUniqueId(), item.createNew(itemStack.getAmount()));
            }
        });

        Bukkit.getLogger().info("TRADE ITEMS (%s | %s): %s ||| %s".formatted(
                firstTrader.getUser().getSpigot().getName(), secondTrader.getUser().getSpigot().getName(),
                firstTraderItems.stream()
                        .filter(item -> item.getItemMeta() != null)
                        .map(item -> item.getItemMeta().getDisplayName() + " x" + item.getAmount())
                        .collect(Collectors.joining(", ")),
                secondTraderItems.stream()
                        .filter(item -> item.getItemMeta() != null)
                        .map(item -> item.getItemMeta().getDisplayName() + " x" + item.getAmount())
                        .collect(Collectors.joining(", "))
        ));
    }

    private void processCurrency() {
        this.firstTrader.getUser().changeCurrency(CurrencyType.GRAN, -this.firstTrader.getGran() + this.secondTrader.getGran());
        this.secondTrader.getUser().changeCurrency(CurrencyType.GRAN, -this.secondTrader.getGran() + this.firstTrader.getGran());

        this.firstTrader.getUser().changeCurrency(CurrencyType.FIRM, -this.firstTrader.getFirm() + this.secondTrader.getFirm());
        this.secondTrader.getUser().changeCurrency(CurrencyType.FIRM, -this.secondTrader.getFirm() + this.firstTrader.getFirm());

        Bukkit.getLogger().info("TRADE CURRENCY (%s | %s): gran1 = %d; gran2 = %d; firm1 = %d; firm2 = %d;".formatted(
                firstTrader.getUser().getSpigot().getName(), secondTrader.getUser().getSpigot().getName(),
                firstTrader.getGran(), secondTrader.getGran(), firstTrader.getFirm(), secondTrader.getFirm()
        ));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradePlayerSession that = (TradePlayerSession) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
