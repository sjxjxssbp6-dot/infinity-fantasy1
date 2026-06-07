package cupcake.nikky.imperium.tower.item.generator.description;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class DescriptionContainer {

    private final ImperiumItem item;

    private final List<String> lore = new ArrayList<>();
    private final List<String> advancedLore = new ArrayList<>();

    private boolean withAdvanced;

    public void applyLine(@NotNull final String line) {
        this.lore.add(line);
        this.advancedLore.add(line);
    }

    public void applyBaseLine(@NotNull final String line) {
        this.lore.add(line);
    }

    public void applyAdvancedLine(@NotNull final String line) {
        withAdvanced = true;
        this.advancedLore.add(line);
    }

    public void applyLines(@NotNull final Collection<String> lines) {
        this.lore.addAll(lines);
        this.advancedLore.addAll(lines);
    }

    public void applyBaseLines(@NotNull final Collection<String> lines) {
        this.lore.addAll(lines);
    }

    public void applyAdvancedLines(@NotNull final Collection<String> lines) {
        withAdvanced = true;
        this.advancedLore.addAll(lines);
    }

    public void applyEmptyLine() {
        this.lore.add(" ");
        this.advancedLore.add(" ");
    }

    public void applyBaseEmptyLine() {
        this.lore.add(" ");
    }

    public void applyAdvancedEmptyLine() {
        withAdvanced = true;
        this.advancedLore.add(" ");
    }

    public boolean isLastLineEmpty() {
        val size = getLore().size();
        if (size == 0) return true;

        return getLore().get(size - 1).trim().isEmpty();
    }

}
