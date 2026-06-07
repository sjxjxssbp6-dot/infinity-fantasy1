package cupcake.nikky.imperium.tower.tree.element.exact.character;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class AddManaResEffElementProcess extends TreeElementProcess {

    private final int reservationEfficiency;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.MANA_RESERVATION_EFFICIENCY).applyBaseValue(reservationEfficiency);
    }

    @Override
    public String description() {
        return String.format("+%d%% к эффективности резервации маны", reservationEfficiency);
    }

}
