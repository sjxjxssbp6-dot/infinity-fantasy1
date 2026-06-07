package cupcake.nikky.imperium.tower.quest.listener;

import cupcake.nikky.imperium.tower.entity.monster.event.AsyncFantasyMonsterDeath;
import cupcake.nikky.imperium.tower.character.event.CharacterInteractEvent;
import cupcake.nikky.imperium.tower.entity.item.FantasyEntityItem;
import cupcake.nikky.imperium.tower.protocol.packet.ImperiumGlowPacket;
import cupcake.nikky.imperium.tower.quest.requirement.exact.*;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public class QuestListener implements DefaultListener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockInteract(@NotNull final PlayerInteractEvent event) {
        val player = event.getPlayer();
        val block = event.getClickedBlock();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (block == null || block.getType() == Material.AIR) return;

        val user = player.<User>getBungeePlayer();
        val questedPlayer = user.findQuestSettings();

        questedPlayer.findAllInProgressQuests().forEach(playerQuest -> {
            if (playerQuest.isCompleted()) return;

            val isUpdated = new AtomicBoolean(false);

            playerQuest.findRequirementsByClass(BlockInteractRequirement.class).stream().filter(questRequirement -> !questRequirement.isDone()).forEach(questRequirement -> {
                questRequirement.onInteract(player, block, true);
                isUpdated.set(true);
            });

            if (isUpdated.get()) playerQuest.createUpdatePacket(player);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemCollect(@NotNull final PlayerPickupItemEvent event) {
        val item = event.getItem();
        val player = event.getPlayer();

        if (!(((CraftItem) item).getHandle() instanceof FantasyEntityItem))
            return;

        val user = player.<User>getBungeePlayer();
        val questedPlayer = user.findQuestSettings();

        questedPlayer.findAllInProgressQuests().forEach(playerQuest -> {
            if (playerQuest.isCompleted()) return;

            val isUpdated = new AtomicBoolean(false);

            playerQuest.findRequirementsByClass(CollectItemRequirement.class).stream().filter(questRequirement -> !questRequirement.isDone()).forEach(questRequirement -> {
                questRequirement.onCollect(player, item.getItemStack(), true);
                isUpdated.set(true);
            });

            if (isUpdated.get()) playerQuest.createUpdatePacket(player);
        });
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onEntityKill(@NotNull final AsyncFantasyMonsterDeath event) {
        val monster = event.getMonster();

        event.getAttackers().forEach(player -> {
            val user = player.<User>getBungeePlayer();
            val questedPlayer = user.findQuestSettings();

            questedPlayer.findAllInProgressQuests().forEach(playerQuest -> {
                if (playerQuest.isCompleted()) return;

                val isUpdated = new AtomicBoolean(false);

                playerQuest.findRequirementsByClass(KillEntityRequirement.class).stream().filter(questRequirement -> !questRequirement.isDone()).forEach(questRequirement -> {
                    questRequirement.onKill(player, monster, true);
                    isUpdated.set(true);
                });

                playerQuest.findRequirementsByClass(KillAnyEntityRequirement.class).stream().filter(questRequirement -> !questRequirement.isDone()).forEach(questRequirement -> {
                    questRequirement.onKill(player, monster, true);
                    isUpdated.set(true);
                });

                if (isUpdated.get()) playerQuest.createUpdatePacket(user.getSpigot());
            });
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCharacterInteract(@NotNull final CharacterInteractEvent event) {
        val player = event.getPlayer();
        val character = event.getCharacter();

        new ImperiumGlowPacket(player, character.getEntityId()).notice(1);

        val user = player.<User>getBungeePlayer();
        val questedPlayer = user.findQuestSettings();

        questedPlayer.findAllInProgressQuests().forEach(playerQuest -> {
            if (playerQuest.isCompleted()) return;

            val isUpdated = new AtomicBoolean(false);

            playerQuest.findRequirementsByClass(TalkToNPCRequirement.class).stream().filter(questRequirement -> !questRequirement.isDone()).forEach(questRequirement -> {
                if (questRequirement.findState(character.getType()) == 0) {
                    questRequirement.onTalk(player, character.getType(), true);
                    isUpdated.set(true);
                }
            });

            if (isUpdated.get()) playerQuest.createUpdatePacket(player);
        });
    }

}
