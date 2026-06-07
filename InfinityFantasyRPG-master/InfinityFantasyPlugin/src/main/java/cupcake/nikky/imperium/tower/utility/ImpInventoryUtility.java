package cupcake.nikky.imperium.tower.utility;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.InventoryUtility;
import me.anfanik.steda.api.utility.ItemUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImpInventoryUtility {

    public static boolean addItem(@NotNull final Player player, @NotNull final ItemStack... itemStacks) {
        var dropped = false;

        for (ItemStack itemStack : itemStacks) {
            if (player.getInventory().firstEmpty() == -1) {
                Fantasy.bukkitProvider().spawnItem(player.getLocation(), player.getUniqueId(), itemStack);
                dropped = true;
            } else {
                player.getInventory().addItem(NbtUtility.clearSessionTags(itemStack));
            }
        }

        return dropped;
    }

    public static boolean canMoveToInventory(@NotNull final Inventory inventory, @NotNull final ImperiumItem item, final int amount) {
        if (inventory.firstEmpty() != -1) return true;
        if (item.isUnique() || item.getImperiumType() == ItemType.ARMOR || item.getImperiumType() == ItemType.WEAPON || item.getImperiumType() == ItemType.TOOL) return false;

        return ImpInventoryUtility.canMoveToInventory(inventory.getContents(), item, amount);
    }

    /* Best used in async */
    public static boolean canMoveToPlayerEmulate(@NotNull final Player player, @Nullable final ItemStack[] copiedContent, @NotNull final Map<ImperiumItem, Integer> items) {
        if (InventoryUtility.getAmountOfEmptySlots(player.getInventory()) >= items.size()) return true;

        for (ImperiumItem item : items.keySet()) {
            val amount = items.getOrDefault(item, 0);
            if (amount == 0) continue;

            val slot = ImpInventoryUtility.moveToInventory(copiedContent, item, amount);
            if (slot == -1) return false;
        }

        return true;
    }

    public static int moveToInventory(@Nullable final ItemStack[] sourceContent, @NotNull final ImperiumItem item, final int amount) {
        for (int i = 0; i < sourceContent.length; i++) {
            val slot = sourceContent[i];

            if (item.isUnique() || item.getImperiumType() == ItemType.ARMOR || item.getImperiumType() == ItemType.WEAPON || item.getImperiumType() == ItemType.TOOL) {
                if (slot == null || slot.getType() == Material.AIR) {
                    sourceContent[i] = item.createNew(amount);
                    return i;
                }
            } else {
                if (slot == null || slot.getType() == Material.AIR) {
                    sourceContent[i] = item.createNew(amount);
                    return i;
                } else if (slot.getType() == item.getMaterial()) {
                    val foundItem = Fantasy.itemProvider().find(slot);
                    if (foundItem == null) continue;

                    if (foundItem.getObject() == item.getObject() && foundItem.getGeneralSettings().itemLevel() == item.getGeneralSettings().itemLevel() &&
                            foundItem.getDropSettings().droppable() == item.getDropSettings().droppable()) {
                        sourceContent[i] = item.createNew(amount);
                        return i;
                    }
                }
            }
        }

        return -1;
    }

    public static boolean canMoveToInventory(@Nullable final ItemStack[] content, @NotNull final ImperiumItem item, final int amount) {
        return Arrays.stream(content)
                .filter(Objects::nonNull)
                .filter(itemStack -> itemStack.getType() == item.getMaterial())
                .anyMatch(itemStack -> {
                    if (itemStack.getAmount() + amount > itemStack.getMaxStackSize()) return false;

                    val foundItem = Fantasy.itemProvider().find(itemStack);
                    if (foundItem == null) return false;

                    return foundItem.getObject() == item.getObject() &&
                            foundItem.getGeneralSettings().itemLevel() == item.getGeneralSettings().itemLevel() &&
                            foundItem.getDropSettings().droppable() == item.getDropSettings().droppable();
                });
    }

    public static boolean removeItem(@NotNull final Inventory inventory, @NotNull final String uniqueIdToRemove, final int amount){
        var tempAmount = amount;
        final List<ItemStack> toRemove = new ArrayList<>();

        for (ItemStack itemStack : inventory) {
            if(ItemUtility.isNull(itemStack)) continue;
            val imperiumItem = Fantasy.itemProvider().find(itemStack);

            if (imperiumItem != null && imperiumItem.getUniqueId().equals(uniqueIdToRemove)) {
                tempAmount -= itemStack.getAmount();

                if(tempAmount <= 0) {
                    inventory.removeItem(toRemove.toArray(new ItemStack[0]));
                    if(tempAmount == 0){
                        inventory.removeItem(itemStack);
                    } else
                        itemStack.setAmount(Math.abs(tempAmount));

                    return true;
                } else {
                    toRemove.add(itemStack);
                }
            }
        }

        return false;
    }

    public static int find(@NotNull final Inventory inventory, @NotNull final String uniqueId){
        var currentAmount = 0;

        for (ItemStack itemStack : inventory) {
            val imperiumItem = Fantasy.itemProvider().find(itemStack);
            if(imperiumItem != null && imperiumItem.getUniqueId().equals(uniqueId))
                currentAmount += itemStack.getAmount();
        }

        return currentAmount;
    }

}
