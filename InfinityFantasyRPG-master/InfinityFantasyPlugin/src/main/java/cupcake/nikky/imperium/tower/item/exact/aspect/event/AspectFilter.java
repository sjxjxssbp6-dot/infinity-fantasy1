package cupcake.nikky.imperium.tower.item.exact.aspect.event;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface AspectFilter<T extends AspectEvent> {

    boolean canUse(@NotNull final T aspectEvent);

}
