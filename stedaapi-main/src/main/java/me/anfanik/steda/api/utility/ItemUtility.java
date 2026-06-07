package me.anfanik.steda.api.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemUtility {

    public static String serialize(@NotNull final ItemStack item) throws IOException {
        val outputStream = new ByteArrayOutputStream();
        val dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeObject(item);
        dataOutput.close();

        return Base64Coder.encodeLines(outputStream.toByteArray());
    }

    public static ItemStack deserialize(@Nullable final String data) throws IOException {
        if (data == null || data.isEmpty()) return new ItemStack(Material.AIR);

        try {
            val inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            val dataInput = new BukkitObjectInputStream(inputStream);
            val item = dataInput.readObject();

            dataInput.close();

            return (ItemStack) item;
        } catch (ClassNotFoundException exception) {
            throw new IOException("Unable to decode class type", exception);
        } catch (ClassCastException exception) {
            throw new IOException("Deserialized object is not of type ItemStack", exception);
        }
    }

    public static boolean isNull(@Nullable final ItemStack itemStack){
        return itemStack == null || itemStack.getType() == Material.AIR;
    }

    public static ItemStack removeItem(@NotNull final ItemStack itemStack, final int amount){
        if(amount < 1) return itemStack;

        val totalAmount = itemStack.getAmount() - amount;

        if(totalAmount > 0) itemStack.setAmount(totalAmount);
        else return new ItemStack(Material.AIR);

        return itemStack;
    }

}
