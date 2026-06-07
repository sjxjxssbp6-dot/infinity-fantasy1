package cupcake.nikky.imperium.tower.item.exact.harvest;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.client.protocol.cursor.CursorPromptData;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.exact.gear.tool.ToolItem;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.val;
import me.anfanik.steda.api.utility.InventoryUtility;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.List;

@Getter
public abstract class HarvestItem extends DefaultImperiumItem {

    private final int experience;
    private final int currency;

    public HarvestItem(Material material, int durability, String name, final int experience, final int currency) {
        super(ItemRare.NONE, ItemType.MATERIAL, material, (short) durability, name,
                List.of("Материал для улучшения инструмента",
                        "Содержит опыта: &3%d".formatted(experience),
                        "&r\uE3FF &7держа в руке для получения %d&r\uE042".formatted(currency)));

        this.experience = experience;
        this.currency = currency;

        getDescriptionSettings().showItemType(true);

        this.getInteractSettings().onRightClick(player -> {
            if (player.hasCooldown(getMaterial())) return;

            var user = player.<User>getBungeePlayer();

            var amount = player.isSneaking() ? 8 : 1;
            if (InventoryUtility.removeFromMainHand(player.getInventory(), amount)) {
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);

                val currencyGain = (int) Math.round(amount * currency * (player.<User>getBungeePlayer().findDonateSettings().donateData().isSubscribed() ? 1.5D : 1));

                user.changeCurrency(CurrencyType.MINES, currencyGain);
                Fantasy.clientProvider().cursorNotify(player, new CursorPromptData("+" + currencyGain, FantasyImage.MINE.toTexture()));

                player.setCooldown(getMaterial(), 3);
            }
        });
    }

}
