package cupcake.nikky.imperium.tower.item.settings.exact;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.modifier.*;
import cupcake.nikky.imperium.tower.item.settings.ItemSettings;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ModifierSettings extends ItemSettings {

    private final List<Modifier> possibleModifiers = new ArrayList<>();
    private final List<SpecificModifier> specificModifiers = new ArrayList<>();

    public ModifierSettings(ImperiumItem item) {
        super(item);
    }

    public ModifierSettings registerPossibleModifier(@NotNull final Modifier... modifiers) {
        this.possibleModifiers.addAll(Arrays.asList(modifiers));
        return this;
    }

    public ModifierSettings registerPossibleModifier(@NotNull final Collection<Modifier> modifiers) {
        this.possibleModifiers.addAll(modifiers);
        return this;
    }

    public ModifierSettings registerSpecificModifier(@NotNull final SpecificModifier modifier) {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        this.specificModifiers.add(modifier);
        return this;
    }

    public ModifierSettings registerSpecificModifier(@NotNull final Collection<SpecificModifier> modifiers) {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        this.specificModifiers.addAll(modifiers);
        return this;
    }

    public List<Modifier> findPossibleByPlace(@NotNull final ModifierPlace... places) {
        return this.findAllPossibleModifiers().stream().filter(modifier -> {
            for (ModifierPlace place : places) {
                if (modifier.getPlace() == place) return true;
            }

            return false;
        }).collect(Collectors.toList());
    }

    public Modifier findPossibleById(@NotNull final String id) {
        return this.findAllPossibleModifiers().stream().filter(modifier -> modifier.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Modifier> findPossibleByTag(@NotNull final ModifierTag tag) {
        return this.findAllPossibleModifiers().stream().filter(modifier -> modifier.containsTag(tag)).collect(Collectors.toList());
    }

    public SpecificModifier findSpecificById(@NotNull final String id) {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        return this.findAllSpecificModifiers().stream().filter(specificModifier -> specificModifier.getModifierId().equals(id)).findFirst().orElse(null);
    }

    public boolean hasExaltedSpecificModifier() {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        return this.findAllSpecificModifiers().stream().anyMatch(SpecificModifier::isExalted);
    }

    public long findSpecificCount(@NotNull final SpecificModifier targetSpecificModifier) {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        return this.findAllSpecificModifiers().stream().filter(specificModifier -> specificModifier.getModifierId().equals(targetSpecificModifier.getModifierId())).count();
    }

    public List<SpecificModifier> findSpecificByPlace(@NotNull final ModifierPlace... places) {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        return this.findAllSpecificModifiers().stream().filter(modifier -> {
            for (ModifierPlace place : places) {
                if (modifier.getModifier().getPlace() == place) return true;
            }

            return false;
        }).collect(Collectors.toList());
    }

    public List<SpecificModifier> findSpecificByType(@NotNull final ModifierType... types) {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        return this.findAllSpecificModifiers().stream().filter(modifier -> {
            for (ModifierType type : types) {
                if (modifier.getModifierType() == type) return true;
            }

            return false;
        }).collect(Collectors.toList());
    }

    public List<SpecificModifier> findSpecificByTag(@NotNull final ModifierTag tag) {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        return this.findAllSpecificModifiers().stream().filter(modifier -> modifier.getModifier().containsTag(tag)).collect(Collectors.toList());
    }

    public void removeSpecificModifier(@NotNull final String id) {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        this.specificModifiers.removeIf(specificModifier -> specificModifier.getModifierId().equals(id));
    }

    public void removeSpecificModifierByPlace(@NotNull final ModifierPlace place) {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        this.specificModifiers.removeIf(specificModifier -> specificModifier.getModifier().getPlace() == place);
    }

    public void removeSpecificModifierByType(@NotNull final ModifierType type) {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        this.specificModifiers.removeIf(specificModifier -> specificModifier.getModifierType() == type);
    }

    public void removePossibleModifier(@NotNull final String id) {
        this.possibleModifiers.removeIf(modifier -> modifier.getId().equals(id));
    }

    public void removePossibleModifierByTag(@NotNull final ModifierTag tag) {
        this.possibleModifiers.removeIf(modifier -> modifier.getTags().contains(tag));
    }

    public List<Modifier> findAllPossibleModifiers() {
        return new ArrayList<>(possibleModifiers);
    }

    public List<SpecificModifier> findAllSpecificModifiers() {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        return new ArrayList<>(specificModifiers);
    }

    public int findSpecificExtendedModifiersSize() {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        return (int) this.specificModifiers.stream().filter(specificModifier -> specificModifier.getModifierType() == ModifierType.EXTENDED).count();
    }

    public int findSpecificModifiersSize() {
        item().getItemWatcherRegistry().watch(ItemWatcher.MODIFIERS);
        return this.specificModifiers.size();
    }

    public int findPossibleModifiersSize() {
        return this.possibleModifiers.size();
    }

}
