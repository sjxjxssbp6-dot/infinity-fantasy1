package cupcake.nikky.imperium.tower.item.exact.mask;

import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import lombok.Getter;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

@Getter
public class MaskItem extends DefaultImperiumItem {

    private final Masks maskType;

    public MaskItem(@NotNull final Masks maskType) {
        super(ItemRare.NONE, ItemType.SHARD, Material.STICK, (short) maskType.getData(), maskType.getTitle(), maskType.generateDescription());

        this.maskType = maskType;

        getGeneralSettings().npcTradePrice(500);

        getItemWatcherRegistry().registerWatcher(ItemWatcher.DROPPABLE);
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf("MASK_" + maskType.name());
    }

    @Override
    public ImperiumItem create() {
        return new MaskItem(maskType);
    }

}
