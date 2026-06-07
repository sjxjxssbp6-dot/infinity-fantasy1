package cupcake.nikky.imperium.tower.entity.monster.listener;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

public class StickSpawnerInteract implements DefaultListener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onStickInteract(@NotNull final PlayerInteractEvent event) {
        val player = event.getPlayer();
        val itemInHand = player.getInventory().getItemInMainHand();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if (itemInHand == null || itemInHand.getType() != Material.STICK) return;

        val tagMonster = NbtUtility.getTagCompound(itemInHand).getString("monster");
        if (tagMonster == null || tagMonster.trim().equals("")) return;

        event.setCancelled(true);

        val monster = MonsterType.valueOf(tagMonster);
        val blockLocation = event.getClickedBlock().getLocation().toCenterLocation().add(0, 1, 0);

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (!monster.getSpawnLocations().contains(blockLocation)) {
                monster.getSpawnLocations().add(blockLocation);

                Fantasy.monsterProvider().locationProvider().registerLocation(monster, blockLocation);
                Fantasy.monsterProvider().createInstance(monster, blockLocation).naturallySpawn(true);

                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&eДля монстра &c" + monster.getMonsterData().getName() + " &eуспешно добавлен новый якорь");
            }
        } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (monster.getSpawnLocations().contains(blockLocation)) {
                if (Fantasy.monsterProvider().locationProvider().findStorage(monster).findAll().isEmpty()) return;

                val removed = monster.getSpawnLocations().remove(blockLocation);
                Fantasy.monsterProvider().locationProvider().findStorage(monster).remove(blockLocation);

                Fantasy.monsterProvider().findAll().stream()
                        .filter(monster1 -> monster1.getSpawnLocation().equals(blockLocation))
                        .forEach(monster1 -> {
                            monster1.setCanRespawn(false);
                            monster1.invalidateEntity();
                        });

                if (removed) {
                    Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM,
                            "&eДля монстра &c" + monster.getMonsterData().getName() + " &eуспешно удален якорь в точке " +
                                    String.format("%.1f %.1f %.1f %.1f %.1f", blockLocation.getX(), blockLocation.getY(), blockLocation.getZ(), blockLocation.getYaw(), blockLocation.getPitch()));
                }
            }
        }
    }

}
