package cupcake.nikky.imperium.tower.connection;

import com.destroystokyo.paper.event.player.PlayerInitialSpawnEvent;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.chat.ChatsInitializer;
import cupcake.nikky.imperium.tower.donate.shop.ShopSelection;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.exact.potion.PotionItem;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.packet.connection.session.SessionConnectionPacket;
import cupcake.nikky.imperium.tower.protocol.packet.connection.session.SessionState;
import cupcake.nikky.imperium.tower.protocol.packet.connection.session.SessionUpdatePacket;
import cupcake.nikky.imperium.tower.quest.session.QuestSessionProvider;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeNpcProvider;
import cupcake.nikky.imperium.tower.trade.player.TradePlayerProvider;
import cupcake.nikky.imperium.tower.utility.PermissionUtility;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import me.func.mod.Anime;
import me.func.mod.events.VisualModLoadEvent;
import me.func.mod.ui.MultiChat;
import me.func.protocol.math.Position;
import me.func.protocol.ui.indicator.Indicators;
import net.diamondden.api.AsyncPlayerJoinEvent;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;
import ru.cristalix.core.CoreApi;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConnectionListener implements DefaultListener {

    @Getter private static final ConnectionListener instance = new ConnectionListener();
    private final Map<UUID, CompletableFuture<Void>> disconnectMap = new ConcurrentHashMap<>(32, 0.75F, 2);

    public Map<UUID, CompletableFuture<Void>> findDisconnectMap() {
        return new HashMap<>(disconnectMap);
    }

    @EventHandler
    public void onVisualDriverLoad(@NotNull final VisualModLoadEvent event) {
        MultiChat.sendChats(event.getPlayer(), ChatsInitializer.getInstance().getKeys());
        Anime.hideIndicator(event.getPlayer(),
                Indicators.EXP,
                Indicators.HEALTH,
                Indicators.ARMOR,
                Indicators.HUNGER,
                Indicators.AIR_BAR,
                Indicators.NAME_TEMPLATE,
                Indicators.POTIONS,
                Indicators.VEHICLE
        );
        Anime.overlayText(event.getPlayer(), Position.BOTTOM_RIGHT, "");
    }

    @EventHandler
    public void onSpawnLocation(@NotNull final PlayerSpawnLocationEvent event) {
        event.setSpawnLocation(BeaconObjects.START.getBeacon().getRespawnLocation());
    }

    @EventHandler
    public void onInitSpawnLocation(@NotNull final PlayerInitialSpawnEvent event) {
        event.setSpawnLocation(BeaconObjects.START.getBeacon().getRespawnLocation());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(@NotNull final PlayerJoinEvent event) {
        val user = Fantasy.userService().findById(event.getPlayer().getUniqueId()).orElse(null);
        if (user == null) {
            event.getPlayer().kickPlayer("При загрузке данных произошла ошибка (P2). Попробуйте позже.");
            return;
        }

        event.setJoinMessage(null);

        val player = event.getPlayer();
        player.setBungeePlayer(user);

        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.updateInventory();

        Fantasy.protocolProvider().registerPacketInjector(((CraftPlayer) player).getHandle());

        new ConnectionProcess(user).processBukkitConnect();
    }

    @EventHandler
    public void onTryConnect(@NotNull final AsyncPlayerJoinEvent event) {
        if (Fantasy.getJavaPlugin() == null || !CoreApi.get().getSocketClient().isConnected()) {
            event.disconnect("Сервер в процессе загрузки");
            return;
        }

        PermissionUtility.canJoin(event.getPlayerId()).whenComplete((result, exception) -> {
            if (exception != null || !result) {
                event.disconnect("Нет доступа");
            } else {
                PacketRegistry.writeAndAwaitResponse(Fantasy.gameServer().nameSpace(), new SessionConnectionPacket(event.getPlayerId())).whenComplete((sessionConnectionPacket, throwable) -> {
                    if (throwable != null || !sessionConnectionPacket.isAvailable()) {
                        if (throwable != null) {
                            event.disconnect("Ошибка во время получения данных:" + throwable.getMessage() + ". Попробуйте позже.");
                        } else {
                            if (sessionConnectionPacket.getBanData() != null) {
                                event.disconnect(sessionConnectionPacket.getBanData().message());
                            } else {
                                event.disconnect("Ошибка во время получения данных (P1). Попробуйте позже.");
                            }
                        }
                    } else {
                        Fantasy.userService().load(event.getPlayerId()).whenComplete((user, throwable1) -> {
                            if (throwable1 != null) {
                                Fantasy.runLater(throwable1::printStackTrace);
                                event.disconnect("При загрузке данных произошла ошибка: " + throwable1.getMessage());
                            } else event.allow();
                        });
                    }
                });
            }
        });
    }

    @EventHandler
    public void onQuit(@NotNull final PlayerQuitEvent event) {
        ShopSelection.getInstance().getConfirmRequestMarks().remove(event.getPlayer().getUniqueId());

        event.setQuitMessage(null);

        val player = event.getPlayer();
        player.closeInventory();

        val userOptional = Fantasy.userService().findById(player.getUniqueId());

        if (Fantasy.combatProvider().isInCombat(player.getUniqueId())) {
            userOptional.ifPresent(user -> user.processRespawn(player1 -> {}));
        }

        ItemObject.getPotions().forEach(item -> item.getItem().apply(PotionItem.class).removeCooldown(player));

        TradeNpcProvider.getInstance().unregister(player.getUniqueId());
        QuestSessionProvider.getInstance().invalidateSession(player.getUniqueId());
        val session = TradePlayerProvider.getInstance().findSession(player.getUniqueId());
        if (session != null) session.closeSession(player.getUniqueId());

        userOptional.ifPresent(user -> {
            if (user.isInvalidated() || !user.isLoaded()) return;

            PacketRegistry.write(Fantasy.gameServer().nameSpace(), new SessionUpdatePacket(player.getUniqueId(), SessionState.SAVING));

            val future = user.saveAllDataAndInvalidateCache();
            this.disconnectMap.put(player.getUniqueId(), future);
            future.whenCompleteAsync((v, throwable) -> {
                this.disconnectMap.remove(player.getUniqueId());

                if (throwable != null)
                    Fantasy.runLater(throwable::printStackTrace);

                PacketRegistry.write(Fantasy.gameServer().nameSpace(), new SessionUpdatePacket(player.getUniqueId(), SessionState.DISCONNECTED));
            });
        });
    }

}
