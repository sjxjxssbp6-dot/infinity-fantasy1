package me.anfanik.steda.api.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import lombok.var;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InventoryUtility {

    public static boolean canMoveToOtherInventory(@NotNull final Inventory to){
        return to.firstEmpty() != -1;
    }

    public static int getAmountOfEmptySlots(@NotNull final Inventory inventory) {
        int amount = 0;
        for (ItemStack storageContent : inventory.getStorageContents()) {
            if(storageContent == null || storageContent.getType() == Material.AIR) amount += 1;
        }

        return amount;
    }

    public static int getNearInventorySize(final int size){
        int[] sizes = {9, 18, 27, 36, 45, 54};

        for (int i : sizes)
            if(i > size)
                return i;

        return sizes[5];
    }

    public static boolean removeFromMainHand(@NotNull final PlayerInventory inventory, final int amount) {
        val slot = inventory.getHeldItemSlot();

        val itemInHand = inventory.getItem(slot);
        if (itemInHand == null) return false;

        val amountInHand = itemInHand.getAmount();

        if (amountInHand > amount) {
            itemInHand.setAmount(amountInHand - amount);
            inventory.setItem(slot, itemInHand);
            return true;
        } else if (amountInHand == amount) {
            inventory.setItem(slot, new ItemStack(Material.AIR));
            return true;
        }

        return false;
    }

    public static boolean removeItem(@NotNull final Inventory inventory, @NotNull final ItemStack itemRemove, final int amount){
        var tempAmount = amount;
        final List<ItemStack> toRemove = new ArrayList<>();

        for (ItemStack itemStack : inventory.getStorageContents()) {
            if(ItemUtility.isNull(itemStack)) continue;

            if (itemStack.isSimilar(itemRemove)) {
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

    public static int find(@NotNull final Inventory inventory, @NotNull final ItemStack findItem){
        var currentAmount = 0;

        for (ItemStack itemStack : inventory.getStorageContents()) {
            if(findItem.isSimilar(itemStack))
                currentAmount += itemStack.getAmount();
        }

        return currentAmount;
    }

}
