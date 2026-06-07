package cupcake.nikky.imperium.tower.harvest.fishing;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.exact.gear.tool.FishingRodItem;
import cupcake.nikky.imperium.tower.item.exact.gear.tool.ToolItem;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.quest.requirement.exact.FishingRequirement;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import me.anfanik.steda.api.utility.MathUtility;
import net.minecraft.server.v1_12_R1.EntityFishingHook;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class FishingListener implements DefaultListener {

    private final Cache<UUID, Boolean> delay = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build();

    @EventHandler(ignoreCancelled = true)
    public void onFishing(@NotNull final PlayerFishEvent event) {
        val player = event.getPlayer();

        if (player.hasCooldown(Material.FISHING_ROD)) {
            event.setCancelled(true);
            return;
        }

        val currentState = event.getState();
        ImperiumItem toolItem = Fantasy.itemProvider().find(player.getInventory().getItemInMainHand());
        boolean offhand = false;

        event.setExpToDrop(0);

        val hook = (EntityFishingHook) ((CraftEntity) event.getHook()).getHandle();
        hook.aw = 2;

        if (currentState == PlayerFishEvent.State.IN_GROUND || currentState == PlayerFishEvent.State.FAILED_ATTEMPT)
            return;

        if (currentState == PlayerFishEvent.State.BITE) {
            Fantasy.bukkitProvider().sound(player, AdvancedSounds.RING);
            return;
        } else {
            player.setCooldown(Material.FISHING_ROD, 20);
        }

        if (toolItem == null || !toolItem.checkInstance(FishingRodItem.class)) {
            toolItem = Fantasy.itemProvider().find(player.getInventory().getItemInOffHand());
            offhand = true;
            if (toolItem == null || !toolItem.checkInstance(FishingRodItem.class)) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Ловля рыбы возможна только", "при помощи спец. инструмента");
                event.setCancelled(true);
                return;
            }
        }

        if (currentState == PlayerFishEvent.State.CAUGHT_ENTITY) {
            if (offhand) player.getInventory().setItemInOffHand(player.getInventory().getItemInOffHand());
            else player.getInventory().setItemInMainHand(player.getInventory().getItemInMainHand());
            event.setCancelled(true);
            return;
        }

        if (currentState != PlayerFishEvent.State.CAUGHT_FISH) return;

        val caught = event.getCaught();
        if (caught != null) caught.remove();

        val patterns = FishingPatterns.findPattern(toolItem.apply(ToolItem.class));
        val size = patterns.size();

        if (size == 0) {
            event.setCancelled(true);
            return;
        }

        if (delay.getIfPresent(player.getUniqueId()) != null) return;
        delay.put(player.getUniqueId(), true);

        val questedPlayer = player.<User>getBungeePlayer().findQuestSettings();

        questedPlayer.findAllInProgressQuests().forEach(playerQuest -> {
            if (playerQuest.isCompleted()) return;

            val isUpdated = new AtomicBoolean(false);

            playerQuest.findRequirementsByClass(FishingRequirement.class).stream().filter(questRequirement -> !questRequirement.isDone()).forEach(questRequirement -> {
                if (questRequirement.onFish(player, true)) {
                    isUpdated.set(true);
                }
            });

            if (isUpdated.get()) playerQuest.createUpdatePacket(player);
        });

        val pattern = size > 1 ? patterns.get(MathUtility.randomInRange(0, size - 1)) : patterns.getFirst();
        pattern.generate(player, player.getLocation());
        Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
    }

}
