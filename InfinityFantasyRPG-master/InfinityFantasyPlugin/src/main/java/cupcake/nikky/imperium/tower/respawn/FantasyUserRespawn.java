package cupcake.nikky.imperium.tower.respawn;

import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
public class FantasyUserRespawn extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter private final User user;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
