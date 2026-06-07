package cupcake.nikky.imperium.tower.item.modifier.exact;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTag;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTier;
import cupcake.nikky.imperium.tower.item.modifier.description.ModifierDescriptionOneValue;
import cupcake.nikky.imperium.tower.item.modifier.description.ModifierDescriptionWithoutValue;
import cupcake.nikky.imperium.tower.item.modifier.process.AttributeModifierProcess;
import cupcake.nikky.imperium.tower.item.modifier.range.ModifierRange;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collections;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModifierUniqueItemList {

    public static final Modifier CRITICAL_CHANCE_ON_PLAYER = Modifier.ofFilter("crit_chance_on_player", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.1f%% %sувеличение шанса крит. удара по игрокам"),
            new AttributeModifierProcess(ImperiumAttribute.INCREASE_CRITICAL_RATE_ON_PLAYER),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(50, 100).requireBaseLevel(1),
                    new ModifierTier().tier(2).weight(1000).range(101, 150).requireBaseLevel(1).exalted(true)
            ),
            Collections.singletonList(item -> false));

    public static final Modifier CAN_RECEIVE_ONLY_PHYSICAL_DAMAGE_BY_PLAYER = Modifier.ofFilter("can_receive_only_phys_dmg_by_player", ModifierPlace.SUFFIX,
            new ModifierDescriptionWithoutValue("%sВы получаете только физический и хаос урон от игроков"),
            new AttributeModifierProcess(ImperiumAttribute.CAN_RECEIVE_ONLY_PHYSICAL_AND_CHAOS_DAMAGE_BY_PLAYER),
            ModifierRange.of(
                    new ModifierTier().tier(1).weight(1000).range(1, 1).requireBaseLevel(1)
            ),
            Collections.singletonList(item -> false));

    public static final Modifier CHANCE_TO_CAUSE_BLEEDING_ON_HIT = Modifier.ofFilter("chance_bleed_on_hit", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sшанс вызвать кровотечение"),
            new AttributeModifierProcess(ImperiumAttribute.CHANCE_TO_CAUSE_BLEEDING),
            ModifierRange.of(
                    new ModifierTier().tier(1).canGetViaCraft(false).weight(1000).range(10, 25).requireBaseLevel(1),
                    new ModifierTier().tier(2).canGetViaCraft(false).exalted(true).weight(1000).range(26, 40).requireBaseLevel(2)
            ),
            Collections.singletonList(item -> false),
            ModifierTag.DAMAGE, ModifierTag.OVER_TIME, ModifierTag.PHYSICAL, ModifierTag.ATTACK);

    public static final Modifier CHANCE_TO_POISON_ON_HIT = Modifier.ofFilter("chance_poison_on_hit", ModifierPlace.SUFFIX,
            new ModifierDescriptionOneValue("%s%.0f%% %sшанс отравить"),
            new AttributeModifierProcess(ImperiumAttribute.CHANCE_TO_POISON),
            ModifierRange.of(
                    new ModifierTier().tier(1).canGetViaCraft(false).weight(1000).range(10, 25).requireBaseLevel(1),
                    new ModifierTier().tier(2).canGetViaCraft(false).exalted(true).weight(1000).range(26, 40).requireBaseLevel(1)
            ),
            Collections.singletonList(item -> false),
            ModifierTag.DAMAGE, ModifierTag.OVER_TIME, ModifierTag.CHAOS, ModifierTag.ATTACK);

    public static final Modifier CHANCE_TO_GAIN_POWER_CHARGE_ON_HIT = Modifier.ofFilter("chance_power_charge_on_hit",
            ModifierPlace.PREFIX, new ModifierDescriptionOneValue("%s%.0f%% %sшанс получить Заряд энергии при ударе"),
            new AttributeModifierProcess(ImperiumAttribute.CHANCE_TO_GAIN_POWER_CHARGE_ON_HIT),
            ModifierRange.of(
                    new ModifierTier().tier(1).canGetViaCraft(false).weight(1000).range(10, 20).requireBaseLevel(1),
                    new ModifierTier().tier(2).canGetViaCraft(false).exalted(true).weight(1000).range(21, 30).requireBaseLevel(1)
            ),
            Collections.singletonList(item -> false),
            ModifierTag.CRITICAL, ModifierTag.DAMAGE);

    public static final Modifier CHANCE_TO_GAIN_ENDURANCE_CHARGE_ON_HIT = Modifier.ofFilter("chance_endurance_charge_on_hit",
            ModifierPlace.PREFIX, new ModifierDescriptionOneValue("%s%.0f%% %sшанс получить Заряд выносливости при ударе"),
            new AttributeModifierProcess(ImperiumAttribute.CHANCE_TO_GAIN_ENDURANCE_CHARGE_ON_HIT),
            ModifierRange.of(
                    new ModifierTier().tier(1).canGetViaCraft(false).weight(1000).range(10, 20).requireBaseLevel(1),
                    new ModifierTier().tier(2).canGetViaCraft(false).exalted(true).weight(1000).range(21, 30).requireBaseLevel(1)
            ),
            Collections.singletonList(item -> false),
            ModifierTag.DEFENCE, ModifierTag.RESISTANCE);

    public static final Modifier CHANCE_TO_GAIN_FRENZY_CHARGE_ON_HIT = Modifier.ofFilter("chance_frenzy_charge_on_hit",
            ModifierPlace.PREFIX, new ModifierDescriptionOneValue("%s%.0f%% %sшанс получить Заряд безумия при ударе"),
            new AttributeModifierProcess(ImperiumAttribute.CHANCE_TO_GAIN_FRENZY_CHARGE_ON_HIT),
            ModifierRange.of(
                    new ModifierTier().tier(1).canGetViaCraft(false).weight(1000).range(10, 20).requireBaseLevel(1),
                    new ModifierTier().tier(2).canGetViaCraft(false).exalted(true).weight(1000).range(21, 30).requireBaseLevel(1)
            ),
            Collections.singletonList(item -> false),
            ModifierTag.SPEED, ModifierTag.DAMAGE);

}
