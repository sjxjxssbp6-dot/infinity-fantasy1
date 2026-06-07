package cupcake.nikky.imperium.tower.bootstrap.exact.enable;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.bootstrap.BootstrapProcess;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.packet.PacketService;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.*;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.*;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench.CraftBenchSlotClick;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench.CraftBenchTryCraft;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.debug.AttributeClient;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.debug.AttributeDebugPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.pandora.PandoraChestClickPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.trade.player.*;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.donate.DonateType;
import cupcake.nikky.imperium.tower.protocol.packet.UserDonateLogPacket;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.storage.PlayerGlobalStorage;
import cupcake.nikky.imperium.tower.storage.StoragePage;
import cupcake.nikky.imperium.tower.trade.player.TradePlayerProvider;
import cupcake.nikky.imperium.tower.trade.player.validate.ValidateSlotResult;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.module.confirm.ConfirmRequest;

import java.time.Instant;

public class ClientPacketListenerProcess implements BootstrapProcess {

    @Override
    public void invoke(@NotNull JavaPlugin javaPlugin) {
        if (Fantasy.gameServer().isTest() && Fantasy.DEBUG) {
            Fantasy.runAsyncTimer(() -> Bukkit.getOnlinePlayers().forEach(player -> {
                var user = player.<User>getBungeePlayer();
                user.sendPacket(new AttributeDebugPacket(
                        user.getAttributes().getAll().stream()
                        .map(pair -> new AttributeClient(
                                pair.getFirst().name(),
                                pair.getSecond().getBaseValue(),
                                pair.getSecond().getAdditional(),
                                pair.getSecond().getIncrease(),
                                pair.getSecond().getMore()
                        )).toList()));
            }), 4);
        }

        PacketService.getInstance().addServerListener(PandoraChestClickPacket.class, (uuid, pandoraChestClickPacket) -> {
            val player = Bukkit.getPlayer(uuid);
            if (player == null) return;

            var user = player.<User>getBungeePlayer();
            if (user.findFragmentSettings().isInFragment()) {
                var pandoraData = user.findPandoraSettings().findDataByUUID(pandoraChestClickPacket.getUuid());
                if (pandoraData != null) user.findPandoraSettings().interactPandora(pandoraData);
            }
        });

        PacketService.getInstance().addServerListener(CaseChangeAmountPacket.class, (uuid, caseChangeAmountPacket) -> {
            val player = Bukkit.getPlayer(uuid);
            if (player == null) return;

            var amount = caseChangeAmountPacket.getAmount();

            if (amount > 1 && !player.<User>getBungeePlayer().findDonateSettings().donateData().isSubscribed()) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR,
                        "Изменять количество открытий",
                        "можно только с премиум подпиской"
                );
            } else if (amount >= 1 && amount <= 5) {
                val user = player.<User>getBungeePlayer();
                user.findCaseSettings().setRollAmount(caseChangeAmountPacket.getAmount());
                user.sendPacket(caseChangeAmountPacket);
            }
        });

        PacketService.getInstance().addServerListener(CaseClickPacket.class, (uuid, caseClickPacket) -> {
            val player = Bukkit.getPlayer(uuid);
            if (player == null) return;

            val user = player.<User>getBungeePlayer();
            if (user.findCaseSettings().getCaseSessionData().getCurrentAmount() > 1) {
                if (!player.<User>getBungeePlayer().findDonateSettings().donateData().isSubscribed()) {
                    Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR,
                            "Изменять количество открытий",
                            "можно только с премиум подпиской"
                    );
                    return;
                }
            }

            user.findCaseSettings().roll();
        });

        PacketService.getInstance().addServerListener(SettingsPacket.class, (uuid, settingsPacket) -> {
            val player = Bukkit.getPlayer(uuid);
            if (player == null) return;

            val user = player.<User>getBungeePlayer();
            val settings = settingsPacket.getSettings();

            user.findClientSettings().handleClientSettings(settings);
        });

        PacketService.getInstance().addServerListener(CraftBenchTryCraft.class, (uuid, craftBenchTryCraft) -> {
            val player = Bukkit.getPlayer(uuid);
            if (player == null) return;

            val user = player.<User>getBungeePlayer();
            user.findSyncCraftBenchSettings().tryCraft(craftBenchTryCraft.getTitle(), craftBenchTryCraft.getSubTitle());
        });

        PacketService.getInstance().addServerListener(CraftBenchSlotClick.class, (uuid, craftBenchSlotClick) -> {
            val player = Bukkit.getPlayer(uuid);
            if (player == null) return;

            val user = player.<User>getBungeePlayer();
            user.findSyncCraftBenchSettings().clickScreen(craftBenchSlotClick.getSlot());
        });

        PacketService.getInstance().addServerListener(TradeUpdateCurrencyPacket.class, (uuid, tradeUpdateCurrencyPacket) -> {
            val session = TradePlayerProvider.getInstance().findSession(uuid);
            if (session == null || session.isSomeConfirmed()) return;

            val data = session.findData(uuid);
            if (data == null) return;

            data.tryToUpdateCurrency(CurrencyType.GRAN, tradeUpdateCurrencyPacket.getGran());
            data.tryToUpdateCurrency(CurrencyType.FIRM, tradeUpdateCurrencyPacket.getFirm());
            session.updater().sendCurrencyUpdate();
        });

        PacketService.getInstance().addServerListener(TradeTryConfirmPacket.class, (uuid, tradeTryConfirmPacket) -> {
            val session = TradePlayerProvider.getInstance().findSession(uuid);
            if (session == null) return;

            val data = session.findData(uuid);
            if (data == null) return;

            if (session.isEmpty()) {
                tradeTryConfirmPacket.setAccepted(false);
                data.getUser().sendPacket(tradeTryConfirmPacket);
                Fantasy.clientProvider().notification(data.getUser().getSpigot(), SystemNotificationType.ERROR, "Предложение обмена пустое");
                return;
            }

            if (!data.validateCurrentCurrency()) {
                val player = data.getUser().getSpigot();
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Недостаточно валюты");
                tradeTryConfirmPacket.setAccepted(false);
                data.getUser().sendPacket(tradeTryConfirmPacket);
                return;
            }

            val result = data.validateCurrentSlots(session.findParticipant(data).convertCurrentSlots());
            if (result != ValidateSlotResult.ACCEPT) {
                val player = data.getUser().getSpigot();
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, result.getError());

                tradeTryConfirmPacket.setAccepted(false);
                data.getUser().sendPacket(tradeTryConfirmPacket);
                return;
            }

            data.setConfirm(!data.isConfirm());
            tradeTryConfirmPacket.setAccepted(true);
            data.getUser().sendPacket(tradeTryConfirmPacket);

            val participant = session.findParticipant(data);
            participant.getUser().sendPacket(new TradeParticipantConfirmPacket(data.isConfirm()));

            session.tryProcessTrade();
        });

        PacketService.getInstance().addServerListener(TradeStatePacket.class, (uuid, tradeStatePacket) -> {
            if (tradeStatePacket.isClosed()) {
                TradePlayerProvider.getInstance().invalidateSession(uuid);
            } else {
                val session = TradePlayerProvider.getInstance().findSession(uuid);
                if (session == null) return;

                if (session.isAllOnline()) {
                    session.updater().sendTraderName(uuid);
                } else {
                    session.updater().sendClose(session.firstTrader());
                    session.updater().sendClose(session.secondTrader());
                }
            }
        });

        PacketService.getInstance().addServerListener(TradeTryClickItemPacket.class, (uuid, tradeTryClickItemPacket) -> {
            val session = TradePlayerProvider.getInstance().findSession(uuid);
            val player = Bukkit.getPlayer(uuid);
            if (player == null) return;

            if (session == null) {
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                return;
            }

            if (session.isSomeConfirmed()) {
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "В данный момент нельзя", "изменять состояние обмена");
                return;
            }

            val data = session.findData(uuid);
            if (data == null) {
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                return;
            }

            val slot = tradeTryClickItemPacket.getSlot();
            val contains = data.containsSlot(slot);
            var accepted = false;

            if (contains) accepted = data.removeSlot(slot);
            else accepted = data.tryToAddSlot(slot);

            if (!accepted) {
                if (!contains) {
                    Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Данный предмет нельзя передать");
                }
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                return;
            }

            tradeTryClickItemPacket.setAccepted(accepted);

            data.getUser().sendPacket(tradeTryClickItemPacket);
            session.updater().sendItemsUpdate();
            Fantasy.bukkitProvider().sound(player, AdvancedSounds.CLICK_1);
        });

        PacketService.getInstance().addServerListener(StorageClickPacket.class, (uuid, storageClickPacket) -> {
            val player = Bukkit.getPlayer(uuid);
            if (player == null || player.getItemOnCursor() != null && player.getItemOnCursor().getType() != Material.AIR) return;

            val page = storageClickPacket.getPage();
            val user = player.<User>getBungeePlayer();
            val donateData = user.findDonateSettings().donateData();
            val purchased = donateData.getPurchasedStoragePages();
            if (page == purchased + 2) {
                if (donateData.getTotalCurrency() < 999) {
                    Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Ваших средств недостаточно");
                    return;
                }

                player.closeInventory();
                ConfirmRequest.get().request(player, "Купить &eхранилище &c#" + page + " &fза &e999&r\uE104?", accepted -> {
                    if (!accepted) {
                        new PlayerGlobalStorage(user).open();
                        return;
                    }

                    if (user.getCurrencyHandler().withdrawDonateCurrencyWithPriority(999)) {
                        donateData.setPurchasedStoragePages(purchased + 1);
                        user.getGlobalStorageData().registerPage(page, new StoragePage(new ItemStack[54], 18));

                        PacketRegistry.write(Fantasy.gameServer().nameSpace(), new UserDonateLogPacket(user.getUniqueId(), DonateType.STORAGE, "PAGE", Instant.now().toEpochMilli()));

                        Fantasy.clientProvider().notification(player, SystemNotificationType.FINE, "&aВы успешно купили", "&eхранилище &c#" + page);
                        Fantasy.bukkitProvider().sound(player, AdvancedSounds.COINS_GIVE);
                    } else {
                        Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Произошла ошибка во время", "выполнения операции");
                        new PlayerGlobalStorage(user).open();
                        return;
                    }

                    new PlayerGlobalStorage(user).open(page);
                });
            } else if (page <= purchased + 1) {
                if (user.getGlobalStorageData().getLastOpenedPage() == page) return;
                player.closeInventory();
                new PlayerGlobalStorage(user).open(page);
            }
        });
    }

}
