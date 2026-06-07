package cupcake.nikky.imperium.tower.trade.player;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.trade.player.validate.ValidateSlotResult;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.ImpInventoryUtility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@RequiredArgsConstructor
public class TradePlayerData {

    private final User user;
    private final List<Integer> slots = new CopyOnWriteArrayList<>();
    private int gran;
    private int firm;
    @Setter private boolean confirm;
    @Getter @Setter private boolean closed;

    public boolean containsSlot(final int slot) {
        return slots.contains(slot);
    }

    public boolean removeSlot(@NotNull final Integer slot) {
        return this.slots.remove(slot);
    }

    public boolean tryToAddSlot(final int slot) {
        val player = user.getSpigot();
        if (player == null) return false;

        if (this.validateSlot(player, slot)) {
            slots.add(slot);
            return true;
        }

        return false;
    }

    private boolean validateSlot(@NotNull final Player player, final int slot) {
        val item = player.getInventory().getItem(slot);
        if (item == null || item.getType() == Material.AIR) return false;

        val imperiumItem = Fantasy.itemProvider().find(item);
        if (imperiumItem == null) return false;

        return imperiumItem.getDropSettings().droppable() && imperiumItem.getDropSettings().canDrop().test(player);
    }

    public Map<ImperiumItem, Integer> convertCurrentSlots() {
        val player = getUser().getSpigot();
        if (player == null) return new HashMap<>();

        final Map<ImperiumItem, Integer> result = new HashMap<>();
        this.getSlots().forEach(slot -> {
            val item = player.getInventory().getItem(slot);
            result.put(Fantasy.itemProvider().find(item), item.getAmount());
        });

        return result;
    }

    public boolean tryToUpdateCurrency(@NotNull final CurrencyType currencyType, final int value) {
        if (user.getCurrency(currencyType) >= value) {
            if (currencyType == CurrencyType.GRAN)
                this.gran = value;
            else if (currencyType == CurrencyType.FIRM)
                this.firm = value;
        }

        return false;
    }

    public ValidateSlotResult validateCurrentSlots(@NotNull final Map<ImperiumItem, Integer> participantItems) {
        val player = getUser().getSpigot();
        if (player == null) return ValidateSlotResult.PLAYER_OFFLINE;

        if (!this.slots.stream().allMatch(slot -> this.validateSlot(player, slot))) return ValidateSlotResult.INVALID_ITEM;

        val content = player.getInventory().getContents();
        val playerContent = Arrays.copyOf(content, content.length);

        getSlots().forEach(slot -> playerContent[slot] = null);

        return ImpInventoryUtility.canMoveToPlayerEmulate(player, playerContent, participantItems) ? ValidateSlotResult.ACCEPT : ValidateSlotResult.FULL_INVENTORY;
    }

    public boolean validateCurrentCurrency() {
        return getUser().getCurrency(CurrencyType.GRAN) >= getGran() && getUser().getCurrency(CurrencyType.FIRM) >= getFirm();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TradePlayerData that = (TradePlayerData) o;
        return Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }
}
