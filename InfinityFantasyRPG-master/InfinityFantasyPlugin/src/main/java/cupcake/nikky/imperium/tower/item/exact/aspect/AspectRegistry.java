package cupcake.nikky.imperium.tower.item.exact.aspect;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AspectRegistry {

    @Getter
    private static final AspectRegistry instance = new AspectRegistry();

    private final Map<Class<? extends Aspect>, Aspect> aspects = new HashMap<>();
    private final Map<String, Aspect> aspectsByClass = new HashMap<>();

    public void registerAspect(@NotNull final Aspect aspect) {
        this.aspects.put(aspect.getClass(), aspect);
        this.aspectsByClass.put(aspect.getClass().getName(), aspect);
    }

    @SuppressWarnings("unchecked")
    public <T extends Aspect> T findAspect(@NotNull final Class<? extends T> aspectClass) {
        return (T) this.aspects.get(aspectClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends Aspect> T findAspect(@NotNull final String aspectClass) {
        return (T) this.aspectsByClass.get(aspectClass);
    }

}
