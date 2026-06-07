package cupcake.nikky.imperium.tower.storage;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.currency.UserCurrencyHandler;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.donate.DonateData;
import cupcake.nikky.imperium.tower.protocol.donate.DonateType;
import cupcake.nikky.imperium.tower.protocol.packet.UserDonateLogPacket;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.menu.Menu;
import me.anfanik.steda.api.menu.MenuSession;
import me.anfanik.steda.api.menu.content.exact.StaticContentProvider;
import me.anfanik.steda.api.menu.item.MenuItem;
import me.anfanik.steda.api.utility.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.module.confirm.ConfirmRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PlayerGlobalStorage {

    private final User user;

    public void open() {
        val data = user.getGlobalStorageData();
        val lastOpenedPage = data.getLastOpenedPage();
        this.open(lastOpenedPage);
    }

    public void open(final int page) {
        val data = user.getGlobalStorageData();

        val pages = data.findAllPages();
        StoragePage currentPage = pages.get(page);

        val purchasedPages = user.findDonateSettings().donateData().getPurchasedStoragePages();
        if (currentPage == null && page <= purchasedPages) {
            currentPage = new StoragePage(new ItemStack[54], 18);
            user.getGlobalStorageData().registerPage(page, currentPage);
        } else if (currentPage == null) return;

        val blockedSlots = currentPage.getBlockedSlots();
        val items = currentPage.getUserStorageItems();

        val finalCurrentPage = currentPage;

        val menuBuilder = Menu.builder()
                .title("Хранилище #" + page)
                .chestRows(6)
                .setLockUserInventory(false)
                .disableStickiness()
                .onOpen(menuSession -> Fantasy.clientProvider().storagePacket(menuSession.getPlayer()))
                .onClose(menuSession -> this.updateStorage(menuSession, finalCurrentPage));
        val provider = new StaticContentProvider();

        int slot = 0;

        for (ItemStack item : items) {
            provider.setItem(slot++, MenuItem.createStatic(item == null ? new ItemStack(Material.AIR) : item).disableStickiness());
        }

        for (int i = (54 - blockedSlots); i < 54; i++) {
            val index = currentPage.getInitBlockedSlots() - blockedSlots;
            provider.setItem(i, MenuItem.createDynamic(session -> createBlockedSlotItemStack(index), (session, menuClick) -> {
                if (session.getPlayer().getItemOnCursor() != null && session.getPlayer().getItemOnCursor().getType() != Material.AIR) return;

                Pair<CurrencyType, Integer> price = null;

                if (menuClick.getType() == ClickType.LEFT || menuClick.getType() == ClickType.SHIFT_LEFT) {
                    price = new Pair<>(CurrencyType.GRAN, this.getPrice(index));
                } else if (menuClick.getType() == ClickType.RIGHT || menuClick.getType() == ClickType.SHIFT_RIGHT) {
                    price = new Pair<>(CurrencyType.FIRM, this.getAlternatePrice(index));
                } else return;

                boolean canBuy = price.getFirst() == CurrencyType.FIRM ? user.findDonateSettings().donateData().getTotalCurrency() > price.getSecond() : canBuy(price);
                String description = "&fРазблокировать новый слот за &6" + price.getSecond() + " " + price.getFirst().getTextTransformer().apply(price.getSecond()) + "&f?";
                if (price.getFirst() == CurrencyType.FIRM) description += " &cНе сохраняется после вайпа.";

                if (!canBuy) {
                    Fantasy.clientProvider().notification(session.getPlayer(), SystemNotificationType.ERROR, "Ваших средств недостаточно");
                } else {
                    session.getPlayer().closeInventory();
                    val finallyPrice = price;

                    ConfirmRequest.get().request(session.getPlayer(), description, accepted -> {
                        if (!accepted) {
                            this.open();
                            return;
                        }
                        val success = finallyPrice.getFirst() == CurrencyType.FIRM ?
                                new UserCurrencyHandler(user).withdrawDonateCurrencyWithPriority(finallyPrice.getSecond()) :
                                new UserCurrencyHandler(user).withdrawCurrency(finallyPrice.getFirst(), finallyPrice.getSecond());
                        if (success) {
                            finalCurrentPage.setBlockedSlots(blockedSlots - 1);
                            Fantasy.clientProvider().notification(session.getPlayer(), SystemNotificationType.FINE, "Вы успешно разблокировали", "новый слот в хранилище");
                            Fantasy.bukkitProvider().sound(session.getPlayer(), AdvancedSounds.TAP);

                            if (finallyPrice.getFirst() == CurrencyType.FIRM)
                                PacketRegistry.write(Fantasy.gameServer().nameSpace(), new UserDonateLogPacket(user.getUniqueId(), DonateType.STORAGE, "SLOT", Instant.now().toEpochMilli()));
                        } else {
                            Fantasy.clientProvider().notification(session.getPlayer(), SystemNotificationType.ERROR, "Произошла ошибка, попробуйте позже");
                            Fantasy.bukkitProvider().sound(session.getPlayer(), AdvancedSounds.CONFUSION);
                        }

                        this.open();
                    });
                }
            }));
        }

        user.getGlobalStorageData().setLastOpenedPage(page);
        menuBuilder.contentProvider(provider).build().open(user.getSpigot());
    }

    private ItemStack createBlockedSlotItemStack(final int index) {
        final List<String> lore = new ArrayList<>();

        val price = getPrice(index);
        val alternatePrice = getAlternatePrice(index);

        val priceLine = price + " " + CurrencyType.GRAN.getTextTransformer().apply(price);
        val altPriceLine = alternatePrice + " " + CurrencyType.FIRM.getTextTransformer().apply(alternatePrice);

        lore.add("&7Цена: &e" + priceLine);
        lore.add("&7Альтернативная цена: &c" + altPriceLine);
        lore.add(" ");
        lore.add("&r\uE3FE &7Разблокировать за основную цену");
        lore.add("&r\uE3FF &7Разблокировать за альтернативную цену");

        return Fantasy.newItemBuilder(Material.STAINED_GLASS_PANE)
                .setName("&cЗаблокировано")
                .setLore(lore)
                .setDurability(14)
                .build();
    }

    private void updateStorage(@NotNull final MenuSession session, final StoragePage page) {
        val lastSlot = 54 - page.getBlockedSlots();

        for (int i = 0; i < lastSlot; i++) {
            page.update(session.getInventory().getItem(i), i);
        }
    }

    private int getPrice(final int index) {
        return (index + 1) * 1000;
    }

    private int getAlternatePrice(final int index) {
        return 29;
    }

    private boolean canBuy(@NotNull final Pair<CurrencyType, Integer> priceData) {
        return new UserCurrencyHandler(user).getCurrency(priceData.getFirst()) >= priceData.getSecond();
    }

}
