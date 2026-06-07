package cupcake.nikky.imperium.tower.entity.monster.modifier;

import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@RequiredArgsConstructor
public enum ModifierCollection {

    NONE(ChatColor.WHITE, Collections.emptyList(), new ModifierAffix(0, 0, Collections.emptyList())),
    RARE(ChatColor.BLUE, Arrays.asList(
            new AttributeModifier(ImperiumAttribute.HEALTH, monster -> monster.getLevel() * .003 + .5 + .87, ModifierProcess.ADD_MULTIPLIED_VALUE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MONSTER_INCREASE_EXP_DROP, 2.5, ModifierProcess.ADD_VALUE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, 1.3, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_PHYSICAL_DAMAGE, 1.3, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_COLD_DAMAGE, 1.3, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_FIRE_DAMAGE, 1.3, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_FIRE_DAMAGE, 1.3, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_COLD_DAMAGE, 1.3, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_LIGHTNING_DAMAGE, 1.3, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_LIGHTNING_DAMAGE, 1.3, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_CHAOS_DAMAGE, 1.3, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_CHAOS_DAMAGE, 1.3, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MOVEMENT_SPEED, .1, ModifierProcess.ADD_MULTIPLIED_VALUE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS, 4, ModifierProcess.ADD_VALUE_PROCESS)
    ), new ModifierAffix(0, 1, Arrays.asList(BuffType.SPEED_AMPLIFIER, BuffType.DEFENSE_PENETRATION_AMPLIFIER, BuffType.BLEED_AMPLIFIER))),
    EPIC(ChatColor.DARK_PURPLE, Arrays.asList(
            new AttributeModifier(ImperiumAttribute.HEALTH, monster -> Math.min(monster.getLevel() / 100D + .33, 1) + 3.6, ModifierProcess.ADD_MULTIPLIED_VALUE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MONSTER_INCREASE_EXP_DROP, 7.5, ModifierProcess.ADD_VALUE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, 1.5, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_PHYSICAL_DAMAGE, 1.5, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_FIRE_DAMAGE, 1.5, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_FIRE_DAMAGE, 1.5, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_COLD_DAMAGE, 1.5, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_COLD_DAMAGE, 1.5, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_LIGHTNING_DAMAGE, 1.5, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_LIGHTNING_DAMAGE, 1.5, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_CHAOS_DAMAGE, 1.5, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_CHAOS_DAMAGE, 1.5, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MOVEMENT_SPEED, .25, ModifierProcess.ADD_MULTIPLIED_VALUE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS, 7, ModifierProcess.ADD_VALUE_PROCESS)
    ), new ModifierAffix(1, 2,
            Arrays.asList(BuffType.SPEED_AMPLIFIER,
                    BuffType.DEFENSE_AMPLIFIER,
                    BuffType.DEFENSE_PENETRATION_AMPLIFIER,
                    BuffType.FATUM_DEFENSE_AMPLIFIER,
                    BuffType.EDGE_PROTECTION_AMPLIFIER,
                    BuffType.ATTACK_AMPLIFIER,
                    BuffType.BLEED_AMPLIFIER))),
    UNIQUE(ChatColor.GOLD, Arrays.asList(
            new ShardingMonsterAttributeModifier(),
            new AttributeModifier(ImperiumAttribute.HEALTH, 6, ModifierProcess.ADD_MULTIPLIED_VALUE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MONSTER_INCREASE_EXP_DROP, 4.5, ModifierProcess.ADD_VALUE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_PHYSICAL_DAMAGE, 1.7, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_PHYSICAL_DAMAGE, 1.7, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_FIRE_DAMAGE, 1.7, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_FIRE_DAMAGE, 1.7, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_COLD_DAMAGE, 1.7, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_COLD_DAMAGE, 1.7, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_LIGHTNING_DAMAGE, 1.7, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_LIGHTNING_DAMAGE, 1.7, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MIN_CHAOS_DAMAGE, 1.7, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MAX_CHAOS_DAMAGE, 1.7, ModifierProcess.ADD_TO_MORE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MOVEMENT_SPEED, .1, ModifierProcess.ADD_MULTIPLIED_VALUE_PROCESS),
            new AttributeModifier(ImperiumAttribute.MONSTER_QUANTITY_OF_ITEMS, 10, ModifierProcess.ADD_VALUE_PROCESS)
    ), new ModifierAffix(1, 2, Arrays.asList(
            BuffType.SPEED_AMPLIFIER,
            BuffType.DEFENSE_AMPLIFIER,
            BuffType.DEFENSE_PENETRATION_AMPLIFIER,
            BuffType.EDGE_DEFENSE_AMPLIFIER,
            BuffType.EDGE_PROTECTION_AMPLIFIER,
            BuffType.ATTACK_AMPLIFIER,
            BuffType.BLEED_AMPLIFIER)));

    @Getter
    private final ChatColor nameColor;
    @Getter
    private final Collection<AttributeModifier> modifiers;
    @Getter
    private final ModifierAffix modifierAffix;

    public int getPosition() {
        return switch (this) {
            case NONE -> 0;
            case RARE -> 1;
            case EPIC -> 2;
            case UNIQUE -> 3;
        };
    }

}
