package cupcake.nikky.imperium.tower.user.death;

import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
public class AsyncFantasyUserDeath extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final DamageContainer lastDamageContainer;
    @Getter
    private final User user;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
