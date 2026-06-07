package cupcake.nikky.imperium.tower.hud.box;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseExactReward;
import dev.xdark.clientapi.item.ItemStack;
import gg.cristalix.particle.system.RandomUtils;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;
import ru.cristalix.enginex.animation.Animation;
import ru.cristalix.enginex.animation.EasingFunc;
import ru.cristalix.enginex.element.HorizontalLayout;
import ru.cristalix.enginex.element.HorizontalScrollView;
import ru.cristalix.enginex.util.SoundUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RollHandler extends HorizontalScrollView<HorizontalLayout> {

    private final Map<Integer, CaseExactReward> currentItems = new HashMap<>();

    private final String rollSound = "sounds/custom/roll.ogg";
    private final String winSound = "sounds/custom/tap.ogg";

    private double lastPos = 0;

    public RollHandler() {
        super(HorizontalLayout.class);
        this.layout.setSpacing(4D);
        this.getBar().setEnabled(false);
        this.onWheelListeners.clear();
    }

    public void fill(@NotNull final Map<Integer, CaseExactReward> items) {
        Animation.stop(this);

        this.currentItems.clear();
        this.clearChilds();
        this.currentItems.putAll(items);

        this.lastPos = 0;

        val listItems = new ArrayList<>(items.values());

        listItems.addAll(items.values());
        listItems.addAll(items.values());
        listItems.addAll(items.values());
        listItems.addAll(items.values());
        listItems.addAll(items.values());
        listItems.addAll(items.values());

        Collections.shuffle(listItems);
        Collections.shuffle(listItems);
        Collections.shuffle(listItems);

        listItems.forEach(caseExactItem -> this.addChild(new CaseIconBox(caseExactItem.getIcon(), caseExactItem.getIconTitle(), caseExactItem.getRarity())));
        this.setScrollProgress(0D);
    }

    public void roll(final int id) {
        val winItem = currentItems.get(id);
        val winElement = new CaseIconBox(winItem.getIcon(), winItem.getIconTitle(), winItem.getRarity());
        this.addChild(winElement);

        for (int i = 0; i < 3; i++) {
            val randomItem = currentItems.get(RandomUtils.getRandomInt(1, currentItems.size()) - 1);
            this.addChild(new CaseIconBox(randomItem.getIcon(), randomItem.getIconTitle(), randomItem.getRarity()));
        }

        Animation.play(this, "rolling", 6, EasingFunc.EASE_IN_OUT_QUINT, (abstractElement, aDouble) -> {
                    this.setScrollProgress(aDouble);

                    val layoutPosX = this.getLayoutMaxOffset() * aDouble + 26;

                    if (layoutPosX > lastPos + 96) {
                        lastPos = layoutPosX;
                        SoundUtil.play(rollSound, 1.0F, 1.0F);
                    }
                })
                .onComplete(abstractElement -> {
                    winElement.animateWin();
                    SoundUtil.play(winSound, 1.0F, 1.0F);
                });
    }

}
