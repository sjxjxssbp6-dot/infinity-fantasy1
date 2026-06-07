package cupcake.nikky.imperium.tower.tree.element.exact.edge;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class AddToMaximumFragmentEventCountElementProcess extends TreeElementProcess {

    private final int count;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.FRAGMENT_EVENT_MAXIMUM_AMOUNT).applyAdditional(count);
    }

    @Override
    public String description() {
        return String.format("+%d к максимальному количеству событий на Осколке%n%n&8(Стандартное количество событий - 1)", count);
    }

}
