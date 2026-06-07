package cupcake.nikky.imperium.tower.tree.element.exact.character;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class IncreaseSpellDamageElementProcess extends TreeElementProcess {

    private final double percent;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.SPELL_DAMAGE).applyIncrease(percent / 100D);
    }

    @Override
    public String description() {
        return String.format("%.1f%% увеличение урона заклинаниями%n%n&8(Заклинания вызываются при помощи аспектов)", percent);
    }

}
