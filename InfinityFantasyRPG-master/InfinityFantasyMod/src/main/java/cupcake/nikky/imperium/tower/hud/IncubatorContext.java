package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.incubator.IncubatorData;
import cupcake.nikky.imperium.tower.data.IncubatorStorage;
import cupcake.nikky.imperium.tower.item.FantasyRenderItem;
import dev.xdark.clientapi.opengl.GlStateManager;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

public class IncubatorContext extends VerticalLayout {

    public IncubatorContext() {
        super(4D);
        this.initialize();
    }

    private void initialize() {
        this.setOrigin(Relative.TOP_LEFT).setAlign(Relative.CENTER).setPos(new V3(-205, -165));
    }

    public void update() {
        this.clearChilds();
        IncubatorStorage.getInstance().getData().stream().map(this::createElement).forEach(this::addChild);
    }

    private AbstractElement createElement(@NotNull final IncubatorData data) {
        val itemRender = new FantasyRenderItem(data.toClient(), false, true).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
        itemRender.setSize(new V3(32, 32));
        val progress = new Text(data.getCurrentProgress() + "/" + data.getMaxProgress()).setShadow(true).setOrigin(Relative.RIGHT).setAlign(Relative.LEFT);
        progress.onPreRender(element -> GlStateManager.disableDepth());
        progress.onPostRender(element -> GlStateManager.enableDepth());
        return itemRender.addChild(progress.setPos(new V3(-2, 0)));
    }

}
