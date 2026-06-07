package cupcake.nikky.imperium.tower.rng.exact.box;

import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.box.builder.CaseExactRewardBuilder;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseData;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseRarity;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

public class WeightCaseAuraWrapper extends CaseWrapper<AuraObject> {

    public WeightCaseAuraWrapper(@NotNull final AuraObject auraObject, final int weight, @NotNull final CaseRarity caseRarity, final int index) {
        super(auraObject, weight, caseRarity, index);
    }

    @Override
    public String getTitle() {
        return "&f" + getWrapped().getFantasyAura().title();
    }

    @Override
    public void applyToCaseData(@NotNull CaseData caseData, double chance) {
        caseData.addReward(getIndex(), CaseExactRewardBuilder.of()
                .setIcon(getWrapped().getFantasyAura().resourceLocation())
                .setTitle(getTitle())
                .setChance(chance, getCaseRarity())
                .build());
    }

    @Override
    public WeightCaseAuraWrapper copy() {
        return new WeightCaseAuraWrapper(getWrapped(), getWeight(), getCaseRarity(), getIndex());
    }

}
