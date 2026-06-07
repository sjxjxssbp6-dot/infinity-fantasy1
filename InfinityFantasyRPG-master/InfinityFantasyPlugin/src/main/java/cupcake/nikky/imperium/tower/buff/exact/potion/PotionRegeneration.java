package cupcake.nikky.imperium.tower.buff.exact.potion;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;

public class PotionRegeneration extends FantasyBuff {

    public PotionRegeneration(final double valueDefault, final double valueInFight) {
        super(60, "Увеличение регенерации здоровья");

        this.registerValueAttribute(ImperiumAttribute.REGENERATION_HEALTH, valueDefault);
        this.registerValueAttribute(ImperiumAttribute.REGENERATION_HEALTH_IN_FIGHT, valueInFight);
    }

}
