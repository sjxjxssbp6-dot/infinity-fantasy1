package cupcake.nikky.imperium.tower.buff.exact.potion;

import cupcake.nikky.imperium.tower.buff.exact.regeneration.RegenerationManaInTickBuff;

public class PotionMana extends RegenerationManaInTickBuff {

    public PotionMana(final double percent) {
        super(5, percent);
        this.setShouldInvalidateAfterDeath(true);
    }

}