package cupcake.nikky.imperium.tower.item.exact.harvest;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import org.bukkit.Material;

public class RedstoneHarvest extends HarvestItem {

    public RedstoneHarvest() {
        super(Material.REDSTONE, 0, "Красная пыль", 24, 25);
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.RED_STONE_HARVEST;
    }

    @Override
    public ImperiumItem create() {
        return new RedstoneHarvest();
    }
}
