package cupcake.nikky.imperium.tower.item.exact.harvest;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import lombok.Getter;
import org.bukkit.Material;

@Getter
public class FishHarvest extends HarvestItem {

    private final int level;

    public FishHarvest(Material material, int durability, int level, int experience, int currency) {
        super(material, durability, "Рыба", experience, currency);
        this.level = level;
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf("FISH_HARVEST_" + level);
    }

    @Override
    public ImperiumItem create() {
        return new FishHarvest(getMaterial(), getDurability(), level, getExperience(), getCurrency());
    }

}
