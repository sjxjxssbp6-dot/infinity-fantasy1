package cupcake.nikky.imperium.tower.command;

import com.github.neitexx.bukkit.command.CommandBuilder;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.achievement.Achievements;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.beacon.context.BeaconContextProvider;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsType;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.donate.shop.ShopSelection;
import cupcake.nikky.imperium.tower.edge.EdgeCoreList;
import cupcake.nikky.imperium.tower.fraction.FractionChoicer;
import cupcake.nikky.imperium.tower.inventory.StorageSerialization;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.gear.tool.ToolItem;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.Service;
import cupcake.nikky.imperium.tower.protocol.database.Collections;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.protocol.packet.UserTransferAccessibilityPacket;
import cupcake.nikky.imperium.tower.protocol.packet.connection.session.SessionState;
import cupcake.nikky.imperium.tower.protocol.packet.connection.session.SessionUpdatePacket;
import cupcake.nikky.imperium.tower.storage.StorageData;
import cupcake.nikky.imperium.tower.storage.StoragePage;
import cupcake.nikky.imperium.tower.trade.player.TradePlayerProvider;
import cupcake.nikky.imperium.tower.trade.player.request.TradeRequestData;
import cupcake.nikky.imperium.tower.trade.player.request.TradeRequestStorage;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.controller.ExpType;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import cupcake.nikky.imperium.tower.utility.PermissionUtility;
import lombok.val;
import me.anfanik.steda.api.menu.Menu;
import me.anfanik.steda.api.menu.content.exact.StaticContentProvider;
import me.anfanik.steda.api.menu.item.MenuItem;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.account.IAccountService;
import ru.cristalix.core.coupons.ICouponsService;
import ru.cristalix.core.realm.RealmId;
import ru.cristalix.core.transfer.ITransferService;
import ru.cristalix.wada.Wada;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class GeneralCommands {

    public GeneralCommands(@NotNull final JavaPlugin javaPlugin) {
        CommandBuilder.get().addCommandName("deposit").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (strings.length < 3) {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&c/deposit [gran/firm] [игрок] [кол-во Гран/Фирм-монет]");
            } else {
                val currencyType = strings[0].equalsIgnoreCase("gran") ? CurrencyType.GRAN : strings[0].equalsIgnoreCase("firm") ? CurrencyType.FIRM : null;
                if (currencyType == null) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&c/deposit [gran/firm] [игрок] [кол-во Гран/Фирм-монет]");
                    return Optional.empty();
                }

                val targetPlayer = Bukkit.getPlayer(strings[1]);
                val currency = NumberUtility.parseInteger(strings[2]);

                if (currency <= 0) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&c/deposit [gran, firm] [игрок] [кол-во Гран/Фирм-монет]");
                    return Optional.empty();
                }

                if (targetPlayer == null) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cИгрок не в сети либо на другом реалме.");
                } else {
                    Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> Fantasy.userService().findById(targetPlayer.getUniqueId()).ifPresent(targetUser -> {
                        if (user.getCurrencyHandler().withdrawCurrency(currencyType, currency)) {
                            if (targetUser.getCurrencyHandler().depositCurrency(currencyType, currency)) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&7Вы перевели &e" + currency + " " +
                                        currencyType.getTextTransformer().apply(currency) + " &7игроку &e" + targetPlayer.getName());

                                Fantasy.bukkitProvider().sendMessage(targetPlayer, ChatType.SYSTEM, "&7Вы получили &e" + currency + " " +
                                        currencyType.getTextTransformer().apply(currency) + " &7от игрока &e" + player.getName());
                            } else {
                                user.getCurrencyHandler().depositCurrency(CurrencyType.GRAN, currency);
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cИгрок не может в данный момент получить Граны");
                            }
                        } else {
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cНедостаточно средств");
                        }
                    }));
                }
            }

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("trade").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;

            if (strings.length > 2 || strings.length == 0) {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&f/trade [игрок]");
                return Optional.empty();
            }

            if (strings.length == 1) {
                if (Fantasy.userService().findById(player.getUniqueId()).map(user -> user.findNotifySettings().isDoNotDisturb()).orElse(false)) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fНельзя отправлять запросы с включенным режимом `Не беспокоить`.");
                    return Optional.empty();
                }

                var receiver = Bukkit.getPlayer(strings[0]);
                if (receiver == null || !receiver.isOnline()) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fИгрок не в сети либо на другом реалме.");
                    return Optional.empty();
                } else if (player.getUniqueId().equals(receiver.getUniqueId()) && !PermissionUtility.isOperator(player)) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fВы не можете предложить обмен самому себе.");
                    return Optional.empty();
                }

                if (Fantasy.userService().findById(receiver.getUniqueId()).map(user -> user.findNotifySettings().isDoNotDisturb()).orElse(false)) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fУ данного игрока включен режим `Не беспокоить`.");
                    return Optional.empty();
                }

                if (TradeRequestStorage.getInstance().isDelay(player.getUniqueId())) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fНельзя так часто отправлять предложения обмена.");
                    return Optional.empty();
                }

                val currentRequest = TradeRequestStorage.getInstance().findRequest(player.getUniqueId());
                if (currentRequest != null && currentRequest.receiver().getUniqueId().equals(receiver.getUniqueId())) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fПредложение обмена данному игроку уже было отправлено");
                    return Optional.empty();
                }

                val requestData = new TradeRequestData(player, receiver);
                TradeRequestStorage.getInstance().registerRequest(requestData);
                requestData.processNotify();
            } else {
                if (strings[0].equals("accept")) {
                    val sender = Bukkit.getPlayer(strings[1]);
                    if (sender == null || !sender.isOnline()) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fИгрок не в сети либо на другом реалме.");
                        return Optional.empty();
                    }

                    if (Fantasy.combatProvider().isInCombat(sender.getUniqueId())) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fИгрок в данный момент не может обмениваться.");
                        Fantasy.bukkitProvider().sendMessage(sender, ChatType.SYSTEM, "&fВы не можете принять обмен в бою.");
                        return Optional.empty();
                    } else if (Fantasy.combatProvider().isInCombat(player.getUniqueId())) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fВы в данный момент не можете обмениваться.");
                        return Optional.empty();
                    }

                    val request = TradeRequestStorage.getInstance().findRequest(sender.getUniqueId());
                    if (request != null && !request.isExpired()) {
                        if (!TradePlayerProvider.getInstance().isTradable(player.getUniqueId(), sender.getUniqueId())) {
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fВы в данный момент не можете обмениваться.");
                            return Optional.empty();
                        }

                        TradeRequestStorage.getInstance().invalidateRequest(sender.getUniqueId());
                        TradePlayerProvider.getInstance().registerSession(sender, player);
                        var session = TradePlayerProvider.getInstance().findSession(player.getUniqueId());
                        if (session != null) session.updater().sendOpen();
                    } else {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fПредложение на обмен более не действительно.");
                    }
                } else if (strings[0].equals("decline")) {
                    val sender = Bukkit.getPlayer(strings[1]);
                    if (sender == null) return Optional.empty();

                    TradeRequestStorage.getInstance().invalidateRequest(sender.getUniqueId());
                } else {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&f/trade [игрок]");
                }
            }

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("quest").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (!Fantasy.gameServer().isTest() && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length < 1) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&7/quest [id]");
                        return;
                    }

                    val id = NumberUtility.parseInteger(strings[0]);
                    if (id == 0) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&7/quest [id]");
                        return;
                    }

                    val questedPlayer = player.<User>getBungeePlayer().findQuestSettings();
                    questedPlayer.findInProgress(id).ifPresent(quest -> quest.getRequirements().forEach(questRequirement -> questRequirement.setDone(true)));
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&aЗадание &e" + id + " &aбыло выполнено");
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("fly").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (!Fantasy.gameServer().isTest() && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    player.setAllowFlight(!player.getAllowFlight());
                    player.setFlying(player.getAllowFlight());

                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&eПолет " + (player.getAllowFlight() ? "включен" : "выключен"));
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("dnd").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            val user = player.<User>getBungeePlayer();

            val chatSettings = user.findNotifySettings();
            chatSettings.setDoNotDisturb(!chatSettings.isDoNotDisturb());
            if (chatSettings.isDoNotDisturb()) {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6Режим `Не беспокоить` включен");
            } else {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&6Режим `Не беспокоить` выключен");
            }

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("shard").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (!Fantasy.gameServer().isTest() && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    var level = 1;

                    if (strings.length == 1)
                        level = Math.max(NumberUtility.parseInteger(strings[0]), 1);
                    else {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&c/shard [level]");
                        return;
                    }

                    level = Math.min(Math.max(level, LevelStorage.getInstance().getStartShardEdgeLevel()), LevelStorage.getInstance().getStartShardEdgeLevel() + 15);

                    var itemStack = player.getInventory().getItemInMainHand();
                    var item = Fantasy.itemProvider().find(itemStack);

                    if (item == null || item.getImperiumType() != ItemType.SHARD_BREAKTHROUGH) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cНельзя использовать на данном типе предмета");
                    } else {
                        item.getGeneralSettings().itemLevel(level);
                        player.getInventory().setItemInMainHand(item.createNew(itemStack.getAmount()));
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("edgecore").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (!Fantasy.gameServer().isTest() && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    var index = 0;
                    var edgeCores = EdgeCoreList.values();

                    if (strings.length == 1)
                        index = Math.max(NumberUtility.parseInteger(strings[0]), 1);
                    else {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&c/edgecore [число]");
                        return;
                    }

                    index = Math.min(index, edgeCores.length);
                    var exactEdgeCore = edgeCores[index - 1];

                    exactEdgeCore.createEffect(player.getLocation(), player);
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("items").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (!Fantasy.gameServer().isTest() && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    int page = 1;

                    if (strings.length == 1)
                        page = Math.max(NumberUtility.parseInteger(strings[0]), 1);

                    val objects = ItemObject.values();

                    val itemsCount = objects.length;
                    val existPages = itemsCount / 54 + (itemsCount % 54 == 0 ? 0 : 1);

                    if (existPages < page) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&7Такой страницы не существует");
                        return;
                    }

                    val menuBuilder = Menu.builder()
                            .title("Все доступные предметы")
                            .chestRows(6);

                    val provider = new StaticContentProvider();

                    for (int i = (page - 1) * 54; i < page * 54; i++) {
                        if (i + 1 > itemsCount) break;

                        val itemObject = objects[i];
                        val item = itemObject.getItem().create();
                        if (itemObject == ItemObject.PROJECTION_YI_BINA_SHARD || itemObject == ItemObject.PROJECTION_YI_HOCHMA_SHARD) {
                            item.getGeneralSettings().itemLevel(LevelStorage.getInstance().getStartShardEdgeLevel());
                        }

                        if (item.getLootSettings().lootGeneratorSettings().potential())
                            item.getGearSettings().potential(50);

                        provider.setItem(
                                i - 54 * (page - 1),
                                MenuItem.createDynamic(session -> item.createNew(item.getMaterial().getMaxStackSize())).disableStickiness()
                        );
                    }

                    menuBuilder.contentProvider(provider)
                            .build()
                            .open(player);
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("gran").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length == 1) {
                        val gran = NumberUtility.parseInteger(strings[0]);

                        if (gran == 0) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&cНедопустимое значение Гран", player);
                            return;
                        }

                        Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> user.changeCurrency(CurrencyType.GRAN, gran));
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eГраны были зачислены на ваш счет", player);
                    } else {
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&c/gran [значение]", player);
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("change").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length == 3) {
                        var targetPlayer = Bukkit.getPlayer(strings[0]);
                        if (targetPlayer == null) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "Игрок оффлайн", player);
                            return;
                        }

                        var type = strings[1].equalsIgnoreCase("gran") ? CurrencyType.GRAN : strings[1].equalsIgnoreCase("firmp") ? CurrencyType.FIRM_PRIVATE : CurrencyType.FIRM;
                        var value = NumberUtility.parseInteger(strings[2]);

                        if (value == 0) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&cНедопустимое значение", player);
                            return;
                        }

                        targetPlayer.<User>getBungeePlayer().changeCurrency(type, value);
                        player.sendMessage("Зачислено &6" + value + type.getTextTransformer().apply(value) + " &fигроку &6" + targetPlayer.getName());
                    } else {
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "/change [name] [gran, firm, firmp] [значение]", player);
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("achievements").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    var user = player.<User>getBungeePlayer();
                    for (Achievements value : Achievements.values()) {
                        var achievement = user.findAchievementSettings().findByEnum(value);
                        value.getChecker().getGoals().forEach(achievement::addProgress);
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("firm").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length > 0) {
                        val firm = NumberUtility.parseInteger(strings[0]);

                        if (firm == 0) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&cНедопустимое значение Фирм-монет", player);
                            return;
                        }

                        var isPublic = strings.length > 1 && strings[1].equalsIgnoreCase("pub");
                        var currencyType = isPublic ? CurrencyType.FIRM : CurrencyType.FIRM_PRIVATE;

                        Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> user.changeCurrency(currencyType, firm));
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eЛичные фирм-монеты были зачислены на ваш счет", player);
                    } else {
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&c/firm [значение] [pub]", player);
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("mirn").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length == 1) {
                        val gran = NumberUtility.parseInteger(strings[0]);
                        if (gran == 0) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&cНедопустимое значение Мирн", player);
                            return;
                        }

                        Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> user.changeCurrency(CurrencyType.MINES, gran));
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eМирны были зачислены на ваш счет", player);
                    } else {
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&c/mirn [значение]", player);
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("flower").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length == 1) {
                        val flower = NumberUtility.parseInteger(strings[0]);
                        if (flower == 0) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&cНедопустимое значение Цветков", player);
                            return;
                        }

                        Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> user.changeCurrency(CurrencyType.FLOWER, flower));
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eЦветки были зачислены на ваш счет", player);
                    } else {
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&c/flower [значение]", player);
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("gear").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    new TesterItemContainer(player).openMenu();
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("incubator").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    val user = player.<User>getBungeePlayer();
                    user.findIncubatorSettings().findAllIncubators().forEach(incubator -> {
                        incubator.getType().getReward().reward(player);
                        incubator.onComplete(player);
                    });
                    Fantasy.clientProvider().updateIncubators(player);
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("tool").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length == 1) {
                        val exp = NumberUtility.parseInteger(strings[0]);
                        if (exp == 0) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&cНедопустимое значение опыта", player);
                            return;
                        }

                        val item = Fantasy.itemProvider().find(player.getInventory().getItemInMainHand());
                        if (item == null || !item.checkInstance(ToolItem.class)) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&cНеобходимо держать инструмент в руке", player);
                            return;
                        }

                        if (!item.getToolSettings().canUpgrade()) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&cИнструмент уже макс. уровня", player);
                            return;
                        }

                        item.getToolSettings().acceptExp(exp);
                        player.getInventory().setItemInMainHand(item.createNew());

                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eОпыт для инструмента был добавлен", player);
                    } else {
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&c/tool [значение]", player);
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("exp").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length == 1) {
                        val exp = NumberUtility.parseInteger(strings[0]);
                        if (exp == 0) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&cНедопустимое значение опыта", player);
                            return;
                        }

                        Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> user.getLevelController().changeExp(exp, ExpType.OTHER));
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eОпыт был добавлен", player);
                    } else {
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&c/exp [значение]", player);
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("bpadvance").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    player.<User>getBungeePlayer().findBattlePassSettings().setAdvanced(true);
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("bpexp").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length == 1) {
                        val exp = NumberUtility.parseInteger(strings[0]);
                        if (exp == 0) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&cНедопустимое значение опыта", player);
                            return;
                        }

                        player.<User>getBungeePlayer().findBattlePassSettings().changeExp(exp);
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eОпыт был добавлен", player);
                    } else {
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&c/bpexp [значение]", player);
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("level").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length == 1) {
                        int level = NumberUtility.parseInteger(strings[0]);
                        if (level <= 0) {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&cНедопустимое значение уровня", player);
                            return;
                        }

                        val finalValue = Math.min(level, LevelStorage.getInstance().getMaxBasicLevel());

                        Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> {
                            user.getLevelController().setLevel(finalValue);
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&7Установлен уровень &e" + user.getLevelController().getLevel());
                        });
                    } else {
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&c/level [значение]", player);
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("fraction").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length == 1) {
                        if (strings[0].equalsIgnoreCase("drop")) {
                            Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> user.setFractionType(FractionType.NONE));
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eВаша фракция силы сброшена", player);
                        } else if (strings[0].equalsIgnoreCase("time")) {
                            Fantasy.buffService().findById(player.getUniqueId()).ifPresent(entity -> entity.invalidateType(BuffType.FRACTION_CHANGE_DEBUFF));
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eВаше ограничение на смену фракции силы было сброшено", player);
                        } else {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&c/fraction [drop, time] - сброс фракции", player);
                        }
                    } else {
                        Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> new FractionChoicer(user).open());
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("reset").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length >= 1) {
                        if (strings[0].equalsIgnoreCase("buff")) {
                            Fantasy.buffService().findById(player.getUniqueId()).ifPresent(buffedEntity -> {
                                buffedEntity.getAll().forEach(buff -> Fantasy.buffService().invalidateBuff(player.getUniqueId(), buff.getBuffType()));
                                Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eВаши усиления были сброшены", player);
                            });
                        } else if (strings[0].equalsIgnoreCase("incubator")) {
                            val user = player.<User>getBungeePlayer();
                            user.findIncubatorSettings().findAllIncubators().forEach(incubator -> user.findIncubatorSettings().invalidateIncubator(incubator.getUuid()));
                            Fantasy.clientProvider().updateIncubators(player);
                        } else if (strings[0].equalsIgnoreCase("premium")) {
                            player.<User>getBungeePlayer().findDonateSettings().donateData().invalidateSubscription();
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eВаша премиум подписка была сброшена", player);
                        } else if (strings[0].equalsIgnoreCase("lootfilter")) {
                            player.<User>getBungeePlayer().findDonateSettings().donateData().invalidateLootFilter();
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eВаш расширенный фильтр предмета был сброшен", player);
                        } else if (strings[0].equalsIgnoreCase("aura")) {
                            val donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();
                            donateData.findAllMetadata().stream().filter(metadata -> metadata.contains("AURA")).forEach(donateData::invalidateMetadata);
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eВаши донат-ауры были сброшены", player);
                        } else if (strings[0].equalsIgnoreCase("mount")) {
                            val donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();
                            donateData.findAllMetadata().stream().filter(metadata -> metadata.contains("MOUNT")).forEach(donateData::invalidateMetadata);
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&eВаши донат-спутники были сброшены", player);
                        } else if (strings[0].equals("quest")) {
                            if (strings.length < 2) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&7/reset quest [id]");
                                return;
                            }

                            val id = NumberUtility.parseInteger(strings[1]);
                            if (id <= 0) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&7/reset quest [id]");
                                return;
                            }

                            player.<User>getBungeePlayer().findQuestSettings().invalidatePassedQuest(id);
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cВаши задания были сброшены");
                        } else if (strings[0].equals("trades")) {
                            Fantasy.tradeNpcService().findByUuid(player.getUniqueId()).invalidateAll();
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cВаши трейды были сброшены");
                        } else if (strings[0].equals("storage")) {
                            Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> {
                                user.getGlobalStorageData().setLastOpenedPage(0);
                                user.getGlobalStorageData().findAllPages().values().forEach(storagePage -> storagePage.update(new StoragePage(new ItemStack[54])));
                            });
                            player.<User>getBungeePlayer().findDonateSettings().donateData().setPurchasedStoragePages(0);
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cВаш склад был сброшен");
                        } else if (strings[0].equals("battlepass")) {
                            var user = player.<User>getBungeePlayer();
                            user.findBattlePassSettings().setAdvanced(false);
                            user.findBattlePassSettings().setCurrentExp(0);
                            user.findBattlePassSettings().getPassedRewards().clear();
                            user.findBattlePassSettings().getPassedQuests().clear();
                            user.findBattlePassSettings().getActiveQuests().clear();
                            user.findBattlePassSettings().initialize();
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cВаш БП был сброшен");
                        } else if (strings[0].equals("stats")) {
                            var user = player.<User>getBungeePlayer();
                            user.findStatisticSettings().invalidateAll();
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cВся ваша статистика была сброшена");
                        } else if (strings[0].equals("calendar")) {
                            var user = player.<User>getBungeePlayer();
                            user.findCalendarSettings().setLastDay(0);
                            user.findCalendarSettings().setLastGet(Instant.now().toEpochMilli() - TimeUnit.DAYS.toMillis(1));
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cДанные ежедневных наград сброшены");
                        } else {
                            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&c/reset [premium, lootfilter, quest, trades, buff] - сброс конкретных данных", player);
                        }
                    } else {
                        Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, "&c/reset [premium, lootfilter, quest, trades, buff] - сброс конкретных данных", player);
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("id").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    val imperiumItem = Fantasy.itemProvider().find(player.getInventory().getItemInMainHand());
                    if (imperiumItem == null) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&eПусто");
                    } else {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Айди: " + imperiumItem.getUniqueId());
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("sub").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                if (strings.length != 1) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&7/sub [time in seconds]");
                    return;
                }

                val time = TimeUnit.SECONDS.toMillis(NumberUtility.parseInteger(strings[0]));
                player.<User>getBungeePlayer().findDonateSettings().donateData().updateSubscription(time);
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&7Установлена премиум-подписка на &e+" +
                        TimeUnit.MILLISECONDS.toSeconds(time) + "с");
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("cleardata").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> {
                    val questedPlayer = user.findQuestSettings();
                    questedPlayer.getPassedQuests().forEach(questedPlayer::invalidatePassedQuest);
                    questedPlayer.findAllInProgressQuests().forEach(quest -> questedPlayer.invalidateQuestInProgress(quest.getId()));

                    user.getLevelController().setLevel(1);
                    user.getLevelController().setExp(0);

                    user.changeCurrency(CurrencyType.GRAN, -user.getGrans());

                    val achievementSettings = user.findAchievementSettings();
                    achievementSettings.getAchievements().forEach(achievement -> achievementSettings.invalidate(achievement.getType()));
                    user.findTreeSettings().invalidateUpgrades();

                    Fantasy.buffService().findById(player.getUniqueId()).ifPresent(buffedEntity -> buffedEntity.getAll().forEach(buff -> buffedEntity.invalidateType(buff.getBuffType())));
                }));
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("get").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (Service.toRealm().getId() > 0 && !PermissionUtility.isOperator(player)) return Optional.empty();

            if (strings.length != 2) {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&c/get [айди] [кол-во]");
            } else {
                PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                    if (!isSupporter) return;

                    Fantasy.runLater(() -> {
                        val id = strings[0];
                        val amount = NumberUtility.parseInteger(strings[1]);
                        if (amount <= 0) {
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&c/get [айди] [кол-во]");
                            return;
                        }

                        val targetItem = Fantasy.itemProvider().find(id);
                        if (targetItem == null) {
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cПредмета с таким айди не существует");
                            return;
                        }

                        Fantasy.bukkitProvider().spawnItem(player.getLocation(), targetItem.createNew(amount));
                    });
                });
            }

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("tag").addAccessCheck(commandSender -> commandSender instanceof Player player && PermissionUtility.isOperator(player)).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;

            val itemInHand = CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand());
            if (itemInHand.getTag() == null) {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&eПусто");
            } else {
                itemInHand.getTag().map.forEach((key, value) -> Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&ekey: &f" + key + " &e| value: &f" + value.toString()));
            }

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("calendar").addAccessCheck(commandSender -> commandSender instanceof Player player && PermissionUtility.isOperator(player)).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (strings.length == 0) {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "/calendar [all/time/day]");
                return Optional.empty();
            }

            val user = player.<User>getBungeePlayer();
            val type = strings[0];
            if (type.equalsIgnoreCase("all")) {
                user.findCalendarSettings().updateState();
            } else if (type.equalsIgnoreCase("time")) {
                user.findCalendarSettings().setLastGet(Instant.now().toEpochMilli() - TimeUnit.DAYS.toMillis(1));
            } else if (type.equalsIgnoreCase("day")) {
                val oldTime = user.findCalendarSettings().getLastGet();
                user.findCalendarSettings().updateState();
                user.findCalendarSettings().setLastGet(oldTime);
            }

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("rekt").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (!PermissionUtility.isOperator(player) && !PermissionUtility.isAttentiveRekt(player.getUniqueId())) return Optional.empty();

            new RektItemContainer(player).openMenu();
            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("rektfarm").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            if (!PermissionUtility.isOperator(player) && !PermissionUtility.isAttentiveRekt(player.getUniqueId())) return Optional.empty();

            player.teleport(new Location(MapServiceProcess.GAME_WORLD, 353, 103.5, 3606.8));
            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("hide").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            val user = player.<User>getBungeePlayer();
            val settings = user.findSyncHideSettings();
            val changed = user.findSyncHideSettings().setHideAll(!settings.isHideAll());

            if (changed) {
                user.findClientSettings().getSettings().put(SettingsType.PLAYER_VISIBILITY, !user.findClientSettings().find(SettingsType.PLAYER_VISIBILITY));
                Fantasy.clientProvider().sendSettings(player);
            }

            var message = "";

            if (changed) message = settings.isHideAll() ? "&fВы &6скрыли &fвсех игроков" : "&fВы &6показали &fвсех игроков";
            else message = "&cНельзя так часто! Попробуйте позже.";

            Fantasy.bukkitProvider().sendMessage(ChatType.SYSTEM, message, player);

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("donate").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            player.closeInventory();
            ShopSelection.getInstance().open(player, player.<User>getBungeePlayer().findDonateSettings().donateData(), ICouponsService.get().getDiscount(player.getUniqueId()));
            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("battlepass", "bp").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            player.closeInventory();
            Fantasy.battlePassProvider().open(player);
            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("spawn").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;
            player.closeInventory();
            Fantasy.getRespawnAndBeaconExecutor().execute(() -> BeaconContextProvider.getInstance().registerContext(player));
            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("suicide").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;

            val user = Fantasy.userService().findById(player.getUniqueId()).orElse(null);
            if (user == null) {
                BeaconObjects.START.forceTeleportToRespawn(player);
            } else {
                Fantasy.damageProvider().processDamage(player).setDamage(InfluenceType.PHYSICAL, Integer.MAX_VALUE).apply();
            }

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("tp").addAccessCheck(Player.class::isInstance).setExecutor((commandSender, strings) -> {
            val player = (Player) commandSender;

            PermissionUtility.isSupporter(player).whenComplete((isSupporter, thrower) -> {
                if (!isSupporter) return;

                Fantasy.runLater(() -> {
                    if (strings.length < 1) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&c/tp [player]");
                    } else {
                        val targetPlayer = Bukkit.getPlayer(strings[0]);
                        if (strings.length == 2) {
                            val toPlayer = Bukkit.getPlayer(strings[1]);

                            if (targetPlayer == null || toPlayer == null) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cОдин из игроков оффлайн либо на другом реалме.");
                            } else {
                                targetPlayer.teleport(toPlayer);
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cВы были телепортированы к " + targetPlayer.getName());
                            }
                        } else {
                            if (targetPlayer == null) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cИгрок оффлайн либо на другом реалме.");
                            } else {
                                player.teleport(targetPlayer);
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cВы были телепортированы к " + targetPlayer.getName());
                            }
                        }
                    }
                });
            });

            return Optional.empty();
        }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("gm", "gamemode")
                .addAccessCheck(commandSender -> commandSender instanceof Player)
                .setExecutor((commandSender, strings) -> {
                    val player = (Player) commandSender;

                    PermissionUtility.isSupporter(player).thenAccept(isSupporter -> {
                        if (!isSupporter) return;

                        val gameMode = player.getGameMode() == GameMode.SURVIVAL ? GameMode.CREATIVE : GameMode.SURVIVAL;
                        player.setGameMode(gameMode);

                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&eИгровой режим изменен");
                    });
                    return Optional.empty();
                }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("speed")
                .addAccessCheck(commandSender -> commandSender instanceof Player player && PermissionUtility.isOperator(player))
                .setExecutor((commandSender, strings) -> {
                    val player = (Player) commandSender;

                    if (strings.length < 1) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&c/speed [value]");
                        return Optional.empty();
                    }

                    val number = Math.max(NumberUtility.parseInteger(strings[0]), 0.01F);
                    player.setFlySpeed(Math.min(0.1F * number, 1F));

                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, String.format("&7Ваша скорость: &e%.1f", player.getFlySpeed()));

                    return Optional.empty();
                }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("realm")
                .addAccessCheck(Player.class::isInstance)
                .setExecutor((commandSender, strings) -> {
                    val player = (Player) commandSender;

                    if (strings.length < 1) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&c/realm [id]");
                        return Optional.empty();
                    }

                    if (Fantasy.combatProvider().isInCombat(player.getUniqueId())) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cПеремещаться между реалмами в бою нельзя");
                        return Optional.empty();
                    }

                    val id = NumberUtility.parseInteger(strings[0]);
                    if (id == Fantasy.gameServer().getRealmId()) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cВы не можете переместиться на текущий реалм");
                        return Optional.empty();
                    }

                    player.closeInventory();
                    Wada.get().getConfirmRequest().request(player, "Вы желаете переместиться на &6" + id + " &fреалм?", accepted -> {
                        if (!accepted) return;

                        val targetRealm = RealmId.of(Fantasy.gameServer().getRealmType(), id);
                        val user = Fantasy.userService().findById(player.getUniqueId()).orElse(null);

                        if (user == null) {
                            player.kickPlayer("Ошибка состояния пользователя");
                            return;
                        }

                        PacketRegistry.writeAndAwaitResponse(Fantasy.gameServer().nameSpace(), new UserTransferAccessibilityPacket(targetRealm, false)).whenComplete((userTransferAccessibilityPacket, throwable) -> {
                            if (throwable != null) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cДанный реалм недоступен. Попробуйте позже.");
                            } else {
                                if (!userTransferAccessibilityPacket.isAccess()) {
                                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cДанный реалм недоступен. Попробуйте позже.");
                                    return;
                                }

                                user.saveAllDataAndInvalidateCache().whenComplete((v, throwable1) -> {
                                    if (throwable1 != null) {
                                        Fantasy.runLater(throwable1::printStackTrace);
                                        player.kickPlayer("Произошла ошибка во время сохранения данных: " + throwable1.getMessage());
                                    } else {
                                        PacketRegistry.writeAndAwaitResponse(Fantasy.gameServer().nameSpace(), new SessionUpdatePacket(player.getUniqueId(), SessionState.DISCONNECTED))
                                                .whenComplete((sessionUpdatePacket, sessionThrowable) -> {
                                                    if (sessionThrowable != null) {
                                                        player.kickPlayer("Произошла ошибка во время смены реалма: " + sessionThrowable.getMessage());
                                                    } else {
                                                        ITransferService.get().transfer(player.getUniqueId(), targetRealm);
                                                    }
                                                });
                                    }
                                });
                            }
                        });
                    });

                    return Optional.empty();
                }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("see")
                .addAccessCheck(Player.class::isInstance)
                .setExecutor((commandSender, strings) -> {
                    val player = (Player) commandSender;

                    if (!PermissionUtility.isOperator(player)) return Optional.empty();

                    if (strings.length < 2) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&c/view [player name] [inv/str [page]]");
                        return Optional.empty();
                    }

                    val name = strings[0];
                    val type = strings[1];

                    if (!type.equalsIgnoreCase("inv") && !type.equalsIgnoreCase("str"))
                        return Optional.empty();

                    IAccountService.get().getUuidByName(name).whenComplete((uuid, throwable) -> {
                        if (throwable != null) {
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, throwable.getMessage());
                            return;
                        }

                        Fantasy.databaseProvider().asyncCollection(Collections.USERS).thenAcceptAsync(collection -> {
                            var document = collection.find(Filters.eq("_id", uuid.toString())).first();
                            if (document == null) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "document is null (_id error)");
                                return;
                            }

                            val grans = document.get("gran", 0);

                            if (type.equalsIgnoreCase("inv")) {
                                var isUpdate = strings.length > 2 ? strings[2].equalsIgnoreCase("update") : false;
                                var itemStacks = new StorageSerialization(new ItemStack[41]).deserialize(document.get("playerStorage", ""));

                                Fantasy.runLater(() -> {
                                    val contentProvider = new StaticContentProvider();
                                    for (int i = 0; i < itemStacks.length; i++) {
                                        contentProvider.setItem(i, MenuItem.createStatic(itemStacks[i]).disableStickiness());
                                    }

                                    Menu.builder()
                                            .title(name + " [inv] Граны: " + grans)
                                            .chestRows(6)
                                            .contentProvider(contentProvider)
                                            .setLockUserInventory(false)
                                            .disableStickiness()
                                            .onClose(session -> {
                                                if (isUpdate) {
                                                    var items = new ItemStack[41];
                                                    for (int i = 0; i < items.length; i++) {
                                                        items[i] = session.getInventory().getItem(i);
                                                    }

                                                    Fantasy.databaseProvider().updateUser(uuid, Updates.set("playerStorage", new StorageSerialization(items).serialize()))
                                                            .whenComplete((ignore, throwable2) -> {
                                                                if (throwable2 != null) {
                                                                    throwable2.printStackTrace();
                                                                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, throwable2.getMessage());
                                                                } else {
                                                                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Обновлено");
                                                                }
                                                            });
                                                }
                                            })
                                            .build()
                                            .open(player);
                                });
                            } else {
                                var isUpdate = strings.length > 3 ? strings[3].equalsIgnoreCase("update") : false;
                                var storageData = StorageData.deserialize(document.get("globalStorage", ""));
                                int page = Math.max(NumberUtility.parseInteger(strings[2]), 0);

                                val pages = storageData.findAllPages();
                                val findPage = pages.get(page);

                                if (findPage == null) {
                                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Такой страницы не существует");
                                    return;
                                }

                                val items = findPage.getUserStorageItems();

                                Fantasy.runLater(() -> {
                                    val contentProvider = new StaticContentProvider();
                                    for (int i = 0; i < items.length; i++) {
                                        contentProvider.setItem(i, MenuItem.createStatic(items[i]).disableStickiness());
                                    }

                                    Menu.builder()
                                            .title(name + " [str] Граны: " + grans)
                                            .chestRows(6)
                                            .contentProvider(contentProvider)
                                            .setLockUserInventory(false)
                                            .disableStickiness()
                                            .onClose(session -> {
                                                if (isUpdate) {
                                                    val lastSlot = 54 - findPage.getBlockedSlots();

                                                    for (int i = 0; i < lastSlot; i++) {
                                                        findPage.update(session.getInventory().getItem(i), i);
                                                    }

                                                    Fantasy.databaseProvider()
                                                            .updateUser(uuid, Updates.set("globalStorage", storageData.serialize()))
                                                            .whenComplete((ignore, throwable2) -> {
                                                                if (throwable2 != null) {
                                                                    throwable2.printStackTrace();
                                                                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, throwable2.getMessage());
                                                                } else {
                                                                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Обновлено");
                                                                }
                                                            });
                                                }
                                            })
                                            .build().open(player);
                                });
                            }
                        });
                    });

                    return Optional.empty();
                }).build().register(javaPlugin);

        CommandBuilder.get().addCommandName("view")
                .addAccessCheck(Player.class::isInstance)
                .setExecutor((commandSender, strings) -> {
                    val player = (Player) commandSender;

                    PermissionUtility.isModerator(player).whenComplete(((isModerator, throwable) -> {
                        if (!isModerator) return;

                        if (strings.length < 2) {
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&c/view [player name] [inv/str [page]]");
                            return;
                        }

                        val name = strings[0];
                        val type = strings[1];

                        val targetPlayer = Bukkit.getPlayer(name);
                        if (targetPlayer == null) {
                            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Игрок оффлайн или на другом реалме");
                            return;
                        }

                        if (type.equalsIgnoreCase("inv")) {
                            val donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();
                            if (donateData == null) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Произошла ошибка: donate data is null");
                                return;
                            }

                            val user = Fantasy.userService().findById(targetPlayer.getUniqueId()).orElse(null);
                            if (user == null) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Произошла ошибка: user is null");
                                return;
                            }

                            val inventory = targetPlayer.getInventory().getContents();
                            val contentProvider = new StaticContentProvider();
                            for (int i = 0; i < inventory.length; i++) {
                                contentProvider.setItem(i, MenuItem.createStatic(inventory[i]));
                            }

                            Menu.builder()
                                    .title(name + " [inv] | Гран: " + user.getGrans() + " | Фирм: " + donateData.getCurrency() + " | Личные Ф.: " + donateData.getPrivateCurrency())
                                    .chestRows(6)
                                    .lockUserInventory()
                                    .contentProvider(contentProvider)
                                    .build().open(player);
                        } else if (type.equalsIgnoreCase("str")) {
                            val user = Fantasy.userService().findById(targetPlayer.getUniqueId()).orElse(null);
                            if (user == null) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Игрок оффлайн или на другом реалме");
                                return;
                            }

                            int page = NumberUtility.parseInteger(strings[2]);
                            if (page < 0) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Неверная страница");
                                return;
                            }

                            val pages = user.getGlobalStorageData().findAllPages();
                            val findPage = pages.get(page);
                            if (findPage == null) {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "Такой страницы не существует");
                                return;
                            }

                            val items = findPage.getUserStorageItems();
                            val contentProvider = new StaticContentProvider();
                            for (int i = 0; i < items.length; i++) {
                                contentProvider.setItem(i, MenuItem.createStatic(items[i]));
                            }

                            Menu.builder()
                                    .title(name + " [str]")
                                    .chestRows(6)
                                    .lockUserInventory()
                                    .contentProvider(contentProvider)
                                    .build().open(player);
                        }
                    }));

                    return Optional.empty();
                }).build().register(javaPlugin);
    }

}
