package cupcake.nikky.imperium.tower.rng.exact.box;

import cupcake.nikky.imperium.tower.box.builder.CaseExactRewardBuilder;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseData;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseRarity;
import cupcake.nikky.imperium.tower.item.ItemObject;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
public class WeightCaseItemWrapper extends CaseWrapper<ItemObject> {

    private int amount;

    public WeightCaseItemWrapper(@NotNull ItemObject wrapped, int weight, int amount, @NotNull final CaseRarity caseRarity, final int index) {
        super(wrapped, weight, caseRarity, index);
        this.amount = amount;
    }

    @Override
    public String getTitle() {
        return getWrapped().getItem().getRare().getColor() + getWrapped().getItem().getName();
    }

    @Override
    public void applyToCaseData(@NotNull final CaseData caseData, final double chance) {
        val item = getWrapped().getItem().create();
        item.getDropSettings().droppable(false);

        caseData.addReward(getIndex(), CaseExactRewardBuilder.of()
                .setIcon(item.createNew(getAmount()))
                .setChance(chance, getCaseRarity())
                .build());
    }

    @Override
    public WeightCaseItemWrapper copy() {
        return new WeightCaseItemWrapper(getWrapped(), getWeight(), getAmount(), getCaseRarity(), getIndex());
    }

}
