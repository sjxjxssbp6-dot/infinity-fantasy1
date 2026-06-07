package cupcake.nikky.imperium.tower.texture;

import dev.xdark.clientapi.resource.ResourceLocation;
import lombok.Data;

@Data
public class RemoteTexture {

    private final ResourceLocation location;
    private final String address, sha1;

}
