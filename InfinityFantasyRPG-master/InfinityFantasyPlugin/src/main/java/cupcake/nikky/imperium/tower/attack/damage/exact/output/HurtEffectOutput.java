package cupcake.nikky.imperium.tower.attack.damage.exact.output;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.BloodType;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.EntityEffect;
import org.bukkit.craftbukkit.v1_12_R1.CraftSound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftLivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HurtEffectOutput extends DamageProcess {

    @Override
    @InvokeIgnore
    public void invoke(@NotNull DamageContainer container) {
        val defender = container.getDefender().getLivingEntity();
        if (defender == null) return;

        val defenderLocation = defender.getLocation();
        var attacker = container.getAttackInfluence();

        var owner = attacker instanceof Player player ? player : defender instanceof Player player2 ? player2 : null;
        if (owner != null) {
            var bloodType = owner.equals(attacker) ? owner.<User>getBungeePlayer().getCurrentBloodType() : BloodType.DEFAULT;
            Fantasy.particleProvider().sendEntityBlood(owner, defender.getEntityId(), bloodType);
        }

        SoundEffect soundEffect = SoundEffects.fF;

        if (attacker != null) {
            var nmsAttacker = ((CraftLivingEntity) attacker).getHandle();
            var damageSource = nmsAttacker instanceof EntityHuman entityHuman ? DamageSource.playerAttack(entityHuman) :
                    nmsAttacker instanceof EntityLiving entityLiving ? DamageSource.mobAttack(entityLiving) : null;

            soundEffect = damageSource == null ? null : ((CraftLivingEntity) defender).getHandle().d(damageSource);

            if (soundEffect == null)
                soundEffect = SoundEffects.fF;
        }

        Fantasy.bukkitProvider().sound(defenderLocation, CraftSound.getSoundByEffect(soundEffect));
        defender.playEffect(EntityEffect.HURT);
    }

}
