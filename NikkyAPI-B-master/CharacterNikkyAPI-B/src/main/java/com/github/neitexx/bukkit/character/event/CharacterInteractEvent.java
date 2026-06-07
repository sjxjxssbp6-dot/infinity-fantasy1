package com.github.neitexx.bukkit.character.event;

import com.comphenix.protocol.wrappers.EnumWrappers;
import com.github.neitexx.bukkit.character.Character;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class CharacterInteractEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    @Getter @Setter
    private boolean cancelled = false;
    @Getter
    private final EnumWrappers.Hand hand;
    @Getter
    private final Character character;

    public CharacterInteractEvent(@NotNull final Player who, @NotNull final Character character, @NotNull final EnumWrappers.Hand hand) {
        super(who);

        this.character = character;
        this.hand = hand;
    }

    public static final HandlerList getHandlerList() {
        return handlers;
    }

    public final HandlerList getHandlers() {
        return handlers;
    }

}
