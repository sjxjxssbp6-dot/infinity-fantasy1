package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.carved.FantasyCarvedRectangle;
import cupcake.nikky.imperium.tower.image.FantasyImageRender;
import dev.xdark.clientapi.resource.ResourceLocation;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.enginex.element.CarvedRectangle;
import ru.cristalix.enginex.element.HorizontalLayout;
import ru.cristalix.enginex.element.Image;
import ru.cristalix.enginex.element.Text;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

public class DataBox extends FantasyCarvedRectangle {

    @Getter
    private final Text text = new Text().setShadow(true).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
    @Getter
    private final Image image = new FantasyImageRender().setSize(new V3(16, 16)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
    @Getter @Setter
    private double carveSizeBox = 2D;
    @Getter @Setter
    private double height = 24D;

    public DataBox(@NotNull final ResourceLocation resourceLocation, @NotNull final String... content) {
        this.updateContent(resourceLocation, content);
    }

    public DataBox(@NotNull final String... content) {
        this.updateText(content);
    }

    public DataBox() {

    }

    private void initialize() {
        val layout = new HorizontalLayout(1D);
        layout.setOrigin(Relative.CENTER).setAlign(Relative.CENTER);

        if (text.getText() != null) {
            layout.addChild(text);
        }

        if (image.getTexture() != null && !image.getTexture().isEmpty()) {
            layout.addChild(image);
        }

        if (preTransformListeners != null) preTransformListeners.clear();
        this.onPreTransform(element -> this.setSize(new V3(layout.getSize().getX() + 16, height)));
        this.setCarveSize(carveSizeBox).setColor(Palette.BLACK.alpha(.6));

        this.addChild(layout);
    }

    public void updateContent(@Nullable final ResourceLocation resourceLocation, @NotNull final String... content) {
        this.getText().setValue(content);
        this.getImage().setTexture(resourceLocation);
        this.clearChilds();
        this.initialize();
    }

    public void updateContent(@NotNull final String icon, @NotNull final String... content) {
        this.getText().setValue(content);
        this.getImage().setTexture(icon);
        this.clearChilds();
        this.initialize();
    }

    public void updateText(@NotNull final String... content) {
        this.updateContent((ResourceLocation) null, content);
    }

    public void updateTexture(@NotNull final ResourceLocation resourceLocation) {
        this.getImage().setTexture(resourceLocation);
        this.clearChilds();
        this.initialize();
    }

}
