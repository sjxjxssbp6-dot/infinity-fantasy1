package cupcake.nikky.imperium.tower.item.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import me.anfanik.steda.api.utility.InventoryUtility;
import org.bukkit.Material;

import java.util.Arrays;

public class ResetOrb extends DefaultImperiumItem {

    public ResetOrb() {
        super(ItemRare.NONE, ItemType.SHARD, Material.QUARTZ, (short) 0, "Сфера восстановления",
                Arrays.asList(
                        "При использовании дает",
                        "очко восстановления для дерева Грани",
                        " ",
                        "&r\uE3FF &7Использовать",
                        "&r\uE3FF + SHIFT &7Использовать 8 штук"));

        getLootSettings().dropEntityLevel(3)
                .weightApplier(data -> totalSize -> totalSize * 8 + Math.max(data.getLevel() - LevelStorage.getInstance().getStartShardEdgeLevel(), 0) * 2)
                .maxAmountHandler(data -> data.getLevel() > LevelStorage.getInstance().getStartShardEdgeLevel() ? 2 : 1);
        getGeneralSettings().npcTradePrice(20);

        getItemWatcherRegistry().registerWatcher(ItemWatcher.DROPPABLE);

        getInteractSettings().onRightClick(player -> {
            if (player.hasCooldown(getMaterial())) return;

            var amount = player.isSneaking() ? 8 : 1;
            if (InventoryUtility.removeFromMainHand(player.getInventory(), amount)) {
                player.<User>getBungeePlayer().findTreeSettings().changeResetPoints(amount);
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);

                player.setCooldown(getMaterial(), 3);
                Fantasy.clientProvider().notification(player, SystemNotificationType.FINE, "Добавлено очков восстановления: %d".formatted(amount));
            }
        });
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.RESET_ORB;
    }

    @Override
    public ImperiumItem create() {
        return new ResetOrb();
    }

}
