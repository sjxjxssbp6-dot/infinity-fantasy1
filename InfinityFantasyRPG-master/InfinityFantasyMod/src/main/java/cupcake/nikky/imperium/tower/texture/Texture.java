package cupcake.nikky.imperium.tower.texture;

import dev.xdark.clientapi.resource.ResourceLocation;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Texture {

    private final String key, pathName;

    public ResourceLocation toLocation() {
        return ResourceLocation.of("neitexx", pathName);
    }

    public ResourceLocation toBuffLocation() {
        return ResourceLocation.of("neitexx", "buff/" + pathName);
    }

    public ResourceLocation toGuideLocation() {
        return ResourceLocation.of("neitexx", "guide/" + pathName);
    }

}
