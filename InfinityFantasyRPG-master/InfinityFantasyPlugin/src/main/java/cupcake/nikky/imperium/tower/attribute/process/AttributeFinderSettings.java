package cupcake.nikky.imperium.tower.attribute.process;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public final class AttributeFinderSettings {

    private ImperiumAttribute attribute;
    @Builder.Default private int fireResMax = 75;
    @Builder.Default private int coldResMax = 75;
    @Builder.Default private int lightningResMax = 75;
    @Builder.Default private int chaosResMax = 75;
    private double value;

}