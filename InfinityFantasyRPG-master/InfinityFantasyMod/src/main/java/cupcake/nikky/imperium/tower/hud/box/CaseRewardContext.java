package cupcake.nikky.imperium.tower.hud.box;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseExactReward;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseRarity;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.enginex.element.*;
import ru.cristalix.enginex.util.LayoutPriority;
import ru.cristalix.enginex.util.Palette;
import ru.cristalix.enginex.util.Relative;
import ru.cristalix.enginex.util.V3;

import java.util.Comparator;
import java.util.Map;

public class CaseRewardContext extends VerticalLayout {

    private final GridLayout gridRewards = new GridLayout(LayoutPriority.HORIZONTAL, -1, 6, 14, 14);
    private final VerticalScrollView<?> rewardScroll = new VerticalScrollView<>();

    public CaseRewardContext() {
        super(12D);

        this.addChild(new Text("Возможные награды"), rewardScroll.setDisableBarOnInactive(true).setColor(Palette.GREY).addChild(gridRewards));
    }

    public void updateRewards(@NotNull final Map<Integer, CaseExactReward> itemStacks) {
        gridRewards.clearChilds();

        if (itemStacks.size() > 6) {
            rewardScroll.setSize(new V3(622, 249));
        } else {
            rewardScroll.setSize(new V3(622, 121));
        }

        rewardScroll.getBar().setPos(new V3(8, 0));

        itemStacks.entrySet().stream().sorted(Comparator.comparingInt(entry -> entry.getKey() + Math.max(entry.getKey(), 1) * entry.getValue().getRarity().getOrder())).forEachOrdered(entry -> {
            val caseExactReward = entry.getValue();
            gridRewards.addChild(this.createReward(caseExactReward.getIcon(), caseExactReward.getIconTitle(), caseExactReward.getRarity(), caseExactReward.getChance()));
        });
    }

    private Rectangle createReward(@NotNull final Object icon, @Nullable final String iconTitle, final CaseRarity caseRarity, final double chance) {
        val result = new Rectangle().setColor(Palette.GREY.alpha(0)).setOrigin(Relative.TOP).setAlign(Relative.TOP);
        val iconElement = new CaseIconBox(icon, iconTitle, caseRarity);
        result.setSize(new V3(92, 116));

        val chanceText = new Text(String.format("%.2f%%", chance)).setOrigin(Relative.BOTTOM).setAlign(Relative.BOTTOM);
        result.addChild(iconElement, chanceText);

        return result;
    }

}
