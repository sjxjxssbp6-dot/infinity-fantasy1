package cupcake.nikky.imperium.tower.block;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

@Getter
@RequiredArgsConstructor
public class RegenerationBlockData {

    private final Block block;
    private final Material blockMaterial;
    private final byte blockData;
    private final int regenerationDuration;
    private int currentTicks;

    public Location getBlockLocation() {
        return block.getLocation();
    }

    public void increaseTick(final int ticks) {
        currentTicks += ticks;
    }

    public boolean isDone() {
        return currentTicks >= regenerationDuration;
    }

}
