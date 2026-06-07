package cupcake.nikky.imperium.tower.item.exact.fragment;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.*;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierFragmentListExtended;
import cupcake.nikky.imperium.tower.item.settings.exact.FragmentSettings;
import cupcake.nikky.imperium.tower.item.watcher.ItemWatcher;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.zone.ZoneList;
import me.anfanik.steda.api.utility.InventoryUtility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public abstract class FragmentItem extends DefaultImperiumItem {

    private final Location targetLocation;

    public FragmentItem(@NotNull final String title, @NotNull final Location targetLocation, final short data, final int wayTier) {
        super(ItemRare.COMMON, ItemType.EDGE_FRAGMENT, Material.WEB, data, title,
                List.of("Является артефактом, который временно",
                        "перенесет Вас на специальный осколок",
                        "",
                        "&r\uE3FF &7Использовать"
                )
        );

        this.targetLocation = targetLocation;
        getLootSettings().lootGeneratorSettings().rarity(true).baseModifiers(false).extendedModifiers(true);
        getLootSettings().maxAmountHandler(tdata -> 1);
        getDescriptionSettings().showItemType(true).showItemLevel(true).itemLevelPattern("&fУровень пути: %s%d");
        getGeneralSettings().disableRequiredLevel(true);
        getGeneralSettings().itemLevel(wayTier);

        this.getModifierSettings().registerPossibleModifier(List.of(
                ModifierFragmentListExtended.DECREASE_ALL_DAMAGE,
                ModifierFragmentListExtended.DECREASE_ALL_RESISTANCES,
                ModifierFragmentListExtended.DECREASE_ALL_MAXIMUM_RESISTANCES,
                ModifierFragmentListExtended.REFLECT_ELEMENTAL_DAMAGE,
                ModifierFragmentListExtended.REFLECT_PHYSICAL_DAMAGE,
                ModifierFragmentListExtended.DECREASE_CHAOS_RESISTANCE,
                ModifierFragmentListExtended.DECREASE_COLD_RESISTANCE,
                ModifierFragmentListExtended.DECREASE_FIRE_RESISTANCE,
                ModifierFragmentListExtended.DECREASE_LIGHTNING_RESISTANCE,
                ModifierFragmentListExtended.DECREASE_ATTACK_SPEED,
                ModifierFragmentListExtended.DECREASE_MOVEMENT_SPEED,
                ModifierFragmentListExtended.CANNOT_DEAL_CRITICAL_STRIKES,
                ModifierFragmentListExtended.CANNOT_REGENERATE_HEALTH_AND_MANA,
                ModifierFragmentListExtended.INCREASE_INPUT_CHAOS_DAMAGE,
                ModifierFragmentListExtended.INCREASE_INPUT_COLD_DAMAGE,
                ModifierFragmentListExtended.INCREASE_INPUT_FIRE_DAMAGE,
                ModifierFragmentListExtended.INCREASE_INPUT_LIGHTNING_DAMAGE,
                ModifierFragmentListExtended.CHANCE_GET_DOUBLE_DAMAGE
        ));

        getItemWatcherRegistry().registerWatcher(ItemWatcher.DROPPABLE);
        getItemWatcherRegistry().registerWatcher(ItemWatcher.MODIFIERS);
        getItemWatcherRegistry().registerWatcher(ItemWatcher.RARITY);
        getItemWatcherRegistry().registerWatcher(ItemWatcher.CORRUPTED);

        getInteractSettings().onRightClick(player -> {
            if (player.hasCooldown(getMaterial())) return;
            player.setCooldown(getMaterial(), 20);

            var user = player.<User>getBungeePlayer();

            if (player.isInsideVehicle()) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Перемещения со спутником запрещены");
                return;
            }

            if (user.getLevel() < LevelStorage.getInstance().getStartShardEdgeLevel()) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Ваш уровень не подходит", "для использования");
                return;
            }

            if (Fantasy.combatProvider().isInCombat(player.getUniqueId())) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Нельзя использовать в бою");
                return;
            }

            var userFragmentSettings = user.findFragmentSettings();
            if (userFragmentSettings.getExitTime() != 0L) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Нельзя использовать", "уже находясь на Осколке");
                return;
            }

            if (InventoryUtility.removeFromMainHand(player.getInventory(), 1)) {
                var multiplier = 1 + user.findAttribute(ImperiumAttribute.INCREASE_FRAGMENT_DURATION).findValue() / 100D;
                var time = Instant.now().toEpochMilli() + 1_000 * 60 * 2 * multiplier;
                var itemFragmentSettings = this.findSettings(FragmentSettings.class);
                userFragmentSettings.setExitTime(Math.round(time));
                userFragmentSettings.setRespawnLeft(player.<User>getBungeePlayer().findDonateSettings().donateData().isSubscribed() ? 6 : 4);
                userFragmentSettings.setLastLocation(player.getLocation());
                userFragmentSettings.setFragmentAttributeMap(this.getAttributes());
                userFragmentSettings.setFragmentItemQuantity(itemFragmentSettings.getFragmentItemQuantity());
                userFragmentSettings.setFragmentExperienceIncrease(itemFragmentSettings.getFragmentExperienceIncrease());
                userFragmentSettings.setFragmentCorruption(this.getGearSettings().corrupted());
                userFragmentSettings.setFragmentEventStarted(0);
                userFragmentSettings.setFragmentZone(ZoneList.findByLocation(targetLocation));

                final List<String> description = new ArrayList<>();

                if (itemFragmentSettings.getFragmentItemQuantity() > 0)
                    description.add("&c+%d%% &6количество выпадаемых предметов".formatted(itemFragmentSettings.getFragmentItemQuantity()));
                if (itemFragmentSettings.getFragmentExperienceIncrease() > 0)
                    description.add("&c%d%% &6увеличение получаемого опыта".formatted(itemFragmentSettings.getFragmentExperienceIncrease()));

                if (this.getGearSettings().corrupted() > 0) {
                    if (!description.isEmpty()) description.add(" ");
                    description.add("&c%d%% увеличение получаемого урона".formatted(this.getGearSettings().corrupted()));
                }

                var specificModifiers = this.getModifierSettings().findAllSpecificModifiers();
                if (!specificModifiers.isEmpty() && !description.isEmpty()) description.add(" ");
                specificModifiers.forEach(specificModifier -> {
                    var tier = specificModifier.getModifier().getRange().findByNumericTier(specificModifier.findTier()).getFragmentTierIfPresent();
                    if (tier == null) return;

                    description.add(specificModifier.getModifier().getModifierDescription().generateDescription("&c", "&6", specificModifier));
                });

                userFragmentSettings.setDescriptionOfFragmentModifiers(description);

                if (this.getGearSettings().corrupted() > 0 || !specificModifiers.isEmpty())
                    Fantasy.buffService().appendBuff(player.getUniqueId(), BuffType.FRAGMENT_DEBUFF_INFO);
                Fantasy.clientProvider().updateFragmentTime(player);
                user.setCurrentZone(ZoneList.BATTLE_GROUND_ZONE);
                player.teleport(getTargetLocation());
            }
        });
    }

    public int getTier() {
        return getGeneralSettings().itemLevel();
    }

    public Location getTargetLocation() {
        if (targetLocation.getWorld() == null)
            targetLocation.setWorld(MapServiceProcess.GAME_WORLD);
        return targetLocation;
    }
}
