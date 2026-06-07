package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.buff.Buff;
import cupcake.nikky.imperium.tower.protocol.ImperiumPacket;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import me.func.mod.conversation.ModTransfer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ImperiumBuffPacket extends ImperiumPacket<User> {

    private final Buff noticeBuff;
    private final int type;

    public ImperiumBuffPacket(@NotNull final User client, @Nullable final Buff noticeBuff) {
        this(client, noticeBuff, 0);
    }

    public ImperiumBuffPacket(@NotNull final User client, @Nullable final Buff noticeBuff, final int type) {
        super(client);
        this.noticeBuff = noticeBuff;

        if (type != 0 && type != 1)
            throw new IllegalArgumentException("Packet type can only be 0 or 1;");

        this.type = type;
    }

    @Override
    public void notice(int... flags) {
        for (int flag : flags) {
            if (flag == 0) {
                this.registerBuff();
            } else if (flag == 1) {
                this.updateBuff();
            } else if (flag == 2) {
                this.unregisterBuff();
            } else if (flag == 3) {
                this.clearAllBuffs();
            }
        }
    }

    private void clearAllBuffs() {
        new ModTransfer().varInt(type).send("imp:buff-clear", getClient().getSpigot());
    }

    private void registerBuff() {
        if (noticeBuff == null) return;

        val transfer = new ModTransfer();
        val description = noticeBuff.getDescription();
        val descSize = description.size();

        transfer.varInt(type)
                .varInt(noticeBuff.getBuffType().getId())
                .varInt(noticeBuff.getBuffType().getBuffCategory().ordinal())
                .varInt(noticeBuff.getDurationSeconds() - noticeBuff.getCurrentSeconds())
                .varInt(noticeBuff.getMultiplier())
                .putString(noticeBuff.getBuffType().getImage().name())
                .putString(TextUtility.colorize("&f" + noticeBuff.getTitle()))
                .varInt(descSize);

        for (String s : description) transfer.putString(s);

        transfer.send("imp:buff-register", getClient().getSpigot());
    }

    private void updateBuff() {
        if (noticeBuff == null) return;

        val transfer = new ModTransfer();
        val description = noticeBuff.getDescription();
        val descSize = description.size();

        transfer.varInt(type)
                .varInt(noticeBuff.getBuffType().getId())
                .varInt(noticeBuff.getDurationSeconds() - noticeBuff.getCurrentSeconds())
                .varInt(noticeBuff.getMultiplier())
                .varInt(descSize);

        for (String s : description) transfer.putString(s);

        transfer.send("imp:buff-update", getClient().getSpigot());
    }

    private void unregisterBuff() {
        if (noticeBuff == null) return;

        new ModTransfer().varInt(type).varInt(noticeBuff.getBuffType().getId()).send("imp:buff-unregister", getClient().getSpigot());
    }

}
