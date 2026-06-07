package cupcake.nikky.imperium.tower.texture;

import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.cristalix.enginex.Enginex;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TextureLoader {

    @Getter
    private static final TextureLoader instance = new TextureLoader();

    public void loadAll() {
        for (FantasyImage value : FantasyImage.values()) {
            Enginex.getTextureManager().loadTextureFromUrl(value.getUrl(), "neitexx:" + value.getName());
        }
    }

}
