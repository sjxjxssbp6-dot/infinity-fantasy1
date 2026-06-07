package cupcake.nikky.imperium.tower.box.builder;

import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.box.CaseServerData;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseCurrency;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseData;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseRarity;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseRewards;
import cupcake.nikky.imperium.tower.rng.Weight;
import cupcake.nikky.imperium.tower.rng.WeightPool;
import cupcake.nikky.imperium.tower.rng.exact.box.CaseWrapper;
import cupcake.nikky.imperium.tower.rng.exact.box.WeightCaseAuraWrapper;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

@NoArgsConstructor(staticName = "of")
public class CaseDataBuilder {

    private final CaseData caseData = new CaseData();
    private WeightPool<CaseWrapper<?>> weightPool;
    private int index;

    public CaseDataBuilder setTitle(@NotNull final String title) {
        caseData.setTitle(title);
        return this;
    }

    public CaseDataBuilder setPrice(final int price, @NotNull final CaseCurrency caseCurrency) {
        caseData.setPrice(price);
        caseData.setCurrency(caseCurrency);
        return this;
    }

    public CaseDataBuilder applyReward(@NotNull final WeightPool<CaseWrapper<?>> weightPool) {
        this.weightPool = weightPool;
        return this;
    }

    public CaseDataBuilder applyReward(@NotNull final CaseWrapper<?> weight) {
        weight.setIndex(index++);
        this.weightPool.apply(new Weight<>(weight));
        return this;
    }

    public CaseDataBuilder applyAuraReward(@NotNull final AuraObject[] auraObjects) {
        this.weightPool = new WeightPool<>(1);
        for (AuraObject auraObject : auraObjects) {
            if (auraObject == AuraObject.NONE || auraObject == AuraObject.NONE_ALL) continue;
            weightPool.apply(new Weight<>(new WeightCaseAuraWrapper(auraObject, 100, CaseRarity.EPIC, index++)));
        }
        return this;
    }

    public CaseServerData build() {
        caseData.setCaseRewards(new CaseRewards());

        if (weightPool == null) {
            Bukkit.getLogger().warning("Error while build case");
        } else {
            var totalWeight = (double) weightPool.findTotalSize();
            weightPool.copyStorage().stream().sorted(Comparator.comparingDouble(weight -> -weight.weightWrapper().getWeight() / totalWeight * 100D)).forEach(weight -> {
                val chance = weight.weightWrapper().getWeight() / totalWeight * 100D;
                val wrapper = weight.weightWrapper();

                wrapper.applyToCaseData(caseData, chance);
            });
        }

        return new CaseServerData(caseData, weightPool);
    }

}
