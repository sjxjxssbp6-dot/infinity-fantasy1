package cupcake.nikky.imperium.tower.item.listener;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectUseEvent;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorContainer;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import cupcake.nikky.imperium.tower.protocol.packet.ImperiumCraftItemPacket;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.metadata.MetadataType;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import me.anfanik.steda.api.utility.ItemUtility;
import net.diamondden.event.NoFreeSpaceForItemEvent;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class ItemListener implements DefaultListener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemDrop(@NotNull final PlayerDropItemEvent event) {
        val player = event.getPlayer();
        val item = event.getItemDrop().getItemStack();
        val imperiumItem = Fantasy.itemProvider().find(item);

        if (imperiumItem == null || Fantasy.userService().findById(player.getUniqueId()).map(user -> user.isInvalidated() || !user.isLoaded()).orElse(true)) {
            event.setCancelled(true);
            return;
        }

        val dropSettings = imperiumItem.getDropSettings();

        event.setCancelled(false);

        if (!dropSettings.droppable() || !dropSettings.canDrop().test(event.getPlayer())) {
            event.setCancelled(true);
        }

        if (event.isCancelled()) return;

        dropSettings.onDrop().accept(event.getPlayer());
    }

    @EventHandler
    public void onItemDropWithNoSpace(NoFreeSpaceForItemEvent event) {
        val player = event.getPlayer();
        event.getItems().forEach(itemStack -> {
            Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cПривязанный к Вам предмет, что Вы выкинули, исчезнет через некоторое время.");
            Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
            Fantasy.bukkitProvider().spawnItem(player.getLocation(), player.getUniqueId(), itemStack);
        });
    }

    @EventHandler
    public void onInteract(@NotNull final PlayerInteractEvent event) {
        val player = event.getPlayer();

        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getType() == Material.SNOW_BALL) {
                event.setCancelled(true);
                return;
            }

            val item = event.getItem();
            if (item == null || item.getType() == Material.AIR || player.hasCooldown(item.getType())) return;

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null
                    && (event.getClickedBlock().getType() == Material.ENDER_CHEST || event.getClickedBlock().getType() == Material.WORKBENCH)) return;

            val user = player.<User>getBungeePlayer();
            if (user == null) return;

            val imperiumItem = Fantasy.itemProvider().find(item);

            if (imperiumItem == null) return;
            val aspect = imperiumItem.getAspectSettings().aspect();

            if (aspect == null) {
                imperiumItem.getInteractSettings().onRightClick().accept(player);
            } else {
                val eventHandler = imperiumItem.getAspectSettings().aspect().findEvent(AspectUseEvent.class);
                if (eventHandler == null) return;

                val level = imperiumItem.getAspectSettings().aspectLevel();
                val aspectEvent = AspectUseEvent.create(imperiumItem, user, level);

                if (!aspect.aspectFilter(AspectUseEvent.class, aspectEvent)) return;

                if (aspect.costData().getCostProcessType().getProcess().apply(user, user.getAttributes(), aspect.costData().getCostValue().apply(level))) {
                    eventHandler.accept(aspectEvent);

                    val cooldown = aspectEvent.getCooldown();
                    if (cooldown != -1) {
                        Arrays.asList(Material.WOOD_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.DIAMOND_SWORD)
                                .forEach(material -> player.setCooldown(material, cooldown));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInteractAt(@NotNull final PlayerInteractAtEntityEvent event) {
        val player = event.getPlayer();

        val imperiumItem = Fantasy.userService().findById(player.getUniqueId()).map(user -> user.getFantasyInventory().getHeldItem()).orElse(null);
        if (imperiumItem == null || imperiumItem.getImperiumType() != ItemType.POTION) return;

        imperiumItem.getInteractSettings().onRightClick().accept(player);
    }

    @EventHandler
    public void onEntityInteract(@NotNull final PlayerInteractEntityEvent event) {
        val player = event.getPlayer();

        if (event.getRightClicked() instanceof LivingEntity) {
            val imperiumItem = Fantasy.userService().findById(player.getUniqueId()).map(user -> user.getFantasyInventory().getHeldItem()).orElse(null);
            if (imperiumItem == null) return;

            imperiumItem.getInteractSettings().onRightClickAtEntity().accept(player, (LivingEntity) event.getRightClicked());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDragItem(@NotNull final InventoryDragEvent event) {
        val player = (Player) event.getWhoClicked();

        Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> {
            if (user.getMetadataStorage().containsType(MetadataType.CRAFT_VIEW)) {
                new ImperiumCraftItemPacket(player, null).notice(1);
            }
        });
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCursorClickSelf(@NotNull final InventoryClickEvent event) {
        val player = (Player) event.getWhoClicked();
        val clickedInventory = event.getClickedInventory();
        val cursorItem = event.getCursor();
        val slotItem = event.getCurrentItem();

        val user = player.<User>getBungeePlayer();
        if (user == null) return;

        if (ItemUtility.isNull(cursorItem) || ItemUtility.isNull(slotItem)) {
            if (user.getMetadataStorage().containsType(MetadataType.CRAFT_VIEW)) {
                new ImperiumCraftItemPacket(player, null).notice(1);
            }
            return;
        }

        val slotImperiumItem = Fantasy.itemProvider().find(slotItem);
        if (slotImperiumItem == null) return;

        val cursorContainer = new CursorContainer(player, slotItem, cursorItem, event.getClick(), clickedInventory, event.getSlot(), CursorResultType.NONE);
        slotImperiumItem.getCursorSettings().findSelfHandlers().forEach(cursorHandler -> cursorHandler.onHandle(cursorContainer));

        switch (cursorContainer.cursorResultType()) {
            case CANCEL: {
                event.setCancelled(true);
                break;
            }
            case REMOVE_TARGET_AND_ONE_CURSOR:
            case REMOVE_ONE_ITEM: {
                cursorItem.setAmount(Math.max(cursorItem.getAmount() - 1, 0));
                event.setCursor(cursorItem.getAmount() == 0 ? null : cursorItem);
                event.setCancelled(true);
                break;
            }
            case REMOVE_ALL_ITEMS: {
                event.setCursor(null);
                event.setCancelled(true);
                break;
            }
            case REPLACE_CURSOR: {
                event.setCursor(NbtUtility.clearSessionTags(cursorContainer.cursorItemStack()));
                event.setCancelled(true);
                break;
            }
            case NONE: break;
        }
    }

}
