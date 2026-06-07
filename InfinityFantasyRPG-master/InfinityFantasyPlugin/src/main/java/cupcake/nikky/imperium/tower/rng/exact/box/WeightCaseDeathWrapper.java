package cupcake.nikky.imperium.tower.rng.exact.box;

import cupcake.nikky.imperium.tower.box.builder.CaseExactRewardBuilder;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseData;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseRarity;
import cupcake.nikky.imperium.tower.death.DeathObject;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class WeightCaseDeathWrapper extends CaseWrapper<DeathObject> {

    public WeightCaseDeathWrapper(@NotNull final DeathObject deathObject, final int weight, @NotNull final CaseRarity caseRarity, final int index) {
        super(deathObject, weight, caseRarity, index);
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
            reward.setIcon(getWrapped().getIcon());
        } else {
            reward.setIcon(getWrapped().getTexture());
            reward.setIconTitle(getTitle());
        }

        caseData.addReward(getIndex(), reward);
    }

    @Override
    public WeightCaseDeathWrapper copy() {
        return new WeightCaseDeathWrapper(getWrapped(), getWeight(), getCaseRarity(), getIndex());
    }

}
