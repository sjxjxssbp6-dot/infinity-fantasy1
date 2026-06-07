package cupcake.nikky.imperium.tower.buff.exact.amplifier;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class EdgeProtection extends FantasyBuff {

    public EdgeProtection() {
        super(Integer.MAX_VALUE, "Покровительство Грани");

        this.registerPercentAttribute(ImperiumAttribute.LEECH_PERCENT, 50);
        this.registerPercentAttribute(ImperiumAttribute.MOVEMENT_SPEED, 50);
    }

}
