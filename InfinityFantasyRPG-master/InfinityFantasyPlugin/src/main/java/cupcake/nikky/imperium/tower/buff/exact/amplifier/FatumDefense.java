package cupcake.nikky.imperium.tower.buff.exact.amplifier;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class FatumDefense extends FantasyBuff {

    public FatumDefense() {
        super(Integer.MAX_VALUE, "Защита Фатума");

        this.registerPercentAttribute(ImperiumAttribute.FIRE_RESISTANCE, 50);
        this.registerPercentAttribute(ImperiumAttribute.COLD_RESISTANCE, 50);
        this.registerPercentAttribute(ImperiumAttribute.LIGHTNING_RESISTANCE, 50);
        this.registerPercentAttribute(ImperiumAttribute.CHAOS_RESISTANCE, 30);
        this.registerPercentAttribute(ImperiumAttribute.DEFENSE, 20);
    }

}