package cupcake.nikky.imperium.tower.item.exact.modification;

import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.modification.drop.ModificationDropApplier;
import org.bukkit.Material;

import java.util.List;

public interface Modification {

    String title();

    List<String> description();

    Material material();

    List<ItemType> acceptItems();

    ModificationDropApplier modificationDrop();

    boolean unique();

    ModificationCursorHandler handler();

    default int data() {
        return 0;
    }

    default int color() {
        return -1;
    }

}
