package cupcake.nikky.imperium.tower.harvest;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public abstract class HarvestPattern {

    private final int toolLevelRequire;
    private final ItemObject harvestItem;

    public void generate(@NotNull final Player player, @NotNull final Location lootLocation) {
        var user = player.<User>getBungeePlayer();

        val increaseCurrency = user.findAttribute(ImperiumAttribute.INCREASE_TOOL_CURRENCY).findValue();
        val chanceToDoubleCurrency = user.findAttribute(ImperiumAttribute.INCREASE_TOOL_CHANCE_TO_DOUBLE_CURRENCY).findValue();

        var defaultAmount = MathUtility.randomInRange(1, 100) <= (20 * (1 + increaseCurrency / 100)) ? 3D : MathUtility.randomInRange(1D, 2D);
        defaultAmount = defaultAmount * (1 + increaseCurrency / 100D);

        if (chanceToDoubleCurrency > 0 && MathUtility.randomInRange(1, 100) <= chanceToDoubleCurrency)
            defaultAmount *= 2D;

        Fantasy.bukkitProvider().spawnItem(lootLocation, harvestItem.getItem().createNew((int) Math.round(defaultAmount)));
    }

}
