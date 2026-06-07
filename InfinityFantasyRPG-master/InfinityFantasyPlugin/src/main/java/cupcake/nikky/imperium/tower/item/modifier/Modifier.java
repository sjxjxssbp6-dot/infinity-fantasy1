package cupcake.nikky.imperium.tower.item.modifier;

import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseRegistry;
import cupcake.nikky.imperium.tower.item.modifier.description.ModifierDescription;
import cupcake.nikky.imperium.tower.item.modifier.filter.ItemFilter;
import cupcake.nikky.imperium.tower.item.modifier.process.ModifierProcess;
import cupcake.nikky.imperium.tower.item.modifier.range.ModifierRange;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class Modifier {

    private final String id;
    private final List<ModifierTag> tags;
    private final List<ItemFilter> itemTarget;
    private final ModifierPlace place;
    private final ModifierDescription modifierDescription;
    private final ModifierProcess process;
    private final ModifierRange range;

    public static Modifier ofType(@NotNull final String id, @NotNull final ModifierPlace place, @NotNull final ModifierDescription modifierDescription,
                              @NotNull final ModifierProcess process, @NotNull final ModifierRange range, @NotNull final List<ItemType> targets, @NotNull final ModifierTag... tags) {
        return Modifier.ofWithoutRegistry(id, place, modifierDescription, process, range, targets, Arrays.asList(tags)).addToRegistry();
    }

    public static Modifier ofFilter(@NotNull final String id, @NotNull final ModifierPlace place, @NotNull final ModifierDescription modifierDescription,
                              @NotNull final ModifierProcess process, @NotNull final ModifierRange range, @NotNull final List<ItemFilter> targets, @NotNull final ModifierTag... tags) {
        return new Modifier(id, place, modifierDescription, process, range, targets, Arrays.asList(tags)).addToRegistry();
    }

    public static Modifier ofWithoutRegistry(@NotNull final String id, @NotNull final ModifierPlace place, @NotNull final ModifierDescription modifierDescription,
                              @NotNull final ModifierProcess process, @NotNull final ModifierRange range, @NotNull final List<ItemType> targets, @NotNull final List<ModifierTag> tags) {
        return new Modifier(id, place, modifierDescription, process, range, Collections.singletonList(item -> targets.isEmpty() || targets.contains(item.getImperiumType())), tags);
    }

    public static Modifier ofWithoutRegistry(@NotNull final String id, @NotNull final ModifierPlace place, @NotNull final ModifierDescription modifierDescription,
                                             @NotNull final ModifierProcess process, @NotNull final ModifierRange range, @NotNull final List<ItemType> targets, @NotNull final ModifierTag... tags) {
        return new Modifier(id, place, modifierDescription, process, range, Collections.singletonList(item -> targets.isEmpty() || targets.contains(item.getImperiumType())), Arrays.asList(tags));
    }

    public Modifier(@NotNull final String id, @NotNull final ModifierPlace place, @NotNull final ModifierDescription modifierDescription,
                    @NotNull final ModifierProcess process, @NotNull final ModifierRange range, @NotNull final List<ItemFilter> targets,
                    @NotNull final List<ModifierTag> tags) {
        this.id = id;
        this.tags = tags;
        this.place = place;
        this.modifierDescription = modifierDescription;
        this.process = process;
        this.range = range;
        this.itemTarget = targets;
    }

    private Modifier addToRegistry() {
        ModifierBaseRegistry.getRegisteredModifiers().putIfAbsent(getId(), this);
        return this;
    }

    public Modifier copyWithTierLimit(final int limitTier) {
        return new Modifier(id, place, modifierDescription, process, getRange().copyWithTiers(limitTier), new ArrayList<>(itemTarget), new ArrayList<>(tags));
    }

    public boolean containsTag(@NotNull final ModifierTag tag) {
        return this.tags.contains(tag);
    }

    public ModifierCreator process() {
        return new ModifierCreator(this);
    }

}
