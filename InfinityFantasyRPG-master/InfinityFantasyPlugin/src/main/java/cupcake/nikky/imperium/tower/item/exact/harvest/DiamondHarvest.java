package cupcake.nikky.imperium.tower.item.exact.harvest;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import org.bukkit.Material;

public class DiamondHarvest extends HarvestItem {

    public DiamondHarvest() {
        super(Material.DIAMOND, 0, "Алмаз", 16, 10);
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.DIAMOND_HARVEST;
    }

    @Override
    public ImperiumItem create() {
        return new DiamondHarvest();
    }
}
