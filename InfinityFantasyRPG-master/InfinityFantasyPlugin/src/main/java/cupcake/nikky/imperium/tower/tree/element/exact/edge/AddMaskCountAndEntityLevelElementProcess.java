package cupcake.nikky.imperium.tower.tree.element.exact.edge;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class AddMaskCountAndEntityLevelElementProcess extends TreeElementProcess {

    private final int count;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.ADDITIONAL_MASK_DROP).applyAdditional(count);
    }

    @Override
    public String description() {
        return String.format("+%d к уровню Проводника Грани%n+%d к количеству слепков модификатора%n%n&8(Проводник Грани - монстр, который с некоторым шансом появляется на осколках Грани)", count, count);
    }

}