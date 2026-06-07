package cupcake.nikky.imperium.tower.incubator.reward;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.incubator.Incubators;
import cupcake.nikky.imperium.tower.incubator.reward.value.IntValue;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import cupcake.nikky.imperium.tower.utility.PotentialUtility;
import me.anfanik.steda.api.utility.ItemBuilder;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class IncubatorRandomGearReward extends IncubatorItemRandomReward {

    private final Function<Player, Integer> level;
    private final Function<ImperiumItem, String> nameGenerator;

    public IncubatorRandomGearReward(@NotNull final List<ImperiumItem> items, final IntValue amount, @NotNull final String rewardName,
                                     @NotNull final Function<Player, Integer> level, @NotNull final Function<ImperiumItem, String> nameGenerator) {
        super(items, amount, rewardName);
        this.level = level;
        this.nameGenerator = nameGenerator;
    }

    @Override
    public ItemStack icon(@NotNull final Incubators type) {
        if (icon == null) {
            final List<String> description = new ArrayList<>(getItems().stream()
                    .sorted(Comparator.comparingInt(imperiumItem -> NumberUtility.parseInteger(imperiumItem.getUniqueId())))
                    .map(nameGenerator).map(s -> "&7- " + s).toList());
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
        var index = MathUtility.randomInRange(0, getItems().size() - 1);
        var item = getItems().get(index).create();
        var gearLevel = level.apply(player);

        ModifierProvider.getInstance().randomizeItemRarity(item, gearLevel, Arrays.asList(ItemRare.COMMON, ItemRare.RARE, ItemRare.EPIC));
        ModifierProvider.getInstance().createRandomizedBaseModifiers(item);
        ModifierProvider.getInstance().createRandomizedExtendedModifiers(item);
        ModifierProvider.getInstance().createRandomizedDirectionModifiers(item, false);
        ModifierProvider.getInstance().createRandomizedDirectionModifiers(item, false);
        item.setName(nameGenerator.apply(item));
        item.getGearSettings().potential(PotentialUtility.processPotential(gearLevel));

        var itemStack = NbtUtility.applyTagCustomize(item.createNew(getAmount().get(), player), tag -> tag.setBoolean("incubator", true));
        Fantasy.runLater(() -> Fantasy.bukkitProvider().spawnItem(player.getLocation(), player.getUniqueId(), itemStack));
    }

}
