package cupcake.nikky.imperium.tower.item.modifier;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import me.anfanik.steda.api.utility.MathUtility;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class ModifierTier {

    private int tier;
    private int weight;
    private double min;
    private double max;
    private double amplitude;
    private int requireBaseLevel;
    private boolean exalted;
    private boolean canGetViaCraft = true;

    public ModifierTier range(double min, double max) {
        return this.min(min).max(max);
    }

    public double generate() {
        return MathUtility.randomInRange(min, max);
    }

    public ModifierTier copy() {
        return new ModifierTier().exalted(exalted).amplitude(amplitude).canGetViaCraft(canGetViaCraft).tier(tier).weight(weight).min(min).max(max).requireBaseLevel(requireBaseLevel);
    }

    public FragmentModifierTier getFragmentTierIfPresent() {
        if (this instanceof FragmentModifierTier fragmentTier) return fragmentTier;
        return null;
    }

}
