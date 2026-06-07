package cupcake.nikky.imperium.tower.item.exact.modification;

import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.drop.ModificationDropApplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public abstract class ModificationBase implements Modification {

    private final String title;
    private final List<String> description;
    private final Material material;
    private final int data;
    private final int color;
    private final boolean unique;
    private final List<ItemType> acceptItems;
    private final ModificationDropApplier modificationDrop;


    public ModificationBase(@NotNull final String title, @NotNull final List<String> description, @NotNull final Material material,
                            final int data, final int color, @NotNull final List<ItemType> acceptItems,
                            @NotNull final ModificationDropApplier modificationDrop) {
        this.title = title;
        this.description = description;
        this.material = material;
        this.data = data;
        this.color = color;
        this.acceptItems = acceptItems;
        this.modificationDrop = modificationDrop;
        this.unique = false;
    }

}
