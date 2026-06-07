package cupcake.nikky.imperium.tower.item.exact.harvest;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import org.bukkit.Material;

public class GoldHarvest extends HarvestItem {

    public GoldHarvest() {
        super(Material.GOLD_INGOT, 0, "Золото", 8, 6);
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.GOLD_HARVEST;
    }

    @Override
    public ImperiumItem create() {
        return new GoldHarvest();
    }
}
