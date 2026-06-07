package cupcake.nikky.imperium.tower.item.exact.harvest;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import org.bukkit.Material;

public class CoalHarvest extends HarvestItem {

    public CoalHarvest() {
        super(Material.COAL, 0, "Уголь", 2, 1);
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.COAL_HARVEST;
    }

    @Override
    public ImperiumItem create() {
        return new CoalHarvest();
    }
}
