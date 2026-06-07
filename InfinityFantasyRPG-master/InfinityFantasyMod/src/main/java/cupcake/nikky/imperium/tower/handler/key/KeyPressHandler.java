package cupcake.nikky.imperium.tower.handler.key;

import cupcake.nikky.imperium.tower.FantasyMod;
import dev.xdark.clientapi.event.input.KeyPress;
import dev.xdark.clientapi.gui.ingame.ChatScreen;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;

import java.util.Optional;

public class KeyPressHandler {

    public KeyPressHandler(@NotNull final FantasyMod mod) {
        Enginex.registerEvent(KeyPress.class, event -> {
            if (Enginex.getMinecraft().currentScreen() instanceof ChatScreen) return;
            Optional.ofNullable(KeyHandler.findHandler(event.getKey())).ifPresent(handler -> handler.accept(mod));
        }, 1);
    }

}
