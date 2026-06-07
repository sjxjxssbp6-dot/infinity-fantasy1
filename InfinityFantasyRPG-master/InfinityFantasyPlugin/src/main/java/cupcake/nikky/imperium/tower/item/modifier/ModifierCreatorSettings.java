package cupcake.nikky.imperium.tower.item.modifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ModifierCreatorSettings {

    @Getter
    private final Map<ModifierPlace, Integer> storage = new HashMap<>();

    public ModifierCreatorSettings registerPlace(@NotNull final ModifierPlace place, final int amount) {
        this.storage.put(place, amount);
        return this;
    }

}
