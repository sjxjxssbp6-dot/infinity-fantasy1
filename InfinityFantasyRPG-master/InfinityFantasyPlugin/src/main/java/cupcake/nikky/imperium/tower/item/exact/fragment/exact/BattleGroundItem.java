package cupcake.nikky.imperium.tower.item.exact.fragment.exact;

import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.exact.fragment.FragmentItem;
import cupcake.nikky.imperium.tower.zone.ZoneList;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public class BattleGroundItem extends FragmentItem {

    public BattleGroundItem(final int dropLevel, final int tier) {
        super("Путь: Поле Битвы", ZoneList.BATTLE_GROUND_ZONE.spawnLocation("spawn" + tier), (short) 1000, tier);

        getLootSettings().dropEntityLevel(dropLevel);
        getLootSettings().weightApplier(data -> totalSize -> totalSize * 4);

        this.getGeneralSettings().npcTradePrice(45 * tier);
    }

    @Override
    public String getName() {
        return "Путь: Поле Битвы " + findTierText(getTier());
    }

    private String findTierText(final int tier) {
        return switch (tier) {
            case 1 -> "I";
            case 3 -> "II";
            case 6 -> "III";
            case 8 -> "IV";
            default -> "I";
        };
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf("FRAGMENT_BATTLE_" + getTier());
    }

    @Override
    public ImperiumItem create() {
        return new BattleGroundItem(getGeneralSettings().itemLevel(), getTier());
    }

}
