package cupcake.nikky.imperium.tower.item.settings.exact.cursor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public class CursorContainer {

    private final Player player;
    @Setter private ItemStack targetItemStack;
    @Setter private ItemStack cursorItemStack;
    private final ClickType clickType;
    private final Inventory clickedInventory;
    private final int inventorySlotClick;

    @Setter
    private CursorResultType cursorResultType;

}
