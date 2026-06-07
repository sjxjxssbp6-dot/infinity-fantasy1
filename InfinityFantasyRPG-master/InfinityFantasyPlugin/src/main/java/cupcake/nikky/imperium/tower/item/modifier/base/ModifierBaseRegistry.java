package cupcake.nikky.imperium.tower.item.modifier.base;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModifierBaseRegistry {

    @Getter
    private static final Map<String, Modifier> registeredModifiers = new HashMap<>();

    public static Modifier findById(@NotNull final String id) {
        return registeredModifiers.get(id);
    }

    private static final Map<String, Modifier[]> cachedBaseModifiers = new HashMap<>();

    public static List<Modifier> findExtendedModifiers(@NotNull final ImperiumItem item) {
        val filter = Arrays.asList(ModifierPlace.PREFIX, ModifierPlace.SUFFIX);
        return registeredModifiers.values().stream()
                .filter(modifier -> modifier.getItemTarget().stream().allMatch(itemFilter -> itemFilter.isSuitedItem(item)))
                .filter(modifier -> filter.contains(modifier.getPlace()))
                .collect(Collectors.toList());
    }

    public static Modifier[] createBaseModifiers(@NotNull final ModifierBaseCreatorType creatorType) {
        val cached = cachedBaseModifiers.get(creatorType.name().toLowerCase());
        if (cached != null) return cached;

        val result = creatorType.getCreator().create();
        cachedBaseModifiers.put(creatorType.name().toLowerCase(), result);

        return result;
    }

}
