package cupcake.nikky.imperium.tower.tree.element.exact.character;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.attribute.process.AttributeFinderSettings;
import cupcake.nikky.imperium.tower.attribute.process.AttributeProcess;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class AddMovementSpeedElementProcess extends TreeElementProcess {

    private final double percent;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap, int level) {
        attributeMap.find(ImperiumAttribute.MOVEMENT_SPEED).applyBaseValue(percent);
    }

    @Override
    public String description() {
        return String.format("+%.1f%% к скорости передвижения", percent);
    }

}
