package cupcake.nikky.imperium.tower.attribute.process;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import org.jetbrains.annotations.NotNull;

public final class AttributeConverter {

    private final ImperiumAttribute attribute;
    private final double value;
    private final int fireResMax;
    private final int coldResMax;
    private final int lightningResMax;
    private final int chaosResMax;

    public AttributeConverter(@NotNull final ImperiumAttribute attribute,
                              final double value, final int fireResMax, final int coldResMax, final int lightningResMax, final int chaosResMax) {
        this.attribute = attribute;
        this.value = value;
        this.fireResMax = fireResMax;
        this.coldResMax = coldResMax;
        this.lightningResMax = lightningResMax;
        this.chaosResMax = chaosResMax;
    }

    public double overLimit() {
        return defaultValue() - valueWithLimit();
    }

    public double defaultValue() {
        return value;
    }

    public double valueWithLimit() {
        switch (attribute) {
            case FIRE_RESISTANCE: {
                return Math.clamp(value, -fireResMax, fireResMax);
            }
            case COLD_RESISTANCE: {
                return Math.clamp(value, -coldResMax, coldResMax);
            }
            case LIGHTNING_RESISTANCE: {
                return Math.clamp(value, -lightningResMax, lightningResMax);
            }
            case CHAOS_RESISTANCE: {
                return Math.clamp(value, -chaosResMax, chaosResMax);
            }
            case DEFENSE:
                return Math.clamp(value, -90, 90);
            case ACCURACY:
                return 100 + value;
            default: return value;
        }
    }

}
