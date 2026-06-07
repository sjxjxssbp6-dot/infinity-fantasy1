package cupcake.nikky.imperium.tower.buff.exact.amplifier;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class SpeedAmplifier extends FantasyBuff {

    public SpeedAmplifier() {
        super(Integer.MAX_VALUE, "Ускорение");
        this.registerPercentAttribute(ImperiumAttribute.MOVEMENT_SPEED, 50);
        this.registerPercentAttribute(ImperiumAttribute.ATTACK_SPEED, 30);
    }

}
