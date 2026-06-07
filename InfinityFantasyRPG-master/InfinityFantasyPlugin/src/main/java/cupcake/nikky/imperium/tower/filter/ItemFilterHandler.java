package cupcake.nikky.imperium.tower.filter;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.card.DivinationCardItem;
import cupcake.nikky.imperium.tower.item.exact.fragment.FragmentItem;
import cupcake.nikky.imperium.tower.item.exact.mask.MaskItem;
import cupcake.nikky.imperium.tower.item.exact.modification.ModificationItem;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeNpcProvider;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ItemFilterHandler(@NotNull Player player, User user) {

    public boolean isValidToRemove(@NotNull final ImperiumItem imperiumItem) {
        val filter = user.findLootFilterSettings();

        val isEquip = imperiumItem.getImperiumType() == ItemType.ARMOR || imperiumItem.getImperiumType() == ItemType.WEAPON;
        val isModification = imperiumItem.checkInstance(ModificationItem.class) ||
                imperiumItem.getObject() == ItemObject.RESET_ORB ||
                imperiumItem.getObject() == ItemObject.STACKED_DECK ||
                imperiumItem.getObject().getItem().checkInstance(FragmentItem.class);
        val isAspect = imperiumItem.getImperiumType() == ItemType.ASPECT;
        val isDivinationCard = imperiumItem.checkInstance(DivinationCardItem.class);
        val isIncubator = imperiumItem.getImperiumType() == ItemType.INCUBATOR;
        val isMask = imperiumItem.checkInstance(MaskItem.class);

        val isAttachableToFilter = isEquip || isAspect || isModification || isDivinationCard || isIncubator || isMask;

        if (!isAttachableToFilter) return false;

        if (isEquip) {
            var status = filter.getGearToIgnore().getOrDefault(imperiumItem.getObject(), 0);

            if (status == 1 && imperiumItem.getModifierSettings().findSpecificByType(ModifierType.DIRECTION).isEmpty()) {
                return true;
            } else if (status == 2) return true;
        }

        return isAspect && filter.getAspectsToIgnore().contains(imperiumItem.getObject()) ||
                isIncubator && filter.getIncubators().contains(imperiumItem.getObject()) ||
                isMask && filter.getMasks().contains(imperiumItem.getObject()) ||
                isDivinationCard && filter.getDivinationCardsToIgnore().contains(imperiumItem.getObject()) ||
                isModification && filter.getItemsToIgnore().contains(imperiumItem.getObject()) ||
                isEquip && filter.getMinimumRare().highestThan(imperiumItem.getRare()) ||
                isEquip && filter.getMinimumItemLevel() > imperiumItem.getGeneralSettings().itemLevel();
    }

    public void process(@Nullable final Item item, @NotNull final ImperiumItem imperiumItem, final int amount, final boolean isValid) {
        if (!isValid) return;

        val donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();
        if (donateData == null || imperiumItem.getGeneralSettings().npcTradePrice() == -2) return;

        if (item != null)
            item.remove();

        if (donateData.isDonateLootFilter()) {
            user.changeCurrency(CurrencyType.GRAN, TradeNpcProvider.getInstance().findPriceForItem(imperiumItem, amount));
            Fantasy.bukkitProvider().sound(player, AdvancedSounds.COINS_RECEIVE);
        }
    }

}
