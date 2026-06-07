package cupcake.nikky.imperium.tower.buff.exact.potion;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class PotionSpeed extends FantasyBuff {

    public PotionSpeed(final double value) {
        super(60 * 15, "Увеличение скорости передвижения");
        this.registerPercentAttribute(ImperiumAttribute.MOVEMENT_SPEED, value);
    }

}
