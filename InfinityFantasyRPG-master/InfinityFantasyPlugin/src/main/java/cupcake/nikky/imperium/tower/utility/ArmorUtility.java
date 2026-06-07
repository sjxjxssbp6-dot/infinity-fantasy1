package cupcake.nikky.imperium.tower.utility;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ArmorUtility {

    public static PlayerArmorChangeEvent.SlotType findArmorType(@NotNull final Material material) {
        val name = material.name;
        if (name.endsWith("HELMET")) {
            return PlayerArmorChangeEvent.SlotType.HEAD;
        } else if (name.endsWith("LEGGINGS")) {
            return PlayerArmorChangeEvent.SlotType.LEGS;
        } else if (name.endsWith("CHESTPLATE")) {
            return PlayerArmorChangeEvent.SlotType.CHEST;
        } else if (name.endsWith("BOOTS")) {
            return PlayerArmorChangeEvent.SlotType.FEET;
        }

        return null;
    }

    public static ItemStack findArmorByType(@NotNull final Player player, @Nullable final PlayerArmorChangeEvent.SlotType slotType) {
        if (slotType == null) return null;

        if (slotType == PlayerArmorChangeEvent.SlotType.HEAD) return player.getInventory().getHelmet();
        if (slotType == PlayerArmorChangeEvent.SlotType.CHEST) return player.getInventory().getChestplate();
        if (slotType == PlayerArmorChangeEvent.SlotType.LEGS) return player.getInventory().getLeggings();
        if (slotType == PlayerArmorChangeEvent.SlotType.FEET) return player.getInventory().getBoots();

        return null;
    }

}
