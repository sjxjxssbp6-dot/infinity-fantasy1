package cupcake.nikky.imperium.tower.hud.prompt.cursor;

import cupcake.nikky.imperium.tower.client.protocol.cursor.CursorPromptData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.cristalix.enginex.animation.Animation;
import ru.cristalix.enginex.element.AbstractElement;
import ru.cristalix.enginex.element.HorizontalLayout;
import ru.cristalix.enginex.element.Image;
import ru.cristalix.enginex.element.Text;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class LinePrompt extends HorizontalLayout {

    @Getter
    private final CursorPromptData data;
    @Getter
    private final Consumer<LinePrompt> onComplete;

    public LinePrompt initialize() {
        this.setSpacing(2.5);

        val text = new Text(data.getText()).setShadow(true).setOrigin(Relative.LEFT);
        final AbstractElement image = data.getTexture().isEmpty() ? null : new Image(data.getTexture()).setOrigin(Relative.LEFT).setSize(new V3(16, 16));

        this.addChild(text);
        if (image != null)
            this.addChild(image);

        this.setAlpha(0).setSize(new V3(text.getSize().getX() + 10, 20));

        this.startAnimation();

        return this;
    }

    public void startAnimation() {
        Animation.stop(this);
        Animation.play(this, "start", 1D, (element, aDouble) -> {
            this.setAlpha(aDouble);
            if (aDouble == 1D) {
                Animation.play(this, "waiting", 2D, (element1, aDouble1) -> {
                    if (aDouble1 == 1D) this.stopAnimation();
                });
            }
        });
    }

    public void stopAnimation() {
        Animation.stop(this);
        Animation.play(this, "end", .3D, (element, aDouble) -> {
            this.setAlpha(1 - aDouble);
            if (aDouble >= .95) {
                onComplete.accept(this);
            }
        });
    }

}
