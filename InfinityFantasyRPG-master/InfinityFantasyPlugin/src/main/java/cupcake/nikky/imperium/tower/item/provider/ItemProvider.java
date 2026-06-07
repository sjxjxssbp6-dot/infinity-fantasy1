package cupcake.nikky.imperium.tower.item.provider;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemProvider {

    public @Nullable ImperiumItem find(@Nullable final ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return null;

        val craftItem = CraftItemStack.asNMSCopy(itemStack);
        val tags = craftItem.getTag();

        if(tags == null) return null;

        val id = tags.getString("uid");

        if(id != null) {
            val item = this.find(id);
            if (item == null) return null;

            return item.originalStack(itemStack);
        }

        return null;
    }

    public @Nullable ImperiumItem find(@NotNull final String id) {
        for (ItemObject value : ItemObject.values()) {
            if (value.getItem().getUniqueId().equals(id)) return value.getItem().create();
        }

        return null;
    }

}
