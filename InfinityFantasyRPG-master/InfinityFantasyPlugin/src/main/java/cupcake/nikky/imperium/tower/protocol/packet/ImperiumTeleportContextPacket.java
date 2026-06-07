package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.beacon.context.BeaconContext;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.protocol.ImperiumPacket;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.QuestUtility;
import lombok.val;
import me.func.mod.conversation.ModTransfer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ImperiumTeleportContextPacket extends ImperiumPacket<Player> {

    private final BeaconContext beaconContext;

    public ImperiumTeleportContextPacket(@NotNull BeaconContext context) {
        super(context.getPlayer());
        this.beaconContext = context;
    }

    public ImperiumTeleportContextPacket(@NotNull final Player player) {
        super(player);
        this.beaconContext = null;
    }

    @Override
    public void notice(int... flags) {
        for (int flag : flags) {
            if (flag == 0) {
                this.registerContext();
            } else if (flag == 1) {
                this.closeContext();
            }
        }
    }

    public void closeContext() {
        new ModTransfer().send("imp:tp-close", getClient());
    }

    public void registerContext() {
        val transfer = new ModTransfer();
        val selection = beaconContext.getSelection();

        val user = getClient().<User>getBungeePlayer();
        val questedPlayer = user.findQuestSettings();

        transfer.varInt(selection.size());

        selection.forEach((id, beacon) -> transfer.varInt(id)
                .putString(String.join("\n", QuestUtility.convertDescription((id + 1) + ". " + beacon.getBeaconSettings().title(), 40)))
                .putString(FantasyImage.valueOf("TELEPORT_" + beacon.getId()).toTexture())
                .putString(beacon.getBeaconSettings().requirement())
                .varInt(beacon.getBeaconSettings().handlerUser().test(user) && beacon.getBeaconSettings().handlerQuest().test(questedPlayer) ? 0 : 1)
                .varInt(beaconContext.findCost(beacon)));

        transfer.send("imp:tp-open", getClient());
    }

}
