package cupcake.nikky.imperium.tower.item.exact.gear.tool;

import cupcake.nikky.imperium.tower.entity.monster.exact.sharding.BreakThroughMonster;
import cupcake.nikky.imperium.tower.item.*;
import lombok.val;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class PickaxeItem extends ToolItem {

    public PickaxeItem() {
        super(Material.WOOD_PICKAXE, (short) 0, "Фирм-кирка", Arrays.asList("Используется для добычи ресурсов", "&fРуды: &cУголь"));
    }

    @Override
    public Material getMaterial() {
        return switch (getGeneralSettings().itemLevel()) {
            case 1 -> Material.WOOD_PICKAXE;
            case 2 -> Material.STONE_PICKAXE;
            case 3 -> Material.IRON_PICKAXE;
            case 4, 5, 6 -> Material.DIAMOND_PICKAXE;
            default -> this.getMaterial();
        };
    }

    @Override
    public void updateAttributes() {
        super.updateAttributes();

        getGeneralSettings().customNBTKey("weapons");
        getGeneralSettings().customNBTValue("mythrile_%s_pickaxe".formatted(getMaterial().name.replace("_PICKAXE", "").toLowerCase()));
    }

    @Override
    public List<String> getAdditionalDescription() {
        val level = getGeneralSettings().itemLevel();

        val ores = new String[] {"Уголь", "Железо", "Золото", "Алмаз", "Изумруд", "Красная пыль"};
        val lineOre = new StringBuilder();

        for (int i = 0; i < Math.min(level, 6); i++) {
            if (!lineOre.isEmpty()) lineOre.append(", ");
            lineOre.append("&a").append(ores[i]);
        }

        return Arrays.asList("Используется для добычи ресурсов", "&fРуды: " + lineOre, " ", "&cНельзя &7изменить редкость предмета");
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.PICKAXE;
    }

    @Override
    public ImperiumItem create() {
        return new PickaxeItem();
    }
}
