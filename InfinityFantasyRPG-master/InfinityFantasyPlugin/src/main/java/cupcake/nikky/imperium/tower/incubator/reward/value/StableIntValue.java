package cupcake.nikky.imperium.tower.incubator.reward.value;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StableIntValue implements IntValue {

    private final int value;

    @Override
    public int get() {
        return value;
    }

    @Override
    public String getDescription() {
        return String.valueOf(get());
    }
}
