package cupcake.nikky.imperium.tower.client;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.client.protocol.cursor.CursorPromptData;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.*;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.fragment.FragmentDataPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.guide.GuideOpenPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.incubator.IncubatorData;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.incubator.IncubatorUpdatePacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsType;
import cupcake.nikky.imperium.tower.client.protocol.storage.PageState;
import cupcake.nikky.imperium.tower.client.protocol.storage.StorageData;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.tree.PointType;
import cupcake.nikky.imperium.tower.user.User;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import lombok.NoArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import me.func.mod.ui.Glow;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.Color;

import java.time.Instant;
import java.util.*;

@NoArgsConstructor
public final class ClientProvider {

    public void sendSettings(@NotNull final Player player) {
        val user = player.<User>getBungeePlayer();
        user.sendPacket(new SettingsPacket(new Object2BooleanOpenHashMap<>(user.findClientSettings().getSettings())));
    }

    public void updateFragmentTime(@NotNull final Player player) {
        val user = player.<User>getBungeePlayer();
        user.sendPacket(new FragmentDataPacket(user.findFragmentSettings().getExitTime(), user.findFragmentSettings().getRespawnLeft()));
    }

    public void updateIncubators(@NotNull final Player player) {
        val user = player.<User>getBungeePlayer();

        val incubators = user.findIncubatorSettings().findAllIncubators();
        user.sendPacket(new IncubatorUpdatePacket(incubators.stream().map(incubator -> {
            var requirement = incubator.getType().getRequirement();
            return new IncubatorData(incubator.getUuid(),
                    incubator.getData().getCurrent(),
                    requirement.deathRequirement(),
                    requirement.levelRequirement(),
                    incubator.getType().getReward().icon(incubator.getType())
            );
        }).toList()));
    }

    public void openGuide(@NotNull final Player player) {
        player.<User>getBungeePlayer().sendPacket(new GuideOpenPacket());
    }

    public void storagePacket(@NotNull final Player player) {
        val user = player.<User>getBungeePlayer();
        val data = new StorageData();
        val purchasedPages = player.<User>getBungeePlayer().findDonateSettings().donateData().getPurchasedStoragePages();

        for (int i = 0; i < 7; i++) {
            if (i <= purchasedPages + 1) data.apply(i, PageState.PURCHASED);
            else if (i == purchasedPages + 2) data.apply(i, PageState.CAN_BUY);
            else data.apply(i, PageState.CANT_BUY);
        }

        user.sendPacket(new StoragePacket(data));
    }

    public void closeStoragePacket(@NotNull final Player player) {
        player.<User>getBungeePlayer().sendPacket(new StoragePacket());
    }

    public void teleportIndicator(@NotNull final Player player, final long millis) {
        player.<User>getBungeePlayer().sendPacket(new TeleportIndicatorPacket(millis, TeleportIndicatorPacket.TeleportState.START));
    }

    public void teleportIndicator(@NotNull final Player player, final long millis, final TeleportIndicatorPacket.TeleportState state) {
        player.<User>getBungeePlayer().sendPacket(new TeleportIndicatorPacket(millis, state));
    }

    public void vignette(@NotNull final Player player, final double duration, @NotNull final Color color) {
        if (player.<User>getBungeePlayer().findClientSettings().find(SettingsType.GLOW_SCREEN_EFFECT))
            Glow.animate(player, duration, color.getIntR(), color.getIntG(), color.getIntB(), .75);
    }

    public void sendTreeIndicator(@NotNull final User user) {
        user.sendPacket(new TreePointIndicatorPacket(user.findTreeSettings().findFreePoints(PointType.CHARACTER) + user.findTreeSettings().findFreePoints(PointType.EDGE)));
    }

    public void sendTreeIndicator(@NotNull final Player player) {
        this.sendTreeIndicator(player.<User>getBungeePlayer());
    }

    public void updateCurrency(@NotNull final Player player) {
        this.updateCurrency(player.<User>getBungeePlayer());
    }

    public void updateCurrency(@NotNull final User user) {
        if (user.findDonateSettings().donateData() == null) return;
        user.sendPacket(new CurrencyPacket(user.getGrans(), user.getMines(), user.getCurrency(CurrencyType.FIRM),
                user.getCurrency(CurrencyType.FIRM_PRIVATE), user.getCurrency(CurrencyType.FLOWER)));
    }

    public void showBattleDuration(@NotNull final Player player) {
        val duration = Fantasy.combatProvider().findEndBattle(player.getUniqueId());
        if (duration == -1) return;

        player.<User>getBungeePlayer().sendPacket(new BattleDurationPacket((int) (duration - Instant.now().toEpochMilli()), false));
    }

    public void hideBattleDuration(@NotNull final Player player) {
        player.<User>getBungeePlayer().sendPacket(new BattleDurationPacket(0, true));
    }

    public void showBattleDuration(@NotNull final User user) {
        val duration = Fantasy.combatProvider().findEndBattle(user.getUniqueId());
        if (duration == -1) return;

        user.sendPacket(new BattleDurationPacket((int) (duration - Instant.now().toEpochMilli()), false));
    }

    public void hideBattleDuration(@NotNull final User user) {
        user.sendPacket(new BattleDurationPacket(0, true));
    }

    public void showAttackSpeed(@NotNull final Player player) {
        val user = player.<User>getBungeePlayer();
        user.sendPacket(new AttackSpeedPacket((int) (user.getNextAvailableAttack() - Instant.now().toEpochMilli()), false));
    }

    public void showAttackSpeed(@NotNull final User user) {
        user.sendPacket(new AttackSpeedPacket((int) (user.getNextAvailableAttack() - Instant.now().toEpochMilli()), false));
    }

    public void hideAttackSpeed(@NotNull final User user) {
        user.sendPacket(new AttackSpeedPacket(0, true));
    }

    public void hideAttackSpeed(@NotNull final Player player) {
        player.<User>getBungeePlayer().sendPacket(new AttackSpeedPacket(0, true));
    }

    public void cursorNotify(@NotNull final Player player, @NotNull final CursorPromptData data) {
        var user = player.<User>getBungeePlayer();
        if (user.findClientSettings().find(SettingsType.CURSOR_INFORMATION)) {
            user.sendPacket(new CursorPromptPacket().applyPrompt(data));
        }
    }

    public void cursorNotify(@NotNull final User user, @NotNull final CursorPromptData data) {
        if (user.findClientSettings().find(SettingsType.CURSOR_INFORMATION)) {
            user.sendPacket(new CursorPromptPacket().applyPrompt(data));
        }
    }

    public void cursorNotify(@NotNull final Player player, @NotNull final List<CursorPromptData> data) {
        var user = player.<User>getBungeePlayer();
        if (user.findClientSettings().find(SettingsType.CURSOR_INFORMATION)) {
            user.sendPacket(new CursorPromptPacket(data));
        }
    }

    public void cursorNotify(@NotNull final User user, @NotNull final List<CursorPromptData> data) {
        if (user.findClientSettings().find(SettingsType.CURSOR_INFORMATION)) {
            user.sendPacket(new CursorPromptPacket(data));
        }
    }

    public void cursorNotify(@NotNull final User user, @NotNull final String text, @NotNull final String texture) {
        if (user.findClientSettings().find(SettingsType.CURSOR_INFORMATION)) {
            user.sendPacket(new CursorPromptPacket().applyPrompt(new CursorPromptData(text, texture)));
        }
    }

    public void notification(@NotNull final Player player, @NotNull final String... text) {
        player.<User>getBungeePlayer().sendPacket(new NotificationPacket(SystemNotificationType.NONE, TextUtility.colorize(Arrays.stream(text).toList()).toArray(new String[0])));
    }

    public void notification(@NotNull final Player player, @NotNull final SystemNotificationType type, @NotNull final String... text) {
        player.<User>getBungeePlayer().sendPacket(new NotificationPacket(type, TextUtility.colorize(Arrays.stream(text)
                .map(line -> {
                    if (type == SystemNotificationType.WARN || type == SystemNotificationType.ERROR) {
                        return "&c" + line;
                    } else if (type == SystemNotificationType.FINE) {
                        return "&a" + line;
                    }

                    return line;
                })
                .toList()).toArray(new String[0])));
    }

    public void inventoryOverlayText(@NotNull final User user) {
        user.sendPacket(new InventoryOverlayTextPacket(user.getOverlayText(), Fantasy.currentSeason().getPatchNoteUrl()));
    }

    public void inventoryOverlayText(@NotNull final User user, @NotNull final String[] overlay) {
        user.sendPacket(new InventoryOverlayTextPacket(overlay, Fantasy.currentSeason().getPatchNoteUrl()));
    }

    public void subscribeIndicator(@NotNull final User user) {
        user.sendPacket(new PremiumPacket(user.findDonateSettings().donateData().isSubscribed()));
    }

    public void levelUpTitle(@NotNull final User user, final int level) {
        user.sendPacket(new LevelUpTitlePacket(level));
    }

    public void hotbarHealth(@NotNull final User user) {
        val maxHealth = (int) user.findAttribute(ImperiumAttribute.HEALTH).findValue();
        val currentHealth = (int) user.getHealth();

        user.sendPacket(new HealthBarPacket(currentHealth, maxHealth));
    }

    public void hotbarMana(@NotNull final User user) {
        val maxMana = (int) user.findAttribute(ImperiumAttribute.MANA).findValue();
        val currentMana = (int) user.getManaController().getMana();

        user.sendPacket(new ManaBarPacket(user.getLevel() < 12, currentMana, maxMana));
    }

    public void hotbarLevel(@NotNull final User user) {
        val levelHandler = user.getLevelController();
        val requirementExp = LevelStorage.getInstance().getBasicRequirementToNextLevel(levelHandler.getLevel());
        val expMultiplier = (int) (levelHandler.getBattleMultiplier() * 100);

        user.sendPacket(new LevelPacket(
                levelHandler.getLevel(),
                (int) levelHandler.getExp(),
                requirementExp,
                expMultiplier,
                TextUtility.colorize("&6"))
        );
    }

    public void attributeContext(@NotNull final User user) {
        val packet = new AttributePacket();
        for (ImperiumAttribute attribute : ImperiumAttribute.findAttackAndDefenseAttributes()) {
            var value = user.findAttribute(attribute).findValueWithLimit();
            if (attribute == ImperiumAttribute.CRITICAL_DAMAGE || attribute == ImperiumAttribute.MOVEMENT_SPEED)
                value += 100;
            packet.applyValue(attribute, value);
        }
        user.sendPacket(packet);
    }

}
