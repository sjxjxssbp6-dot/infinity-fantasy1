package cupcake.nikky.imperium.tower.bootstrap.exact.enable.listener;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.ArmorUtility;
import cupcake.nikky.imperium.tower.world.GeneralListener;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.InventoryClickType;
import net.minecraft.server.v1_12_R1.PacketPlayInWindowClick;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class ArmorClickListener {

    private final EntityPlayer entityPlayer;

    public boolean clickWindow(@Nullable final PacketPlayInWindowClick packetPlayInWindowClick) {
        val user = entityPlayer.getBukkitEntity().<User>getBungeePlayer();
        if (user == null) return true;

        val cursorItem = entityPlayer.inventory.getCarried();
        val shift = packetPlayInWindowClick.shift;
        val slot = packetPlayInWindowClick.slot;
        val player = entityPlayer.getBukkitEntity();

        if (slot >= 5 && slot <= 8 && cursorItem.isEmpty() && shift != InventoryClickType.SWAP) return false;
        if ((slot < 5 || slot > 8) && shift != InventoryClickType.QUICK_MOVE) {
            if (shift == InventoryClickType.SWAP) user.getFantasyInventory().setUpdateHeldItem(true);
            return false;
        }

        if (slot <= -1) return false;

        val targetCraftItemStack = slot >= 36 ? entityPlayer.inventory.getItem(slot - 36) : entityPlayer.inventory.getItem(slot);

        if (shift == InventoryClickType.SWAP) {
            val hotBarSlot = packetPlayInWindowClick.button;

            val hotbarItem = player.getInventory().getItem(hotBarSlot);
            if (hotbarItem == null || hotbarItem.getType() == Material.AIR) return false;

            val imperiumHotbarItem = Fantasy.itemProvider().find(hotbarItem);
            if (imperiumHotbarItem == null || imperiumHotbarItem.getImperiumType() != ItemType.ARMOR) return false;

            if (imperiumHotbarItem.getGeneralSettings().requiredLevel() > user.getLevelController().getLevel()) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.WARN, "Ваш уровень слишком мал", "для подобного снаряжения");
                GeneralListener.appendCachedArmorAccess(player.getUniqueId(), false);
                return false;
            } else {
                user.getFantasyInventory().setUpdateArmor(true);
            }
        } else if (!cursorItem.isEmpty() && slot >= 5 && slot <= 8) {
            val imperiumCursorItem = Fantasy.itemProvider().find(cursorItem.asBukkitCopy());
            if (imperiumCursorItem == null || imperiumCursorItem.getImperiumType() != ItemType.ARMOR) return false;

            if (imperiumCursorItem.getGeneralSettings().requiredLevel() > user.getLevelController().getLevel()) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.WARN, "Ваш уровень слишком мал", "для подобного снаряжения");
                GeneralListener.appendCachedArmorAccess(player.getUniqueId(), false);
                return false;
            } else {
                user.getFantasyInventory().setUpdateArmor(true);
            }
        } else if (!targetCraftItemStack.isEmpty() && shift == InventoryClickType.QUICK_MOVE) {
            val targetItemStack = targetCraftItemStack.asBukkitCopy();
            val armorType = ArmorUtility.findArmorType(targetItemStack.getType());
            val equippedArmor = ArmorUtility.findArmorByType(player, armorType);

            if (equippedArmor != null && equippedArmor.getType() != Material.AIR) return false;

            val targetItem = Fantasy.itemProvider().find(targetItemStack);
            if (targetItem == null || targetItem.getImperiumType() != ItemType.ARMOR) return false;

            if (targetItem.getGeneralSettings().requiredLevel() > user.getLevelController().getLevel()) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.WARN, "Ваш уровень слишком мал", "для подобного снаряжения");
                GeneralListener.appendCachedArmorAccess(player.getUniqueId(), false);
                return false;
            } else {
                user.getFantasyInventory().setUpdateArmor(true);
            }
        }

        return false;
    }

}
