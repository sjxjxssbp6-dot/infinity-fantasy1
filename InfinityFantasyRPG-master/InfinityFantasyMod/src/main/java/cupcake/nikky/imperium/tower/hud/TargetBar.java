package cupcake.nikky.imperium.tower.hud;

import cupcake.nikky.imperium.tower.data.MonsterStorage;
import cupcake.nikky.imperium.tower.hud.buff.BuffContext;
import cupcake.nikky.imperium.tower.utility.NumberUtility;
import cupcake.nikky.imperium.tower.utility.ScaleUtility;
import lombok.Getter;
import lombok.val;
import ru.cristalix.enginex.animation.Animation;
import ru.cristalix.enginex.animation.EasingFunc;
import ru.cristalix.enginex.element.CarvedRectangle;
import ru.cristalix.enginex.element.Text;
import ru.cristalix.enginex.element.VerticalLayout;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

public class TargetBar extends VerticalLayout {

    private final CarvedRectangle healthBar = new CarvedRectangle(2D);
    private final Text health = new Text("0/0");
    private final Text percentHealth = new Text("0%");
    private final Text level = new Text("Ур. 1");

    private final CarvedRectangle nameBox = new CarvedRectangle(2D);
    private final Text nameText = new Text();

    private final CarvedRectangle internalHealthBar = new CarvedRectangle(2D);
    @Getter
    private final BuffContext buffContext = new BuffContext(1, 0D);

    private static final double BAR_SIZE = 297D * 1.5D;

    public TargetBar() {
        super(5D);
        this.initialize();
        this.update();
        this.onPreTransform(element -> this.setScale(ScaleUtility.findCurrentScale()));
    }

    private void initialize() {
        this.setPos(new V3(0, 16)).setOrigin(Relative.TOP).setAlign(Relative.TOP);
        healthBar.setSize(new V3(BAR_SIZE, 24)).setColor(Palette.BLACK.alpha(.86));

        internalHealthBar.setColor(Palette.RED).setSize(new V3(BAR_SIZE, 24)).setAlign(Relative.LEFT).setOrigin(Relative.LEFT);

        health.setOrigin(Relative.CENTER).setAlign(Relative.CENTER);
        percentHealth.setOrigin(Relative.RIGHT).setAlign(Relative.RIGHT).setPos(new V3(-4, 0));

        level.setOrigin(Relative.LEFT).setAlign(Relative.LEFT).setPos(new V3(4, 0));

        nameBox.setSize(new V3(BAR_SIZE, 24)).addChild(nameText).setAlign(Relative.TOP).setOrigin(Relative.BOTTOM).setColor(Palette.BLACK.alpha(.62));
        nameText.setAlign(Relative.CENTER).setOrigin(Relative.CENTER);

        buffContext.setOrigin(Relative.TOP_LEFT).setAlign(Relative.TOP_LEFT).setScale(new V3(.5, .5)).setPos(new V3());

        this.addChild(
                nameBox,
                healthBar.addChild(internalHealthBar, health.setShadow(true), percentHealth.setShadow(true), level.setShadow(true)),
                new CarvedRectangle().setColor(Palette.BLACK.alpha(0D)).setSize(new V3(BAR_SIZE, 1)).addChild(buffContext)
        );
    }

    public void update() {
        if (!MonsterStorage.getInstance().isTarget()) {
            this.setEnabled(false);
            return;
        } else this.setEnabled(true);

        val healthInt = Math.max(1D, MonsterStorage.getInstance().getHealth());
        val maxHealthInt = Math.max(MonsterStorage.getInstance().getMaxHealth(), 1D);

        val percent = Math.max(healthInt / maxHealthInt * 100D, 1D);

        val sizeGoal = percent / 100D * BAR_SIZE;
        val currentSize = this.internalHealthBar.getSize().getX();

        Animation.play(internalHealthBar,
                "target_bar_health_down",
                .2D,
                EasingFunc.EASE_OUT_CUBIC,
                (element, progress) -> this.internalHealthBar.getSize().setX(-(currentSize - sizeGoal) * progress + currentSize));

        health.setValue(NumberUtility.classicalFormat(healthInt) + "/" + NumberUtility.classicalFormat(maxHealthInt));
        percentHealth.setValue((int) percent + "%");

        nameText.setValue(MonsterStorage.getInstance().getName());
        level.setValue(String.format("Ур. %d", MonsterStorage.getInstance().getLevel()));

        buffContext.update();
    }

}
