package cupcake.nikky.imperium.tower.item.exact.card;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.utility.ImpInventoryUtility;
import cupcake.nikky.imperium.tower.utility.PotentialUtility;
import lombok.val;
import me.anfanik.steda.api.utility.InventoryUtility;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DivinationCardItem extends DefaultImperiumItem {

    private final DivinationCard divinationCard;
    private final List<String> result = new ArrayList<>();

    public DivinationCardItem(@NotNull final DivinationCard divinationCard) {
        super(ItemRare.NONE, ItemType.DIVINATION_CARD, Material.STICK, (short) 1043, divinationCard.title(),
                Arrays.asList(
                        "Соберите полную колоду, чтобы получить предмет!",
                        "Стопка колоды: &6" + divinationCard.deckStack()));

        this.divinationCard = divinationCard;

        if (divinationCard.entityLevelDrop() != -1) {
            getLootSettings().dropEntityLevel(divinationCard.entityLevelDrop())
                    .weightApplier(data -> totalSize -> {
                        val defaultWeight = getRare() == ItemRare.COMMON ? 15 : getRare() == ItemRare.RARE ? 10 : getRare() == ItemRare.EPIC ? 6 : 1;
                        return totalSize * defaultWeight;
                    })
                    .maxAmountHandler(data -> 1);
        }
        getGeneralSettings().npcTradePrice(20);

        getItemWatcherRegistry().registerWatcher(ItemWatcher.DROPPABLE);

        getInteractSettings().onRightClick(player -> {
            if (player.hasCooldown(getMaterial())) return;

            if (InventoryUtility.removeFromMainHand(player.getInventory(), divinationCard.deckStack())) {
                if (divinationCard.rewards().isEmpty()) {
                    divinationCard.rewards().putAll(divinationCard.taskFinder().get());
                }

                if (divinationCard.rewards().size() == 1) {
                    divinationCard.rewards().forEach((itemObject, amount) -> {
                        var item = itemObject.getItem();
                        if (item.getRare() == ItemRare.UNIQUE && (item.getImperiumType() == ItemType.WEAPON || item.getImperiumType() == ItemType.ARMOR)) {
                            item = item.create();
                            ModifierProvider.getInstance().createRandomizedBaseModifiers(item);
                            ModifierProvider.getInstance().createRandomizedDirectionModifiers(item, true);
                            item.getGearSettings().potential(PotentialUtility.processPotential(item.getGeneralSettings().itemLevel()));
                        }

                        ImpInventoryUtility.addItem(player, item.createNew(amount));
                    });
                } else {
                    val itemReward = new ArrayList<>(divinationCard.rewards().keySet()).get(MathUtility.randomInRange(0, divinationCard.rewards().size() - 1));
                    var item = itemReward.getItem();
                    if (item.getRare() == ItemRare.UNIQUE && (item.getImperiumType() == ItemType.WEAPON || item.getImperiumType() == ItemType.ARMOR)) {
                        item = item.create();
                        ModifierProvider.getInstance().createRandomizedBaseModifiers(item);
                        ModifierProvider.getInstance().createRandomizedDirectionModifiers(item, true);
                    }

                    ImpInventoryUtility.addItem(player, item.createNew(divinationCard.rewards().get(itemReward)));
                }

                Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                player.setCooldown(getMaterial(), 5);
            }
        });
    }

    @Override
    public List<String> getAdditionalDescription() {
        if (result.isEmpty()) {
            result.add("Соберите полную колоду, чтобы получить предмет!");
            result.add("&fСтопка колоды: &6" + divinationCard.deckStack());
            result.add(" ");
            result.add("&fНаграды: ");
            if (divinationCard.rewardDescription() == null) {
                val rewards = new ArrayList<>(divinationCard.rewards().keySet());
                if (!rewards.isEmpty()) {
                    val item = rewards.get(0).getItem();
                    result.add("&f- " + divinationCard.rewards().get(item.getObject()) + "x " + item.getRare().getColor() + item.getName());
                } else {
                    result.add("&cОшибка во время получения награды");
                }
            } else {
                result.addAll(divinationCard.rewardDescription());
            }
        }

        return result;
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf("DIVINATION_CARD_" + divinationCard.id());
    }

    @Override
    public ImperiumItem create() {
        return new DivinationCardItem(divinationCard);
    }

}
