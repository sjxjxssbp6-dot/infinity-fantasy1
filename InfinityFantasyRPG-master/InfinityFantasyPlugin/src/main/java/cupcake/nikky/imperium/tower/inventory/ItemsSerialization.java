package cupcake.nikky.imperium.tower.inventory;

import cupcake.nikky.imperium.tower.Fantasy;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.core.data.BinaryData;
import ru.cristalix.core.data.CompressedBinaryData;
import ru.cristalix.core.data.SimpleBinaryData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;

public abstract class ItemsSerialization {

    protected String serialize(@NotNull final Collection<ItemStack> data) {
        return this.serialize(data.toArray(new ItemStack[0]));
    }

    protected String serialize(@NotNull final ItemStack[] data) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeInt(data.length);

            for (ItemStack datum : data) {
                dataOutput.writeObject(datum);
            }

            dataOutput.close();

            var binaryData = new SimpleBinaryData();
            binaryData.setValue(outputStream.toByteArray());
            return Fantasy.gson().toJson(binaryData, SimpleBinaryData.class);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to save item stacks.", ex);
        }
    }

    protected ItemStack[] deserialize(@Nullable final String serialized) {
        if (serialized == null) return new ItemStack[1];

        try {
            var binaryData = Fantasy.gson().fromJson(serialized, SimpleBinaryData.class);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(binaryData.getValue());
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (Exception ex) {
            Bukkit.getLogger().warning("Unable to decode class type. ItemsSerialization#deserialize(String)");
            return new ItemStack[1];
        }

    }

}
