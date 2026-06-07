package cupcake.nikky.imperium.tower.hud.buff;

import cupcake.nikky.imperium.tower.carved.FantasyCarvedRectangle;
import cupcake.nikky.imperium.tower.data.buff.BuffData;
import cupcake.nikky.imperium.tower.image.FantasyImageRender;
import dev.xdark.clientapi.resource.ResourceLocation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import ru.cristalix.enginex.element.Text;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

@RequiredArgsConstructor(staticName = "of")
public class BuffBox extends FantasyCarvedRectangle {

    @Getter
    private final BuffData data;

    private Text stack;
    private Text duration;

    public BuffBox initialize() {
        stack = new Text(this.getStackText(data.getStack())).setShadow(true);
        stack.setOrigin(Relative.CENTER).setAlign(Relative.CENTER);

        duration = new Text(this.getTimeText(data.getDuration())).setShadow(true);
        duration.setOrigin(Relative.BOTTOM).setAlign(Relative.BOTTOM).setColor(Palette.WHITE).setPos(new V3(0, -3));

        this.setSize(new V3(68, 88, 0)).setCarveSize(4D).setColor(Palette.BLACK.alpha(.62));
        this.setRarityColor(this.convertRarityColor());

        if (data.getBuffImage() != null) {
            val image = new FantasyImageRender(ResourceLocation.of(data.getBuffImage().toTexture()));
            image.setSize(new V3(64, 64, 0))
                    .setOrigin(Relative.TOP)
                    .setAlign(Relative.TOP).setPos(new V3(0, 2));
            this.addChild(image.addChild(stack));
        }

        this.addChild(duration);
        return this;
    }

    private Palette convertRarityColor() {
        switch (data.getBuffCategory()) {
            case POSITIVE: return Palette.GREEN;
            case NEUTRAL: return Palette.GREY;
            case NEGATIVE: return Palette.RED;
        }

        return Palette.GREY;
    }

    private String getStackText(final int stack) {
        return String.format("x%d", stack);
    }

    private String getTimeText(final int duration) {
        val days = 60 * 60 * 24 * 99;
        val hours = 60 * 60 * 24;
        val minutes = 60 * 60;

        if (duration > days) return "~";

        if (duration > hours) {
            return (duration / hours) + "д";
        } else if (duration > minutes) {
            val resultHours = duration / minutes;
            val resultMin = duration / 60 - resultHours * 60;

            return String.format("%s:%s", resultHours > 9 ? resultHours : "0" + resultHours, resultMin > 9 ? resultMin : "0" + resultMin);
        } else if (duration < minutes) {
            val resultMin = duration / 60;
            val resultSeconds = duration - resultMin * 60;
            return String.format("%s:%s", resultMin > 9 ? resultMin : "0" + resultMin, resultSeconds > 9 ? resultSeconds : "0" + resultSeconds);
        }

        return "~";
    }

    public void update() {
        stack.setValue(this.getStackText(data.getStack()));
        stack.setEnabled(data.getStack() != 1);

        duration.setValue(this.getTimeText(data.getDuration()));
    }

}
