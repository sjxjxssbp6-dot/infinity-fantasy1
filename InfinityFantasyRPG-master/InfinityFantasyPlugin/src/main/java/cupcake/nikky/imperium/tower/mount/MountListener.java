package cupcake.nikky.imperium.tower.mount;

import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

public class MountListener implements DefaultListener {

    @EventHandler
    public void onSaddleClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.SADDLE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        val mount = event.getDismounted();
        val entity = event.getEntity();

        mount.remove();
        if (!(entity instanceof Player player)) return;

        val user = player.<User>getBungeePlayer();
        user.setMountData(new MountData(null, user.getCurrentMountData().getMountType()));
    }

}
