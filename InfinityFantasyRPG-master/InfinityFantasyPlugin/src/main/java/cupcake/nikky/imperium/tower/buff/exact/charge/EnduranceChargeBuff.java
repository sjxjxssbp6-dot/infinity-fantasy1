package cupcake.nikky.imperium.tower.buff.exact.charge;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class EnduranceChargeBuff extends FantasyBuff {

    public EnduranceChargeBuff() {
        super(6, "Заряд выносливости");
        setShouldInvalidateAfterDeath(true);
        this.setMaxMultiplier(3);
        this.setStackable(true);
        this.registerPercentAttribute(ImperiumAttribute.FIRE_RESISTANCE, 4);
        this.registerPercentAttribute(ImperiumAttribute.COLD_RESISTANCE, 4);
        this.registerPercentAttribute(ImperiumAttribute.LIGHTNING_RESISTANCE, 4);
        this.registerPercentAttribute(ImperiumAttribute.DEFENSE, 4);
        this.addAttributeHandler(level -> getAttributes().register(ImperiumAttribute.ENDURANCE_CHARGE, 1));
    }

}
