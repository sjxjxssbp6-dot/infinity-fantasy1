package cupcake.nikky.imperium.tower.harvest.mining;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.item.exact.gear.tool.PickaxeItem;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.quest.requirement.exact.BreakBlockRequirement;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class MiningListener implements DefaultListener {

    @EventHandler
    public void onMining(@NotNull final BlockBreakEvent event) {
        val player = event.getPlayer();
        val block = event.getBlock();

        val patternEnum = MiningPatterns.findPatternEnum(block);
        if (patternEnum == null) {
            event.setCancelled(true);
            return;
        }

        val pattern = patternEnum.getPattern();

        val user = player.<User>getBungeePlayer();
        if (user == null) {
            event.setCancelled(true);
            return;
        }

        if (user.getLevel() < pattern.getLevelRequire()) {
            Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Необходимый уровень персонажа: " + pattern.getLevelRequire());
            return;
        }

        val toolItem = user.getFantasyInventory().getHeldItem();
        if (toolItem == null || !toolItem.checkInstance(PickaxeItem.class)) {
            Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Вскапывание руды возможно только", "при помощи спец. инструмента");
            return;
        }

        if (toolItem.getGeneralSettings().itemLevel() < pattern.getToolLevelRequire()) {
            Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Ваш уровень инструмента слишком мал");
            return;
        }

        val questedPlayer = user.findQuestSettings();
        questedPlayer.findAllInProgressQuests().forEach(playerQuest -> {
            if (playerQuest.isCompleted()) return;

            val isUpdated = new AtomicBoolean(false);

            playerQuest.findRequirementsByClass(BreakBlockRequirement.class).stream().filter(questRequirement -> !questRequirement.isDone()).forEach(questRequirement -> {
                if (questRequirement.onBreak(player, event.getBlock(), true)) {
                    isUpdated.set(true);
                }
            });

            if (isUpdated.get()) playerQuest.createUpdatePacket(player);
        });

        pattern.generate(player, block.getLocation());
        if (patternEnum == MiningPatterns.REDSTONE_ORE || patternEnum == MiningPatterns.GLOWING_REDSTONE_ORE) {
            user.changeHealthPercent(10);
            Fantasy.particleProvider().sendHeal(player, player.getEntityId());
        }

        Fantasy.regenerationProvider().registerBlock(block, pattern.getBlockMaterial(), pattern.getData(), pattern.getRegenerationInTicks());
        Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);

        event.setCancelled(true);
    }

}
