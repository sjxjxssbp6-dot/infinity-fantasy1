package cupcake.nikky.imperium.tower.buff.exact.charge;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class PowerChargeBuff extends FantasyBuff {

    public PowerChargeBuff() {
        super(6, "Заряд энергии");
        setShouldInvalidateAfterDeath(true);
        this.setMaxMultiplier(3);
        this.setStackable(true);
        this.registerPercentAttribute(ImperiumAttribute.CRITICAL_RATE, 3);
        this.addAttributeHandler(level -> getAttributes().register(ImperiumAttribute.POWER_CHARGE, 1));
    }

}
