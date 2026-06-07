package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.protocol.ImperiumPacket;
import me.func.mod.conversation.ModTransfer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImperiumCraftItemPacket extends ImperiumPacket<Player> {

    private final ItemStack itemStack;

    public ImperiumCraftItemPacket(@NotNull final Player client, @Nullable final ItemStack craftStack) {
        super(client);
        this.itemStack = craftStack;
    }

    @Override
    public void notice(int... flags) {
        for (int flag : flags) {
            if (flag == 0) {
                this.noticeStack();
            } else if (flag == 1) {
                this.noticeInvalidate();
            }
        }
    }

    private void noticeStack() {
        if (itemStack == null || itemStack.getType() == Material.AIR) return;
        new ModTransfer().varInt(1).item(itemStack).send("imp:craft-view", getClient());
    }

    private void noticeInvalidate() {
        new ModTransfer().varInt(0).send("imp:craft-view", getClient());
    }

}
