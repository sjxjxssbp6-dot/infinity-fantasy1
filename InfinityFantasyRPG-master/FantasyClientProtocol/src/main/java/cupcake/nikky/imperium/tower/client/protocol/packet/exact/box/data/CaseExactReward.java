package cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data;

import io.netty.buffer.ByteBuf;
import lombok.*;
import org.bukkit.inventory.ItemStack;
import ru.cristalix.wada.util.ModTransfer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaseExactReward {

    private String iconTitle;
    private Object icon;
    private double chance;
    private CaseRarity rarity;

    public void write(ByteBuf data) {
        if (icon instanceof ItemStack)
            new ModTransfer(data).writeShort(0).writeItem((ItemStack) icon).writeDouble(chance).writeInt(rarity.ordinal());
        else if (icon instanceof String)
            new ModTransfer(data).writeShort(1).writeString((String) icon).writeString(iconTitle).writeDouble(chance).writeInt(rarity.ordinal());
    }

    public CaseExactReward read(ByteBuf data) {
        val transfer = new ru.cristalix.enginex.util.ModTransfer(data);
        this.icon = transfer.readShort() == 0 ? transfer.readItem() : transfer.readString();
        if (icon instanceof String)
            this.iconTitle = transfer.readString();
        this.chance = transfer.readDouble();
        this.rarity = CaseRarity.values()[transfer.readInt()];

        return this;
    }

}
