package cupcake.nikky.imperium.tower.data.prompt;

import dev.xdark.clientapi.item.Item;
import dev.xdark.clientapi.item.ItemStack;
import dev.xdark.clientapi.item.Items;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArmorUtility {

    private static final List<Item> helmets = Arrays.asList(Items.LEATHER_HELMET, Items.CHAINMAIL_HELMET, Items.IRON_HELMET, Items.DIAMOND_HELMET);
    private static final List<Item> chestPlates = Arrays.asList(Items.LEATHER_CHESTPLATE, Items.CHAINMAIL_CHESTPLATE, Items.IRON_CHESTPLATE, Items.DIAMOND_CHESTPLATE);
    private static final List<Item> leggings = Arrays.asList(Items.LEATHER_LEGGINGS, Items.CHAINMAIL_LEGGINGS, Items.IRON_LEGGINGS, Items.DIAMOND_LEGGINGS);
    private static final List<Item> boots = Arrays.asList(Items.LEATHER_BOOTS, Items.CHAINMAIL_BOOTS, Items.IRON_BOOTS, Items.DIAMOND_BOOTS);

    public static @Nullable ArmorType findTypeByItem(@Nullable final ItemStack itemStack) {
        if (itemStack == null) return null;

        val item = itemStack.getItem();
        if (item == null || item == Items.AIR) return null;

        if (helmets.contains(item)) return ArmorType.HELMET;
        if (chestPlates.contains(item)) return ArmorType.CHESTPLATE;
        if (leggings.contains(item)) return ArmorType.LEGGINGS;
        if (boots.contains(item)) return ArmorType.BOOTS;

        return null;
    }

}
