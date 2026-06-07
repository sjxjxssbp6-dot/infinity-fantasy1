package cupcake.nikky.imperium.tower.utility;

import cupcake.nikky.imperium.tower.texture.RemoteTexture;
import cupcake.nikky.imperium.tower.texture.Texture;
import dev.xdark.clientapi.ClientApi;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TextureUtility {

    public static void loadPNG(@NotNull final List<RemoteTexture> images, @NotNull final ClientApi clientApi) {
        TextureUtility.load(images, clientApi, "png");
    }

    public static void load(@NotNull final List<RemoteTexture> images, @NotNull final ClientApi clientApi, @NotNull final String extension) {
        val cacheDir = Paths.get("cache//neitexx");

        try {
            if(!Files.exists(Paths.get("cache//")))
                Files.createDirectory(Paths.get("cache//"));
            if(!Files.exists(cacheDir))
                Files.createDirectory(cacheDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        images.forEach(remoteTexture -> {
            val path = cacheDir.resolve(remoteTexture.getSha1());
            BufferedImage image = null;

            try (val stream = Files.newInputStream(path)){
                image = ImageIO.read(stream);
            } catch (IOException e) {
                try {
                    val url = new URL(remoteTexture.getAddress());
                    image = ImageIO.read(url);

                    val bas = new ByteArrayOutputStream();

                    ImageIO.write(image, extension, bas);
                    bas.flush();

                    val imageInByte = bas.toByteArray();
                    bas.close();

                    Files.write((Paths.get(cacheDir + "/" + remoteTexture.getSha1())), imageInByte);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            clientApi.renderEngine().loadTexture(remoteTexture.getLocation(), clientApi.renderEngine().newImageTexture(image,false,false));
        });
    }

}
