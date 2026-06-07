package cupcake.nikky.imperium.tower.handler;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.pandora.PandoraChestClickPacket;
import cupcake.nikky.imperium.tower.cooldown.CooldownService;
import cupcake.nikky.imperium.tower.data.pandora.PandoraStorage;
import dev.xdark.clientapi.block.Blocks;
import dev.xdark.clientapi.event.block.BlockLeftClick;
import dev.xdark.clientapi.event.block.BlockRightClick;
import dev.xdark.clientapi.util.ActionResult;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.Enginex;

public class BlockClickHandler {

    public BlockClickHandler(@NotNull final FantasyMod fantasyMod) {
        Enginex.registerEvent(BlockRightClick.class, blockRightClick -> {
            val blockPosition = blockRightClick.getPosition();
            val pandora = PandoraStorage.getInstance().findPandora(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
            if (pandora != null && CooldownService.getInstance().getAndExtendCooldown("block_click", .25)) {
                fantasyMod.sendPacket(new PandoraChestClickPacket(pandora.getUuid()));
                blockRightClick.setResult(ActionResult.FAIL);
            }
        });

        Enginex.registerEvent(BlockLeftClick.class, blockLeftClick -> {
            val blockPosition = blockLeftClick.getPosition();

            val blockState = Enginex.getPlayer().getWorld().getBlockState(blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
            if (blockState == null) return;

            val block = blockState.getBlock();
            if (block == null) return;

            val blockId = block.getId();

            blockLeftClick.setCancelled(
                    blockId != Blocks.COAL_ORE.getId() &&
                    blockId != Blocks.EMERALD_ORE.getId() &&
                    blockId != Blocks.IRON_ORE.getId() &&
                    blockId != Blocks.GOLD_ORE.getId() &&
                    blockId != Blocks.DIAMOND_ORE.getId() &&
                    blockId != Blocks.REDSTONE_ORE.getId() &&
                    blockId != Blocks.LIT_REDSTONE_ORE.getId()
            );
        }, 1);
    }

}
