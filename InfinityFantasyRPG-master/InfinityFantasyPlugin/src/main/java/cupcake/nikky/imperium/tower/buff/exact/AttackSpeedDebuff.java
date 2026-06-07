package cupcake.nikky.imperium.tower.buff.exact;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class AttackSpeedDebuff extends FantasyBuff {

    public AttackSpeedDebuff() {
        super(5, "Помутнение эндермита");
        this.registerPercentAttribute(ImperiumAttribute.ATTACK_SPEED, -25);
        this.setShouldInvalidateAfterDeath(true);
    }

}
