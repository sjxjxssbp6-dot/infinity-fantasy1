package cupcake.nikky.imperium.tower.buff.exact.amplifier;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class EdgeDefense extends FantasyBuff {

    public EdgeDefense() {
        super(Integer.MAX_VALUE, "Защита Грани");

        this.registerPercentAttribute(ImperiumAttribute.FIRE_RESISTANCE, 75);
        this.registerPercentAttribute(ImperiumAttribute.COLD_RESISTANCE, 75);
        this.registerPercentAttribute(ImperiumAttribute.LIGHTNING_RESISTANCE, 75);
        this.registerPercentAttribute(ImperiumAttribute.CHAOS_RESISTANCE, 50);
        this.registerPercentAttribute(ImperiumAttribute.DEFENSE, 35);
    }

}
