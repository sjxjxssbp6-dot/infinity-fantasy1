package cupcake.nikky.imperium.tower.box.builder;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseExactReward;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseRarity;
import lombok.NoArgsConstructor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(staticName = "of")
public class CaseExactRewardBuilder {

    private final CaseExactReward caseExactReward = new CaseExactReward();

    public CaseExactRewardBuilder setTitle(@NotNull final String iconTitle) {
        caseExactReward.setIconTitle(iconTitle);
        return this;
    }

    public CaseExactRewardBuilder setIcon(@NotNull final String icon) {
        caseExactReward.setIcon(icon);
        return this;
    }

    public CaseExactRewardBuilder setIcon(@NotNull final ItemStack icon) {
        caseExactReward.setIcon(icon);
        return this;
    }

    public CaseExactRewardBuilder setChance(final double chance, @NotNull final CaseRarity caseRarity) {
        caseExactReward.setChance(chance);
        caseExactReward.setRarity(caseRarity);
        return this;
    }

    public CaseExactReward build() {
        return caseExactReward;
    }

}
