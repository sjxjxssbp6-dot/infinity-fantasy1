package cupcake.nikky.imperium.tower.item.exact.gear.tool;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import lombok.val;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class FishingRodItem extends ToolItem {

    public FishingRodItem() {
        super(Material.FISHING_ROD, (short) 0, "Фирм-удочка", Arrays.asList(
                "Используется для ловли морской живности",
                "&fВозможная ценность: &cУголь"));
    }

    @Override
    public List<String> getAdditionalDescription() {
        val level = getGeneralSettings().itemLevel();
        String result = "4 уровень";

        if (level == 1) result = "1 уровень";
        else if (level == 2) result = "1-2 уровень";
        else if (level == 3) result = "2-3 уровень";
        else if (level == 4) result = "3-4 уровень";
        else if (level == 6) result = "5 уровень";

        return Arrays.asList("Используется для ловли морской живности", "&fВозможная ценность: &a" + result, " ", "&cНельзя &7изменить редкость предмета");
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.FISHING_ROD;
    }

    @Override
    public ImperiumItem create() {
        return new FishingRodItem();
    }

}
