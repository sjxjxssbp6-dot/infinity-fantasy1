package cupcake.nikky.imperium.tower.item.exact.aspect;

import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

@Getter
@Accessors(fluent = true)
public class AspectBase extends Aspect {

    private final short data;

    public AspectBase(@NotNull final String title, @NotNull final AspectType type,
                      @NotNull final AspectCategory category,
                      @NotNull final Function<Integer, List<String>> description, @NotNull final CostData costData) {
        super(title, type, category, description, costData);

        this.data = 1039;

        AspectRegistry.getInstance().registerAspect(this);
    }

}
