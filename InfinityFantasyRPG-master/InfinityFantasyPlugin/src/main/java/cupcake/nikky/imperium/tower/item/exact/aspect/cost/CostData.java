package cupcake.nikky.imperium.tower.item.exact.aspect.cost;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public class CostData {

    private final CostProcessType costProcessType;
    private final Function<Integer, Integer> costValue;

    public CostData(@NotNull final CostProcessType type, final int costValue) {
        this.costProcessType = type;
        this.costValue = level -> costValue;
    }

}
