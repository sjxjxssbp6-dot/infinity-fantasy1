package cupcake.nikky.imperium.tower.user;

import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.BloodType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.DeathType;
import cupcake.nikky.imperium.tower.entity.Attributable;
import cupcake.nikky.imperium.tower.entity.monster.damage.OutboundDamageProcess;
import cupcake.nikky.imperium.tower.entity.monster.damage.InboundDamageProcess;
import cupcake.nikky.imperium.tower.mount.MountData;
import cupcake.nikky.imperium.tower.protocol.board.TimeType;
import cupcake.nikky.imperium.tower.donate.DonateData;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.session.Session;
import cupcake.nikky.imperium.tower.statistic.Statistics;
import cupcake.nikky.imperium.tower.storage.StorageData;
import cupcake.nikky.imperium.tower.user.controller.HealthController;
import cupcake.nikky.imperium.tower.user.controller.ManaController;
import cupcake.nikky.imperium.tower.user.inventory.FantasyInventory;
import cupcake.nikky.imperium.tower.user.controller.LevelController;
import cupcake.nikky.imperium.tower.user.metadata.MetadataStorage;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import cupcake.nikky.imperium.tower.user.settings.exact.*;
import cupcake.nikky.imperium.tower.user.settings.SettingsStorage;
import cupcake.nikky.imperium.tower.user.target.TargetEntity;
import cupcake.nikky.imperium.tower.user.time.TimeData;
import cupcake.nikky.imperium.tower.utility.TimeUtility;
import cupcake.nikky.imperium.tower.zone.Zone;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface User extends TickedUser, CurrencyUser, PacketUser, Attributable, OutboundDamageProcess, InboundDamageProcess {

    UUID getUniqueId();

    Session getSession();

    SettingsStorage getSettingsStorage();

    BloodType getCurrentBloodType();

    void setCurrentBloodType(final BloodType bloodType);

    DeathType getCurrentDeathType();

    void setCurrentDeathType(final DeathType deathType);

    default boolean isDeathless() {
        return this.findStatisticSettings().findStatistic(Statistics.DEATHS_BY_MONSTER).data() <= 0;
    }

    default SyncCalendarSettings findCalendarSettings() {
        return getSettingsStorage().findSettings(SyncCalendarSettings.class);
    }

    default PvpSettings findPvpSettings() {
        return getSettingsStorage().findSettings(PvpSettings.class);
    }

    default StatisticSettings findStatisticSettings() {
        return getSettingsStorage().findSettings(StatisticSettings.class);
    }

    default PandoraSettings findPandoraSettings() {
        return getSettingsStorage().findSettings(PandoraSettings.class);
    }

    default NotifySettings findNotifySettings() {
        return getSettingsStorage().findSettings(NotifySettings.class);
    }

    default ClientSettings findClientSettings() {
        return getSettingsStorage().findSettings(ClientSettings.class);
    }

    default SyncCaseSettings findCaseSettings() {
        return getSettingsStorage().findSettings(SyncCaseSettings.class);
    }

    default SyncBattlePassSettings findBattlePassSettings() {
        return getSettingsStorage().findSettings(SyncBattlePassSettings.class);
    }

    default SyncHideSettings findSyncHideSettings() {
        return getSettingsStorage().findSettings(SyncHideSettings.class);
    }

    default FragmentSettings findFragmentSettings() {
        return getSettingsStorage().findSettings(FragmentSettings.class);
    }

    default SyncCraftBenchSettings findSyncCraftBenchSettings() {
        return getSettingsStorage().findSettings(SyncCraftBenchSettings.class);
    }

    default IncubatorSettings findIncubatorSettings() {
        return getSettingsStorage().findSettings(IncubatorSettings.class);
    }

    default AchievementSettings findAchievementSettings() {
        return getSettingsStorage().findSettings(AchievementSettings.class);
    }

    default LootFilterSettings findLootFilterSettings() {
        return getSettingsStorage().findSettings(LootFilterSettings.class);
    }

    default SyncTradeSettings findSyncTradeSettings() {
        return getSettingsStorage().findSettings(SyncTradeSettings.class);
    }

    default DonateSettings findDonateSettings() {
        return getSettingsStorage().findSettings(DonateSettings.class);
    }

    default CorpseSettings findCorpseSettings() {
        return getSettingsStorage().findSettings(CorpseSettings.class);
    }

    default QuestSettings findQuestSettings() {
        return getSettingsStorage().findSettings(QuestSettings.class);
    }

    default TreeSettings findTreeSettings() {
        return getSettingsStorage().findSettings(TreeSettings.class);
    }

    FantasyInventory getFantasyInventory();

    void validateSpeed(Player player);

    AuraObject getCurrentAura();

    void setCurrentAura(AuraObject auraObject);

    MountData getCurrentMountData();

    void setMountData(MountData data);

    LevelController getLevelController();

    TimeData findTimeData(TimeType timeType);

    void appendTimeData(TimeData timeData);

    JumpData getJumpData();

    TargetEntity getTargetEntity();

    BeaconObjects getBeaconObject();

    BeaconObjects findNearestBeacon();

    void processRespawn(Consumer<Player> onComplete);

    DamageContainer getLastDamageContainer();

    void setLastDamageContainer(DamageContainer damageContainer);

    boolean isCanAttack();

    long getNextAvailableAttack();

    void setNextAvailableAttack(long millis);

    void setBeaconObject(BeaconObjects beaconObject);

    void setSession(Session session);

    HealthController getHealthController();

    ManaController getManaController();

    default double getHealth() {
        return getHealthController().getHealth();
    }

    default void setHealth(double health) {
        getHealthController().setHealth(health);
    }

    default void setHealthForce(double health) {
        getHealthController().setHealthForce(health);
    }

    default void changeHealth(double delta) {
        getHealthController().changeHealth(delta);
    }

    default void changeHealthPercent(double percent) {
        getHealthController().changeHealthPercent(percent);
    }

    default boolean isShouldDeath() {
        return getHealthController().isShouldDeath();
    }

    Zone getCurrentZone();

    void setCurrentZone(Zone zone);

    FractionType getFractionType();

    void setFractionType(FractionType type);

    void setTeam(Team team);

    MetadataStorage getMetadataStorage();

    @Override
    default int getLevel() {
        return getLevelController().getLevel();
    }

    default String[] getOverlayText() {
        var donateData = this.findDonateSettings().donateData();

        final List<String> result = new ArrayList<>();

        if (this.findQuestSettings().isPassed(10_000)) {
            result.add("Призвать спутника: [MOUNT_USE]");
        }

        if (donateData != null) {
            if (donateData.isSubscribed()) {
                val time = TimeUtility.formatSeconds(TimeUnit.MILLISECONDS.toSeconds(donateData.getSubscriptionTimeLong()));
                if (!time.isEmpty()) result.add(TextUtility.colorize("Премиум » &e" + time));
            }

            if (donateData.isDonateLootFilter()) {
                val time = TimeUtility.formatSeconds(TimeUnit.MILLISECONDS.toSeconds(donateData.getLootFilterTimeLong()));
                if (!time.isEmpty()) result.add(TextUtility.colorize("Фильтр предметов » &e" + time));
            }

            result.add("Сезон: &b" + Fantasy.currentSeason().getNumber() + "&f, &b" + Fantasy.currentSeason().getTitle() + " &f(&r\uE3FE &fОткрыть патчноут)");
        }

        return result.toArray(new String[0]);
    }

    Player getSpigot();

    StorageData getGlobalStorageData();

    void onRespawn();

    void validateGear(Player player, AttributeMap attributeMap);

    void validateBuffs(AttributeMap attributeMap);

    default CompletableFuture<Void> saveData(final List<Bson> updates) {
        return Fantasy.databaseProvider().updateUser(getUniqueId(), updates);
    }

    default CompletableFuture<Void> saveData(final Bson updates) {
        return Fantasy.databaseProvider().updateUser(getUniqueId(), updates);
    }

    default CompletableFuture<Void> saveAllData() {
        final List<Bson> updates = new ArrayList<>();

        updates.addAll(Fantasy.userService().update(getSpigot()));
        updates.addAll(Fantasy.buffService().saveEntity(getUniqueId()));
        updates.addAll(Fantasy.tradeNpcService().update(getUniqueId()));

        getSettingsStorage().findAll().stream()
                .filter(SavableUserSettings.class::isInstance)
                .map(SavableUserSettings.class::cast)
                .forEach(savableUserSettings -> updates.addAll(savableUserSettings.createUpdates()));

        return this.saveData(Updates.combine(updates));
    }

    default CompletableFuture<Void> saveAllDataAndInvalidateCache() {
        Fantasy.sessionProvider().removeFromSession(this);
        this.setInvalidated(true);

        final List<Bson> updates = new ArrayList<>();

        updates.addAll(Fantasy.userService().invalidate(getSpigot()));
        updates.addAll(Fantasy.buffService().invalidateEntity(getUniqueId()));
        updates.addAll(Fantasy.tradeNpcService().invalidate(getUniqueId()));

        getSettingsStorage().findAll().stream()
                .filter(SavableUserSettings.class::isInstance)
                .map(SavableUserSettings.class::cast)
                .forEach(savableUserSettings -> updates.addAll(savableUserSettings.createUpdates()));

        return Fantasy.databaseProvider().updateAndInvalidateUser(getUniqueId(), Updates.combine(updates));
    }

    default CompletableFuture<User> loadDataSync() {
        return Fantasy.supplyAsync(() -> Fantasy.buffService().registerBuffedEntity(getUniqueId(), true))
                .thenComposeAsync(ignore -> Fantasy.tradeNpcService().load(getUniqueId()))
                .thenApplyAsync(ignore -> this);
    }

}
