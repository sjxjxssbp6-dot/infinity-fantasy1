package cupcake.nikky.imperium.tower.entity.monster;

import com.google.common.util.concurrent.AtomicDouble;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.event.AsyncFantasyMonsterDeath;
import cupcake.nikky.imperium.tower.entity.monster.battle.EntityBattleData;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.OutboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.InboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.conduct.ConductType;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterDataHandler;
import cupcake.nikky.imperium.tower.entity.monster.goal.GoalUpdater;
import cupcake.nikky.imperium.tower.entity.monster.goal.exact.MeleeGoalUpdater;
import cupcake.nikky.imperium.tower.entity.monster.loot.other.ExpLoot;
import cupcake.nikky.imperium.tower.entity.monster.modifier.ModifierCollection;
import cupcake.nikky.imperium.tower.entity.monster.modifier.ModifierEngine;
import cupcake.nikky.imperium.tower.entity.monster.updater.MonsterUpdater;
import cupcake.nikky.imperium.tower.entity.monster.target.TargetBarData;
import cupcake.nikky.imperium.tower.entity.monster.tracker.target.MonsterTargetTracker;
import cupcake.nikky.imperium.tower.utility.EntityUtility;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.cristalix.core.datasync.EntityDataParameters;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public abstract class DefaultMonster implements Monster {

    @Getter private final String name;
    private AtomicReference<LivingEntity> entity = new AtomicReference<>();
    @Getter private final EntityType entityType;
    @Getter private final MonsterType monsterType;
    @Getter private final ConductType conduct;
    @Getter private final GoalUpdater goalUpdater;
    @Getter private final int level;
    @Getter private final int respawnTicks;
    @Getter private final Location spawnLocation;
    @Getter private final MonsterData monsterData;
    @Getter private final ItemStack[] armor;
    @Getter private final ItemStack itemInMainHand;
    @Getter private final ItemStack itemInOffHand;

    @Getter private final ModifierEngine modifierEngine = new ModifierEngine(this);
    @Getter private final AttributeMap attributes = new AttributeMap();
    @Getter private final TargetBarData targetBarData = new TargetBarData(this);
    @Getter private final MonsterTargetTracker targetTracker = new MonsterTargetTracker(this);

    private final AtomicBoolean respawn = new AtomicBoolean();
    private final AtomicBoolean ready = new AtomicBoolean();

    private final AtomicReference<ExpLoot> dropExpLoot = new AtomicReference<>();

    private final AtomicInteger tickCounter = new AtomicInteger(0);
    private final AtomicInteger invulnerability = new AtomicInteger(0);

    private final AtomicDouble health = new AtomicDouble();
    private final AtomicDouble auraRadius = new AtomicDouble();
    private final AtomicBoolean shouldDeath = new AtomicBoolean(false);
    private final AtomicReference<DamageContainer> lastDamageContainer = new AtomicReference<>();

    private final Set<Player> attackers = new CopyOnWriteArraySet<>();
    private final Map<UUID, EntityBattleData> battleData = new ConcurrentHashMap<>(16, 0.75F, 2);

    protected final List<InboundDamageCast> inboundDamageCasts = new CopyOnWriteArrayList<>();
    protected final List<OutboundDamageCast> outboundDamageCasts = new CopyOnWriteArrayList<>();

    private final AtomicReference<UUID> monsterUniqueId = new AtomicReference<>(UUID.randomUUID());

    private final AtomicLong updateAttackers = new AtomicLong(Instant.now().toEpochMilli());
    private final AtomicBoolean initArmor = new AtomicBoolean();

    @Override
    public void naturallySpawn(final boolean respawn) {
        if (getSpawnLocation() == null || Fantasy.monsterProvider().respawnTracker().isTracked(this.getMonsterUniqueId()) || this.isExist()) return;

        this.setCanRespawn(respawn);

        this.shouldDeath.set(false);
        this.initArmor.set(false);
        this.setInvulnerability(0);
        this.getTargetTracker().reset();

        if (!getSpawnLocation().getChunk().isLoaded()) {
            getSpawnLocation().getChunk().load(false);
        }

        val entity = (LivingEntity) Fantasy.bukkitProvider().spawn(getSpawnLocation().clone().add(0, 1, 0), getEntityType(), e -> {
            this.setEntity((LivingEntity) e.getBukkitEntity());
            this.getCreature().setUUID(getMonsterUniqueId());

            getGoalUpdater().update(this.getCreature(), this);

            e.getBukkitEntity().setMetadata("fantasy_monster", new FixedMetadataValue(Fantasy.getJavaPlugin(), true));
        });

        val creature = this.getCreature();

        creature.getDataWatcher().set(EntityDataParameters.SCALE_DATA, getMonsterData().getScaleData());
        creature.getDataWatcher().set(EntityDataParameters.COLOR_DATA, getMonsterData().getColorData());
        ((CraftLivingEntity) entity).getHandle().setInvisible(false);

        val map = new MonsterDataHandler(this.getMonsterData()).applyMap();

        this.getAttributes().replace(map);

        entity.setCanPickupItems(false);
        entity.setCustomNameVisible(true);
        entity.setRemoveWhenFarAway(false);
        entity.setInvulnerable(false);
        entity.setNoDamageTicks(0);
        entity.setGravity(true);

        creature.collides = false;

        this.onSpawn();
    }

    @Override
    public void onAsyncTick() {
        val isSecond = tickCounter.getAndAdd(10) >= 20;
        if (isSecond && !initArmor.get()) {
            val entity = this.getSpigot();
            if (entity == null) return;

            Fantasy.runLater(() -> {
                entity.getEquipment().clear();

                entity.getEquipment().setArmorContents(getArmor() == null ? new ItemStack[4] : getArmor());
                entity.getEquipment().setItemInMainHand(this.getItemInMainHand());
                entity.getEquipment().setItemInOffHand(this.getItemInOffHand());
            });

            initArmor.set(true);
        }

        MonsterUpdater.getInstance().update(this, isSecond);
        if (isSecond) tickCounter.set(0);
    }

    @Override
    public void invalidateEntity() {
        invalidateReady();

        this.getAttackers().forEach(this::removeAttacker);
        clearBattleData();

        val spigot = this.getSpigot();
        this.setEntity(null);

        if (spigot != null) spigot.remove();

        Fantasy.combatProvider().invalidateCombat(getMonsterUniqueId());
        MonsterUpdater.getInstance().invalidateMovementCache(getMonsterUniqueId());

        Fantasy.buffService().invalidateEntity(getMonsterUniqueId());
        this.getTargetBarData().unsubscribeAll();

        getModifierEngine().reset();

        this.invalidateFromProviderIfNeed();
    }

    @Override
    public void initAttributes() {
        val maxHealth = this.findAttribute(ImperiumAttribute.HEALTH).findValue();

        val spigot = this.getSpigot();

        spigot.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(16);
        spigot.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(1F);

        this.health.set(maxHealth);
    }

    @Override
    public void initModifiers() {
        Fantasy.buffService().registerBuffedEntity(getMonsterUniqueId(), false);
        this.getModifierEngine().generateModifiers();
        this.getModifierEngine().applyModifiers();

        var multiplier = this.getMonsterData().isUnique() ? .35 : 1;
        getAttributes().find(ImperiumAttribute.AILMENT_THRESHOLD).setBaseValue(this.findAttribute(ImperiumAttribute.HEALTH).findValue() * multiplier);

        val entity = getSpigot();
        entity.setCustomName(this.getNameForPlayer(null));
        entity.setCustomNameVisible(true);
    }

    @Override
    public double onInboundDamage(@NotNull final LivingEntity entity, final double damage) {
        if (!this.isExist() || this.isShouldDeath() || this.isInvulnerability()) return damage;

        if (entity instanceof Player player) {
            if (this.getConduct() == ConductType.NEUTRAL && getTargetTracker().goalTarget() == null || getAttackers().isEmpty()) {
                getTargetTracker().goalTarget(entity);
            }

            this.addAttacker(player);

            EntityBattleData battleData = this.findBattleData(entity.getUniqueId()).orElse(null);

            if (battleData == null) {
                battleData = new EntityBattleData(entity.getUniqueId(), damage);
                this.addBattleData(entity.getUniqueId(), battleData);
            } else {
                this.appendDamageToBattle(entity.getUniqueId(), damage);
            }

            if (this.canReflectInboundDamage() && Instant.now().toEpochMilli() - battleData.getLastReceiveDamage() > getMonsterData().getAttackSpeed() * 50L + 1_500) {
                Fantasy.damageProvider().processDamage(getSpigot(), entity).apply();
                if (getGoalUpdater() instanceof MeleeGoalUpdater) {
                    getCreature().a(EnumHand.MAIN_HAND);
                }
                battleData.setLastReceiveDamage(Instant.now().toEpochMilli());
            }
        }

        val percent = EntityUtility.getHealthPercentage(getHealth() - damage, findAttribute(ImperiumAttribute.HEALTH).findValue());
        for (InboundDamageCast inboundDamageCast : getInboundDamageCasts()) {
            inboundDamageCast.setCurrentHealthPercent(percent);
            if (inboundDamageCast.canApply()) {
                inboundDamageCast.apply();
            }
        }

        return damage;
    }

    @Override
    public double onOutboundDamage(@NotNull final LivingEntity entity, final double damage) {
        if (!this.isExist() || this.isShouldDeath()) return damage;

        if (entity instanceof Player) {
            this.addAttacker((Player) entity);

            EntityBattleData battleData = this.findBattleData(entity.getUniqueId()).orElse(null);

            if (battleData == null) {
                battleData = new EntityBattleData(entity.getUniqueId(), 0D);
                this.addBattleData(entity.getUniqueId(), battleData);
            }

            battleData.setLastReceiveDamage(Instant.now().toEpochMilli());
        }

        val percent = EntityUtility.getHealthPercentage(getHealth() - damage, findAttribute(ImperiumAttribute.HEALTH).findValue());
        for (OutboundDamageCast outboundDamageCast : getOutboundDamageCasts()) {
            outboundDamageCast.setCurrentHealthPercent(percent);
            if (outboundDamageCast.canApply()) {
                outboundDamageCast.apply(entity);
                break;
            }
        }

        return damage;
    }

    @Override
    public void onForgotTarget() {
        if (getMonsterData().isInvalidateWhenForgotTarget()) {
            Fantasy.runLater(this::invalidateEntity);
        }
    }

    @Override
    public void setupReady() {
        ready.set(true);
    }

    @Override
    public void invalidateReady() {
        ready.set(false);
    }

    @Override
    public String getNameForPlayer(@Nullable final UUID uuid) {
        var countAmplifiers = getModifierEngine().getCountAmplifiers();
        String amplifier = "";
        if (countAmplifiers > 0 || countAmplifiers == 0 && getModifierEngine().getCurrentModifiers() != ModifierCollection.NONE) {
            amplifier = " &f" + countAmplifiers + "&r\uE0D2";
        }

        if (uuid == null) return TextUtility.colorize("%s%s%s%n&fУр. %d".formatted(getModifierEngine().getCurrentModifiers().getNameColor(), getName(), amplifier, getLevel()));
        val user = Fantasy.userService().findById(uuid).filter(user1 -> user1.isLoaded() && !user1.isInvalidated()).orElse(null);
        if (user == null) return TextUtility.colorize("%s%s%s%n&fУр. %d".formatted(getModifierEngine().getCurrentModifiers().getNameColor(), getName(), amplifier, getLevel()));

        String levelColor;
        val userAverageArmorLevel = user.getFantasyInventory().getAverageArmorLevel();
        val monsterLevel = getLevel() + getModifierEngine().getCurrentModifiers().getPosition();

        if (userAverageArmorLevel + 1 == monsterLevel) levelColor = "&6";
        else if (userAverageArmorLevel >= monsterLevel) levelColor = "&a";
        else levelColor = "&c";

        return TextUtility.colorize("%s%s%s%n&fУр. %s%d".formatted(getModifierEngine().getCurrentModifiers().getNameColor(), getName(), amplifier, levelColor, getLevel()));
    }

    @Override
    public LivingEntity getSpigot() {
        return this.entity.get();
    }

    @Override
    public EntityInsentient getCreature() {
        return (EntityInsentient) ((CraftLivingEntity) getSpigot()).getHandle();
    }

    @Override
    public boolean isExist() {
        val spigot = getSpigot();
        val uuid = getMonsterUniqueId();

        return ready.get() && spigot != null && uuid != null && spigot.getUniqueId().equals(uuid);
    }

    @Override
    public boolean isCanRespawn() {
      return this.respawn.get();
    }

    @Override
    public void setCanRespawn(final boolean respawn) {
        this.respawn.set(respawn);
    }

    @Override
    public UUID getMonsterUniqueId() {
        return this.monsterUniqueId.get();
    }

    @Override
    public void setEntity(@Nullable final LivingEntity entity) {
        this.entity.set(entity);
    }

    public double getMinimumFullDamage() {
        return findAttribute(ImperiumAttribute.MIN_PHYSICAL_DAMAGE).findValue() +
                findAttribute(ImperiumAttribute.MIN_FIRE_DAMAGE).findValue() +
                findAttribute(ImperiumAttribute.MIN_COLD_DAMAGE).findValue() +
                findAttribute(ImperiumAttribute.MIN_LIGHTNING_DAMAGE).findValue() +
                findAttribute(ImperiumAttribute.MIN_CHAOS_DAMAGE).findValue();
    }

    public double getMaximumFullDamage() {
        return findAttribute(ImperiumAttribute.MAX_PHYSICAL_DAMAGE).findValue() +
                findAttribute(ImperiumAttribute.MAX_FIRE_DAMAGE).findValue() +
                findAttribute(ImperiumAttribute.MAX_COLD_DAMAGE).findValue() +
                findAttribute(ImperiumAttribute.MAX_LIGHTNING_DAMAGE).findValue() +
                findAttribute(ImperiumAttribute.MAX_CHAOS_DAMAGE).findValue();
    }

    @Override
    public List<Player> getAttackers() {
        val lastUpdate = updateAttackers.get();
        val now = Instant.now().toEpochMilli();
        if (now > lastUpdate + 3_000) {
            attackers.removeIf(player -> {
                final long key = this.findBattleData(player.getUniqueId()).map(EntityBattleData::getLastReceiveDamage).orElse(-1L);
                return key == -1L || key + 5_000 < now;
            });
            updateAttackers.set(now);
        }
        return new ArrayList<>(attackers);
    }

    @Override
    public void addAttacker(@NotNull final Player player) {
        this.attackers.add(player);
    }

    @Override
    public void removeAttacker(@NotNull final Player player) {
        this.attackers.remove(player);
    }

    public List<InboundDamageCast> getInboundDamageCasts() {
        return new ArrayList<>(inboundDamageCasts);
    }

    public List<OutboundDamageCast> getOutboundDamageCasts() {
        return new ArrayList<>(outboundDamageCasts);
    }

    @Override
    public int getEntityId() {
        return getSpigot().getEntityId();
    }

    @Override
    public Map<UUID, EntityBattleData> getBattleData() {
        return new HashMap<>(battleData);
    }

    @Override
    public void clearBattleData() {
        battleData.clear();
    }

    @Override
    public Optional<EntityBattleData> findBattleData(@NotNull final UUID uuid) {
        return Optional.ofNullable(battleData.get(uuid));
    }

    @Override
    public void addBattleData(@NotNull final UUID uuid, @NotNull final EntityBattleData data) {
        this.battleData.put(uuid, data);
    }

    @Override
    public void appendDamageToBattle(@NotNull final UUID uuid, final double damage) {
        if (!battleData.containsKey(uuid)) return;
        battleData.get(uuid).changeDamage(damage);
    }

    protected void clearInboundCasts() {
        this.inboundDamageCasts.clear();
    }

    protected void clearOutboundCasts() {
        this.outboundDamageCasts.clear();
    }

    protected void appendCast(@NotNull final InboundDamageCast inboundDamageCast) {
        this.inboundDamageCasts.add(inboundDamageCast);
    }

    protected void appendCast(@NotNull final OutboundDamageCast outboundDamageCast) {
        this.outboundDamageCasts.add(outboundDamageCast);
    }

    @Override
    public double getHealth() {
        return health.get();
    }

    @Override
    public void setHealth(final double health) {
        if (!this.isExist() || this.isShouldDeath()) return;

        this.health.set(health);

        if (this.getHealth() <= 0D) {
            this.shouldDeath.set(true);
            Fantasy.getMonsterDeathExecutor().execute(() -> Bukkit.getPluginManager().callEvent(new AsyncFantasyMonsterDeath(getLastDamageContainer(), this, new HashSet<>(getAttackers()))));
        }
    }

    @Override
    public void changeHealth(final double delta) {
        if (!this.isExist() || this.isShouldDeath()) return;

        for(;;) {
            val oldHealth = this.getHealth();
            val maxHealth = this.findAttribute(ImperiumAttribute.HEALTH).findValue();
            val nextHealth = Math.min(Math.max(oldHealth + delta, 0), maxHealth);

            if (this.health.compareAndSet(oldHealth, nextHealth)) {
                if (nextHealth <= 0D) {
                    this.shouldDeath.set(true);
                    Fantasy.getMonsterDeathExecutor().execute(() -> Bukkit.getPluginManager().callEvent(new AsyncFantasyMonsterDeath(getLastDamageContainer(), this, new HashSet<>(getAttackers()))));
                }
                break;
            }
        }
    }

    @Override
    public void setLastDamageContainer(@Nullable final DamageContainer damageContainer) {
        this.lastDamageContainer.set(damageContainer);
    }

    @Override
    public boolean isShouldDeath() {
        return shouldDeath.get();
    }

    @Override
    public DamageContainer getLastDamageContainer() {
        return lastDamageContainer.get();
    }

    @Override
    public void setInvulnerability(final int ticks) {
        invulnerability.set(Math.max(ticks, 0));
    }

    @Override
    public boolean isInvulnerability() {
        return invulnerability.get() > 0;
    }

    @Override
    public int getInvulnerabilityTicksLeft() {
        return invulnerability.get();
    }

    @Override
    public boolean isAuraDamager() {
        return getAuraRadius() > 0;
    }

    @Override
    public double getAuraRadius() {
        return auraRadius.get();
    }

    @Override
    public void setAuraRadius(double radius) {
        auraRadius.set(radius);
    }

    @Override
    public ExpLoot getDropExpLoot() {
        return dropExpLoot.get();
    }

    public void setDropExpLoot(ExpLoot dropExpLoot) {
        this.dropExpLoot.set(dropExpLoot);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultMonster that = (DefaultMonster) o;
        return Objects.equals(monsterUniqueId.get(), that.monsterUniqueId.get());
    }

    @Override
    public int hashCode() {
        return Objects.hash(monsterUniqueId.get());
    }
}
