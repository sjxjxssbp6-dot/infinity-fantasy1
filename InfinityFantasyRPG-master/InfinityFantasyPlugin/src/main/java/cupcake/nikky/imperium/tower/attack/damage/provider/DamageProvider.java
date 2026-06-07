package cupcake.nikky.imperium.tower.attack.damage.provider;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageFlag;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.attack.damage.exact.input.*;
import cupcake.nikky.imperium.tower.attack.damage.exact.output.*;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.utility.NbtUtility;
import lombok.val;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DamageProvider {

    private final DamageProcess[] handlers = new DamageProcess[] {
            new InvulnerabilityDamageInput(),
            new SessionDamageInput(),
            new FractionDamageInput(),
            new EvasionProcess(),
            new AnyDamageInput(),
            new TreeModifierInput(),
            new FragmentInputDamage(),
            new SpellDamageInput(),
            new OverTimeDamageInput(),
            new ChargeDamageInput(),
            new CriticalDamageInput(),
            new DefenseOutput(),
            new ResistanceDamageOutput(),
            new AilmentsDamageOutput(),
            new HurtEffectOutput(),
            new KnockBackOutput(),
            new ApplyingDamageProcess()
    };

    private final Map<Class<? extends DamageProcess>, InvokeIgnore> invokes = new HashMap<>();

    public DamageProvider() {
        for (DamageProcess handler : handlers) {
            this.findAnnotation(handler);
        }
    }

    public void process(@NotNull final DamageContainer damageContainer) {
        for (DamageProcess handler : handlers) {
            val invokeIgnore = this.findAnnotation(handler);

            if (invokeIgnore != null) {
                if (invokeIgnore.ifEvasion() && damageContainer.containsDamageFlag(DamageFlag.EVASION)) continue;
                if (invokeIgnore.ifNotAvailable() && damageContainer.containsDamageFlag(DamageFlag.NOT_AVAILABLE)) continue;
                if (invokeIgnore.ifIgnoreKnockBack() && damageContainer.containsDamageFlag(DamageFlag.IGNORE_KNOCK_BACK)) continue;
                if (invokeIgnore.ifInvulnerability() && damageContainer.containsDamageFlag(DamageFlag.INVULNERABILITY)) continue;
            }

            if (!damageContainer.getDefender().entityTypeCallback().isPresent()) return;

            handler.invoke(damageContainer);
        }
    }

    private InvokeIgnore findAnnotation(@NotNull final DamageProcess damageProcess) {
        return this.invokes.computeIfAbsent(damageProcess.getClass(),
                clazz -> Arrays.stream(clazz.getDeclaredMethods())
                        .filter(method -> method.getName().equals("invoke") && method.isAnnotationPresent(InvokeIgnore.class))
                        .map(method -> method.getAnnotation(InvokeIgnore.class))
                        .findFirst().orElse(null));
    }

    public DamageSubProcess processDamage(@NotNull final Entity defender) {
        return this.processDamage(null, defender);
    }

    public DamageSubProcess processDamage(@Nullable final Entity attacker, @NotNull final Entity defender) {
        val damageContainer = Fantasy.damageProvider().toDamageContainer(attacker, defender);
        return DamageSubProcess.of(damageContainer);
    }

    public DamageContainer toDamageContainer(@Nullable final Entity attacker, @NotNull final Entity defender) {
        if (attacker instanceof Projectile && !(defender instanceof Player)) return null;
        if (Fantasy.userService().findById(defender.getUniqueId()).map(user -> user.isInvalidated() || !user.isLoaded()).orElse(defender instanceof Player)) return null;

        val entityUser = this.findUser(defender);
        val entityMonster = this.findMonster(defender);

        Optional<Monster> damagerMonster;
        Optional<User> damagerUser;

        ImperiumItem itemInHand = null;

        if (attacker instanceof Projectile projectile) {
            damagerMonster = this.projectileToMonster(projectile);
            damagerUser = this.projectileToUser(projectile);

            if (attacker.hasMetadata("itemInHand")) {
                val data = attacker.getMetadata("itemInHand");
                if (!data.isEmpty()) {
                    itemInHand = Fantasy.itemProvider().find(data.getFirst().asString());
                }

                attacker.removeMetadata("itemInHand", Fantasy.getJavaPlugin());
            }

            attacker.remove();
        } else {
            damagerMonster = this.findMonster(attacker);
            damagerUser = this.findUser(attacker);
        }

        DamageContainer damageContainer = null;

        if (entityUser.isPresent() && damagerMonster.isPresent()) {
            damageContainer = new DamageContainer(damagerMonster.get(), entityUser.get());
        } else if (damagerUser.isPresent() && entityMonster.isPresent()) {
            damageContainer = new DamageContainer(damagerUser.get(), entityMonster.get());
        } else if (damagerUser.isPresent() && entityUser.isPresent()) {
            damageContainer = new DamageContainer(damagerUser.get(), entityUser.get());
        } else if (entityUser.isPresent()) {
            damageContainer = new DamageContainer(entityUser.get());
        } else if (entityMonster.isPresent()){
            damageContainer = new DamageContainer(entityMonster.get());
        }

        if (damageContainer != null && itemInHand != null) {
            damageContainer.getAttacker().setImperiumItemInHand(itemInHand);
            damageContainer.getAttacker().setItemInHand(NbtUtility.clearSessionTags(itemInHand.createNew()));
        }

        return damageContainer;
    }

    private Optional<User> projectileToUser(@NotNull final Projectile projectile) {
        if (projectile.getShooter() instanceof Player) {
            return this.findUser((Player) projectile.getShooter());
        } else return Optional.empty();
    }

    private Optional<Monster> projectileToMonster(@NotNull final Projectile projectile) {
        if (projectile.getShooter() instanceof LivingEntity) {
            return this.findMonster((LivingEntity) projectile.getShooter());
        } else return Optional.empty();
    }

    private Optional<User> findUser(@Nullable final Entity entity) {
        if (entity == null) return Optional.empty();
        return Fantasy.userService().findById(entity.getUniqueId());
    }

    private Optional<Monster> findMonster(@Nullable final Entity entity) {
        if (entity == null) return Optional.empty();
        return Fantasy.monsterProvider().findByUUID(entity.getUniqueId());
    }

}
