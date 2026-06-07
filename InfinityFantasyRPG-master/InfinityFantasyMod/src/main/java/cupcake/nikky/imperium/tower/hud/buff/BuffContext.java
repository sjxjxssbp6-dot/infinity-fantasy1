package cupcake.nikky.imperium.tower.hud.buff;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.data.buff.BuffData;
import cupcake.nikky.imperium.tower.data.buff.BuffStorage;
import cupcake.nikky.imperium.tower.utility.ScaleUtility;
import lombok.val;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.element.GridLayout;
import ru.cristalix.enginex.util.LayoutPriority;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BuffContext extends GridLayout {

    private final BuffStorage instance;

    public BuffContext(final int flag, final double scale) {
        super(LayoutPriority.HORIZONTAL, 3, flag == 0 ? 6 : 9, 6D);
        this.setOrigin(Relative.TOP_LEFT)
                .setAlign(Relative.TOP_LEFT)
                .setPos(new V3(16, 16, 0))
                .setInteractive(true);

        if (flag == 0) instance = BuffStorage.getInstance();
        else instance = BuffStorage.getMonsterInstance();

        if (scale != 0) this.onPreTransform(element -> this.setScale(new V3(scale, scale)).getScale().multiply(ScaleUtility.findCurrentScale()));

        this.reinitialize();
    }

    public void reinitialize() {
        this.clearChilds();

        instance.getBuffs().stream().sorted(Comparator.comparingInt(BuffData::getId)).forEachOrdered(buffData -> {
            val box = BuffBox.of(buffData).initialize();
            box.update();
            this.addChild(box);

            FantasyMod.tooltipHandler().applyHoverTooltip(box, buffBox1 -> {
                final List<String> description = new ArrayList<>();
                val data = ((BuffBox) buffBox1).getData();

                description.add(data.getTitle());
                if (data.getDescription() != null && data.getDescription().length > 0) {
                    description.add(" ");
                    description.addAll(Arrays.asList(data.getDescription()));
                }

                description.add("  ");
                description.add("§fДлительность: §6" + Enginex.formatSeconds(data.getDuration()));

                return description;
            });
        });
    }

    public void update() {
        getChilds().stream().map(BuffBox.class::cast).forEach(buffBox -> {
            buffBox.update();
            this.addChild(buffBox);
        });
    }

}
