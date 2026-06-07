package cupcake.nikky.imperium.tower.incubator.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.incubator.Incubators;
import cupcake.nikky.imperium.tower.incubator.reward.value.IntValue;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.utility.ItemBuilder;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class IncubatorItemRandomReward implements IncubatorReward {

    private final List<ImperiumItem> items;
    private final IntValue amount;
    private final String rewardName;

    protected ItemStack icon;

    @Override
    public String rewardName() {
        return rewardName;
    }

    @Override
    public ItemStack icon(@NotNull final Incubators type) {
        if (icon == null) {
            final List<String> description = items.stream().map(item -> "&7- " + item.getRare().getColor() + item.getImperiumType().getTypeColor() + item.getName()).collect(Collectors.toList());
            description.addFirst("&6Возможные награды: ");
            description.addLast(" ");
            description.addLast("&fНеобходимый уровень жертв: &6" + type.getRequirement().levelRequirement() + "+");
            description.addLast("&fИнкубируемое количество предметов: &6" + getAmount().getDescription());

            icon = ItemBuilder.fromMaterial(Material.STICK).nbtBuilder().setTag("rid", "1050").apply()
                    .setName("&fКристалл-инкубатор")
                    .setLore(description)
                    .build();
        }
        return icon;
    }

    @Override
    public void reward(Player player) {
        var itemStack = NbtUtility.applyTagCustomize(this.items.get(MathUtility.randomInRange(0, this.items.size() - 1)).createNew(amount.get(), player),
                tag -> tag.setBoolean("incubator", true));
        Fantasy.runLater(() -> Fantasy.bukkitProvider().spawnItem(player.getLocation(), player.getUniqueId(), itemStack));
    }

}
