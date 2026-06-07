package cupcake.nikky.imperium.tower.hud.box;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseExactReward;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.animation.Animation;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.Map;

public class CaseMainRollContext extends CarvedRectangle {

    private final RollHandler rollHandler = new RollHandler();
    @Getter @Setter private boolean rolling;

    public CaseMainRollContext() {
        super();

        this.setSize(new V3(622, 94)).setColor(Palette.WHITE.alpha(0));
        rollHandler.setSize(this.getSize().clone());
        this.addChild(rollHandler.setOrigin(Relative.CENTER).setAlign(Relative.CENTER));
    }

    public void addPoints() {
        this.addUpPoint(1D);
        this.addDownPoint(1D);
    }

    public void addUpPoint(final double multiplierSize) {
        this.addChild(new Image("\uE0DB").setOrigin(Relative.BOTTOM).setAlign(Relative.TOP).setPos(new V3(0, -8)).setSize(new V3(16 * multiplierSize, 16 * multiplierSize)));
    }

    public void addDownPoint(final double multiplierSize) {
        this.addChild(new Image("\uE0DC").setOrigin(Relative.TOP).setAlign(Relative.BOTTOM).setPos(new V3(0, 8)).setSize(new V3(16 * multiplierSize, 16 * multiplierSize)));
    }

    public void fillItems(@NotNull final Map<Integer, CaseExactReward> items) {
        this.rollHandler.fill(items);
    }

    public void roll(final int winId) {
        setRolling(true);
        rollHandler.roll(winId);
    }

}
