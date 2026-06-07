package cupcake.nikky.imperium.tower.harvest.mining;

import cupcake.nikky.imperium.tower.harvest.HarvestPattern;
import cupcake.nikky.imperium.tower.item.ItemObject;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

@Getter
public class MiningPattern extends HarvestPattern {

    private final int levelRequire;
    private final Material blockMaterial;
    private final byte data;
    private final int regenerationInTicks;

    public MiningPattern(int levelRequire, Material blockMaterial, byte data, int toolLevelRequire, ItemObject harvestItem, int regenerationInTicks) {
        super(toolLevelRequire, harvestItem);

        this.levelRequire = levelRequire;
        this.blockMaterial = blockMaterial;
        this.data = data;
        this.regenerationInTicks = regenerationInTicks;
    }


    public static MiningPattern of(final int levelRequire, final int toolLevelRequire, @NotNull final ItemObject harvestItem, @NotNull final Material blockMaterial, final byte data, final int regenerationInTicks) {
        return new MiningPattern(levelRequire, blockMaterial, data, toolLevelRequire, harvestItem, regenerationInTicks);
    }

}
