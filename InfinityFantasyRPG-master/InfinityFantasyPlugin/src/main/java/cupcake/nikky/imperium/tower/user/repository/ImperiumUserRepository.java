package cupcake.nikky.imperium.tower.user.repository;

import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.BloodType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.DeathType;
import cupcake.nikky.imperium.tower.inventory.StorageSerialization;
import cupcake.nikky.imperium.tower.mount.MountData;
import cupcake.nikky.imperium.tower.mount.MountType;
import cupcake.nikky.imperium.tower.protocol.board.TimeType;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.storage.StorageData;
import cupcake.nikky.imperium.tower.user.FantasyUser;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import cupcake.nikky.imperium.tower.user.time.TimeData;
import cupcake.nikky.imperium.tower.utility.InventoryValidation;
import cupcake.nikky.imperium.tower.zone.ZoneList;
import lombok.val;
import me.anfanik.steda.api.utility.LocationUtility;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ImperiumUserRepository {

    private final Map<UUID, User> data = new ConcurrentHashMap<>(32, 0.75F, 2);

    public User findById(@NotNull final UUID uuid) {
        return this.data.get(uuid);
    }

    public Collection<User> findAll() {
        return new ArrayList<>(this.data.values());
    }

    public List<Bson> invalidate(@NotNull final Player player) {
        val updates = this.save(player);
        this.data.remove(player.getUniqueId());
        return updates;
    }

    public List<Bson> save(@NotNull final Player player) {
        val user = this.findById(player.getUniqueId());
        if (user == null) return Collections.emptyList();

        val location = Fantasy.combatProvider().isInCombat(player.getUniqueId()) ? user.getBeaconObject().getBeacon().getRespawnLocation() : player.getLocation();
        val fragmentExitLocation = user.findFragmentSettings().getLastLocation();
        val inventory = player.getInventory();
        val levelHandler = user.getLevelController();

        val binaTimeData = user.findTimeData(TimeType.BINA);
        val hochmaTimeData = user.findTimeData(TimeType.HOCHMA);

        return Arrays.asList(
                Updates.set("level", levelHandler.getLevel()),
                Updates.set("exp", (int) levelHandler.getExp()),
                Updates.set("location", LocationUtility.serialize(fragmentExitLocation == null ? location : fragmentExitLocation, true)),
                Updates.set("health", (int) user.getHealth()),
                Updates.set("mana", (int) user.getManaController().getMana()),
                Updates.set("playerStorage", new StorageSerialization(inventory.getContents()).serialize()),
                Updates.set("globalStorage", user.getGlobalStorageData().serialize()),
                Updates.set("fraction", user.getFractionType().name()),
                Updates.set("gran", user.getGrans()),
                Updates.set("mine", user.getMines()),
                Updates.set("flower", user.getFlowers()),
                Updates.set("currentAura", user.getCurrentAura().name()),
                Updates.set("currentMount", user.getCurrentMountData().getMountType().name()),
                Updates.set("bloodType", user.getCurrentBloodType().name()),
                Updates.set("deathType", user.getCurrentDeathType().name()),
                Updates.set(TimeType.BINA.getFieldName() + "_time", binaTimeData.getDuration()),
                Updates.set(TimeType.BINA.getFieldName() + "_level", binaTimeData.getLevel()),
                Updates.set(TimeType.BINA.getFieldName() + "_corrupt", binaTimeData.getCorrupt()),
                Updates.set(TimeType.BINA.getFieldName() + "_score", binaTimeData.getScore()),
                Updates.set(TimeType.HOCHMA.getFieldName() + "_time", hochmaTimeData.getDuration()),
                Updates.set(TimeType.HOCHMA.getFieldName() + "_level", hochmaTimeData.getLevel()),
                Updates.set(TimeType.HOCHMA.getFieldName() + "_corrupt", hochmaTimeData.getCorrupt()),
                Updates.set(TimeType.HOCHMA.getFieldName() + "_score", hochmaTimeData.getScore()),
                Updates.set("globalScore", binaTimeData.getScore() + hochmaTimeData.getScore())
        );
    }

    public CompletableFuture<User> load(@NotNull final UUID uuid) {
        return Fantasy.databaseProvider().findUser(uuid).thenApplyAsync(document -> {
            if (document.isEmpty()) {
                val user = new FantasyUser(uuid, 1,  0, 0,  0, 0);
                user.setHealthForce(user.findAttribute(ImperiumAttribute.HEALTH).findValue());

                for (TimeType value : TimeType.values())
                    user.appendTimeData(new TimeData(value).setLevel(0).setDuration(0).setCorrupt(0));

                user.onDataLoad().add(loadedUser -> BeaconObjects.START.forceTeleportToRespawn(loadedUser.getSpigot()));

                user.getSettingsStorage().findAll().stream()
                        .filter(SavableUserSettings.class::isInstance)
                        .map(SavableUserSettings.class::cast)
                        .forEach(savableUserSettings -> savableUserSettings.readDocument(document));

                return user;
            }

            val grans = document.get("gran", 0);
            val mines = document.get("mine", 0);
            val flowers = document.get("flower", 0);
            val level = document.get("level", 1);
            val exp = document.get("exp", 0);
            val health = document.get("health", 100);
            val mana = document.get("mana", 10);
            val currentAura = AuraObject.valueOf(document.get("currentAura", "NONE"));
            val currentMount = MountType.valueOf(document.get("currentMount", "HORSE_MOUNT"));
            val currentBloodType = BloodType.valueOf(document.get("bloodType", BloodType.DEFAULT.name()));
            val currentDeathType = DeathType.valueOf(document.get("deathType", DeathType.NONE.name()));
            val location = LocationUtility.deserialize(document.getString("location"), MapServiceProcess.GAME_WORLD);
            val fractionString = document.get("fraction", FractionType.NONE.name());
            val fractionType = FractionType.findByName(fractionString);
            val playerStorageSerialized = document.get("playerStorage", "");
            val globalStorageSerialized = document.get("globalStorage", "");

            val user = new FantasyUser(uuid, level, exp, grans, mines, flowers);
            user.setFractionType(fractionType);
            user.setCurrentBloodType(currentBloodType);
            user.setCurrentDeathType(currentDeathType);
            user.onModificationLoad().clear();
            user.onModificationLoad().add(user1 -> {
                user1.getFantasyInventory().updateAll();
                user1.setHealth(health == 0D ? 1D : health);
                user1.getManaController().setMana(mana == 0D ? 1D : mana);
                user1.setCurrentAura(currentAura);
                currentAura.apply(user1.getSpigot());
                user1.setMountData(new MountData(null, currentMount));
            });
            user.setHealthForce(health);
            user.getManaController().setManaForce(mana);

            for (TimeType value : TimeType.values()) {
                val dataLevel = document.get(value.getFieldName() + "_level", 0);
                val dataDuration = document.get(value.getFieldName() + "_time", 0);
                val dataCorrupt = document.get(value.getFieldName() + "_corrupt", 0);

                user.appendTimeData(new TimeData(value).setLevel(dataLevel).setDuration(dataDuration).setCorrupt(dataCorrupt));
            }

            val globalStorageData = StorageData.deserialize(globalStorageSerialized);
            val playerStorageData = new StorageSerialization(new ItemStack[41])
                    .deserialize(playerStorageSerialized);

            user.getGlobalStorageData().updateAll(globalStorageData);
            user.getGlobalStorageData().validateAll(itemStacks -> new InventoryValidation(itemStacks).validate());

            val validatedPlayerInventory = new InventoryValidation(playerStorageData).validate();
            user.getFantasyInventory().setHelmet(Fantasy.itemProvider().find(validatedPlayerInventory[39]));
            user.getFantasyInventory().setBody(Fantasy.itemProvider().find(validatedPlayerInventory[38]));
            user.getFantasyInventory().setLeggings(Fantasy.itemProvider().find(validatedPlayerInventory[37]));
            user.getFantasyInventory().setBoots(Fantasy.itemProvider().find(validatedPlayerInventory[36]));

            user.getSettingsStorage().findAll().stream()
                    .filter(SavableUserSettings.class::isInstance)
                    .map(SavableUserSettings.class::cast)
                    .forEach(savableUserSettings -> savableUserSettings.readDocument(document));

            val playerLocation = location == null ? BeaconObjects.START.getBeacon().getRespawnLocation() : location;
            var zone = ZoneList.findByLocation(playerLocation);
            val targetLocation = zone != null && !zone.timeless() ? BeaconObjects.START.getBeacon().getRespawnLocation() : playerLocation;

            user.onDataLoad().add(loadedUser -> {
                val player = user.getSpigot();
                if (player == null) return;

                val playerInventory = player.getInventory();
                playerInventory.setContents(validatedPlayerInventory);

                player.teleport(targetLocation);
            });

            return user;
        }).thenApplyAsync(user -> {
            this.data.put(uuid, user);
            return user;
        }).thenComposeAsync(User::loadDataSync);
    }

}