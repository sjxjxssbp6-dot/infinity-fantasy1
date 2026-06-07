package cupcake.nikky.imperium.tower.entity.monster;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.battle.EntityBattleData;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.OutboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.damage.cast.InboundDamageCast;
import cupcake.nikky.imperium.tower.entity.monster.conduct.ConductType;
import cupcake.nikky.imperium.tower.entity.monster.damage.InboundDamageProcess;
import cupcake.nikky.imperium.tower.entity.monster.damage.OutboundDamageProcess;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.entity.monster.goal.GoalUpdater;
import cupcake.nikky.imperium.tower.entity.monster.loot.other.ExpLoot;
import cupcake.nikky.imperium.tower.entity.Attributable;
import cupcake.nikky.imperium.tower.entity.monster.modifier.ModifierEngine;
import cupcake.nikky.imperium.tower.entity.monster.target.TargetBarData;
import cupcake.nikky.imperium.tower.entity.monster.tracker.target.MonsterTargetTracker;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import me.anfanik.steda.api.utility.MathUtility;
import net.minecraft.server.v1_12_R1.EntityInsentient;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftSound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public interface Monster extends Attributable, OutboundDamageProcess, InboundDamageProcess {

    String getName();

    UUID getMonsterUniqueId();

    TargetBarData getTargetBarData();

    GoalUpdater getGoalUpdater();

    int getEntityId();

    List<Player> getAttackers();

    default void onDamageWhileInvulnerability(LivingEntity livingEntity) {

    }

    void setInvulnerability(int ticks);

    int getInvulnerabilityTicksLeft();

    boolean isInvulnerability();

    void setAuraRadius(final double radius);

    double getAuraRadius();

    boolean isAuraDamager();

    void addAttacker(Player player);

    void removeAttacker(Player player);

    boolean isShouldDeath();

    default boolean canReflectInboundDamage() {
        return !this.isInvulnerability();
    }

    ConductType getConduct();

    MonsterType getMonsterType();

    MonsterData getMonsterData();

    ModifierEngine getModifierEngine();

    ExpLoot getDropExpLoot();

    void setDropExpLoot(ExpLoot expLoot);

    ItemStack[] getArmor();

    ItemStack getItemInMainHand();

    ItemStack getItemInOffHand();

    EntityType getEntityType();

    MonsterTargetTracker getTargetTracker();

    int getRespawnTicks();

    Location getSpawnLocation();

    LivingEntity getSpigot();

    EntityInsentient getCreature();

    List<OutboundDamageCast> getOutboundDamageCasts();

    List<InboundDamageCast> getInboundDamageCasts();

    double getHealth();

    void setHealth(double health);

    void changeHealth(double delta);

    DamageContainer getLastDamageContainer();

    void setLastDamageContainer(DamageContainer damageContainer);

    boolean isExist();

    void naturallySpawn(boolean respawn);

    void invalidateEntity();

    void onAsyncTick();

    default void onSpawn() {
        this.initModifiers();
        this.initAttributes();

        this.setupReady();
    }

    default void onDeath(LivingEntity killer) {
        var spigot = getSpigot();
        var deathSoundEffect = getCreature().getDeathSoundEffect();

        Fantasy.runLater(() -> {
            if (deathSoundEffect != null)
                Fantasy.bukkitProvider().sound(spigot.getLocation(), CraftSound.getSoundByEffect(deathSoundEffect));
            this.invalidateEntity();
        });

        if (!this.invalidateFromProviderIfNeed())
            Fantasy.monsterProvider().respawnTracker().register(this);

        if (killer instanceof Player player && spigot != null) {
            var location = spigot.getLocation();
            var user = player.<User>getBungeePlayer();

            if (user.findFragmentSettings().isInFragment()) {
                var fragmentSettings = user.findFragmentSettings();
                var fragmentEventStarted = fragmentSettings.getFragmentEventStarted();
                if (fragmentEventStarted >= user.findAttribute(ImperiumAttribute.FRAGMENT_EVENT_MAXIMUM_AMOUNT).findValue()) return;

                if (MathUtility.randomInRange(0D, 100D) <= user.findAttribute(ImperiumAttribute.MASK_ENTITY_SPAWN_CHANCE).findValue()) {
                    val maskLevel = (int) user.findAttribute(ImperiumAttribute.ADDITIONAL_MASK_DROP).findValue();

                    var data = MonsterType.MASK_ENTITY.getMonsterData().copy();
                    data.setLevel(getLevel() + 1 + maskLevel);
                    var instance = Fantasy.monsterProvider().createInstance(MonsterType.MASK_ENTITY, data, location);
                    if (instance instanceof Sessional sessional)
                        sessional.setSession(user.getSession());

                    fragmentSettings.setFragmentEventStarted(fragmentSettings.getFragmentEventStarted() + 1);

                    Fantasy.runLater(() -> {
                        instance.naturallySpawn(false);
                        instance.getTargetTracker().goalTarget(killer);
                    });
                } else if (MathUtility.randomInRange(0D, 100D) <= user.findAttribute(ImperiumAttribute.PANDORA_CHANCE).findValue()) {
                    var pandoraLocation = spigot.getLocation().toBlockLocation();
                    pandoraLocation.setYaw(0F);
                    pandoraLocation.setPitch(0F);
                    user.findPandoraSettings().createPandora(pandoraLocation, this);
                    fragmentSettings.setFragmentEventStarted(fragmentSettings.getFragmentEventStarted() + 1);
                }
            }
        }
    }

    default boolean invalidateFromProviderIfNeed() {
        if (!this.isCanRespawn() || this.getRespawnTicks() <= -1) {
            Fantasy.monsterProvider().invalidate(getMonsterUniqueId());
            return true;
        }
        return false;
    }

    void onForgotTarget();

    void initAttributes();

    void initModifiers();

    String getNameForPlayer(UUID uuid);

    void setupReady();

    void invalidateReady();

    void setEntity(LivingEntity entity);

    boolean isCanRespawn();

    void setCanRespawn(boolean respawn);

    Map<UUID, EntityBattleData> getBattleData();

    Optional<EntityBattleData> findBattleData(UUID uuid);

    void clearBattleData();

    void addBattleData(UUID uuid, EntityBattleData data);

    void appendDamageToBattle(UUID uuid, double damage);

}
