package cupcake.nikky.imperium.tower.item.exact.fragment.exact;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.exact.fragment.FragmentItem;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.zone.Zone;
import org.jetbrains.annotations.NotNull;

public class WayFragmentItem extends FragmentItem {

    private final Zone zone;

    public WayFragmentItem(@NotNull String title, @NotNull final Zone zone, int durability, int tier) {
        super(title, zone.spawnLocation("default"), (short) durability, tier);
        this.zone = zone;

        getGeneralSettings().itemLevel(tier);
        getLootSettings().dropEntityLevel(LevelStorage.getInstance().getStartShardEdgeLevel() + tier * 2 - 4)
                .weightApplier(monsterData -> totalSize -> totalSize * 12);

        this.getGeneralSettings().npcTradePrice(30 * getTier());
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf("WAY_FRAGMENT_" + getTier());
    }

    @Override
    public ImperiumItem create() {
        return new WayFragmentItem(getName(), zone, getDurability(), getTier());
    }

}
