package cupcake.nikky.imperium.tower.incubator.reward.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.MathUtility;

@Getter
@RequiredArgsConstructor
public class RandomIntValue implements IntValue {

    private final int min;
    private final int max;

    @Override
    public int get() {
        return MathUtility.randomInRange(min, max);
    }

    @Override
    public String getDescription() {
        return min + "-" + max;
    }
}
