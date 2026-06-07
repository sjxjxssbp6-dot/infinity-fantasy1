package cupcake.nikky.imperium.tower.pandora;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.pandora.PandoraChestRemovePacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.pandora.PandoraChestSpawnPacket;
import cupcake.nikky.imperium.tower.entity.monster.loot.LootStrategies;
import cupcake.nikky.imperium.tower.entity.monster.modifier.ModifierCollection;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PandoraHandler {

    public void interactPandoraBlock(@NotNull final Player player, @NotNull final PandoraData data) {
        new PandoraSelection().open(player, data);
    }

    public void createPandoraBlock(@NotNull final Player player, @NotNull final PandoraData pandoraData) {
        var location = pandoraData.getPandoraLocation();
        player.sendBlockChange(location, Material.CHEST, (byte) 0);
        player.<User>getBungeePlayer().sendPacket(
                new PandoraChestSpawnPacket(pandoraData.getUuid(), location.getX(), location.getY(), location.getZ())
        );
        Fantasy.particleProvider().sendPandoraSpawn(player, location.clone().toCenterLocation());
        Fantasy.bukkitProvider().sound(player, AdvancedSounds.RING);
    }

    public void invalidatePandoraBlockAndSpawnMonster(@NotNull final Player player, @NotNull final PandoraData pandoraData) {
        player.<User>getBungeePlayer().findPandoraSettings().invalidatePandoraCache(pandoraData);
        Fantasy.particleProvider().sendPandoraOpen(player, pandoraData.getPandoraLocation().clone().toCenterLocation());
        Fantasy.bukkitProvider().sound(player, Sound.BLOCK_ANVIL_FALL);
        Fantasy.runLater(() -> {
            var user = player.<User>getBungeePlayer();
            if (!user.isLoaded() || user.isInvalidated()) return;

            this.invalidatePandoraBlock(player, pandoraData);

            var data = pandoraData.getMonster().getMonsterData().copy();
            data.setInvalidateWhenForgotTarget(true);
            data.setApplied(false);
            data.applyLootStrategy(LootStrategies.PANDORA_ADDITIONAL);
            data.removeLootStrategy(LootStrategies.EQUIP);

            for (int i = 0; i < user.findAttribute(ImperiumAttribute.PANDORA_HAS_EQUIP).findValue(); i++) {
                data.applyLootStrategy(LootStrategies.EQUIP);
            }

            var instance = Fantasy.monsterProvider().createInstance(pandoraData.getMonster().getMonsterType(), data, pandoraData.getPandoraLocation());

            data.getLootWeightPool().setRepeatTime((int) Math.round(pandoraData.getAttempts() * (pandoraData.getIncrease() / 100D)));
            data.getLootWeightPool().setInitialSize(0);
            data.setName("Видение Пандоры");
            data.setModifierCollection(ModifierCollection.EPIC);
            data.setPandoraEntity(true);
            data.setHealthMultiplier(1 + Math.max(pandoraData.getCurrentUpgrade() / 5D, 0));
            data.setPhysicalDamageMultiplier(1 + Math.max(pandoraData.getCurrentUpgrade() / 5D, 0));
            data.setElementalAndChaosDamageMultiplier(1 + Math.max(pandoraData.getCurrentUpgrade() / 5D, 0));

            instance.naturallySpawn(false);
            instance.getTargetTracker().goalTarget(player);

            Fantasy.bukkitProvider().sound(player, Sound.ENTITY_BLAZE_SHOOT);
        }, 20);
    }

    public void invalidatePandoraBlock(@NotNull final Player player, @NotNull final PandoraData pandoraData) {
        player.sendBlockChange(pandoraData.getPandoraLocation(), Material.AIR, (byte) 0);
        player.<User>getBungeePlayer().sendPacket(new PandoraChestRemovePacket(pandoraData.getUuid()));
    }

}
