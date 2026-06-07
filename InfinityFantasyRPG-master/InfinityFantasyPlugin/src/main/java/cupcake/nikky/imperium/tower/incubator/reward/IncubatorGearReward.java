package cupcake.nikky.imperium.tower.incubator.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.incubator.Incubators;
import cupcake.nikky.imperium.tower.incubator.reward.value.IntValue;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import cupcake.nikky.imperium.tower.utility.PotentialUtility;
import me.anfanik.steda.api.utility.ItemBuilder;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class IncubatorGearReward extends IncubatorItemReward {

    private final int level;
    private final String name;

    public IncubatorGearReward(@NotNull final ImperiumItem item, final IntValue amount, final int level, @NotNull final String name) {
        super(item, amount);
        this.level = level;
        this.name = name;

        item.setName(name);
        item.getGeneralSettings().itemLevel(level);
    }

    @Override
    public ItemStack icon(@NotNull final Incubators type) {
        if (icon == null) {
            icon = ItemBuilder.fromItem(getItem().createNew())
                    .setLore(
                            "&fСнаряжение с двумя модификаторами направления", "",
                            "&fНеобходимый уровень жертв: &6" + type.getRequirement().levelRequirement() + "+",
                            "&fИнкубируемое количество предметов: &6" + getAmount().getDescription()
                    )
                    .build();
        }
        return icon;
    }

    @Override
    public void reward(Player player) {
        var item = getItem().create();

        ModifierProvider.getInstance().randomizeItemRarity(item, level, Arrays.asList(ItemRare.COMMON, ItemRare.RARE, ItemRare.EPIC));
        ModifierProvider.getInstance().createRandomizedBaseModifiers(item);
        ModifierProvider.getInstance().createRandomizedExtendedModifiers(item);
        ModifierProvider.getInstance().createRandomizedDirectionModifiers(item, false);
        ModifierProvider.getInstance().createRandomizedDirectionModifiers(item, false);
        item.setName(name);
        item.getGearSettings().potential(PotentialUtility.processPotential(level + 16));

        var itemStack = NbtUtility.applyTagCustomize(item.createNew(getAmount().get(), player), tag -> tag.setBoolean("incubator", true));
        Fantasy.runLater(() -> Fantasy.bukkitProvider().spawnItem(player.getLocation(), player.getUniqueId(), itemStack));
    }

}
