package cupcake.nikky.imperium.tower.rng.exact.box;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseData;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseRarity;
import cupcake.nikky.imperium.tower.rng.WeightWrapper;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public abstract class CaseWrapper<T> extends WeightWrapper<T> {

    private int index;
    private CaseRarity caseRarity;

    public CaseWrapper(@NotNull final T t, final int weight, @NotNull final CaseRarity caseRarity, final int index) {
        super(t, weight, false);
        this.caseRarity = caseRarity;
        this.index = index;
    }

    public abstract String getTitle();

    public abstract void applyToCaseData(@NotNull final CaseData caseData, final double chance);

}
