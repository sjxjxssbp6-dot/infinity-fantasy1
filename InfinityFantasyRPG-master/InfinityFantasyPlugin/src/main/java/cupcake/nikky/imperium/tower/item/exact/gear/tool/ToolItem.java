package cupcake.nikky.imperium.tower.item.exact.gear.tool;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.exact.harvest.HarvestItem;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierToolListDirection;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierToolListExtended;
import cupcake.nikky.imperium.tower.item.settings.exact.cursor.CursorResultType;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import org.bukkit.Material;

import java.util.List;

public abstract class ToolItem extends DefaultImperiumItem {

    public ToolItem(Material material, short durability, String name, List<String> additionalDescription) {
        super(ItemRare.COMMON, ItemType.TOOL, material, durability, name, additionalDescription);

        getGeneralSettings().itemLevel(1).npcTradePrice(500);
        this.getToolSettings().exp(0);
        getItemGenerator().setUnique(true);
        getDropSettings().droppable(false);
        getDescriptionSettings().showItemLevel(true).showItemType(true).itemLevelPattern("&fУровень инструмента: %s%d");

        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.LEVEL);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.TOOL_EXP);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.DROPPABLE);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.MODIFIERS);
        this.getItemWatcherRegistry().registerWatcher(ItemWatcher.RARITY);

        getModifierSettings().registerPossibleModifier(
                ModifierToolListExtended.INCREASE_CURRENCY,
                ModifierToolListExtended.INCREASE_EXP,
                ModifierToolListExtended.INCREASE_TOOL_POWER,
                ModifierToolListExtended.CHANCE_TO_DOUBLE_CURRENCY,
                ModifierToolListExtended.CHANCE_TO_DOUBLE_EXP,
                ModifierToolListDirection.CHANCE_TO_DOUBLE_CURRENCY,
                ModifierToolListDirection.CHANCE_TO_DOUBLE_EXP,
                ModifierToolListDirection.INCREASE_TOOL_POWER
        );

        getToolSettings().requirementExp(() -> {
            val level = getGeneralSettings().itemLevel();
            return 25 + (level - 1) * 500 + Math.max(level - 2, 0) * 500;
            /*
                iLvl 1: 100
                iLvl 2: 600
                iLvl 3: 1600
                iLvl 4: 2600
                iLvl 5: 3600
             */
        });

        this.getCursorSettings().registerSelfHandler(container -> {
            var player = container.player();
            var user = player.<User>getBungeePlayer();
            var cursorItemStack = container.cursorItemStack();
            var cursorFantasyItem = Fantasy.itemProvider().find(cursorItemStack);
            var targetFantasyItem = Fantasy.itemProvider().find(container.targetItemStack());

            if (cursorFantasyItem == null || targetFantasyItem == null || cursorFantasyItem.getImperiumType() != ItemType.MATERIAL || !cursorFantasyItem.checkInstance(HarvestItem.class)) return;

            var harvestItem = cursorFantasyItem.apply(HarvestItem.class);

            if (targetFantasyItem.getToolSettings().canUpgrade()) {
                val multiplier = (player.<User>getBungeePlayer().findDonateSettings().donateData().isSubscribed() ? 1.5D : 1);
                var expGain = harvestItem.getExperience() * multiplier;
                val increaseExp = user.findAttribute(ImperiumAttribute.INCREASE_TOOL_EXP).findValue();
                expGain = expGain * (1D + increaseExp / 100D);

                val chanceToDoubleExp = user.findAttribute(ImperiumAttribute.INCREASE_TOOL_CHANCE_TO_DOUBLE_EXP).findValue();
                if (chanceToDoubleExp > 0 && MathUtility.randomInRange(1, 100) <= chanceToDoubleExp)
                    expGain *= 2D;

                val resultExp = (int) Math.round(expGain);

                if (targetFantasyItem.getToolSettings().acceptExp(resultExp)) {
                    Fantasy.clientProvider().notification(player, SystemNotificationType.FINE, "Уровень Вашего инструмента был повышен!");
                    Fantasy.bukkitProvider().sound(player, AdvancedSounds.RING);
                } else {
                    Fantasy.bukkitProvider().sound(player, AdvancedSounds.WEAPON);
                }

                container.cursorResultType(CursorResultType.REMOVE_ONE_ITEM);
                container.clickedInventory().setItem(container.inventorySlotClick(), targetFantasyItem.createNew());
            } else {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Уровень Вашего инструмента максимальный!");
                container.cursorResultType(CursorResultType.CANCEL);
            }
        });
    }

    @Override
    public ItemRare getRare() {
        return getGeneralSettings().itemLevel() >= 2 ? ItemRare.RARE : ItemRare.COMMON;
    }

}
