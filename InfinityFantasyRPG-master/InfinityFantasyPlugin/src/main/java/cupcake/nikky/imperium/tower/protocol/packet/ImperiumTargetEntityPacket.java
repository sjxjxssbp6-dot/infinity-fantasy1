package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.protocol.ImperiumPacket;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import me.func.mod.conversation.ModTransfer;
import org.jetbrains.annotations.NotNull;

public class ImperiumTargetEntityPacket extends ImperiumPacket<User> {

    private final Monster target;

    public ImperiumTargetEntityPacket(@NotNull final User client, @NotNull final Monster monster) {
        super(client);
        this.target = monster;
    }

    @Override
    public void notice(int... flags) {
        for (int flag : flags) {
            if (flag == 0) {
                this.sendRegisterPacket();
            } else if (flag == 1) {
                this.sendUpdatePacket();
            }
        }
    }

    private void sendUpdateBuffs(@NotNull final Monster target, final int... flags) {
        new ImperiumBuffPacket(getClient(), null, 1).notice(3);

        if (flags.length == 0) return;
        Fantasy.buffService().findById(target.getMonsterUniqueId())
                .ifPresent(buffedEntity -> buffedEntity.getAll()
                        .forEach(buff -> new ImperiumBuffPacket(getClient(), buff, 1).notice(flags)));
    }

    private void sendRegisterPacket() {
        if (getClient().getTargetEntity().isEmpty() || !target.isExist()) {
            this.sendUpdatePacket();
            return;
        } else this.sendUpdateBuffs(target, 0);

        new ModTransfer()
                .putString(target.getModifierEngine().getCurrentModifiers().getNameColor() + target.getName())
                .varInt((int) target.getHealth())
                .varInt((int) target.findAttribute(ImperiumAttribute.HEALTH).findValue())
                .varInt(target.getLevel())
                .send("imp:target-ent-reg", getClient().getSpigot());
    }

    private void sendUpdatePacket() {
        val transfer = new ModTransfer().varInt(getClient().getTargetEntity().isEmpty() ? 0 : 1);

        if (!getClient().getTargetEntity().isEmpty() && target.isExist()) {
            transfer.varInt((int) target.getHealth())
                    .send("imp:target-ent-upd", getClient().getSpigot());

            this.sendUpdateBuffs(target, 0);
        } else {
            transfer.send("imp:target-ent-upd", getClient().getSpigot());
            this.sendUpdateBuffs(target);
        }
    }

}
