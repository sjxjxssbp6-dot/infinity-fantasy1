package cupcake.nikky.imperium.tower.buff.exact.amplifier;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class DefensePenetrationAmplifier extends FantasyBuff {

    public DefensePenetrationAmplifier() {
        super(Integer.MAX_VALUE, "Усиление пробития защиты");
        this.registerPercentAttribute(ImperiumAttribute.DEFENSE_PENETRATION, 50);
    }

}
