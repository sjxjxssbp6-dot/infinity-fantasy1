package cupcake.nikky.imperium.tower.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.anfanik.steda.api.utility.MathUtility;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PotentialUtility {

    public static int processPotential(final int level) {
        var maximumPotential = 3 + Math.min(level, 60) / 2;
        return MathUtility.randomInRange(maximumPotential / 2, maximumPotential);
    }

}
