package cupcake.nikky.imperium.tower.buff.exact;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class SlownessDebuff extends FantasyBuff {

    public SlownessDebuff(final double value, final int duration) {
        super(duration, "Замедление");

        this.registerPercentAttribute(ImperiumAttribute.MOVEMENT_SPEED, -value);
        this.setShouldInvalidateAfterDeath(true);
    }

}
