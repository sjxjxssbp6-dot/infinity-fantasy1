package cupcake.nikky.imperium.tower.hud.teleport;

import dev.xdark.clientapi.resource.ResourceLocation;
import lombok.Data;

@Data
public class TeleportContainer {

    private final String title;
    private final ResourceLocation resourceLocation;
    private final String requirement;
    private final boolean blocked;
    private final int teleportCost;

}
