package cupcake.nikky.imperium.tower.inventory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

@AllArgsConstructor
public class StorageSerialization extends ItemsSerialization {

    @Getter
    private ItemStack[] items;

    public String serialize() {
        return super.serialize(Arrays.stream(items)
                .map(itemStack -> itemStack == null ? new ItemStack(Material.AIR) : itemStack)
                .collect(Collectors.toList()));
    }

    @Override
    public ItemStack[] deserialize(@Nullable final String serialized) {
        if (serialized == null || serialized.isEmpty()) return items;

        val deserialized = super.deserialize(serialized);
        items = deserialized;
        return deserialized;
    }

}
