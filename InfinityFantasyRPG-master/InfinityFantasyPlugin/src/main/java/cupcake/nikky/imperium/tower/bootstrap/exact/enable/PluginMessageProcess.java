package cupcake.nikky.imperium.tower.bootstrap.exact.enable;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.achievement.selection.AchievementSelection;
import cupcake.nikky.imperium.tower.beacon.context.BeaconContextProvider;
import cupcake.nikky.imperium.tower.bestiary.BestiarySelection;
import cupcake.nikky.imperium.tower.bootstrap.BootstrapProcess;
import cupcake.nikky.imperium.tower.character.CharacterProvider;
import cupcake.nikky.imperium.tower.client.protocol.QuestState;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.PacketService;
import cupcake.nikky.imperium.tower.connection.ConnectionProcess;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.donate.shop.ShopSelection;
import cupcake.nikky.imperium.tower.filter.LootFilterSelection;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.protocol.packet.*;
import cupcake.nikky.imperium.tower.selection.GeneralSelection;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.quest.Quest;
import cupcake.nikky.imperium.tower.quest.session.QuestSession;
import cupcake.nikky.imperium.tower.quest.session.QuestSessionProvider;
import cupcake.nikky.imperium.tower.storage.PlayerGlobalStorage;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeNpcProvider;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.metadata.MetadataType;
import cupcake.nikky.imperium.tower.utility.ImpInventoryUtility;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import cupcake.nikky.imperium.tower.utility.QuestUtility;
import dev.xdark.feder.NetUtil;
import lombok.val;
import me.func.mod.Anime;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.coupons.ICouponsService;

import java.time.Instant;

public class PluginMessageProcess implements BootstrapProcess {

    @Override
    public void invoke(@NotNull JavaPlugin javaPlugin) {
        Anime.createReader("fantasy:packet", (player, data) -> {
            val packetName = NetUtil.readUtf8(data);

            ClientPacket packet;
            try {
                packet = (ClientPacket) Class.forName(packetName).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                Bukkit.getLogger().warning(e.getClass().getSimpleName() + ": " + e.getMessage());
                return;
            }

            packet.serverRead(data);
            PacketService.getInstance().handle(player.getUniqueId(), packet);
        });

        Anime.createReader("imp:item-al-click", (player, byteBuf) -> {
            val index = byteBuf.readInt();
            val item = player.getInventory().getItem(index);

            if (item == null || item.getType() == Material.AIR) return;

            val imperiumItem = Fantasy.itemProvider().find(item);

            if (imperiumItem == null || imperiumItem.getModifierSettings().findPossibleByPlace(ModifierPlace.PREFIX, ModifierPlace.SUFFIX).isEmpty()) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.WARN, "Данный предмет не подлежит", "анализу Фирмолога");
                return;
            }

            new ItemAnalyzePacket(player, index).notice(2);
            new ItemAnalyzePacket(player, imperiumItem).notice(1);
        });

        Anime.createReader("imp:firm-click", (player, byteBuf) -> {
            if (player.<User>getBungeePlayer().findDonateSettings().donateData().isSubscribed()) {
                player.closeInventory();
                new ItemAnalyzePacket(player, null).notice(0);
            } else {
                Fantasy.clientProvider().notification(player, SystemNotificationType.WARN, "Для данного действия необходима", "премиум подписка");
            }
        });

        Anime.createReader("imp:achieve-click", (player, byteBuf) -> {
            player.closeInventory();
            new AchievementSelection(player).open();
        });

        Anime.createReader("imp:general-click", (player, byteBuf) -> {
            player.closeInventory();
            GeneralSelection.getInstance().open(player);
        });

        Anime.createReader("imp:battlepass", (player, byteBuf) -> {
            player.closeInventory();
            Fantasy.battlePassProvider().open(player);
        });

        Anime.createReader("imp:mount-use", (player, byteBuf) -> {
            val user = player.<User>getBungeePlayer();
            val questedPlayer = user.findQuestSettings();

            if (!questedPlayer.isPassed(10_000)) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR,  "Вам пока что не доступно", "данное действие");
                return;
            }

            player.closeInventory();

            val mountData = user.getCurrentMountData();
            if (!mountData.tryUnmount()) {
                if (!player.isOnGround()) {
                    mountData.setTime(Instant.now().toEpochMilli());
                } else if (mountData.getTime() + 2_000 <= Instant.now().toEpochMilli()) {
                    mountData.getMountType().apply(player);
                } else {
                    Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Подождите перед следующим", "призывом спутника");
                }
            }
        });

        Anime.createReader("imp:tp-navigate", (player, byteBuf) -> {
            player.closeInventory();
            Fantasy.getRespawnAndBeaconExecutor().execute(() -> BeaconContextProvider.getInstance().registerContext(player));
        });

        Anime.createReader("imp:tp-close", (player, byteBuf) -> BeaconContextProvider.getInstance().unregister(player.getUniqueId()));

        Anime.createReader("imp:filter-click", (player, byteBuf) -> {
            player.closeInventory();
            LootFilterSelection.getInstance().open(player);
        });

        Anime.createReader("imp:tp-click", (player, byteBuf) -> {
            val id = byteBuf.readInt();
            val context = BeaconContextProvider.getInstance().findByUuid(player.getUniqueId()).orElse(null);
            if (context == null) {
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
            } else {
                val object = context.findBeacon(id).orElse(null);

                if (object == null || !object.getBeaconSettings().canTeleport().test(player)) {
                    Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                    return;
                }

                val cost = context.findCost(object);

                if (Fantasy.userService().findById(player.getUniqueId()).map(user -> user.getCurrencyHandler().getCurrency(CurrencyType.GRAN) >= cost).orElse(false)) {
                    object.teleportToRespawn(player, cost);
                    Anime.close(player);
                } else {
                    Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Недостаточно средств", "для перемещения");
                    Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                }
            }
        });

        Anime.createReader("imp:quest-close", (player, byteBuf) -> {
            Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> user.getMetadataStorage().invalidateType(MetadataType.QUEST_CONTEXT));
            QuestSessionProvider.getInstance().invalidateSession(player.getUniqueId());
        });

        Anime.createReader("imp:quest-lclick", (player, byteBuf) -> { // click in quest list to show/hide quest description
            val questId = byteBuf.readInt();
            QuestSessionProvider.getInstance().findSession(player.getUniqueId()).ifPresent(questSession -> questSession.tryOpenContext(questId));
        });

        Anime.createReader("imp:trade-click", (player, byteBuf) -> {
            val id = byteBuf.readInt();

            TradeNpcProvider.getInstance().findSession(player.getUniqueId()).ifPresent(tradeSession -> {
                val container = tradeSession.findById(id);
                if (container == null) return;

                Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> {
                    if (container.getMaxCount() != -1 && container.getCurrentCount() == container.getMaxCount()) {
                        Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Достигнут лимит покупки!", "Вы сможете приобрести снова", "этот предмет после обновления торговца");
                        return;
                    }

                    var item = container.getItemGenerator().apply(player);

                    if (!ImpInventoryUtility.canMoveToInventory(player.getInventory(), item, 1)) {
                        Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Недостаточно места в инвентаре");
                        return;
                    }

                    val currency = switch (container.getCurrency()) {
                        case GRAN -> CurrencyType.GRAN;
                        case MINES -> CurrencyType.MINES;
                        case FLOWER -> CurrencyType.FLOWER;
                    };

                    if (user.getCurrencyHandler().withdrawCurrency(currency, container.getPrice())) {
                        if (container.getOnUpdate() != null)
                            container.getOnUpdate().accept(player);

                        player.getInventory().addItem(NbtUtility.clearSessionTags(item.createNew()));

                        Fantasy.clientProvider().notification(player, SystemNotificationType.FINE, "Вы успешно приобрели предмет");
                        new ImperiumTradePacket(user, tradeSession).notice(1);
                        Fantasy.bukkitProvider().sound(player, AdvancedSounds.COINS_GIVE);
                    } else {
                        Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, String.format("Недостаточно %s для покупки", currency.getTextTransformer().apply(100)));
                    }
                });
            });
        });

        Anime.createReader("imp:trade-confirm", (player, byteBuf) -> {
            val tradeSession = TradeNpcProvider.getInstance().findSession(player.getUniqueId()).orElse(null);
            val user = Fantasy.userService().findById(player.getUniqueId()).orElse(null);

            if (tradeSession == null || user == null) return;

            val price = tradeSession.calculateFullPrice();

            if (user.changeCurrency(CurrencyType.GRAN, price)) {
                val air = new ItemStack(Material.AIR);
                tradeSession.getItemsToSell().forEach((index, item) -> player.getInventory().setItem(index, air));

                tradeSession.getItemsToSell().clear();
            }

            Fantasy.bukkitProvider().sound(player, AdvancedSounds.COINS_RECEIVE);
            new ImperiumTradePacket(user, tradeSession).notice(1);
        });

        Anime.createReader("imp:trade-sell", (player, byteBuf) -> {
            val index = byteBuf.readInt();
            val packet = new ImperiumTradeSellPacket(player, index);
            packet.notice(0);

            Fantasy.userService().findById(player.getUniqueId())
                    .ifPresent(user -> TradeNpcProvider.getInstance().findSession(player.getUniqueId()).
                    ifPresent(tradeSession -> new ImperiumTradePacket(user, tradeSession).notice(1)));
         });

        Anime.createReader("imp:trade-close", (player, byteBuf) -> {
            TradeNpcProvider.getInstance().findSession(player.getUniqueId()).ifPresent(tradeSession -> tradeSession.setClosed(true));
            TradeNpcProvider.getInstance().unregister(player.getUniqueId());
        });

        Anime.createReader("imp:loaded", (player, byteBuf) -> {
            val user = Fantasy.userService().findById(player.getUniqueId()).orElse(null);
            if (user == null) {
                Fantasy.runLater(() -> {
                    val repeatUserGetter = Fantasy.userService().findById(player.getUniqueId()).orElse(null);
                    if (repeatUserGetter == null)
                        player.kickPlayer("Error with modification loading.");
                }, 20);
            } else {
                new ConnectionProcess(user).processModificationConnect();
            }
        });

        Anime.createReader("imp:quests-click", (player, byteBuf) -> {
            val user = player.<User>getBungeePlayer();
            val questedPlayer = user.findQuestSettings();

            player.closeInventory();

            val session = QuestSessionProvider.getInstance().createSession(player, Fantasy.questService().findAllQuests().stream().map(Quest::getId).toList());
            session.canReceive((player1, quest) -> {
                val data = user.findDonateSettings().donateData();
                return data != null && data.isSubscribed();
            }).canView((player1, quest) -> questedPlayer.isPassed(quest.getId()) || quest.canReceive(player) || questedPlayer.findInProgress(quest.getId()).isPresent()).openContext();
        });

        Anime.createReader("imp:quest-cont-click", (player, byteBuf) -> {
            val questId = byteBuf.readInt();
            val state = QuestState.of(byteBuf.readInt());

            val donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();
            if (donateData == null) return;

            val user = player.<User>getBungeePlayer();
            val npc = QuestSessionProvider.getInstance().findSession(player.getUniqueId()).map(QuestSession::npcType).orElse(null);
            val questedPlayer = user.findQuestSettings();

            val currentQuestState = QuestUtility.findCurrentQuestState(questedPlayer, questId);
            if (currentQuestState != state || currentQuestState == null) {
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                return;
            }
            val isSubscribed = donateData.isSubscribed();
            val canClick = QuestUtility.canClick(QuestUtility.findCurrentPlayerQuest(questedPlayer, questId), currentQuestState, npc, isSubscribed);

            if (currentQuestState == QuestState.NOT_RECEIVE) {
                if (!canClick && npc == null) {
                    Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Отсутствует премиум-подписка", "для данного действия");
                    return;
                }

                if (!QuestUtility.findCurrentPlayerQuest(questedPlayer, questId).canReceive(player)) return;

                Fantasy.questService().findQuest(questId).ifPresent(quest -> Fantasy.userService().findById(player.getUniqueId()).ifPresent(quest::onAccept));
            } else if (currentQuestState == QuestState.COMPLETE_WITHOUT_PASS) {
                if (!canClick && npc == null) {
                    Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Отсутствует премиум-подписка", "для данного действия");
                    return;
                }

                val quest = questedPlayer.findInProgress(questId).orElse(null);

                if (quest != null && quest.tryGiveReward(player)) {
                    quest.onPassQuest(player);

                    CharacterProvider.getInstance().findAll().stream()
                            .filter(character1 -> character1.getType() == quest.getPassQuestNpc())
                            .forEach(character1 -> new ImperiumGlowPacket(player, character1.getEntityId()).notice(1));
                }
            }

            QuestSessionProvider.getInstance().findSession(player.getUniqueId()).ifPresent(questSession -> {
                val icons = questSession.generateIcons();
                questSession.updateIcons(icons);
                val nextQuest = questSession.findQuestToShow(icons.keySet());

                if (questedPlayer.findInProgress(questId).isPresent() || questedPlayer.isPassed(questId) && nextQuest < questId) {
                    questSession.updateCurrentContext();
                } else {
                    if (nextQuest == questId || nextQuest == -1)
                        questSession.updateCurrentContext();
                    else questSession.tryOpenContext(nextQuest);
                }
            });
        });

        Anime.createReader("imp:storage-click", (player, byteBuf) -> {
            val user = player.<User>getBungeePlayer();
            val donateData = user.findDonateSettings().donateData();
            if (donateData.isSubscribed()) {
                Fantasy.runLater(() -> {
                    Anime.close(player);
                    new PlayerGlobalStorage(user).open();
                });
            } else {
                Fantasy.clientProvider().notification(player, SystemNotificationType.WARN, "Для данного действия необходима", "премиум подписка");
            }
        });

        Anime.createReader("imp:tree-click", (player, byteBuf) -> {
            player.closeInventory();
            Fantasy.treeProvider().openScheme(player);
        });

        Anime.createReader("imp:d-shop-click", (player, byteBuf) -> {
            player.closeInventory();
            ShopSelection.getInstance().open(player, player.<User>getBungeePlayer().findDonateSettings().donateData(), ICouponsService.get().getDiscount(player.getUniqueId()));
        });

        Anime.createReader("imp:best-click", (player, byteBuf) -> {
            player.closeInventory();
            new BestiarySelection(player).open();
        });

        Anime.createReader("imp:craft-click", (player, byteBuf) -> {
            player.closeInventory();
        });
    }

}
