package cupcake.nikky.imperium.tower.item.exact.incubator;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.incubator.Incubators;
import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.user.User;
import me.anfanik.steda.api.utility.InventoryUtility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class IncubatorItem extends DefaultImperiumItem {

    private final Incubators type;

    public IncubatorItem(@NotNull final Incubators incubators) {
        super(ItemRare.NONE, ItemType.INCUBATOR, Material.STICK, (short) 1050, "Кристалл-инкубатор",
                List.of("После заполнения инкубатора жертвами",
                        "Вы сможете получить награду", "",
                        "&fКоличество жертв: &6" + incubators.getRequirement().deathRequirement(),
                        "&fУровень жертв: &6" + incubators.getRequirement().levelRequirement() + "+",
                        "", "&fИнкубируемый предмет: ", incubators.getReward().rewardName()));
        this.type = incubators;

        this.getGeneralSettings().npcTradePrice(250).itemLevel(incubators.getRequirement().levelRequirement());

        getInteractSettings().onRightClick(player -> {
            if (player.hasCooldown(Material.STICK)) return;
            player.setCooldown(Material.STICK, 5);

            var user = player.<User>getBungeePlayer();
            if (user.getLevel() < getGeneralSettings().requiredLevel()) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Ваш уровень не подходит", "для использования");
                return;
            }

            if (user.findIncubatorSettings().canApplyIncubator(player.<User>getBungeePlayer().findDonateSettings().donateData())) {
                if (InventoryUtility.removeFromMainHand(player.getInventory(), 1)) {
                    user.findIncubatorSettings().applyIncubator(incubators);
                    Fantasy.clientProvider().updateIncubators(player);
                    Fantasy.clientProvider().notification(player, SystemNotificationType.FINE, "Кристалл-инкубатор был применен");
                }
            } else {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Вы достигли лимита одновременного", "использования инкубаторов");
            }
        });
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.valueOf(type.name() + "_ITEM");
    }

    @Override
    public ImperiumItem create() {
        return new IncubatorItem(type);
    }

}
