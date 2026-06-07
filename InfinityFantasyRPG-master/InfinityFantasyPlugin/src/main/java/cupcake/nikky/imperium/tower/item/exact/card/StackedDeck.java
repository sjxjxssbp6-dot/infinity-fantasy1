package cupcake.nikky.imperium.tower.item.exact.card;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.utility.ImpInventoryUtility;
import me.anfanik.steda.api.utility.InventoryUtility;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Material;

import java.util.List;

public class StackedDeck extends DefaultImperiumItem {

    public StackedDeck() {
        super(ItemRare.NONE, ItemType.DIVINATION_CARD, Material.STICK, (short) 1052, "Колода гадальных карт",
                List.of("При использовании выкидывает",
                        "случайную гадальную карту"));

        getLootSettings().dropEntityLevel(LevelStorage.getInstance().getStartShardEdgeLevel()).weightApplier(data -> totalSize -> totalSize * 2).maxAmountHandler(data -> 2);
        getGeneralSettings().npcTradePrice(20);

        getItemWatcherRegistry().registerWatcher(ItemWatcher.DROPPABLE);

        getInteractSettings().onRightClick(player -> {
            if (player.hasCooldown(getMaterial())) return;

            if (InventoryUtility.removeFromMainHand(player.getInventory(), 1)) {
                var divinationCards = ItemObject.getDivinationCards();
                var item = divinationCards.get(MathUtility.randomInRange(0, divinationCards.size() - 1)).getItem().createNew(1);

                ImpInventoryUtility.addItem(player, item);

                Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                player.setCooldown(getMaterial(), 5);
            }
        });
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.STACKED_DECK;
    }

    @Override
    public ImperiumItem create() {
        return new StackedDeck();
    }

}
