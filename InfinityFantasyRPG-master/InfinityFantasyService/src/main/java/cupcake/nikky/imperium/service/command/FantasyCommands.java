package cupcake.nikky.imperium.service.command;

import com.github.neitexx.bukkit.command.CommandBuilder;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.service.Fantasy;
import cupcake.nikky.imperium.service.board.user.general.UserGeneralBoardService;
import cupcake.nikky.imperium.service.time.BoardScheduler;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.Service;
import cupcake.nikky.imperium.tower.protocol.ban.BanData;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.protocol.game.GameServer;
import cupcake.nikky.imperium.tower.protocol.game.GameState;
import cupcake.nikky.imperium.tower.protocol.game.GameStorage;
import cupcake.nikky.imperium.tower.protocol.packet.*;
import cupcake.nikky.imperium.tower.protocol.packet.connection.game.GameDisconnectPacket;
import cupcake.nikky.imperium.tower.protocol.packet.connection.session.SessionState;
import cupcake.nikky.imperium.tower.protocol.packet.daily.DailyDataPacket;
import cupcake.nikky.imperium.tower.protocol.session.ConnectionSession;
import cupcake.nikky.imperium.tower.protocol.session.SessionStorage;
import cupcake.nikky.imperium.tower.protocol.trade.npc.TradeDuration;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import joptsimple.internal.Strings;
import lombok.val;
import me.anfanik.steda.api.utility.ArrayUtility;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.account.IAccountService;
import ru.cristalix.core.permissions.IPermissionService;
import ru.cristalix.core.realm.RealmId;
import ru.cristalix.core.transfer.ITransferService;
import ru.cristalix.core.violation.IViolationService;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FantasyCommands {

    public FantasyCommands(@NotNull final JavaPlugin javaPlugin) {
        val dateTimeFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateTimeFormatter.setCalendar(Calendar.getInstance(TimeZone.getTimeZone("GMT+3")));

        CommandBuilder.get()
                .addCommandName("fantasy", "f")
                .addAccessCheck(ConsoleCommandSender.class::isInstance)
                .setExecutor((commandSender, strings) -> {
                    Bukkit.getLogger().info("General command /fantasy, alias /f");
                    Bukkit.getLogger().info("/fantasy [status] [name]");
                    Bukkit.getLogger().info("/fantasy [gstatus]");
                    Bukkit.getLogger().info("/fantasy [m, message] [global message, many args]");
                    Bukkit.getLogger().info("/fantasy [close, restart] [all, realm name]");
                    Bukkit.getLogger().info("/fantasy [clear] [name]");
                    Bukkit.getLogger().info("/fantasy [kickall, kall]");
                    Bukkit.getLogger().info("/fantasy [whitelist, wl] [true, false]");
                    Bukkit.getLogger().info("/fantasy [move] [name]");
                    Bukkit.getLogger().info("/fantasy [reset] [trade]");
                    Bukkit.getLogger().info("/fantasy [sessions]");
                    Bukkit.getLogger().info("/fantasy [syncreload]");
                    Bukkit.getLogger().info("/fantasy [ban] [name] [time] [reason...]");
                    Bukkit.getLogger().info("/fantasy [recalculatetop]");
                    Bukkit.getLogger().info("/fantasy [wipe]");
                    Bukkit.getLogger().info("/fantasy [findtop]");
                    Bukkit.getLogger().info("/fantasy [info] [name]");
                    Bukkit.getLogger().info("/fantasy [check] [names]");
                    return Optional.empty();
                })
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("syncreload")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            Fantasy.restartProvider().setSync(!Fantasy.restartProvider().isSync());
                            Bukkit.getLogger().info("Sync flag changed, now it " + Fantasy.restartProvider().isSync());
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("sessions")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            Bukkit.getLogger().info("Sessions count: " + SessionStorage.getInstance().findAll().size());
                            return Optional.empty();
                        })
                        .buildCommand())
//                .addChildCommand(CommandBuilder.get()
//                        .addCommandName("recalculatetop")
//                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
//                        .setExecutor((commandSender, strings) -> {
//                            Fantasy.databaseService().asyncCollection(cupcake.nikky.imperium.tower.protocol.database.Collections.USERS).thenAcceptAsync(collection -> {
//                                val cursor = collection.find(Filters.exists("levelScore")).cursor();
//                                val timeLimit = 3D * 60D * 1_000D;
//
//                                while (cursor.hasNext()) {
//                                    val document = cursor.next();
//                                    val binaTime = document.getInteger(TimeType.BINA.getFieldName() + "_time");
//                                    val hochmaTime = document.getInteger(TimeType.HOCHMA.getFieldName() + "_time");
//                                    val binaLevel = document.getInteger(TimeType.BINA.getFieldName() + "_level");
//                                    val hochmaLevel = document.getInteger(TimeType.HOCHMA.getFieldName() + "_level");
//                                    val level = document.getInteger("level");
//                                    val exp = document.getInteger("exp");
//
//                                    collection.updateOne(Filters.eq("_id", document.get("_id")),
//                                            Arrays.asList(
//                                                    Updates.set("globalScore", level + exp / LevelStorage.getInstance().getTopExpKey() +
//                                                                    binaLevel + (1D - binaTime / timeLimit) +
//                                                                    hochmaLevel + (1D - hochmaTime / timeLimit)
//                                                    )
//                                            ));
//                                }
//
//                                cursor.close();
//                            }).whenComplete((v, throwable) -> {
//                                if (throwable != null) throwable.printStackTrace();
//                                Bukkit.getLogger().info("Top recalculate completed");
//                            });
//                            return Optional.empty();
//                        })
//                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("wipe")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            if (strings.length < 2 || !strings[1].equals("uwu")) {
                                commandSender.sendMessage("Skipped key-phrase");
                                return Optional.empty();
                            }
                            Fantasy.databaseService().asyncCollection(cupcake.nikky.imperium.tower.protocol.database.Collections.USERS).thenAcceptAsync(collection -> {
                                collection.updateMany(Filters.empty(),
                                        Updates.combine(Updates.unset("level"),
                                                Updates.unset("exp"),
                                                Updates.unset("location"),
                                                Updates.unset("health"),
                                                Updates.unset("mana"),
                                                Updates.unset("gran"),
                                                Updates.unset("mine"),
                                                Updates.unset("boss_1_score"),
                                                Updates.unset("boss_2_score"),
                                                Updates.unset("boss_1_time"),
                                                Updates.unset("boss_2_time"),
                                                Updates.unset("boss_1_level"),
                                                Updates.unset("boss_2_level"),
                                                Updates.unset("boss_1_corrupt"),
                                                Updates.unset("boss_2_corrupt"),
                                                Updates.unset("levelScore"),
                                                Updates.unset("globalScore"),
                                                Updates.unset("questPassData"),
                                                Updates.unset("questProgressData"),
                                                Updates.unset("dailyQuests"),
                                                Updates.unset("treeData"),
                                                Updates.unset("filterData"),
                                                Updates.unset("corpseData"),
                                                Updates.unset("trade_week"),
                                                Updates.unset("trade_day"),
                                                Updates.unset("statistic"),
                                                Updates.unset("flower"),
                                                Updates.unset("globalStorage"),
                                                Updates.unset("incubators"),
                                                Updates.unset("playerStorage"),
                                                Updates.unset("achievementData"),
                                                Updates.unset("battlepass_quests"),
                                                Updates.unset("battlepass_exp"),
                                                Updates.unset("battlepass_rewards"),
                                                Updates.unset("battlepass_advanced"))
                                );
                            }).whenComplete((v, throwable) -> {
                                if (throwable != null) throwable.printStackTrace();
                                Bukkit.getLogger().info("All users wiped");
                            });
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("move")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {

                            if (strings.length < 3) {
                                Bukkit.getLogger().info("/fantasy [move] [realm] [name]");
                                return Optional.empty();
                            }

                            val realm = strings[1];
                            val name = strings[2];
                            val realms = GameStorage.getInstance().findAll();

                            if (realms.size() == 0) {
                                Bukkit.getLogger().info("Can't find game servers");
                                return Optional.empty();
                            }

                            realms.stream().filter(gameServer -> gameServer.toRealm().getRealmName().equals(realm)).findAny()
                                    .ifPresent(gameServer -> IAccountService.get().getUuidByName(name).whenComplete((uuid, throwable) -> {
                                        if (throwable != null) {
                                            throwable.printStackTrace();
                                        } else {
                                            ITransferService.get().transfer(uuid, gameServer.toRealm());
                                            Bukkit.getLogger().info(name + " has been transferred");
                                        }
                                    }));

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("message", "m")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            if (strings.length == 1) {
                                Bukkit.getLogger().info("/fantasy message [global message, many args]");
                                return Optional.empty();
                            }

                            val words = new String[strings.length - 1];
                            ArrayUtility.skipFirst(strings, words, 1);

                            val message = TextUtility.colorize(Strings.join(words, " "));

                            PacketRegistry.write(Fantasy.gameServer().nameSpace(), new GameGlobalMessagePacket(message));

                            Bukkit.getLogger().info("All game servers were notified");
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("findtop")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            var data = UserGeneralBoardService.getInstance().getCachedData().entrySet().stream()
                                    .sorted(Comparator.comparingDouble(entry -> -entry.getValue()))
                                    .limit(100)
                                    .toList();
                            data.forEach(uuidDoubleEntry -> {
                                IViolationService.get().getBan(uuidDoubleEntry.getKey()).whenComplete((violation, throwable) -> {
                                    if (throwable != null) {
                                        throwable.printStackTrace();
                                    } else {
                                        var name = "%s (%s) (%.3f)".formatted(
                                                UserGeneralBoardService.getInstance().getCachedNames().get(uuidDoubleEntry.getKey()),
                                                uuidDoubleEntry.getKey().toString(),
                                                uuidDoubleEntry.getValue()
                                        );

                                        if (violation.isPermanent()) {
                                            Bukkit.getLogger().info(name + ChatColor.RED + " permanent banned: " + violation.getReason());
                                        } else if (!violation.isPassed()) {
                                            Bukkit.getLogger().info(name + ChatColor.RED + " banned: " + violation.getReason());
                                        } else {
                                            Bukkit.getLogger().info(name);
                                        }
                                    }
                                });
                            });
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("printtop")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            if (strings.length < 2) {
                                Bukkit.getLogger().info("/fantasy printtop [size (1-125)]");
                                return Optional.empty();
                            }

                            var limit = Math.clamp(NumberUtility.parseInteger(strings[1]), 1, 125);

                            Bukkit.getScheduler().runTaskAsynchronously(javaPlugin, () -> {
                                var data = UserGeneralBoardService.getInstance().getCachedData().entrySet().stream()
                                        .sorted(Comparator.comparingDouble(entry -> -entry.getValue()))
                                        .limit(limit)
                                        .map(entry -> {
                                            val context = IPermissionService.get().getPermissionContext(entry.getKey()).join();
                                            return context.getUsername();
                                        }) // UserGeneralBoardService.getInstance().getCachedNames().get(entry.getKey())
                                        .collect(Collectors.joining(", "));
                                Bukkit.getLogger().info(data);
                            });
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("info")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            if (strings.length < 2) {
                                Bukkit.getLogger().info("/fantasy info [name]");
                                return Optional.empty();
                            }

                            IAccountService.get().getUuidByName(strings[1]).whenComplete((uuid, throwable) -> {
                                if (uuid == null) {
                                    Bukkit.getLogger().info("Wrong name: " + strings[1]);
                                    return;
                                }

                                if (throwable != null) throwable.printStackTrace();
                                else IViolationService.get().getBan(uuid).whenComplete((violation, throwable1) -> {
                                    if (throwable1 != null) {
                                        throwable1.printStackTrace();
                                    } else if (violation != null) {
                                        Bukkit.getLogger().info(
                                                "name: %s; permanent: %s; reason: %s; "
                                                        .formatted(
                                                                strings[1],
                                                                violation.isPermanent(),
                                                                violation.getReason()
                                                        )
                                        );
                                    } else {
                                        Bukkit.getLogger().info("violation null");
                                    }
                                });
                            });

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("check")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            if (strings.length < 2) {
                                Bukkit.getLogger().info("/fantasy check [name1, name2, ...]");
                                return Optional.empty();
                            }

                            val names = new String[strings.length - 1];
                            ArrayUtility.skipFirst(strings, names, 1);

                            for (final String name : names) {
                                val cleanName = name.trim().replace(",", "");
                                IAccountService.get().getUuidByName(cleanName).whenComplete((uuid, throwable) -> {
                                    if (uuid == null) {
                                        Bukkit.getLogger().info("Wrong name: " + cleanName);
                                        return;
                                    }

                                    if (throwable != null) {
                                        Bukkit.getLogger().info("Wrong name: " + cleanName);
                                    } else
                                        IViolationService.get().getBan(uuid).whenComplete((violation, throwable1) -> {
                                            if (throwable1 != null) {
                                                throwable1.printStackTrace();
                                            } else if (violation != null && (!violation.isPassed() || violation.isPermanent())) {
                                                Bukkit.getLogger().info(
                                                        "Name: %s; permanent: %s; reason: %s; "
                                                                .formatted(
                                                                        cleanName,
                                                                        violation.isPermanent(),
                                                                        violation.getReason()
                                                                )
                                                );
                                            }
                                        });
                                });
                            }

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("kickall", "kall")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            PacketRegistry.write(Fantasy.gameServer().nameSpace(), new GameKickPlayersPacket());
                            Bukkit.getLogger().info("Game kick players packet has been send");
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("whitelist", "wl")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            if (strings.length != 2) {
                                Bukkit.getLogger().info("/fantasy [whitelist, wl] [statement]");
                                return Optional.empty();
                            }

                            val statement = Boolean.parseBoolean(strings[1]);
                            PacketRegistry.write(Fantasy.gameServer().nameSpace(), new GameWhitelistPacket(statement));

                            Bukkit.getLogger().info(String.format("Game white list (%s) packet has been send", statement));
                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("close", "restart")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            if (strings.length != 2) {
                                Bukkit.getLogger().info("/fantasy [close OR restart] [all OR realm name]");
                                return Optional.empty();
                            }

                            val target = strings[1];

                            val gameServers = target.equalsIgnoreCase("all") ?
                                    GameStorage.getInstance().findAll() : Collections.singletonList(GameStorage.getInstance().findServer(RealmId.of(target)));

                            val packet = new GameDisconnectPacket(strings[0].equalsIgnoreCase("restart"));
                            gameServers.forEach(gameServer -> PacketRegistry.forward(gameServer.toRealm(), packet));
                            Bukkit.getLogger().info("Packet sent successfully");

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("clear")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            if (strings.length < 2) {
                                Bukkit.getLogger().info("/fantasy clear [name]");
                                return Optional.empty();
                            }

                            IAccountService.get().getUuidByName(strings[1]).whenComplete((uuid, throwable) -> {
                                if (throwable != null) {
                                    throwable.printStackTrace();
                                } else {
                                    val session = SessionStorage.getInstance().findSession(uuid).orElse(null);
                                    if (session == null) return;

                                    val old = session.getSessionState();
                                    session.setSessionState(SessionState.DISCONNECTED);

                                    Bukkit.getLogger().info("Session old state: " + old.name());
                                    Bukkit.getLogger().info("Session new state: " + SessionState.DISCONNECTED.name());
                                }
                            });

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("reset")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            if (strings.length < 2) {
                                Bukkit.getLogger().info("/fantasy reset [trade]");
                                return Optional.empty();
                            }

                            if (strings[1].equalsIgnoreCase("trade")) {
                                Fantasy.databaseService().collection(cupcake.nikky.imperium.tower.protocol.database.Collections.USERS)
                                        .updateMany(Filters.empty(),
                                                Updates.combine(
                                                        Updates.set(TradeDuration.WEEK.getFieldName(), ""),
                                                        Updates.set(TradeDuration.DAY.getFieldName(), "")
                                                ),
                                                new UpdateOptions().upsert(true));
                                PacketRegistry.write(Fantasy.gameServer().nameSpace(), new DailyDataPacket(true, true));
                                Bukkit.getLogger().info("Done");
                            }

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("unban")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            if (strings.length < 2) {
                                Bukkit.getLogger().info("/fantasy unban [name]");
                                return Optional.empty();
                            }

                            IAccountService.get().getUuidByName(strings[1]).whenComplete((uuid, throwable) -> {
                                if (throwable != null) {
                                    throwable.printStackTrace();
                                } else {
                                    Fantasy.banService().invalidateData(uuid);
                                    Fantasy.banService().registerData(new BanData(uuid, 0, ""));
                                    Bukkit.getLogger().info("User %s unbanned".formatted(strings[1]));
                                }
                            });

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("ban")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            if (strings.length < 4) {
                                Bukkit.getLogger().info("/fantasy ban [name] [time] [reason]");
                                return Optional.empty();
                            }

                            IAccountService.get().getUuidByName(strings[1]).whenComplete((uuid, throwable) -> {
                                if (throwable != null) {
                                    throwable.printStackTrace();
                                } else {
                                    val session = SessionStorage.getInstance().findSession(uuid)
                                            .orElse(new ConnectionSession(uuid, SessionState.DISCONNECTED, new GameServer(RealmId.of("temp", -1), GameState.DISCONNECTED)));

                                    val charArray = strings[2].toCharArray();
                                    val timeUnit = switch (charArray[charArray.length - 1]) {
                                        case 's' -> TimeUnit.SECONDS;
                                        case 'm' -> TimeUnit.MINUTES;
                                        case 'h' -> TimeUnit.HOURS;
                                        case 'd' -> TimeUnit.DAYS;
                                        default -> null;
                                    };

                                    if (timeUnit == null) {
                                        Bukkit.getLogger().info("Unknown type of time.");
                                        return;
                                    }

                                    val time = timeUnit.toMillis(NumberUtility.parseInteger(strings[2].substring(0, strings[2].length() - 1)));
                                    if (time == 0) {
                                        Bukkit.getLogger().info("Invalid time.");
                                        return;
                                    }

                                    val banData = new BanData(uuid, Instant.now().toEpochMilli() + time, String.join(" ", Arrays.copyOfRange(strings, 3, strings.length)));
                                    Fantasy.banService().writeData(banData).thenAccept(unused -> {
                                        if (session.getSessionState() != SessionState.DISCONNECTED) {
                                            PacketRegistry.write(Fantasy.gameServer().nameSpace(), new GameKickPlayerPacket(uuid, banData.message()));
                                        }
                                        Bukkit.getLogger().info("User %s banned".formatted(strings[1]));
                                    });
                                }
                            });

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("status")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            if (strings.length < 2) {
                                Bukkit.getLogger().info("/fantasy status [name]");
                                return Optional.empty();
                            }

                            IAccountService.get().getUuidByName(strings[1]).whenComplete((uuid, throwable) -> {
                                if (throwable != null) {
                                    throwable.printStackTrace();
                                } else {
                                    val session = SessionStorage.getInstance().findSession(uuid)
                                            .orElse(new ConnectionSession(uuid, SessionState.DISCONNECTED, new GameServer(RealmId.of("temp", -1), GameState.DISCONNECTED)));

                                    Bukkit.getLogger().info("Session state: " + session.getSessionState().name());
                                    Bukkit.getLogger().info("Session game server state: " + session.getGameServer().getState());
                                    Bukkit.getLogger().info("Session game server realm: " + session.getGameServer().toRealm().getRealmName());
                                }
                            });

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("gstatus")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            val gameServers = GameStorage.getInstance().findAll();
                            Bukkit.getLogger().info("Games count: " + gameServers.size());

                            gameServers.stream().sorted(Comparator.comparingInt(GameServer::getRealmId)).forEach(gameServer -> {
                                val div = TimeUnit.MILLISECONDS.toDays(Instant.now().toEpochMilli() - gameServer.getLastUpdate());

                                Bukkit.getLogger().info(String.format("%s (%s%s%s): %s",
                                        gameServer.toRealm().getRealmName(),
                                        div >= 1 ? ChatColor.RED : ChatColor.WHITE,
                                        dateTimeFormatter.format(new Date(gameServer.getLastUpdate())),
                                        ChatColor.RESET,
                                        gameServer.getState().name()));
                            });

                            return Optional.empty();
                        })
                        .buildCommand())
                .addChildCommand(CommandBuilder.get()
                        .addCommandName("forcetop")
                        .addAccessCheck(ConsoleCommandSender.class::isInstance)
                        .setExecutor((commandSender, strings) -> {
                            new BoardScheduler().run();
                            Bukkit.getLogger().info("Force updated top boards.");

                            return Optional.empty();
                        })
                        .buildCommand())
                .build().register(javaPlugin);
    }

}
