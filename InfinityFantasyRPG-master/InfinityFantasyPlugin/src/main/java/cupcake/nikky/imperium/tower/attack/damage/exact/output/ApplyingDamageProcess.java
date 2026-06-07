package cupcake.nikky.imperium.tower.attack.damage.exact.output;

import com.google.common.util.concurrent.AtomicDouble;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.*;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsType;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.indicator.DamageIndicatorSettings;
import cupcake.nikky.imperium.tower.protocol.packet.ImperiumTeleportContextPacket;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.Color;

import java.util.ArrayList;

public class ApplyingDamageProcess extends DamageProcess {

    @Override
    @InvokeIgnore(ifEvasion = false, ifInvulnerability = false)
    public void invoke(@NotNull DamageContainer container) {
        val attacker = container.getAttacker().getLivingEntity();
        val defender = container.getDefender().getLivingEntity();

        val isCritical = container.containsDamageFlag(DamageFlag.CRITICAL);
        val isEvasion = container.containsDamageFlag(DamageFlag.EVASION);
        val isInvulnerability = container.containsDamageFlag(DamageFlag.INVULNERABILITY);

        if (defender == null) return;

        container.getDefender().entityTypeCallback().ifUserPresent(user -> Fantasy.clientProvider().vignette((Player) defender, .1, Color.RED));

        AtomicDouble totalDamage = new AtomicDouble(container.findTotalDamage());

        if (!isEvasion && !isInvulnerability) {
            container.getAttackInfluenceCallback().ifAttributablePresent(attributable -> {
                if (attacker != null) {
                    val physReflect = attributable.findAttribute(ImperiumAttribute.FRAGMENT_REFLECT_PHYSICAL_DAMAGE).findValue();
                    val elemReflect = attributable.findAttribute(ImperiumAttribute.FRAGMENT_REFLECT_ELEMENTAL_DAMAGE).findValue();

                    if (physReflect > 0D) {
                        val physDamage = physReflect / 100D * container.findDamageByInfluence(InfluenceType.PHYSICAL);
                        Fantasy.getAttackExecutor().execute(() -> Fantasy.damageProvider().processDamage(attacker)
                                .setAttackInfluence(defender)
                                .setDamage(InfluenceType.PHYSICAL, physDamage)
                                .apply());
                    }

                    if (elemReflect > 0D) {
                        val fireDamage = elemReflect / 100D * container.findDamageByInfluence(InfluenceType.FIRE);
                        val coldDamage = elemReflect / 100D * container.findDamageByInfluence(InfluenceType.COLD);
                        val lightningDamage = elemReflect / 100D * container.findDamageByInfluence(InfluenceType.LIGHTNING);

                        Fantasy.getAttackExecutor().execute(() -> Fantasy.damageProvider().processDamage(attacker)
                                .setAttackInfluence(defender)
                                .setDamage(InfluenceType.FIRE, fireDamage)
                                .setDamage(InfluenceType.COLD, coldDamage)
                                .setDamage(InfluenceType.LIGHTNING, lightningDamage)
                                .apply());
                    }
                }

                if (container.getDamageType() != DamageType.HIT) return;

                val leech = totalDamage.get() * (attributable.findAttribute(ImperiumAttribute.LEECH_PERCENT).findValue() / 100D);
                if (leech <= 0D) return;

                if (attributable instanceof User user) {
                    user.changeHealth(leech);
                } else {
                    ((Monster) attributable).changeHealth(leech);
                }

                totalDamage.addAndGet(-leech);
            });

            container.getDefender().entityTypeCallback().ifMonsterPresent(monster -> {
                totalDamage.set(attacker == null ? totalDamage.get() : monster.onInboundDamage(attacker, totalDamage.get()));
                if (container.getAttackInfluence() != null) monster.setLastDamageContainer(container);
                monster.changeHealth(-totalDamage.get());

                container.getAttackInfluenceCallback().ifUserPresent(user -> monster.getTargetBarData().subscribe(user.getUniqueId()));
            }).ifUserPresent(user -> {
                container.getAttackInfluenceCallback().ifMonsterPresent(monster -> totalDamage.set(monster.onOutboundDamage(defender, totalDamage.get())));

                if (container.getAttackInfluenceCallback().isPresent())
                    user.setLastDamageContainer(container);
                user.changeHealth(-totalDamage.get());

                container.getAttackInfluenceCallback().ifMonsterPresent(monster -> monster.getTargetBarData().subscribe(user.getUniqueId()));
                if (defender instanceof Player player && defender.hasMetadata("teleport_process")) {
                    defender.removeMetadata("teleport_process", Fantasy.getJavaPlugin());
                    new ImperiumTeleportContextPacket(player).closeContext();
                }
            });
        } else if (isEvasion) {
            container.getDefender().entityTypeCallback().ifMonsterPresent(monster -> {
                if (attacker != null)
                    monster.onInboundDamage(attacker, 0);
                if (container.getAttackInfluence() != null)
                    monster.setLastDamageContainer(container);

                container.getAttackInfluenceCallback().ifUserPresent(user -> monster.getTargetBarData().subscribe(user.getUniqueId()));
            });
        }

        if (isInvulnerability) {
            container.getDefender().entityTypeCallback().ifMonsterPresent(monster -> monster.onDamageWhileInvulnerability(attacker));
        }

        container.getDefender().entityTypeCallback().ifAttributablePresent(attributable -> {
            val defenderLocation = defender.getLocation();
            val attackInfluence = container.getAttackInfluence();
            val playersToShowIndicator = new ArrayList<>(Bukkit.getOnlinePlayers())
                    .stream()
                    .filter(player -> {
                        if (attackInfluence == null) return true;
                        var user = player.<User>getBungeePlayer();
                        if (user == null || user.isInvalidated() || !user.isLoaded()) return false;

                        if (user.getUniqueId().equals(attackInfluence.getUniqueId())) {
                            return user.findClientSettings().find(SettingsType.DAMAGE_INDICATOR_OWN_VISIBILITY);
                        } else {
                            return user.findClientSettings().find(SettingsType.DAMAGE_INDICATOR_OTHER_VISIBILITY);
                        }
                    })
                    .filter(player -> player.getLocation().distanceSquared(defenderLocation) < 8D * 8D)
                    .toList();

            if (attackInfluence instanceof Player) {
                Fantasy.newDamageIndicator(defender, DamageIndicatorSettings.builder()
                        .damage(totalDamage.get())
                        .critical(isCritical)
                        .evasion(isEvasion)
                        .invulnerability(isInvulnerability)
                        .build()).spawn(playersToShowIndicator);
            }
        });

        if (attacker != null)
            Fantasy.combatProvider().registerCombat(attacker.getUniqueId(), defender.getUniqueId());
    }

}
