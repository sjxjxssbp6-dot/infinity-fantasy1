package cupcake.nikky.imperium.tower.item.settings.exact.cursor;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.aspect.AspectItem;
import cupcake.nikky.imperium.tower.item.exact.harvest.HarvestItem;
import cupcake.nikky.imperium.tower.item.exact.mask.MaskCursorHandler;
import cupcake.nikky.imperium.tower.item.exact.mask.MaskItem;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationItem;
import cupcake.nikky.imperium.tower.protocol.packet.ImperiumCraftItemPacket;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.metadata.MetadataType;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FantasyCursorHandler implements CursorHandler {

    private final MaskCursorHandler maskHandler = new MaskCursorHandler();

    @Override
    public void onHandle(CursorContainer container) {
        if (container.targetItemStack() == null || container.targetItemStack().getAmount() > 1) return;

        val targetItem = Fantasy.itemProvider().find(container.targetItemStack());
        val cursorItem = Fantasy.itemProvider().find(container.cursorItemStack());

        if (targetItem == null || cursorItem == null || cursorItem.checkInstance(HarvestItem.class)) return;

        boolean sound = false;
        val type = targetItem.getImperiumType();
        val isEquip = type == ItemType.WEAPON || type == ItemType.ARMOR;
        val isAdditional = type == ItemType.ASPECT || type == ItemType.TOOL || type == ItemType.EDGE_FRAGMENT || type == ItemType.SHARD_BREAKTHROUGH;

        if (cursorItem.checkInstance(MaskItem.class) && isEquip) {
            maskHandler.onHandle(container, cursorItem.apply(MaskItem.class), targetItem);
            sound = true;
        } else if (cursorItem.checkInstance(ModificationItem.class) && (isEquip || isAdditional)) {
            val modificationItem = cursorItem.apply(ModificationItem.class);
            if (modificationItem.getModification().acceptItems().contains(targetItem.getImperiumType())) {
                modificationItem.getModification().handler().onHandle(container, targetItem, cursorItem.apply(ModificationItem.class));
                sound = true;
            }
        } else if (cursorItem.getImperiumType() == ItemType.ASPECT && (type == ItemType.WEAPON || type == ItemType.ARMOR)) {
            cursorItem.apply(AspectItem.class).getClickHandler().accept(container, targetItem);
            sound = true;
        }

        if (container.cursorResultType() != CursorResultType.CANCEL && container.cursorResultType() != CursorResultType.NONE) {
            val item = targetItem.createNew(container.targetItemStack().getAmount());

            if (container.cursorResultType() == CursorResultType.REMOVE_TARGET_AND_ONE_CURSOR) {
                container.clickedInventory().setItem(container.inventorySlotClick(), new ItemStack(Material.AIR));
            } else {
                container.clickedInventory().setItem(container.inventorySlotClick(), item);

                if (type == ItemType.WEAPON || type == ItemType.ASPECT || type == ItemType.TOOL || type == ItemType.EDGE_FRAGMENT || type == ItemType.SHARD_BREAKTHROUGH) Fantasy.bukkitProvider().sound(container.player(), AdvancedSounds.WEAPON);
                else if (type == ItemType.ARMOR) Fantasy.bukkitProvider().sound(container.player(), AdvancedSounds.CLOTH);
            }

            if (container.cursorResultType() != CursorResultType.REMOVE_ALL_ITEMS && container.cursorItemStack().getAmount() > 1) {
                new ImperiumCraftItemPacket(container.player(), item).notice(0);
                container.player().<User>getBungeePlayer().getMetadataStorage().registerType(MetadataType.CRAFT_VIEW);
            } else if (container.cursorResultType() == CursorResultType.REMOVE_ONE_ITEM && container.cursorItemStack().getAmount() == 1 ||
                    container.cursorResultType() == CursorResultType.REMOVE_ALL_ITEMS || container.cursorResultType() == CursorResultType.REMOVE_TARGET_AND_ONE_CURSOR) {
                new ImperiumCraftItemPacket(container.player(), null).notice(1);
                container.player().<User>getBungeePlayer().getMetadataStorage().invalidateType(MetadataType.CRAFT_VIEW);
            }
        } else if (sound) {
            Fantasy.bukkitProvider().sound(container.player(), AdvancedSounds.CONFUSION);
        }
    }

}
