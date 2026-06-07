package cupcake.nikky.imperium.tower.buff.exact.charge;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class FrenzyChargeBuff extends FantasyBuff {

    public FrenzyChargeBuff() {
        super(6, "Заряд выносливости");
        setShouldInvalidateAfterDeath(true);
        this.setMaxMultiplier(3);
        this.setStackable(true);
        this.registerPercentAttribute(ImperiumAttribute.ATTACK_SPEED, 4);
        this.registerPercentAttribute(ImperiumAttribute.MORE_DAMAGE, 4);

        this.addAttributeHandler(level -> getAttributes().register(ImperiumAttribute.FRENZY_CHARGE, 1));
    }

}

