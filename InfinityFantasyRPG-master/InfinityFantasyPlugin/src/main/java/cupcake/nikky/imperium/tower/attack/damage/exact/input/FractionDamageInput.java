package cupcake.nikky.imperium.tower.attack.damage.exact.input;

import cupcake.nikky.imperium.tower.attack.damage.DamageContainer;
import cupcake.nikky.imperium.tower.attack.damage.DamageFlag;
import cupcake.nikky.imperium.tower.attack.damage.DamageProcess;
import cupcake.nikky.imperium.tower.attack.damage.annotation.InvokeIgnore;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.zone.ZoneList;
import org.jetbrains.annotations.NotNull;

public class FractionDamageInput extends DamageProcess {

    @Override
    @InvokeIgnore
    public void invoke(@NotNull DamageContainer container) {
        container.getAttackInfluenceCallback().ifUserPresent(attacker -> container.getDefender().entityTypeCallback().ifUserPresent(defender -> {
            var attackerZone = attacker.getCurrentZone();
            var defenderZone = defender.getCurrentZone();

            if ((defender.getFractionType() != FractionType.NONE || attacker.getFractionType() != FractionType.NONE) && attacker.getFractionType() == defender.getFractionType()) {
                container.applyDamageFlag(DamageFlag.NOT_AVAILABLE);
                return;
            }

            if ((!defender.findPvpSettings().localPvpState() || !attacker.findPvpSettings().localPvpState()) &&
                    (attackerZone != ZoneList.FRACTION_ZONE && defenderZone != ZoneList.FRACTION_ZONE)) {
                container.applyDamageFlag(DamageFlag.NOT_AVAILABLE);
                return;
            }

            if (!attackerZone.pvp() || !defenderZone.pvp()) {
                container.applyDamageFlag(DamageFlag.NOT_AVAILABLE);
                return;
            }

            if (defenderZone.anySpawnIn(defender.getSpigot().getLocation()) || attackerZone.anySpawnIn(attacker.getSpigot().getLocation())) {
                container.applyDamageFlag(DamageFlag.NOT_AVAILABLE);
            }
        }));
    }

}
