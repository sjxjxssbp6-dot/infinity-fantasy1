package cupcake.nikky.imperium.tower.item.exact.harvest;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import org.bukkit.Material;

public class EmeraldHarvest extends HarvestItem {

    public EmeraldHarvest() {
        super(Material.EMERALD, 0, "Изумруз", 64, 20);
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.EMERALD_HARVEST;
    }

    @Override
    public ImperiumItem create() {
        return new EmeraldHarvest();
    }
}
