package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.handler.key.KeyHandler;
import cupcake.nikky.imperium.tower.image.FantasyImageRender;
import dev.xdark.clientapi.gui.ingame.ChatScreen;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.animation.Animation;
import ru.cristalix.enginex.element.Text;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

public class TreeIndicator extends FantasyImageRender {

    private final Text count = new Text().setShadow(true).setOrigin(Relative.CENTER).setAlign(Relative.BOTTOM_RIGHT);
    private int currentCount;

    public TreeIndicator(@NotNull final FantasyMod fantasyMod) {
        super(FantasyImage.TREE_ICON.toTexture());
        this.setSize(new V3(32, 32)).setOrigin(Relative.BOTTOM_LEFT).setAlign(Relative.BOTTOM).setPos(new V3(190, -8));
        this.addChild(count);

        this.onPreTick(image -> this.setEnabled(currentCount > 0));
        this.setTooltip("Доступны очки в дереве Искажения");
        this.onLeftClick(image -> {
            if (Enginex.getScreen() instanceof ChatScreen) {
                KeyHandler.TREE.handler().accept(fantasyMod);
            }
        });
    }

    private void updateAnimation() {
        if ((Animation.hasAnimation(this, "pulse_1") || Animation.hasAnimation(this, "pulse_2")) && currentCount > 0) return;
        if (currentCount <= 0) {
            Animation.stop(this);
            return;
        }

        this.playAnimation();
    }

    private void playAnimation() {
        Animation.play(this, "pulse_1", 1, (abstractElement, aDouble) -> {
            this.setAlpha(aDouble);
        }).playAfter(this, "pulse_2", 1, (abstractElement, aDouble) -> {
            this.setAlpha(1 - aDouble);
        }).onComplete(abstractElement -> this.playAnimation());
    }

    public void setCount(final int count) {
        this.currentCount = count;
        this.count.setValue(String.valueOf(count));

        this.updateAnimation();
    }

}
