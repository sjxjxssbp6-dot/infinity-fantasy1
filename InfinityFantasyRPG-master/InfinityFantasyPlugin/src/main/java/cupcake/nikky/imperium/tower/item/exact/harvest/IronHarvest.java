package cupcake.nikky.imperium.tower.item.exact.harvest;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import org.bukkit.Material;

public class IronHarvest extends HarvestItem {

    public IronHarvest() {
        super(Material.IRON_INGOT, 0, "Железо", 4, 3);
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.IRON_HARVEST;
    }

    @Override
    public ImperiumItem create() {
        return new IronHarvest();
    }
}
