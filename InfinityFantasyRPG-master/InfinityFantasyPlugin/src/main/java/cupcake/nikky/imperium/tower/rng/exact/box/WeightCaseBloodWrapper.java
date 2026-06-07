package cupcake.nikky.imperium.tower.rng.exact.box;

import cupcake.nikky.imperium.tower.blood.BloodObject;
import cupcake.nikky.imperium.tower.box.builder.CaseExactRewardBuilder;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseData;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseRarity;
import me.anfanik.steda.api.utility.ItemBuilder;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class WeightCaseBloodWrapper extends CaseWrapper<BloodObject> {

    public WeightCaseBloodWrapper(@NotNull final BloodObject bloodObject, final int weight, @NotNull final CaseRarity caseRarity, final int index) {
        super(bloodObject, weight, caseRarity, index);
    }

    @Override
    public String getTitle() {
        return "&f" + ChatColor.stripColor(TextUtility.colorize(getWrapped().getTitle()));
    }

    @Override
    public void applyToCaseData(@NotNull CaseData caseData, double chance) {
        var reward = CaseExactRewardBuilder.of()
                .setTitle(getTitle())
                .setChance(chance, getCaseRarity())
                .build();
        if (getWrapped().getIcon() != null) {
            reward.setIcon(ItemBuilder.fromItem(getWrapped().getIcon()).setName(getTitle()).build());
        } else {
            reward.setIcon(getWrapped().getTexture());
            reward.setIconTitle(getTitle());
        }

        caseData.addReward(getIndex(), reward);
    }

    @Override
    public WeightCaseBloodWrapper copy() {
        return new WeightCaseBloodWrapper(getWrapped(), getWeight(), getCaseRarity(), getIndex());
    }

}
