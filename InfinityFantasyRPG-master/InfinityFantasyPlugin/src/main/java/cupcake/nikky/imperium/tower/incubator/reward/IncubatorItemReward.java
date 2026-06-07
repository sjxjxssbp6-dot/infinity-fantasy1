package cupcake.nikky.imperium.tower.incubator.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.incubator.Incubators;
import cupcake.nikky.imperium.tower.incubator.reward.value.IntValue;
import cupcake.nikky.imperium.tower.incubator.reward.value.RandomIntValue;
import cupcake.nikky.imperium.tower.incubator.reward.value.StableIntValue;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
public class IncubatorItemReward implements IncubatorReward {

    private final ImperiumItem item;
    private final IntValue amount;

    protected ItemStack icon;

    @Override
    public String rewardName() {
        return item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName() + " &fX &6" + getAmount().getDescription();
    }

    @Override
    public ItemStack icon(@NotNull final Incubators type) {
        if (icon == null) {
            icon = ItemBuilder.fromItem(item.createNew())
                    .appendLore(
                            "",
                            "&fНеобходимый уровень жертв: &6" + type.getRequirement().levelRequirement() + "+",
                            "&fИнкубируемое количество предметов: &6" + getAmount().getDescription()
                    )
                    .build();
        }
        return icon;
    }

    @Override
    public void reward(Player player) {
        var itemStack = NbtUtility.applyTagCustomize(this.item.createNew(amount.get(), player), tag -> tag.setBoolean("incubator", true));
        Fantasy.runLater(() -> Fantasy.bukkitProvider().spawnItem(player.getLocation(), player.getUniqueId(), itemStack));
    }

}
