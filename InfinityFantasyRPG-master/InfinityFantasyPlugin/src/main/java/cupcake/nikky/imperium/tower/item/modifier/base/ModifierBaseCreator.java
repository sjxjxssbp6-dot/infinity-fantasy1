package cupcake.nikky.imperium.tower.item.modifier.base;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.entity.monster.statistic.MonsterStatistic;
import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTier;
import cupcake.nikky.imperium.tower.item.modifier.description.ModifierDescriptionOneValue;
import cupcake.nikky.imperium.tower.item.modifier.process.AttributeBaseModifierProcess;
import cupcake.nikky.imperium.tower.item.modifier.range.ModifierRange;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ModifierBaseCreator {

    private final Object2DoubleMap<ImperiumAttribute> minData = new Object2DoubleOpenHashMap<>();
    private final Object2DoubleMap<ImperiumAttribute> maxData = new Object2DoubleOpenHashMap<>();

    public Modifier[] create() {
        val higherLevel = LevelStorage.getInstance().getArmorLevel();

        final List<Modifier> result = new ArrayList<>();

        minData.forEach((attribute, aDouble) -> {
            val tier = new ModifierTier[higherLevel];

            for (int i = 1; i <= higherLevel; i++) {
                tier[i - 1] = new ModifierTier().tier(i).min(minData.getDouble(attribute)).max(maxData.getDouble(attribute)).requireBaseLevel(i);
            }

            result.add(createBaseModifier(attribute, ModifierRange.of(tier)));
        });

        return result.toArray(new Modifier[0]);
    }

    public ModifierBaseCreator applyValue(@NotNull final ImperiumAttribute attribute, final double value) {
        return this.applyValue(attribute, value, value);
    }

    public ModifierBaseCreator applyValue(@NotNull final ImperiumAttribute attribute, final double minValue, final double maxValue) {
        this.minData.put(attribute, minValue);
        this.maxData.put(attribute, maxValue);
        return this;
    }

    public ModifierBaseCreator applyPhysicalDamage(final double minValue, final double maxValue) {
        this.minData.put(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, minValue);
        this.maxData.put(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, minValue);
        this.minData.put(ImperiumAttribute.MAX_PHYSICAL_DAMAGE, maxValue);
        this.maxData.put(ImperiumAttribute.MAX_PHYSICAL_DAMAGE, maxValue);
        return this;
    }

    private Modifier createBaseModifier(@NotNull final ImperiumAttribute attribute, @NotNull final ModifierRange range) {
        return Modifier.ofType(
                attribute.name().toLowerCase() + "_attribute",
                ModifierPlace.BASE,
                new ModifierDescriptionOneValue(attribute.getDescriptionText()),
                new AttributeBaseModifierProcess(attribute),
                range,
                Collections.emptyList()
        );
    }

}
