package cupcake.nikky.imperium.tower.client.protocol.packet.exact.incubator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class IncubatorData {

    private UUID uuid;
    private int currentProgress;
    private int maxProgress;
    private int requireLevel;
    private Object itemStack;

    public ItemStack toBukkit() {
        return (ItemStack) itemStack;
    }

    public dev.xdark.clientapi.item.ItemStack toClient() {
        return (dev.xdark.clientapi.item.ItemStack) itemStack;
    }

}
