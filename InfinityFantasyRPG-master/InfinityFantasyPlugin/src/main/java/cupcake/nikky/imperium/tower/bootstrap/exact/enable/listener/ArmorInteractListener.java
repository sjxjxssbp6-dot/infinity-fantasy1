package cupcake.nikky.imperium.tower.bootstrap.exact.enable.listener;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.ArmorUtility;
import cupcake.nikky.imperium.tower.world.GeneralListener;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ArmorInteractListener {

    private final EntityPlayer entityPlayer;

    public boolean armorAirClick(@NotNull final PacketPlayInBlockPlace packetPlayInBlockPlace) {
        return this.execute(packetPlayInBlockPlace.a);
    }

    public boolean armorBlockClick(@NotNull final PacketPlayInUseItem packetPlayInUseItem) {
        return this.execute(packetPlayInUseItem.c);
    }

    private boolean execute(@NotNull final EnumHand hand) {
        val user = entityPlayer.getBukkitEntity().<User>getBungeePlayer();
        if (user == null) return true;

        val craftItemStack = hand == EnumHand.MAIN_HAND ? entityPlayer.getItemInMainHand() : entityPlayer.getItemInOffHand();
        if (craftItemStack == null || craftItemStack.isEmpty()) return false;

        val item = craftItemStack.asBukkitCopy();
        val slot = ArmorUtility.findArmorType(item.getType());
        val player = entityPlayer.getBukkitEntity();
        val itemStack = ArmorUtility.findArmorByType(player, slot);

        if (itemStack != null && itemStack.getType() != Material.AIR) return false;

        val imperiumItem = Fantasy.itemProvider().find(item);
        if (imperiumItem == null || imperiumItem.getImperiumType() != ItemType.ARMOR) return false;

        if (imperiumItem.getGeneralSettings().requiredLevel() > user.getLevelController().getLevel()) {
            GeneralListener.appendCachedArmorAccess(player.getUniqueId(), false);
            Fantasy.clientProvider().notification(player, SystemNotificationType.WARN, "Ваш уровень слишком мал", "для подобного снаряжения");
            return false;
        } else {
            GeneralListener.appendCachedArmorAccess(player.getUniqueId(), true);
            user.getFantasyInventory().setUpdateArmor(true);
        }

        return false;
    }

}
