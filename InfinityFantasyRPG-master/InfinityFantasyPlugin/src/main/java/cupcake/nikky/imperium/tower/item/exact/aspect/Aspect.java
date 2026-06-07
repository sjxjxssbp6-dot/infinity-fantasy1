package cupcake.nikky.imperium.tower.item.exact.aspect;

import cupcake.nikky.imperium.tower.item.exact.aspect.cost.CostData;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectEvent;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectFilter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class Aspect {

    private final String title;
    private final AspectType type;
    private final AspectCategory category;
    private final Function<Integer, List<String>> description;
    private final CostData costData;
    private final Map<Class<? extends AspectEvent>, Consumer<?>> events = new HashMap<>();
    private final Map<Class<? extends AspectEvent>, AspectFilter<?>> filters = new HashMap<>();

    public <T extends AspectEvent> void applyEvent(@NotNull final Class<? extends T> clazz, @NotNull final Consumer<T> event) {
        this.events.put(clazz, event);
        this.filters.put(clazz, aspectEvent -> true);
    }

    public <T extends AspectEvent> void applyEvent(@NotNull final Class<? extends T> clazz, @NotNull final Consumer<T> event, @NotNull final AspectFilter<T> filter) {
        this.events.put(clazz, event);
        this.filters.put(clazz, filter);
    }

    @SuppressWarnings("unchecked")
    public <T extends AspectEvent> Consumer<T> findEvent(@NotNull final Class<? extends T> clazz) {
        return (Consumer<T>) this.events.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T extends AspectEvent> boolean aspectFilter(@NotNull final Class<? extends T> clazz, @NotNull final T event) {
        val filter = (AspectFilter<T>) filters.get(clazz);
        return filter == null || filter.canUse(event);
    }

}
