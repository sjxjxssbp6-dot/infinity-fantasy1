package cupcake.nikky.imperium.tower.trade.npc.session.exact;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemBuildProcessor;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseBody;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseBoots;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseHelmet;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseLeggings;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseAxe;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseDagger;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSpear;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSword;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.protocol.utility.NumberUtility;
import cupcake.nikky.imperium.tower.trade.npc.session.TradeContainer;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.PotentialUtility;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.anfanik.steda.api.utility.ItemBuilder;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Accessors(fluent = true)
public class EquipTrade implements TradeGenerator {

    private final String title = "Снаряжение Грани";

    private final Map<ItemObject, Class<? extends ImperiumItem>> data = Map.of(
            ItemObject.FIRM_SWORD, BaseSword.class,
            ItemObject.FIRM_DAGGER, BaseDagger.class,
            ItemObject.FIRM_SPEAR, BaseSpear.class,
            ItemObject.FIRM_AXE, BaseAxe.class,
            ItemObject.FIRM_HELMET, BaseHelmet.class,
            ItemObject.FIRM_BODY, BaseBody.class,
            ItemObject.FIRM_LEGGINGS, BaseLeggings.class,
            ItemObject.FIRM_BOOTS, BaseBoots.class
    );

    @Override
    public List<TradeContainer> generate(@NotNull User user) {
        final List<TradeContainer> result;
        if (user.getLevel() > 10) {
            result = data.keySet().stream()
                    .map(itemObject -> this.createContainerWithRandomGear(itemObject, data.get(itemObject), user.getLevel() - 10))
                    .sorted(Comparator.comparingInt(TradeContainer::getId))
                    .collect(Collectors.toList());
        } else result = new ArrayList<>();

        result.add(TradeContainer.of(7000, ItemBuildProcessor.of(ItemObject.FIRM_SWORD.getUniqueId())
                .apply(item -> {
                    item.getGeneralSettings().itemLevel(1).requiredLevel(1);
                    item.getDropSettings().droppable(false);
                    ModifierProvider.getInstance().createLowestBaseModifiers(item);
                }).catchItem(), 0));

        if (user.findQuestSettings().isPassed(4))
            result.add(TradeContainer.of(6000, ItemObject.PICKAXE.getItem().create(), 1_000));
        if (user.findQuestSettings().isPassed(10))
            result.add(TradeContainer.of(6001, ItemObject.FISHING_ROD.getItem().create(), 1_000));

        return result;
    }

    private TradeContainer createContainerWithRandomGear(@NotNull final ItemObject itemObject, @NotNull final Class<? extends ImperiumItem> randomizedType, final int level) {
        var equipLevel = Math.min(level, LevelStorage.getInstance().getArmorLevel());

        var templateIcon = itemObject.getItem().create();
        templateIcon.getGeneralSettings().itemLevel(equipLevel);

        var icon = ItemBuilder.fromMaterial(templateIcon.getMaterial())
                .setName("&6Случайное снаряжение")
                .setLore("&7Уровень снаряжения: &6%d".formatted(equipLevel), " ", "&7При покупке генерирует случайное снаряжение", "&7соответствующее Вашему уровню")
                .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                .addItemFlag(ItemFlag.HIDE_UNBREAKABLE)
                .addItemFlag(ItemFlag.HIDE_DESTROYS)
                .addItemFlag(ItemFlag.HIDE_PLACED_ON)
                .nbtBuilder().setTag("rid", String.valueOf(templateIcon.getDurability())).apply()
                .build();

        return TradeContainer.of(NumberUtility.parseInteger(itemObject.getItem().getUniqueId()), itemObject.getUniqueId(), icon, equipLevel * 50, player -> {
            var items = Arrays.stream(ItemObject.values())
                    .map(ItemObject::getItem)
                    .filter(imperiumItem -> imperiumItem.checkInstance(randomizedType) && imperiumItem.getRare() == ItemRare.COMMON)
                    .filter(imperiumItem -> imperiumItem.getGeneralSettings().requiredLevel() <= equipLevel)
                    .toList();
            var item = items.get(MathUtility.randomInRange(0, items.size() - 1)).create();
            ModifierProvider.getInstance().randomizeItemRarity(item, equipLevel, Arrays.asList(ItemRare.COMMON, ItemRare.RARE, ItemRare.EPIC));
            ModifierProvider.getInstance().createRandomizedBaseModifiers(item);
            ModifierProvider.getInstance().createRandomizedDirectionModifiers(item);
            ModifierProvider.getInstance().createRandomizedExtendedModifiers(item, true);
            item.getGearSettings().potential(PotentialUtility.processPotential(equipLevel));
            return item;
        });
    }

}
