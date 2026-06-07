package cupcake.nikky.imperium.tower.attack.event;

import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@RequiredArgsConstructor
public class FantasyDamageEvent extends Event implements Cancellable {

    @Setter
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();

    private final DamageContainer damageContainer;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
