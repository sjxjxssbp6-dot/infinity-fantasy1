package cupcake.nikky.imperium.tower.entity;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.attribute.process.AttributeFinderSettings;
import cupcake.nikky.imperium.tower.attribute.process.AttributeProcess;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import org.jetbrains.annotations.NotNull;

public interface Attributable {

    int getLevel();

    AttributeMap getAttributes();

    default AttributeProcess findAttribute(@NotNull final ImperiumAttribute attribute) {
        return AttributeProcess.of(AttributeFinderSettings
                        .builder()
                        .attribute(attribute)
                        .value(this.getAttributes().find(attribute).getValue())
                        .fireResMax((int) getAttributes().find(ImperiumAttribute.FIRE_RESISTANCE_MAXIMUM).getValue())
                        .coldResMax((int) getAttributes().find(ImperiumAttribute.COLD_RESISTANCE_MAXIMUM).getValue())
                        .lightningResMax((int) getAttributes().find(ImperiumAttribute.LIGHTNING_RESISTANCE_MAXIMUM).getValue())
                        .chaosResMax((int) getAttributes().find(ImperiumAttribute.CHAOS_RESISTANCE_MAXIMUM).getValue())
                        .build());
    }

}
