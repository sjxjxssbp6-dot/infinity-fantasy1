package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.utility.ScaleUtility;
import lombok.val;
import ru.cristalix.enginex.animation.Animation;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

public class LevelUpTitle extends HorizontalLayout {

    private int lastLevel;
    private final double defaultScale;

    private final Text levelText = new Text().setShadow(true);

    private static final Palette LEVEL_COLOR = Palette.YELLOW_LIGHT;
    private static final Palette SUB_TITLE_COLOR = Palette.ORANGE;

    public LevelUpTitle() {
        super(8D + 16 * 2.375);
        this.defaultScale = 2D;

        this.updateScaleToDefault();
        levelText.setScale(new V3(2.375D, 2.375D)).setColor(LEVEL_COLOR);

        this.onPreTransform(element -> {
            if (!Animation.hasAnimation(this, "show")) {
                this.updateScaleToDefault();
            }
        });

        this.addChild(
                new VerticalLayout(6D).addChild(new Text("Ваш уровень повышен").setShadow(true).setColor(LEVEL_COLOR),
                                new Text("Доступны новые очки искажений").setShadow(true).setColor(SUB_TITLE_COLOR))
                        .setOrigin(Relative.RIGHT).setAlign(Relative.LEFT),
                levelText.setOrigin(Relative.RIGHT).setAlign(Relative.RIGHT)
        ).setOrigin(Relative.CENTER).setAlign(Relative.CENTER).setPos(new V3(0, -150));

        this.setEnabled(false);
    }

    public void show(final int level) {
        if (this.lastLevel == level) return;

        this.hide(true);

        this.lastLevel = level;
        this.levelText.setValue(String.valueOf(this.lastLevel));

        this.updateScaleToDefault().setEnabled(true).setAlpha(1D);

        Animation.play(this, "show", .5D, (element, progress) -> {
            val next = progress > .5 ? .5 - (progress - .5) : progress;
            this.setScale(new V3(defaultScale + next, defaultScale + next));
        }).onStop(element -> this.hide(false));
    }

    public void hide(final boolean force) {
        if (force) {
            Animation.stop(this);
            this.setEnabled(false);
        } else {
            this.updateScaleToDefault();
            Animation.play(this, "hide", .5, 5D, (element1, progress) -> {
                        this.setAlpha(1 - progress);
                    })
                    .onComplete(element1 -> this.setEnabled(false))
                    .onStop(element1 -> this.setEnabled(false));
        }
    }

    private LevelUpTitle updateScaleToDefault() {
        this.setScale(new V3(defaultScale, defaultScale));
        return this;
    }

    @Override
    public HorizontalLayout setScale(V3 scale) {
        return super.setScale(scale.multiply(ScaleUtility.findCurrentScale()));
    }

    @Override
    public HorizontalLayout setEnabled(boolean enabled) {
        this.lastLevel = 0;
        return super.setEnabled(enabled);
    }
}
