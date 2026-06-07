package cupcake.nikky.imperium.tower.bootstrap.exact.disable;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.bootstrap.BootstrapProcess;
import cupcake.nikky.imperium.tower.connection.ConnectionListener;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.game.GameState;
import cupcake.nikky.imperium.tower.protocol.packet.connection.game.GameConnectionPacket;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class GeneralDisableProcess implements BootstrapProcess {

    @Override
    @SneakyThrows
    public void invoke(@NotNull JavaPlugin javaPlugin) {
        val currentGameServer = Fantasy.gameServer();
        if (currentGameServer.getState() != GameState.RESTARTING)
            currentGameServer.setState(GameState.DISCONNECTED);

        PacketRegistry.write(Fantasy.gameServer().nameSpace(), new GameConnectionPacket(currentGameServer.getState()));
        Thread.sleep(50L);
        ConnectionListener.getInstance().findDisconnectMap().values().forEach(CompletableFuture::join);

        Fantasy.combatProvider().invalidateCombat(Bukkit.getOnlinePlayers().stream().map(Entity::getUniqueId).toArray(UUID[]::new));

        Fantasy.regenerationProvider().unregisterAll();

        Fantasy.loggerService().onDisable();

        new CristalixServiceDisableProcess().invoke(javaPlugin);
    }

}
