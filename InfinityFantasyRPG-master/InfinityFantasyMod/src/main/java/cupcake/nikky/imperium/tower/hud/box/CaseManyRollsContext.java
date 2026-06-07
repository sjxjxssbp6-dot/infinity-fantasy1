package cupcake.nikky.imperium.tower.hud.box;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseExactReward;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.CarvedRectangle;
import ru.cristalix.enginex.element.GuiScreen;
import ru.cristalix.enginex.element.Text;
import ru.cristalix.enginex.element.VerticalLayout;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CaseManyRollsContext extends GuiScreen {

    private final Text title = new Text();
    private final CarvedRectangle rollsRectangle = new CarvedRectangle(6D);
    private final List<CaseMainRollContext> rollContextMap = new ArrayList<>();

    public CaseManyRollsContext(final int amount) {
        rollsRectangle.setColor(Palette.GREY_DARK.alpha(.62)).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
        this.title.setOrigin(Relative.BOTTOM_LEFT).setAlign(Relative.TOP_LEFT).setPos(new V3(0, -14));

        if (amount == 2) {
            rollsRectangle.setSize(new V3(660, 322));
        } else {
            rollsRectangle.setSize(new V3(660, 658));
        }

        val layout = new VerticalLayout(12D).setOrigin(Relative.CENTER).setAlign(Relative.CENTER);

        for (int i = 0; i < amount; i++) {
            val context = new CaseMainRollContext();
            layout.addChild(context);

            if (i == 0) context.addUpPoint(2D);
            else if (i == amount - 1) context.addDownPoint(2D);

            this.rollContextMap.add(context);
        }

        rollsRectangle.addChild(title, layout);
        this.addChild(rollsRectangle.setRenderLayer(1));
    }

    public void fillItems(@NotNull final Map<Integer, CaseExactReward> items) {
        rollContextMap.forEach(rollContext -> rollContext.fillItems(items));
    }

    public void updateTitle(@NotNull final String title) {
        this.title.setValue(title);
    }

    public void roll(final int[] win) {
        for (int i = 0; i < win.length; i++) {
            rollContextMap.get(i).roll(win[i]);
        }
    }

    @Override
    public void close() {

    }
}
