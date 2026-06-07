package cupcake.nikky.imperium.service.listener;

import me.anfanik.steda.api.listener.DefaultListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class ConnectionListener implements DefaultListener {

    @EventHandler
    public void onConnect(@NotNull final PlayerJoinEvent event) {
        event.getPlayer().kickPlayer("");
    }

    @EventHandler
    public void onAsyncConnect(@NotNull final AsyncPlayerPreLoginEvent event) {
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
    }

    @EventHandler
    public void onDisconnect(@NotNull final PlayerQuitEvent event) {

    }

}
