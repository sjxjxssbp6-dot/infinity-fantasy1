package cupcake.nikky.imperium.tower.buff.exact.amplifier;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class DefenseAmplifier extends FantasyBuff {

    public DefenseAmplifier() {
        super(Integer.MAX_VALUE, "Усиление защиты");

        this.registerPercentAttribute(ImperiumAttribute.DEFENSE, 75);
    }

}
