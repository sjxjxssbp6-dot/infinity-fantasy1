package cupcake.nikky.imperium.tower.item.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import me.anfanik.steda.api.utility.InventoryUtility;
import org.bukkit.Material;

import java.util.Collections;

public class SpecialCurrencyItem extends DefaultImperiumItem {

    private final int value;

    public SpecialCurrencyItem(final int value) {
        super(ItemRare.NONE, ItemType.CURRENCY, Material.EMERALD, (short) 1001, "Специальная валюта: " + value,
                Collections.singletonList("При использовании дает: &e" + value + " &7" + CurrencyType.FIRM.getTextTransformer().apply(value)));

        this.value = value;

        getInteractSettings().onRightClick(player -> {
            if (player.hasCooldown(getMaterial())) return;

            Fantasy.userService().findById(player.getUniqueId()).ifPresent(user -> {
                if (InventoryUtility.removeFromMainHand(player.getInventory(), 1)) {
                    if (user.getCurrencyHandler().depositCurrency(CurrencyType.FIRM_PRIVATE, value)) {
                        player.setCooldown(getMaterial(), 2);
                        Fantasy.bukkitProvider().sound(player, AdvancedSounds.COINS_RECEIVE);
                        Fantasy.clientProvider().notification(player, SystemNotificationType.FINE, "Получено: " + value + " " + CurrencyType.FIRM.getTextTransformer().apply(value));
                    } else {
                        player.getInventory().addItem(this.createNew());
                    }
                }
            });
        });
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf("SPECIAL_CURRENCY_" + value);
    }

    @Override
    public ImperiumItem create() {
        return new SpecialCurrencyItem(value);
    }

}
