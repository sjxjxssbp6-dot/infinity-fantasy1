package cupcake.nikky.imperium.tower.server.restart;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.Service;
import cupcake.nikky.imperium.tower.protocol.game.GameState;
import cupcake.nikky.imperium.tower.protocol.packet.connection.game.GameConnectionPacket;
import lombok.*;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import ru.cristalix.core.realm.IRealmService;
import ru.cristalix.core.realm.RealmStatus;

import java.io.File;
import java.util.UUID;

public class AutoRestartProvider {

    private Server server;

    public void initialize() {
        this.server = Fantasy.getJavaPlugin().getServer();
        if(server == null) return;

        this.removeRestartFileKey();
    }

    public void startRestartProcess(final int delayInMinutes) {
        if(server == null) throw new RuntimeException("Server is null. Auto restart ignored.");

        IRealmService.get().getCurrentRealmInfo().setStatus(RealmStatus.RESTARTING);
        Fantasy.gameServer().setState(GameState.RESTARTING);
        PacketRegistry.write(Fantasy.gameServer().nameSpace(), new GameConnectionPacket(Fantasy.gameServer().getState()));

        val broadcastSeconds = new int[]{30, 5, 4, 3, 2, 1};
        var minute = delayInMinutes;

        while (minute > 0) {
            val finalMinute = minute;
            Fantasy.runLater(() -> Fantasy.bukkitProvider().sendMessage(ChatType.NOTIFICATION,
                    String.format("&cСервер будет перезагружен через &e%d %s&7!", finalMinute,
                            TextUtility.transformViaAmount(finalMinute, "минуту", "минуты", "минут")), Bukkit.getOnlinePlayers()), (delayInMinutes - minute) * 60 * 20);
            minute--;
        }

        for (int second : broadcastSeconds) {
            Fantasy.runLater(() -> Fantasy.bukkitProvider().sendMessage(ChatType.NOTIFICATION,
                    String.format("&cСервер будет перезагружен через &e%d %s&7!",
                            second, TextUtility.transformViaAmount(second, "секунду", "секунды", "секунд")), Bukkit.getOnlinePlayers()), (delayInMinutes * 60 - second) * 20);
        }

        Fantasy.runLater(() -> {
            val players = Bukkit.getOnlinePlayers();
            Fantasy.bukkitProvider().sendMessage(ChatType.NOTIFICATION, "&cПерезагрузка началась!", players);

            Fantasy.gameServer().setState(GameState.RESTARTING);

            Fantasy.combatProvider().invalidateCombat(players.stream().map(Entity::getUniqueId).toArray(UUID[]::new));
            players.forEach(player -> player.kickPlayer("Перезагрузка!"));

            this.addRestartFileKey();

            server.shutdown();
        }, (delayInMinutes * 60 + 1) * 20);
    }

    @SneakyThrows
    @SuppressWarnings("all")
    private void addRestartFileKey() {
        val file = new File(Fantasy.getJavaPlugin().getDataFolder().getAbsolutePath().replace("plugins/" + Fantasy.getJavaPlugin().getName(), "restart-key"));
        if (file.exists()) return;
        file.createNewFile();
    }

    @SneakyThrows
    @SuppressWarnings("all")
    private void removeRestartFileKey() {
        val file = new File(Fantasy.getJavaPlugin().getDataFolder().getAbsolutePath().replace("plugins/"  + Fantasy.getJavaPlugin().getName(), "restart-key"));
        if (!file.exists()) return;
        file.delete();
    }

    public void shutdownProcess() {
        Fantasy.bukkitProvider().sendMessage(ChatType.NOTIFICATION, "&cВ ближайшее время сервер будет выключен по техническим причинам", Bukkit.getOnlinePlayers());

        Fantasy.runLater(() -> {
            val players = Bukkit.getOnlinePlayers();
            Fantasy.gameServer().setState(GameState.DISCONNECTED);

            Fantasy.combatProvider().invalidateCombat(players.stream().map(Entity::getUniqueId).toArray(UUID[]::new));
            players.forEach(player -> player.kickPlayer("Технические работы"));

            this.removeRestartFileKey();

            server.shutdown();
        }, 5 * 20);
    }


}
