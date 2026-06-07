package cupcake.nikky.imperium.tower.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NbtUtility {

    public static ItemStack clearSessionTags(@NotNull final ItemStack itemStack) {
        return NbtUtility.applyTagCustomize(itemStack, tag -> {
            tag.remove("uniquePerPlayer");
            tag.remove("uniquePerSession");
            tag.remove("incubator");
        });
    }

    public static NBTTagCompound getTagCompound(@Nullable final ItemStack itemStack) {
        if (itemStack == null) return new NBTTagCompound();
        val nmsItem = CraftItemStack.asNMSCopy(itemStack);
        return nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
    }

    public static ItemStack applyTagCustomize(@NotNull final ItemStack itemStack, @NotNull final Consumer<NBTTagCompound> handler) {
        val craftItem = CraftItemStack.asNMSCopy(itemStack);
        val tag = craftItem.hasTag() ? craftItem.getTag() : new NBTTagCompound();

        handler.accept(tag);

        craftItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(craftItem);
    }

    public static ItemStack applyColorCustomize(@NotNull final ItemStack itemStack, final int color) {
        return applyTagCustomize(itemStack, nbtTagCompound -> nbtTagCompound.setInt("color", color));
    }

}
