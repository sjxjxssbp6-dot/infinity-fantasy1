package cupcake.nikky.imperium.tower.handler;

import dev.xdark.clientapi.event.render.*;
import lombok.val;
import ru.cristalix.enginex.Enginex;

public class DefaultRenderCancelling {

    public DefaultRenderCancelling() {
        val priority = 100;
        Enginex.registerEvent(JumpPowerRender.class, render -> render.setCancelled(true), priority);
        Enginex.registerEvent(ItemNameRender.class, render -> render.setCancelled(true), priority);
    }

}
