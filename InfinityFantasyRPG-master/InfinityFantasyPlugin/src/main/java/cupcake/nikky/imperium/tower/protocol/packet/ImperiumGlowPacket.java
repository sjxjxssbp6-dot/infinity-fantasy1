package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.protocol.ImperiumPacket;
import me.func.mod.conversation.ModTransfer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ImperiumGlowPacket extends ImperiumPacket<Player> {

    private final int entityId;

    public ImperiumGlowPacket(@NotNull Player client, final int targetEntityId) {
        super(client);
        this.entityId = targetEntityId;
    }

    @Override
    public void notice(int... flags) {
        for (int flag : flags) {
            if (flag == 0) {
                this.registerGlowing();
            } else if (flag == 1) {
                this.unregisterGlowing();
            }
        }
    }

    public void registerGlowing() {
        new ModTransfer().varInt(entityId).varInt(0).send("imp:npc-glow", getClient());
    }

    public void unregisterGlowing() {
        new ModTransfer().varInt(entityId).varInt(1).send("imp:npc-glow", getClient());
    }

}
