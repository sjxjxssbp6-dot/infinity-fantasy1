package cupcake.nikky.imperium.service;

import com.github.NeitExx.bukkit.configuration.api.DefaultConfiguration;
import cupcake.nikky.imperium.service.board.user.general.UserGeneralBoardService;
import cupcake.nikky.imperium.service.board.user.time.UserTopBinaBoardService;
import cupcake.nikky.imperium.service.board.user.time.UserTopHochmaBoardService;
import cupcake.nikky.imperium.service.command.FantasyCommands;
import cupcake.nikky.imperium.service.config.DailySettings;
import cupcake.nikky.imperium.service.listener.ConnectionListener;
import cupcake.nikky.imperium.service.time.BoardScheduler;
import cupcake.nikky.imperium.service.time.TimeScheduler;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.database.Collections;
import cupcake.nikky.imperium.tower.protocol.database.DatabaseLoader;
import cupcake.nikky.imperium.tower.protocol.game.GameServer;
import cupcake.nikky.imperium.tower.protocol.game.GameState;
import cupcake.nikky.imperium.tower.protocol.game.GameStorage;
import cupcake.nikky.imperium.tower.protocol.packet.*;
import cupcake.nikky.imperium.tower.protocol.packet.connection.game.GameConnectionPacket;
import cupcake.nikky.imperium.tower.protocol.packet.connection.game.GameConnectionPingPacket;
import cupcake.nikky.imperium.tower.protocol.packet.connection.session.*;
import cupcake.nikky.imperium.tower.protocol.session.ConnectionSession;
import cupcake.nikky.imperium.tower.protocol.session.SessionStorage;
import lombok.val;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.cristalix.core.CoreApi;
import ru.cristalix.core.account.IAccountService;
import ru.cristalix.core.network.Capability;
import ru.cristalix.core.realm.*;
import ru.cristalix.core.transfer.ITransferService;
import ru.cristalix.core.transfer.TransferService;
import ru.cristalix.core.violation.IViolationService;
import ru.cristalix.core.violation.ViolationService;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BootstrapService extends JavaPlugin {

    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

    @Override
    public void onEnable() {
        val dateTimeFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateTimeFormatter.setCalendar(Calendar.getInstance(TimeZone.getTimeZone("GMT+3")));

        val databaseService = new DatabaseLoader(this).load(executor);

        val coreApi = CoreApi.get();

        val realmService = new RealmService(coreApi.bus(), coreApi.getSocketClient());
        coreApi.registerService(ITransferService.class, new TransferService(coreApi.getSocketClient()));
        coreApi.unregisterService(IRealmService.class);
        coreApi.registerService(IRealmService.class, realmService);
        coreApi.registerService(IViolationService.class, new ViolationService(coreApi.getSocketClient()));

        val currentRealm = realmService.getCurrentRealmInfo();
        Fantasy.initialize(this, new GameServer(currentRealm.getRealmId(), GameState.CONNECTED), databaseService);

        currentRealm.setStatus(RealmStatus.RESTARTING);
        currentRealm.setRealmKind(RealmKind.SERVICE);
        currentRealm.setReadableName("FantasyService");
        currentRealm.setCurrentPlayers(0);
        currentRealm.setMaxPlayers(0);

        Bukkit.getWorlds().forEach(world -> Bukkit.unloadWorld(world, false));

        new ConnectionListener().register(this);

        new FantasyCommands(this);

        Fantasy.configurationService().initialize(this).register(DailySettings.class);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new TimeScheduler(), 20L * 60L, 20L * 60L);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> Fantasy.fractionService().findTopFraction(), 0L, 20L * 60L * 5L);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> SessionStorage.getInstance().findAll().forEach(session -> {
            if (session.getLastUpdate() + 20_000 < Instant.now().toEpochMilli()) {
                SessionStorage.getInstance().invalidate(session.getUuid());
            }
        }), 20L * 60L, 20L * 60L * 5);

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, new BoardScheduler(), 0, 20L * 60L * 5);

        PacketRegistry.registerCapabilities(
                Capability.builder().className(GameConnectionPacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).notification(true).build(),
                Capability.builder().className(UserDonateLogPacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).notification(true).build(),
                Capability.builder().className(UserFractionChange.class.getName()).namespace(Fantasy.gameServer().nameSpace()).notification(true).build(),
                Capability.builder().className(UserTransferAccessibilityPacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).build(),
                Capability.builder().className(SessionConnectionPacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).build(),
                Capability.builder().className(SessionPingBulkPacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).notification(true).build(),
                Capability.builder().className(SessionUpdatePacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).build()
        );

        PacketRegistry.addListener(UserFractionChange.class, (realmId, userFractionChange) -> Fantasy.fractionService().updateChanges());

        PacketRegistry.addListener(UserDonateLogPacket.class, (realmId, userDonateLogPacket) ->
                IAccountService.get().getNameByUuid(userDonateLogPacket.getUuid()).whenComplete((name, throwable) -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                    } else {
                        Fantasy.databaseService().asyncCollection(Collections.DONATE_LOGS).whenComplete((collection, throwable2) -> {
                            if (throwable2 != null) {
                                throwable2.printStackTrace();
                            } else {
                                collection.insertOne(new Document()
                                        .append("uuid", userDonateLogPacket.getUuid().toString())
                                        .append("name", name)
                                        .append("data", userDonateLogPacket.getCategory().getTransform().apply(userDonateLogPacket.getArgument()))
                                        .append("time", dateTimeFormatter.format(new Date(userDonateLogPacket.getTime()))));
                            }
                        });
                    }
                })
        );

        PacketRegistry.addListener(UserTransferAccessibilityPacket.class, (realmId, userTransferAccessibilityPacket) -> {
            val gameServer = GameStorage.getInstance().findExactServer(userTransferAccessibilityPacket.getTargetRealm());
            userTransferAccessibilityPacket.setAccess(gameServer != null && gameServer.getState() == GameState.CONNECTED);
            PacketRegistry.write(userTransferAccessibilityPacket);
        });

        PacketRegistry.addListener(SessionPingBulkPacket.class, (realmId, sessionPingBulkPacket) -> {
            val listUuid = sessionPingBulkPacket.getUsers();
            listUuid.forEach(sessionUuid -> SessionStorage.getInstance()
                    .findSession(sessionUuid)
                    .ifPresent(connectionSession -> connectionSession.setLastUpdate(Instant.now().toEpochMilli())));
        });

        PacketRegistry.addListener(GameConnectionPacket.class, (realmId, gameConnectionPacket) -> {
            val server = GameStorage.getInstance().findServer(realmId);
            server.setState(gameConnectionPacket.getGameState());
            server.setLastUpdate(Instant.now().toEpochMilli());

            if (server.getState() == GameState.CONNECTED) {
                UserTopBinaBoardService.getInstance().sendPacket(realmId);
                UserTopHochmaBoardService.getInstance().sendPacket(realmId);
                UserGeneralBoardService.getInstance().sendPacket(realmId);

                Fantasy.fractionService().sendUpdates();

                Bukkit.getLogger().info("Registered new game server: " + ChatColor.WHITE + realmId.getRealmName() + ChatColor.GOLD + " (" + dateTimeFormatter.format(new Date()) + ")");
            } else {
                SessionStorage.getInstance().findAll().stream()
                        .filter(connectionSession -> connectionSession.getGameServer().equals(server))
                        .forEach(connectionSession -> SessionStorage.getInstance().invalidate(connectionSession.getUuid()));
                GameStorage.getInstance().invalidateServer(realmId);
            }
        });

        PacketRegistry.addListener(SessionConnectionPacket.class, (realmId, sessionConnectionPacket) -> {
            val uuid = sessionConnectionPacket.getUserUuid();
            val session = SessionStorage.getInstance()
                    .findSession(uuid)
                    .orElse(new ConnectionSession(uuid, SessionState.DISCONNECTED, GameStorage.getInstance().findServer(realmId)));

            Fantasy.banService().findOrLoad(uuid).thenAccept(banData -> {
                val nowTime = Instant.now().toEpochMilli();
                if (banData != null && banData.getEndTime() > nowTime) {
                    sessionConnectionPacket.setBanData(banData);
                    sessionConnectionPacket.setAvailable(false);
                    PacketRegistry.write(sessionConnectionPacket);
                    return;
                }

                if (session.getSessionState() == SessionState.DISCONNECTED || (session.getLastUpdate() + 6_500L) < nowTime) {
                    session.setBlocked(true);
                    sessionConnectionPacket.setAvailable(true);
                    SessionStorage.getInstance().registerSession(session);

                    PacketRegistry.write(sessionConnectionPacket);
                } else if (session.getSessionState() == SessionState.SAVING || session.getSessionState() == SessionState.PLAYING || session.isBlocked()) {
                    sessionConnectionPacket.setAvailable(false);
                    PacketRegistry.write(sessionConnectionPacket);
                }

                if (session.getGameServer().getState() == GameState.DISCONNECTED) {
                    PacketRegistry.forward(session.getGameServer().toRealm(), new GameConnectionPingPacket());
                }
            });
        });

        PacketRegistry.addListener(GameConnectionPingPacket.class, (realmId, gameConnectionPingPacket) -> {
            val game = GameStorage.getInstance().findServer(realmId);
            if (game == null) {
                GameStorage.getInstance().registerServer(new GameServer(realmId, GameState.CONNECTED));
            } else {
                game.setState(GameState.CONNECTED);
                game.setLastUpdate(Instant.now().toEpochMilli());
            }
        });

        PacketRegistry.addListener(SessionUpdatePacket.class, (realmId, sessionUpdatePacket) -> {
            val uuid = sessionUpdatePacket.getUserUuid();
            val sessionState = sessionUpdatePacket.getState();

            val session = SessionStorage.getInstance().findSession(sessionUpdatePacket.getUserUuid()).orElse(null);
            if (session == null || !session.getGameServer().toRealm().equals(realmId)) return;

            PacketRegistry.write(sessionUpdatePacket);

            if (sessionState == SessionState.DISCONNECTED) {
                SessionStorage.getInstance().invalidate(uuid);
            } else {
                SessionStorage.getInstance().findSession(uuid).ifPresent(connectionSession -> {
                    connectionSession.setSessionState(sessionUpdatePacket.getState());
                    connectionSession.setLastUpdate(Instant.now().toEpochMilli());
                    connectionSession.setGameServer(GameStorage.getInstance().findServer(realmId));
                    connectionSession.setBlocked(false);
                });
            }
        });
    }

    @Override
    public void onDisable() {
        Fantasy.configurationService().findByClass(DailySettings.class).ifPresent(DefaultConfiguration::save);
    }

}
