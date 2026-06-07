package cupcake.nikky.imperium.tower.entity.monster.event;

import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;

@RequiredArgsConstructor
public class AsyncFantasyMonsterDeath extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final DamageContainer lastDamageContainer;
    @Getter
    private final Monster monster;
    @Getter
    private final Set<Player> attackers;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
