package cupcake.nikky.imperium.tower.character.event;

import cupcake.nikky.imperium.tower.character.ImperiumCharacter;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class CharacterInteractEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    @Setter
    private boolean cancelled = false;
    @Getter
    private final ImperiumCharacter character;

    public CharacterInteractEvent(@NotNull final Player who, @NotNull final ImperiumCharacter character) {
        super(who);
        this.character = character;
    }

    public static final HandlerList getHandlerList() {
        return handlers;
    }

    public final HandlerList getHandlers() {
        return handlers;
    }

}
