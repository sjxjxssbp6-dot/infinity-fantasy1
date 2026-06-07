package cupcake.nikky.imperium.tower.attribute.process;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import org.jetbrains.annotations.NotNull;

public final class AttributeProcess {

    private final AttributeFinderSettings settings;
    private final AttributeConverter converter;

    private AttributeProcess(@NotNull final AttributeFinderSettings settings) {
        this.settings = settings;
        this.converter = new AttributeConverter(settings.getAttribute(), settings.getValue(),
                settings.getFireResMax(), settings.getColdResMax(), settings.getLightningResMax(), settings.getChaosResMax());
    }

    public static AttributeProcess of(@NotNull final AttributeFinderSettings settings) {
        return new AttributeProcess(settings);
    }

    public ImperiumAttribute findSourceAttribute() {
        return settings.getAttribute();
    }

    public double findValue() {
        return converter.defaultValue();
    }

    public double findValueWithLimit() {
        return converter.valueWithLimit();
    }

    public double findValueOverLimit() {
        return converter.overLimit();
    }

}
