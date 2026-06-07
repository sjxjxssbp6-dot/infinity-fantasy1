package cupcake.nikky.imperium.tower.item.settings.exact.loot;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true, chain = true)
public class LootGeneratorSettings {

    private boolean rarity;
    private int commonChance = 70;
    private int rareChance = 20;
    private int epicChance = 10;

    private int maxItemLevel = -1;

    private boolean extendedModifiers;
    private boolean exaltedModifiers;
    private boolean directionModifiers;
    private boolean canBeEmptyDirectionModifier = true;
    private boolean baseModifiers;

    private boolean potential;

    private final CorruptionSettings corruptionSettings = new CorruptionSettings();

}
