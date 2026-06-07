package cupcake.nikky.imperium.tower.tree.element.exact.edge;

import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class AddBattleExperienceMultiplier extends TreeElementProcess {

    private final double value;

    @Override
    public void onAttributeAdjust(@NotNull AttributeMap attributeMap) {
        attributeMap.find(ImperiumAttribute.BATTLE_EXPERIENCE_MULTIPLIER).applyBaseValue(value);
    }

    @Override
    public String description() {
        return String.format("+%.1f%% к множителю боевого опыта", value);
    }

}