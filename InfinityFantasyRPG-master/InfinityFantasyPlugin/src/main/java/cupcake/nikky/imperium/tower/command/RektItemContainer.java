package cupcake.nikky.imperium.tower.command;

import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseBody;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseBoots;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseHelmet;
import cupcake.nikky.imperium.tower.item.exact.gear.armour.BaseLeggings;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.WeaponItem;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseAxe;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseDagger;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSpear;
import cupcake.nikky.imperium.tower.item.exact.gear.weapon.base.BaseSword;
import cupcake.nikky.imperium.tower.item.modifier.ModifierProvider;
import cupcake.nikky.imperium.tower.item.modifier.ModifierType;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierItemListDirection;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierItemListExtended;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierUniqueItemList;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.menu.Menu;
import me.anfanik.steda.api.menu.content.exact.StaticContentProvider;
import me.anfanik.steda.api.menu.item.MenuItem;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class RektItemContainer {

    private final Player player;
    private ImperiumItem[] items = new ImperiumItem[10];

    public void createItems() {
        var user = player.<User>getBungeePlayer();

        items[0] = Arrays.stream(ItemObject.values())
                .filter(itemObject -> itemObject.getItem().getRare() == ItemRare.COMMON)
                .filter(itemObject -> itemObject.getItem().checkInstance(BaseSword.class))
                .map(itemObject -> itemObject.getItem().create().apply(BaseSword.class))
                .filter(weaponItem -> weaponItem.getBaseCreatorType().getRequiredUserLevel() <= user.getLevel())
                .max(Comparator.comparingInt(item -> item.getBaseCreatorType().getRequiredUserLevel()))
                .orElse(ItemObject.FIRM_SWORD.getItem().create().apply(BaseSword.class));
        items[0].setName("Клинок Ректа");

        items[1] = Arrays.stream(ItemObject.values())
                .filter(itemObject -> itemObject.getItem().getRare() == ItemRare.COMMON)
                .filter(itemObject -> itemObject.getItem().checkInstance(BaseDagger.class))
                .map(itemObject -> itemObject.getItem().create().apply(BaseDagger.class))
                .filter(weaponItem -> weaponItem.getBaseCreatorType().getRequiredUserLevel() <= user.getLevel())
                .max(Comparator.comparingInt(item -> item.getBaseCreatorType().getRequiredUserLevel()))
                .orElse(ItemObject.FIRM_DAGGER.getItem().create().apply(BaseDagger.class));
        items[1].setName("Кинжал Ректа");

        items[2] = Arrays.stream(ItemObject.values())
                .filter(itemObject -> itemObject.getItem().getRare() == ItemRare.COMMON)
                .filter(itemObject -> itemObject.getItem().checkInstance(BaseSpear.class))
                .map(itemObject -> itemObject.getItem().create().apply(BaseSpear.class))
                .filter(weaponItem -> weaponItem.getBaseCreatorType().getRequiredUserLevel() <= user.getLevel())
                .max(Comparator.comparingInt(item -> item.getBaseCreatorType().getRequiredUserLevel()))
                .orElse(ItemObject.FIRM_SPEAR.getItem().create().apply(BaseSpear.class));
        items[2].setName("Копье Ректа");

        items[3] = Arrays.stream(ItemObject.values())
                .filter(itemObject -> itemObject.getItem().getRare() == ItemRare.COMMON)
                .filter(itemObject -> itemObject.getItem().checkInstance(BaseAxe.class))
                .map(itemObject -> itemObject.getItem().create().apply(BaseAxe.class))
                .filter(weaponItem -> weaponItem.getBaseCreatorType().getRequiredUserLevel() <= user.getLevel())
                .max(Comparator.comparingInt(item -> item.getBaseCreatorType().getRequiredUserLevel()))
                .orElse(ItemObject.FIRM_AXE.getItem().create().apply(BaseAxe.class));
        items[3].setName("Топор Ректа");

        items[4] = Arrays.stream(ItemObject.values())
                .filter(itemObject -> itemObject.getItem().getRare() == ItemRare.COMMON)
                .filter(itemObject -> itemObject.getItem().checkInstance(BaseHelmet.class))
                .map(itemObject -> itemObject.getItem().create().apply(BaseHelmet.class))
                .filter(weaponItem -> weaponItem.getBaseCreatorType().getRequiredUserLevel() <= user.getLevel())
                .max(Comparator.comparingInt(item -> item.getBaseCreatorType().getRequiredUserLevel()))
                .orElse(ItemObject.FIRM_HELMET.getItem().create().apply(BaseHelmet.class));
        items[4].setName("Шлем Ректа");

        items[5] = Arrays.stream(ItemObject.values())
                .filter(itemObject -> itemObject.getItem().getRare() == ItemRare.COMMON)
                .filter(itemObject -> itemObject.getItem().checkInstance(BaseBody.class))
                .map(itemObject -> itemObject.getItem().create().apply(BaseBody.class))
                .filter(weaponItem -> weaponItem.getBaseCreatorType().getRequiredUserLevel() <= user.getLevel())
                .max(Comparator.comparingInt(item -> item.getBaseCreatorType().getRequiredUserLevel()))
                .orElse(ItemObject.FIRM_BODY.getItem().create().apply(BaseBody.class));
        items[5].setName("Нагрудник Ректа");

        items[6] = Arrays.stream(ItemObject.values())
                .filter(itemObject -> itemObject.getItem().getRare() == ItemRare.COMMON)
                .filter(itemObject -> itemObject.getItem().checkInstance(BaseLeggings.class))
                .map(itemObject -> itemObject.getItem().create().apply(BaseLeggings.class))
                .filter(weaponItem -> weaponItem.getBaseCreatorType().getRequiredUserLevel() <= user.getLevel())
                .max(Comparator.comparingInt(item -> item.getBaseCreatorType().getRequiredUserLevel()))
                .orElse(ItemObject.FIRM_LEGGINGS.getItem().create().apply(BaseLeggings.class));
        items[6].setName("Поножи Ректа");

        items[7] = Arrays.stream(ItemObject.values())
                .filter(itemObject -> itemObject.getItem().getRare() == ItemRare.COMMON)
                .filter(itemObject -> itemObject.getItem().checkInstance(BaseBoots.class))
                .map(itemObject -> itemObject.getItem().create().apply(BaseBoots.class))
                .filter(weaponItem -> weaponItem.getBaseCreatorType().getRequiredUserLevel() <= user.getLevel())
                .max(Comparator.comparingInt(item -> item.getBaseCreatorType().getRequiredUserLevel()))
                .orElse(ItemObject.FIRM_BOOTS.getItem().create().apply(BaseBoots.class));
        items[7].setName("Боты Ректа");

        items[4].getModifierSettings().registerSpecificModifier(ModifierItemListDirection.ELEMENTAL_DAMAGE.process().type(ModifierType.DIRECTION).applyHighest(3));
        items[4].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.FIRE_RESISTANCE.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[4].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.COLD_RESISTANCE.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[4].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.LIGHTNING_RESISTANCE.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[4].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.ALL_MAXIMUM_ELEMENTAL_RESISTANCE.process().type(ModifierType.EXTENDED).applyHighest(3));
        items[4].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.REGENERATION_IN_FIGHT.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[4].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.ADDITIONAL_MAX_MANA.process().type(ModifierType.EXTENDED).applyHighest(11));

        items[5].getModifierSettings().registerSpecificModifier(ModifierItemListDirection.REGENERATION_IN_FIGHT.process().type(ModifierType.DIRECTION).applyHighest(6));
        items[5].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.FIRE_RESISTANCE.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[5].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.COLD_RESISTANCE.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[5].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.LIGHTNING_RESISTANCE.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[5].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.DEFENSE.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[5].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.REGENERATION_IN_FIGHT.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[5].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.ADDITIONAL_MANA_REGENERATION.process().type(ModifierType.EXTENDED).applyHighest(5));

        items[6].getModifierSettings().registerSpecificModifier(ModifierItemListDirection.ELEMENTAL_DAMAGE.process().type(ModifierType.DIRECTION).applyHighest(3));
        items[6].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.CHAOS_RESISTANCE.process().type(ModifierType.EXTENDED).applyHighest(6));
        items[6].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.GLOBAL_CRITICAL_CHANCE.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[6].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.INCREASE_DROP_QUANTITY_OF_ITEMS.process().type(ModifierType.EXTENDED).applyHighest(5));
        items[6].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.ADDITIONAL_MAX_HEALTH.process().type(ModifierType.EXTENDED).applyHighest(13));
        items[6].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.DEFENSE.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[6].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.INCREASED_MANA_RESERVE_EFFICIENCY.process().type(ModifierType.EXTENDED).applyHighest(5));

        items[7].getModifierSettings().registerSpecificModifier(ModifierItemListDirection.CHAOS_RESISTANCE.process().type(ModifierType.DIRECTION).applyHighest(3));
        items[7].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.CHAOS_RESISTANCE.process().type(ModifierType.EXTENDED).applyHighest(6));
        items[7].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.GLOBAL_CRITICAL_CHANCE.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[7].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.MOVEMENT_SPEED.process().type(ModifierType.EXTENDED).applyHighest(6));
        items[7].getModifierSettings().registerSpecificModifier(ModifierItemListExtended.DEFENSE.process().type(ModifierType.EXTENDED).applyHighest(7));
        items[7].getModifierSettings().registerSpecificModifier(ModifierUniqueItemList.CHANCE_TO_GAIN_FRENZY_CHARGE_ON_HIT.process().type(ModifierType.EXTENDED).applyHighest(1));
        items[7].getModifierSettings().registerSpecificModifier(ModifierUniqueItemList.CHANCE_TO_GAIN_ENDURANCE_CHARGE_ON_HIT.process().type(ModifierType.EXTENDED).applyHighest(1));

        for (int i = 0; i < 8; i++) {
            var item = items[i];

            if (user.getLevel() >= LevelStorage.getInstance().getStartShardEdgeLevel()) {
                item.getGearSettings().corrupted(10);
            }

            ModifierProvider.getInstance().randomizeItemRarity(item,
                    Math.min(user.getLevel(), LevelStorage.getInstance().getArmorLevel()),
                    List.of(user.getLevel() >= LevelStorage.getInstance().getStartShardEdgeLevel() ? ItemRare.EXALTED : ItemRare.UNIQUE));
            ModifierProvider.getInstance().createHighestBaseModifiers(item);

            item.getDropSettings().droppable(false);
        }

        for (int i = 0; i < 4; i++) {
            var item = items[i];

            item.getModifierSettings().registerSpecificModifier(ModifierItemListDirection.DAMAGE_LEECH_AS_LIFE.process().type(ModifierType.DIRECTION).applyHighest(3));
            item.getModifierSettings().registerSpecificModifier(ModifierItemListExtended.GLOBAL_CRITICAL_CHANCE.process().type(ModifierType.EXTENDED).applyHighest(8));
            item.getModifierSettings().registerSpecificModifier(ModifierItemListExtended.GLOBAL_CRITICAL_DAMAGE.process().type(ModifierType.EXTENDED).applyHighest(6));
            item.getModifierSettings().registerSpecificModifier(ModifierItemListExtended.ATTACK_SPEED.process().type(ModifierType.EXTENDED).applyHighest(8));
            item.getModifierSettings().registerSpecificModifier(ModifierItemListExtended.ADD_LIGHTNING_DAMAGE.process().type(ModifierType.EXTENDED).applyHighest(10));
            item.getModifierSettings().registerSpecificModifier(ModifierItemListExtended.ADD_FIRE_DAMAGE.process().type(ModifierType.EXTENDED).applyHighest(11));
            item.getModifierSettings().registerSpecificModifier(ModifierItemListExtended.ADD_COLD_DAMAGE.process().type(ModifierType.EXTENDED).applyHighest(10));
        }

        items[8] = ItemObject.STAR_OF_CORRUPT_MOD_ITEM.getItem();
        items[9] = ItemObject.EXALTED_ORB_MOD_ITEM.getItem();
    }

    public void openMenu() {
        this.createItems();

        val contentProvider = new StaticContentProvider();
        for (int i = 0; i < items.length; i++) {
            contentProvider.setItem(i, MenuItem.createStatic(NbtUtility.clearSessionTags(items[i].createNew(i > 7 ? 64 : 1))).disableStickiness());
        }

        Menu.builder()
                .title("Эксклюзивные вещи для AttentiveRekt")
                .chestRows(2)
                .contentProvider(contentProvider)
                .build().open(player);
    }

}
