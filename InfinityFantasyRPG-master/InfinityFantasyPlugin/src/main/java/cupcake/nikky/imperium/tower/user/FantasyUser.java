package cupcake.nikky.imperium.tower.user;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attribute.AttributeInstance;
import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.MapServiceProcess;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.buff.BuffedEntity;
import cupcake.nikky.imperium.tower.character.CharacterProvider;
import cupcake.nikky.imperium.tower.character.NPCs;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.BloodType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.DeathType;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.currency.UserCurrencyHandler;
import cupcake.nikky.imperium.tower.fraction.FractionProvider;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.aspect.event.AspectHandEvent;
import cupcake.nikky.imperium.tower.mount.MountData;
import cupcake.nikky.imperium.tower.mount.MountType;
import cupcake.nikky.imperium.tower.protocol.board.TimeType;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.protocol.packet.*;
import cupcake.nikky.imperium.tower.quest.requirement.exact.TalkToNPCRequirement;
import cupcake.nikky.imperium.tower.session.Session;
import cupcake.nikky.imperium.tower.storage.StorageData;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import cupcake.nikky.imperium.tower.user.controller.HealthController;
import cupcake.nikky.imperium.tower.user.controller.ManaController;
import cupcake.nikky.imperium.tower.user.inventory.FantasyInventory;
import cupcake.nikky.imperium.tower.user.controller.LevelController;
import cupcake.nikky.imperium.tower.user.metadata.MetadataStorage;
import cupcake.nikky.imperium.tower.user.settings.SettingsStorage;
import cupcake.nikky.imperium.tower.user.settings.UserSettings;
import cupcake.nikky.imperium.tower.user.settings.exact.*;
import cupcake.nikky.imperium.tower.user.target.TargetEntity;
import cupcake.nikky.imperium.tower.user.time.TimeData;
import cupcake.nikky.imperium.tower.utility.AspectUtility;
import cupcake.nikky.imperium.tower.utility.PermissionUtility;
import cupcake.nikky.imperium.tower.zone.Zone;
import cupcake.nikky.imperium.tower.zone.ZoneList;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import net.minecraft.server.v1_12_R1.PacketPlayOutAbilities;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.wada.util.Color;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FantasyUser implements User {

    public static final int ASYNC_TICK = 5;

    @Getter private final UUID uniqueId;
    @Getter private final AttributeMap attributes;

    @Getter private final SettingsStorage settingsStorage;

    @Getter private final HealthController healthController = new HealthController(this);
    @Getter private final ManaController manaController = new ManaController(this);

    @Getter private final FantasyInventory fantasyInventory = new FantasyInventory(this);

    private final AtomicInteger grans = new AtomicInteger(0);
    private final AtomicInteger mines = new AtomicInteger(0);
    private final AtomicInteger flowers = new AtomicInteger(0);

    private final AtomicReference<BeaconObjects> beaconObject = new AtomicReference<>(BeaconObjects.START);
    @Getter private final TargetEntity targetEntity = new TargetEntity();

    private final AtomicLong nextAvailableAttack = new AtomicLong(Instant.now().toEpochMilli());
    private final AtomicReference<DamageContainer> lastDamageContainer = new AtomicReference<>();

    @Getter private final LevelController levelController;
    @Getter private final UserCurrencyHandler currencyHandler;

    @Getter private final JumpData jumpData = new JumpData();

    @Getter private final StorageData globalStorageData = StorageData.deserialize(null);

    private final AtomicReference<FractionType> fractionType = new AtomicReference<>();
    private final AtomicReference<Team> team = new AtomicReference<>();
    private final AtomicReference<Zone> currentZone = new AtomicReference<>();

    private final AtomicReference<AuraObject> currentAura = new AtomicReference<>(AuraObject.NONE);
    private final AtomicReference<BloodType> currentBloodType = new AtomicReference<>(BloodType.DEFAULT);
    private final AtomicReference<DeathType> currentDeathType = new AtomicReference<>(DeathType.NONE);
    private final AtomicReference<MountData> currentMountData = new AtomicReference<>(new MountData(null, MountType.HORSE_MOUNT));

    private final Map<TimeType, TimeData> timeDataMap = new ConcurrentHashMap<>(2, 1F, 1);

    private final AtomicReference<Session> session = new AtomicReference<>();
    @Getter private final MetadataStorage metadataStorage = new MetadataStorage();

    @Getter
    @Accessors(fluent = true, chain = true)
    private final List<Consumer<User>> onModificationLoad = new CopyOnWriteArrayList<>(Collections.singleton(user -> user.setHealth(user.findAttribute(ImperiumAttribute.HEALTH).findValue())));
    @Getter
    @Accessors(fluent = true, chain = true)
    private final List<Consumer<User>> onDataLoad = new CopyOnWriteArrayList<>();

    private final AtomicReference<String[]> lastOverlayText = new AtomicReference<>();

    private final AtomicBoolean invalidated = new AtomicBoolean(false);
    private final AtomicBoolean loaded = new AtomicBoolean(false);

    private final AtomicInteger tick = new AtomicInteger(0);

    public FantasyUser(@NotNull final UUID uniqueId, final int level, final double exp, final int grans, final int mines, final int flowers) {
        this.uniqueId = uniqueId;
        this.levelController = new LevelController(this, level, exp);
        this.currencyHandler = new UserCurrencyHandler(this);

        this.attributes = this.writeDefaults(new AttributeMap());

        final Map<Class<? extends UserSettings>, UserSettings> data = new HashMap<>();

        data.put(NotifySettings.class, new NotifySettings(this));
        data.put(SyncHideSettings.class, new SyncHideSettings(this));
        data.put(FragmentSettings.class, new FragmentSettings(this));
        data.put(IncubatorSettings.class, new IncubatorSettings(this));
        data.put(AchievementSettings.class, new AchievementSettings(this));
        data.put(QuestSettings.class, new QuestSettings(this));
        data.put(SyncCraftBenchSettings.class, new SyncCraftBenchSettings(this));
        data.put(CorpseSettings.class, new CorpseSettings(this));
        data.put(LootFilterSettings.class, new LootFilterSettings(this));
        data.put(TreeSettings.class, new TreeSettings(this));
        data.put(ClientSettings.class, new ClientSettings(this));
        data.put(SyncBattlePassSettings.class, new SyncBattlePassSettings(this));
        data.put(SyncTradeSettings.class, new SyncTradeSettings(this));
        data.put(SyncCaseSettings.class, new SyncCaseSettings(this));
        data.put(PandoraSettings.class, new PandoraSettings(this));
        data.put(StatisticSettings.class, new StatisticSettings(this));
        data.put(DonateSettings.class, new DonateSettings(this));
        data.put(PvpSettings.class, new PvpSettings(this));
        data.put(SyncCalendarSettings.class, new SyncCalendarSettings(this));

        this.settingsStorage = new SettingsStorage(data);

        changeCurrency(CurrencyType.GRAN, grans);
        changeCurrency(CurrencyType.MINES, mines);
        changeCurrency(CurrencyType.FLOWER, flowers);
        setFractionType(FractionType.NONE);

        Fantasy.sessionProvider().createUserSession(this);
    }

    @Override
    public BeaconObjects findNearestBeacon() {
        BeaconObjects beacon;

        val player = getSpigot();
        val questedPlayer = player == null ? null : player.<User>getBungeePlayer().findQuestSettings();

        if (questedPlayer != null) {
            val userLocation = player.getLocation();
            userLocation.setWorld(MapServiceProcess.GAME_WORLD);

            beacon = Arrays.stream(BeaconObjects.values())
                    .filter(beaconObjects -> beaconObjects.getBeaconSettings().handlerQuest().test(questedPlayer) && beaconObjects.getBeaconSettings().handlerUser().test(this))
                    .min(Comparator.comparingDouble(object -> object.getBeacon().getRespawnLocation().distanceSquared(userLocation))).orElse(BeaconObjects.START);
        } else beacon = BeaconObjects.START;

        this.setBeaconObject(beacon);
        return beacon;
    }

    @Override
    public void processRespawn(@NotNull final Consumer<Player> onComplete) {
        val player = getSpigot();
        var beacon = this.getBeaconObject();
        if (player == null) return;

        val fragmentSettings = findFragmentSettings();
        val lastLocation = fragmentSettings.getLastLocation();
        val zone = getCurrentZone();

        if (!zone.timeless() && lastLocation != null || zone == ZoneList.FRACTION_ZONE) {
            player.leaveVehicle();

            val nearSpawn = zone == ZoneList.FRACTION_ZONE ? ZoneList.FRACTION_ZONE.spawnLocation(getFractionType().name()) : zone.nearSpawnLocation(player.getLocation());
            if (nearSpawn != null && (!fragmentSettings.isInFragment() || fragmentSettings.isInFragment() && fragmentSettings.changeRespawnLeft(-1) >= 0)) {
                Fantasy.runLater(() -> {
                    player.setFallDistance(0F);
                    player.teleport(nearSpawn);
                    onComplete.accept(player);

                    if (fragmentSettings.isInFragment())
                        Fantasy.clientProvider().updateFragmentTime(player);
                }, 2);
            } else {
                fragmentSettings.setLastLocation(null);

                Fantasy.runLater(() -> {
                    fragmentSettings.setExitTime(0L);
                    player.setFallDistance(0F);
                    player.teleport(lastLocation);
                    onComplete.accept(player);
                }, 2);
            }
        } else {
            beacon.forceTeleportToRespawn(player, onComplete);
        }
    }

    @Override
    public void onRespawn() {
        this.processRespawn(player -> {
            player.getActivePotionEffects().forEach(potionEffect -> player.removePotionEffect(potionEffect.getType()));
            getHealthController().setShouldDeath(false);
            this.setHealth(this.findAttribute(ImperiumAttribute.HEALTH).findValue());
        });
    }

    @Override
    public void asyncTick() {
        val player = getSpigot();

        if (player == null || player.getGameMode() == GameMode.SPECTATOR) return;
        if (!player.isOnline()) return;
        if (getAttributes() == null) return;

        val second = tick.addAndGet(ASYNC_TICK) >= 20;

        if (second) {
            tick.set(0);
            val currentZone = this.getCurrentZone();
            val location = player.getLocation();

            Arrays.stream(ZoneList.findAll())
                    .filter(zone -> !zone.equals(currentZone))
                    .filter(zone -> zone.isIn(location))
                    .findAny().ifPresent(this::setCurrentZone);

            var zone = getCurrentZone();

            if (currentZone != zone) {
                if (findPvpSettings().localPvpState()) {
                    if (!currentZone.pvp() && zone == ZoneList.FARM_ZONE_WITH_PVP) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&cВы вошли в опасную зону! &6(Выключить вражду можно у Фракционера)");
                    } else if (currentZone == ZoneList.FARM_ZONE_WITH_PVP && !zone.pvp()) {
                        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&aВы вошли в безопасную зону! &6(Выключить вражду можно у Фракционера)");
                    }
                }

                Fantasy.buffService().findById(getUniqueId()).ifPresent(BuffedEntity::processDeath);
            }

            Fantasy.runLater(() -> {
                this.findBattlePassSettings().processQuests();
                player.setPlayerTime(zone.dayTime(), false);
            });

            this.findNearestBeacon();

            val fragmentSettings = this.findFragmentSettings();

            if (zone == ZoneList.REKT_MINE_ZONE && !PermissionUtility.isAttentiveRekt(getUniqueId())) {
                this.setHealth(0D);
                return;
            } else if (player.getGameMode() == GameMode.SURVIVAL && !zone.timeless() && (zone != fragmentSettings.getFragmentZone() || fragmentSettings.getExitTime() < Instant.now().toEpochMilli())) {
                val lastLocation = fragmentSettings.getLastLocation();
                if (lastLocation == null) {
                    if (fragmentSettings.getExitTime() == 0L) {
                        Fantasy.runLater(() -> player.teleport(BeaconObjects.START.getBeacon().getRespawnLocation()));
                    }
                    return;
                }

                fragmentSettings.setLastLocation(null);
                fragmentSettings.setExitTime(0L);
                Fantasy.runLater(() -> player.teleport(lastLocation));
                return;
            }

            if (location.getY() < 80 && player.isInsideVehicle())
                player.leaveVehicle();
        }

        val nextAttributeMap = this.writeDefaults(new AttributeMap(new EnumMap<>(ImperiumAttribute.class)));
        getLevelController().setBattleMultiplier(1D);

        if (getFantasyInventory().isUpdateArmor()) {
            getFantasyInventory().updateArmor();
        }

        if (getFantasyInventory().isUpdateHeldItem()) {
            getFantasyInventory().updateHeldItem();
        }

        this.validateGear(player, nextAttributeMap);

        val treeData = this.findTreeSettings();

        if (treeData != null) {
            treeData.findUpgrades().forEach(treeElement -> {
                if (treeElement == null) return;
                for (TreeElementProcess elementProcess : treeElement.getElementProcess())
                    elementProcess.onAttributeAdjust(nextAttributeMap, getLevel());
            });
        }

        this.validateAspects(nextAttributeMap);
        this.validateBuffs(nextAttributeMap);

        if (player.<User>getBungeePlayer().findDonateSettings().donateData().isSubscribed()) {
            nextAttributeMap.find(ImperiumAttribute.BATTLE_EXPERIENCE_MULTIPLIER).applyAdditional(30);
            nextAttributeMap.find(ImperiumAttribute.INCREASE_FRAGMENT_DURATION).applyAdditional(50);
        }
        getLevelController().changeBattleMultiplier(nextAttributeMap.find(ImperiumAttribute.BATTLE_EXPERIENCE_MULTIPLIER).getValue() / 100D);

        if (second) {
            if (getCurrentAura() != AuraObject.NONE && !getCurrentAura().getFantasyAura().auraRequirement().getAcceptRequirement().test(player)) {
                AuraObject.NONE.apply(player);
            }

            val mountData = getCurrentMountData();
            if (mountData.getMountType() != MountType.HORSE_MOUNT && !mountData.getMountType().getSettings().getMountRequirement().getAcceptRequirement().test(player)) {
                mountData.tryUnmount();
                this.setMountData(new MountData(null, MountType.HORSE_MOUNT));
            }

            val overlay = this.getOverlayText();
            if (!this.isLastOverlaySame(overlay)) {
                this.updateOverlayText(overlay);
                Fantasy.clientProvider().inventoryOverlayText(this, overlay);
            }

            if (FractionProvider.getInstance().findBuffFraction() == this.getFractionType() && this.getFractionType() != FractionType.NONE) {
                if (Fantasy.buffService().findById(getUniqueId()).map(entity -> entity.find(BuffType.FRACTION_BUFF).isEmpty()).orElse(false)) {
                    Fantasy.buffService().appendBuff(getUniqueId(), BuffType.FRACTION_BUFF);
                }
            }
        }

        if (this.findFragmentSettings().isInFragment()) {
            nextAttributeMap.join(this.findFragmentSettings().getFragmentAttributeMap());
        }

        val isInCombat = Fantasy.combatProvider().isInCombat(getUniqueId());
        this.validateHealth(player, isInCombat, nextAttributeMap);
        this.validateMana(nextAttributeMap);

        var packet = !this.getAttributes().equalsPacketAttributes(nextAttributeMap);

        if (nextAttributeMap.find(ImperiumAttribute.CONVERT_ELEMENT_DAMAGE_INTO_PHYS_DAMAGE).getValue() != 0) {
            val minPhysical = nextAttributeMap.find(ImperiumAttribute.MIN_PHYSICAL_DAMAGE);

            minPhysical.applyInstance(nextAttributeMap.find(ImperiumAttribute.MIN_COLD_DAMAGE));
            minPhysical.applyInstance(nextAttributeMap.find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE));
            minPhysical.applyInstance(nextAttributeMap.find(ImperiumAttribute.MIN_FIRE_DAMAGE));

            val maxPhysical = nextAttributeMap.find(ImperiumAttribute.MAX_PHYSICAL_DAMAGE);

            maxPhysical.applyInstance(nextAttributeMap.find(ImperiumAttribute.MAX_COLD_DAMAGE));
            maxPhysical.applyInstance(nextAttributeMap.find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE));
            maxPhysical.applyInstance(nextAttributeMap.find(ImperiumAttribute.MAX_FIRE_DAMAGE));

            nextAttributeMap.find(ImperiumAttribute.MIN_FIRE_DAMAGE).setInstance(new AttributeInstance(0));
            nextAttributeMap.find(ImperiumAttribute.MAX_FIRE_DAMAGE).setInstance(new AttributeInstance(0));
            nextAttributeMap.find(ImperiumAttribute.MIN_COLD_DAMAGE).setInstance(new AttributeInstance(0));
            nextAttributeMap.find(ImperiumAttribute.MAX_COLD_DAMAGE).setInstance(new AttributeInstance(0));
            nextAttributeMap.find(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).setInstance(new AttributeInstance(0));
            nextAttributeMap.find(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).setInstance(new AttributeInstance(0));
        }

        this.getAttributes().replace(nextAttributeMap);
        if (packet) Fantasy.clientProvider().attributeContext(this);

        this.validateSpeed(player);
        getJumpData().increaseTick();

        if (second) {
            if (findDonateSettings().donateData().isSubscribed() && findCalendarSettings().canTake()) {
                var character = CharacterProvider.getInstance().findAll().stream().filter(imperiumCharacter -> imperiumCharacter.getType() == NPCs.CALENDAR_REWARD).findAny().orElse(null);
                if (character != null) {
                    if (character.findLocation().distanceSquared(player.getLocation()) > 4.5 * 4.5) {
                        new ImperiumGlowPacket(player, character.getEntityId()).notice(0);
                    }
                }
            }

            this.findQuestSettings().findAllInProgressQuests().stream()
                    .filter(quest -> quest.isCompleted() ||
                            quest.getRequirements().stream().anyMatch(questRequirement -> questRequirement instanceof TalkToNPCRequirement && !questRequirement.isDone()))
                    .forEach(quest -> {
                        if (quest.isCompleted()) {
                            CharacterProvider.getInstance().findAll().stream().filter(character -> character.getType() == quest.getPassQuestNpc()).forEach(character -> {
                                val characterLocation = character.findLocation();
                                if (!characterLocation.getWorld().equals(player.getWorld())) return;
                                if (characterLocation.distanceSquared(player.getLocation()) <= 4.5 * 4.5) return;

                                new ImperiumGlowPacket(player, character.getEntityId()).notice(0);
                            });
                        } else {
                            val requirements = quest.findRequirementsByClass(TalkToNPCRequirement.class).stream()
                                    .filter(talkToNPCRequirement -> !talkToNPCRequirement.isDone())
                                    .collect(Collectors.toList());
                            CharacterProvider.getInstance().findAll().forEach(character -> {
                                val characterLocation = character.findLocation();
                                if (!characterLocation.getWorld().equals(player.getWorld())) return;
                                if (characterLocation.distanceSquared(player.getLocation()) <= 4.5 * 4.5) return;

                                for (TalkToNPCRequirement requirement : requirements) {
                                    if (requirement.findState(character.getType()) == 0) {
                                        new ImperiumGlowPacket(player, character.getEntityId()).notice(0);
                                    }
                                }
                            });
                        }
                    });
        }
    }

    @Override
    public void validateSpeed(@NotNull final Player player) {
        val craftPlayer = (CraftPlayer) player;
        val oldSpeed = craftPlayer.getHandle().abilities.walkSpeed;

        craftPlayer.getHandle().abilities.walkSpeed = (float) Math.min(Math.max(.2F * (1 + this.findAttribute(ImperiumAttribute.MOVEMENT_SPEED).findValueWithLimit() / 100F), 0.01), 1F) / 2F;

        val speedUpdated = oldSpeed != craftPlayer.getHandle().abilities.walkSpeed;

        if (speedUpdated) {
            val handle = craftPlayer.getHandle();
            val connection = handle.playerConnection;
            if (connection != null) connection.sendPacket(new PacketPlayOutAbilities(handle.abilities));
        }
    }

    private void validateHealth(@NotNull final Player player, boolean isInCombat, @NotNull final AttributeMap attributes) {
        if (attributes.find(ImperiumAttribute.FRAGMENT_CANNOT_REGENERATE_HEALTH_AND_MANA).getValue() > 0) return;
        val maxHealth = attributes.find(ImperiumAttribute.HEALTH).getValue();

        attributes.find(ImperiumAttribute.AILMENT_THRESHOLD).setBaseValue(maxHealth);

        val attribute = isInCombat ? ImperiumAttribute.REGENERATION_HEALTH_IN_FIGHT : ImperiumAttribute.REGENERATION_HEALTH;
        var regeneration = Math.max(attributes.find(attribute).getValue(), 0) / 2D;

        if (player.getLocation().distanceSquared(ZoneList.FARM_ZONE.spawnLocation("default")) <= 40 * 40)
            regeneration *= 10;

        this.changeHealth(regeneration);

        if (this.getHealth() <= maxHealth * .2) {
            Fantasy.clientProvider().vignette(player, .35, Color.RED);
        }
    }

    private void validateMana(@NotNull final AttributeMap attributes) {
        if (attributes.find(ImperiumAttribute.FRAGMENT_CANNOT_REGENERATE_HEALTH_AND_MANA).getValue() > 0) return;
        val regeneration = Math.max(attributes.find(ImperiumAttribute.MANA_REGENERATION).getValue(), 0) / 2D;
        this.getManaController().changeMana(regeneration);
    }

    @Override
    public void validateBuffs(@NotNull final AttributeMap attributes) {
        Fantasy.buffService().findById(getUniqueId()).ifPresent(buffedPlayer -> buffedPlayer.getAll().forEach(buff -> {
            buff.setLevel(getLevel());
            val buffAttributes = buff.getAttributes();
            if (buffAttributes == null || buffAttributes.isEmpty()) return;

            attributes.join(buffAttributes);
        }));
    }

    public void validateGear(@NotNull final Player player, @NotNull final AttributeMap attributes) {
        this.validateItem(getFantasyInventory().getHeldItem(), attributes);

        final ImperiumItem[] armor = new ImperiumItem[]{getFantasyInventory().getHelmet(), getFantasyInventory().getBody(), getFantasyInventory().getLeggings(), getFantasyInventory().getBoots()};
        for (ImperiumItem armorItem : armor) {
            if (armorItem == null || armorItem.getGeneralSettings().requiredLevel() > getLevelController().getLevel()) continue;

            attributes.join(armorItem.getAttributes());
        }
    }

    public void validateItem(@Nullable final ImperiumItem item, @NotNull final AttributeMap attributes) {
        if (item == null ||
                item.getImperiumType() != ItemType.WEAPON &&
                item.getImperiumType() != ItemType.TOOL ||
                item.getGeneralSettings().requiredLevel() > getLevelController().getLevel()) return;

        attributes.join(item.getAttributes());
    }

    private void validateAspects(@NotNull final AttributeMap attributes) {
        AspectUtility.findAspectEvents(this, AspectHandEvent.class).forEach(pairedEvent -> {
            val eventHandler = pairedEvent.getSecond();
            val item = pairedEvent.getFirst();

            val aspect = item.getAspectSettings().aspect();
            val level = item.getAspectSettings().aspectLevel();

            val aspectEvent = AspectHandEvent.create(item, this, attributes, level);
            if (!aspect.aspectFilter(AspectHandEvent.class, aspectEvent)) return;

            if (aspect.costData().getCostProcessType().getProcess().apply(this, attributes, aspect.costData().getCostValue().apply(level)))
                eventHandler.accept(aspectEvent);
        });
    }

    @Override
    public Player getSpigot() {
        return Bukkit.getPlayer(this.getUniqueId());
    }

    private AttributeMap writeDefaults(@NotNull final AttributeMap attributes) {
        val isNeutralResistances = getLevelController().getLevel() < LevelStorage.getInstance().getStartShardEdgeLevel();
        val mana = Math.max(40 + (getLevel() - 12) * 6D, 0);

        return attributes
                .register(ImperiumAttribute.HEALTH, 50D + (12 * (getLevel() - 1)))
                .register(ImperiumAttribute.REGENERATION_HEALTH, 20D)
                .register(ImperiumAttribute.MANA, mana)
                .register(ImperiumAttribute.MANA_REGENERATION, Math.max(2 + mana / 72D, 0))
                .register(ImperiumAttribute.MANA_RESERVATION_EFFICIENCY, 1)
                .register(ImperiumAttribute.MANA_RESERVED_VALUE, 0)
                .register(ImperiumAttribute.ATTACK_SPEED, 100D)
                .register(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS, 1D)
                .register(ImperiumAttribute.CRITICAL_RATE, 5D)
                .register(ImperiumAttribute.CRITICAL_DAMAGE, 50D)
                .register(ImperiumAttribute.DEFENSE_PENETRATION, 0D)
                .register(ImperiumAttribute.ACCURACY, 0D)
                .register(ImperiumAttribute.MASK_ENTITY_SPAWN_CHANCE, 1)
                .register(ImperiumAttribute.FRAGMENT_EVENT_MAXIMUM_AMOUNT, 1)
                .register(ImperiumAttribute.SPELL_DAMAGE, 100)
                .register(ImperiumAttribute.DAMAGE_OVER_TIME, 100)
                .register(ImperiumAttribute.SHOCK_EFFECT, 100)
                .register(ImperiumAttribute.IGNITE_DAMAGE, 50)
                .register(ImperiumAttribute.CHANCE_TO_SHOCK_ON_HIT, 0)
                .register(ImperiumAttribute.CHANCE_TO_IGNITE_ON_HIT, 0)
                .register(ImperiumAttribute.CHANCE_TO_FREEZE_ON_HIT, 0)
                .register(ImperiumAttribute.PANDORA_BASE_ATTEMPTS, 1)
                .register(ImperiumAttribute.PANDORA_CHANCE, 1)
                .register(ImperiumAttribute.PANDORA_HAS_EQUIP, 1)
                .register(ImperiumAttribute.PANDORA_BASE_PRICE, 1)
                .register(ImperiumAttribute.FREEZE_DURATION, 100)
                .register(ImperiumAttribute.AILMENT_DURATION, 100)
                .register(ImperiumAttribute.FIRE_RESISTANCE, isNeutralResistances ? 0 : -15)
                .register(ImperiumAttribute.COLD_RESISTANCE, isNeutralResistances ? 0 : -15)
                .register(ImperiumAttribute.LIGHTNING_RESISTANCE, isNeutralResistances ? 0 : -15)
                .register(ImperiumAttribute.CHAOS_RESISTANCE, isNeutralResistances ? 0 : -15)
                .register(ImperiumAttribute.FIRE_RESISTANCE_MAXIMUM, 75)
                .register(ImperiumAttribute.COLD_RESISTANCE_MAXIMUM, 75)
                .register(ImperiumAttribute.LIGHTNING_RESISTANCE_MAXIMUM, 75)
                .register(ImperiumAttribute.CHAOS_RESISTANCE_MAXIMUM, 75);
    }

    @Override
    public MountData getCurrentMountData() {
        return currentMountData.get();
    }

    @Override
    public void setMountData(@NotNull final MountData data) {
        for (;;) {
            val old = currentMountData.get();
            if (currentMountData.compareAndSet(old, data)) break;
        }
    }

    private boolean isLastOverlaySame(@NotNull final String[] overlay) {
        var last = this.lastOverlayText.get();
        if (last == null || last.length != overlay.length) return false;

        for (int i = 0; i < last.length; i++) {
            if (!overlay[i].equals(last[i])) return false;
        }

        return true;
    }

    private void updateOverlayText(@NotNull final String[] overlay) {
        for (;;) {
            var old = lastOverlayText.get();
            if (lastOverlayText.compareAndSet(old, overlay)) break;
        }
    }

    @Override
    public FractionType getFractionType() {
        return fractionType.get();
    }

    @Override
    public void setFractionType(FractionType type) {
        for (;;) {
            val old = fractionType.get();
            if (fractionType.compareAndSet(old, type)) break;
        }

        if (team.get() != null) team.get().setColor(type.getNameColor());
    }

    @Override
    public boolean isLoaded() {
        return loaded.get();
    }

    @Override
    public void setLoaded(boolean loaded) {
        for (;;) {
            val old = this.loaded.get();
            if (this.loaded.compareAndSet(old, loaded)) break;
        }
    }

    @Override
    public AuraObject getCurrentAura() {
        return currentAura.get();
    }

    @Override
    public void setCurrentAura(@Nullable final AuraObject auraObject) {
        for (;;) {
            val old = currentAura.get();
            if (this.currentAura.compareAndSet(old, auraObject)) break;
        }
    }

    @Override
    public int getMines() {
        return mines.get();
    }

    @Override
    public void changeMines(int delta) {
        for (;;) {
            val old = mines.get();
            if (this.mines.compareAndSet(old, old + delta)) break;
        }
    }

    @Override
    public void setMines(int value) {
        for (;;) {
            val old = mines.get();
            if (this.mines.compareAndSet(old, value)) break;
        }
    }

    @Override
    public BloodType getCurrentBloodType() {
        return currentBloodType.get();
    }

    @Override
    public void setCurrentBloodType(@Nullable BloodType bloodType) {
        if (bloodType == null)
            bloodType = BloodType.DEFAULT;

        for (;;) {
            var old = getCurrentBloodType();
            if (currentBloodType.compareAndSet(old, bloodType)) break;
        }
    }

    @Override
    public DeathType getCurrentDeathType() {
        return currentDeathType.get();
    }

    @Override
    public void setCurrentDeathType(@Nullable DeathType deathType) {
        if (deathType == null)
            deathType = DeathType.NONE;

        for (;;) {
            var old = getCurrentDeathType();
            if (currentDeathType.compareAndSet(old, deathType)) break;
        }
    }

    @Override
    public int getFlowers() {
        return flowers.get();
    }

    @Override
    public void changeFlowers(int delta) {
        for (;;) {
            val old = flowers.get();
            if (this.flowers.compareAndSet(old, old + delta)) break;
        }
    }

    @Override
    public void setFlowers(int value) {
        for (;;) {
            val old = flowers.get();
            if (this.flowers.compareAndSet(old, value)) break;
        }
    }

    @Override
    public int getGrans() {
        return grans.get();
    }

    @Override
    public void changeGrans(final int delta) {
        for (;;) {
            val old = grans.get();
            if (this.grans.compareAndSet(old, old + delta)) break;
        }
    }

    @Override
    public void setGrans(int value) {
        for (;;) {
            val old = grans.get();
            if (this.grans.compareAndSet(old, value)) break;
        }
    }

    @Override
    public TimeData findTimeData(@NotNull final TimeType timeType) {
        return this.timeDataMap.get(timeType);
    }

    @Override
    public void appendTimeData(TimeData timeData) {
        this.timeDataMap.put(timeData.getTimeType(), timeData);
    }

    @Override
    public Session getSession() {
        return session.get();
    }

    @Override
    public void setSession(@NotNull final Session session) {
        for (;;) {
            val old = this.session.get();
            if (this.session.compareAndSet(old, session)) break;
        }
    }

    @Override
    public void setTeam(Team team) {
        for (;;) {
            val old = this.team.get();
            if (this.team.compareAndSet(old, team)) break;
        }
    }

    @Override
    public void setBeaconObject(BeaconObjects beaconObject) {
        for (;;) {
            val old = this.beaconObject.get();
            if (this.beaconObject.compareAndSet(old, beaconObject)) break;
        }
    }

    @Override
    public BeaconObjects getBeaconObject() {
        return beaconObject.get();
    }

    @Override
    public boolean isInvalidated() {
        return invalidated.get();
    }

    @Override
    public void setInvalidated(boolean invalidated) {
        for (;;) {
            val old = this.invalidated.get();
            if (this.invalidated.compareAndSet(old, invalidated)) break;
        }
    }

    @Override
    public Zone getCurrentZone() {
        return currentZone.get();
    }

    @Override
    public void setCurrentZone(Zone zone) {
        for (;;) {
            val old = this.currentZone.get();
            if (this.currentZone.compareAndSet(old, zone)) break;
        }
    }

    @Override
    public DamageContainer getLastDamageContainer() {
        return lastDamageContainer.get();
    }

    @Override
    public void setLastDamageContainer(DamageContainer damageContainer) {
        for (;;) {
            val old = this.lastDamageContainer.get();
            if (this.lastDamageContainer.compareAndSet(old, damageContainer)) break;
        }
    }

    @Override
    public boolean isCanAttack() {
        return nextAvailableAttack.get() <= Instant.now().toEpochMilli();
    }

    @Override
    public void setNextAvailableAttack(long millis) {
        for (;;) {
            val old = this.nextAvailableAttack.get();
            if (this.nextAvailableAttack.compareAndSet(old, millis)) break;
        }
        Fantasy.clientProvider().showAttackSpeed(this);
    }

    @Override
    public long getNextAvailableAttack() {
        return nextAvailableAttack.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FantasyUser user = (FantasyUser) o;
        return Objects.equals(uniqueId, user.uniqueId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId);
    }
}
