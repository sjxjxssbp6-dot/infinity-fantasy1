package cupcake.nikky.imperium.tower.item.exact.mask;

import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.modifier.Modifier;
import cupcake.nikky.imperium.tower.item.modifier.exact.ModifierItemListExtended;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Masks {

    STRENGTHENING("Слепок Грани: Укрепление", 2307, Map.of(
        ItemType.ARMOR, ModifierItemListExtended.ADDITIONAL_MAX_HEALTH.copyWithTierLimit(9),
        ItemType.WEAPON, ModifierItemListExtended.DAMAGE_LEECH_AS_LIFE
    )),
    CONTEMPT("Слепок Грани: Презрение", 2301, Map.of(
        ItemType.ARMOR, ModifierItemListExtended.ADD_PHYSICAL_DAMAGE.copyWithTierLimit(3),
        ItemType.WEAPON, ModifierItemListExtended.ADD_PHYSICAL_DAMAGE.copyWithTierLimit(7)
    )),
    HATRED("Слепок Грани: Ненависть", 2304, Map.of(
        ItemType.ARMOR, ModifierItemListExtended.COLD_RESISTANCE,
        ItemType.WEAPON, ModifierItemListExtended.ADD_COLD_DAMAGE.copyWithTierLimit(9)
    )),
    ANGER("Слепок Грани: Ярость", 2300, Map.of(
        ItemType.ARMOR, ModifierItemListExtended.FIRE_RESISTANCE,
        ItemType.WEAPON, ModifierItemListExtended.ADD_FIRE_DAMAGE.copyWithTierLimit(9)
    )),
    WRATH("Слепок Грани: Гнев", 2308, Map.of(
        ItemType.ARMOR, ModifierItemListExtended.LIGHTNING_RESISTANCE,
        ItemType.WEAPON, ModifierItemListExtended.ADD_LIGHTNING_DAMAGE.copyWithTierLimit(9)
    )),
    LOATHING("Слепок Грани: Отвращение", 2305, Map.of(
        ItemType.WEAPON, ModifierItemListExtended.GLOBAL_CRITICAL_CHANCE.copyWithTierLimit(7),
        ItemType.ARMOR, ModifierItemListExtended.INCREASED_MANA_RESERVE_EFFICIENCY.copyWithTierLimit(4)
    )),
    ZEAL("Слепок Грани: Усердие", 2309, Map.of(
        ItemType.ARMOR, ModifierItemListExtended.REGENERATION_IN_FIGHT.copyWithTierLimit(6),
        ItemType.WEAPON, ModifierItemListExtended.ATTACK_SPEED.copyWithTierLimit(6)
    )),
    SCORN("Слепок Грани: Насмешка", 2306, Map.of(
        ItemType.WEAPON, ModifierItemListExtended.GLOBAL_CRITICAL_DAMAGE.copyWithTierLimit(5),
        ItemType.ARMOR, ModifierItemListExtended.GLOBAL_CRITICAL_DAMAGE.copyWithTierLimit(5)
    )),
    ENVY("Слепок Грани: Зависть", 2302, Map.of(
        ItemType.ARMOR, ModifierItemListExtended.CHAOS_RESISTANCE,
        ItemType.WEAPON, ModifierItemListExtended.ADD_CHAOS_DAMAGE.copyWithTierLimit(1)
    )),
    DREAD("Слепок Грани: Страх", 2303, Map.of(
        ItemType.ARMOR, ModifierItemListExtended.DEFENSE.copyWithTierLimit(5),
        ItemType.WEAPON, ModifierItemListExtended.INCREASE_DAMAGE.copyWithTierLimit(6)
    )),
    MISERY("Слепок Грани: Невзгоды", 2310, Map.of(
            ItemType.ARMOR, ModifierItemListExtended.ADDITIONAL_MAX_MANA.copyWithTierLimit(10),
            ItemType.WEAPON, ModifierItemListExtended.ADDITIONAL_MANA_REGENERATION.copyWithTierLimit(4)
    ));

    private final String title;
    private final int data;
    private final Map<ItemType, Modifier> linkedModifiers;

    public List<String> generateDescription() {
        List<String> description = getLinkedModifiers().keySet().stream().map(type -> {
                    val modifierDescription = getLinkedModifiers().get(type).getModifierDescription().generateCraftDescription("", "");
                    return "&3" + type.getTitle() + ": &6" + modifierDescription.substring(0, 1).toUpperCase() + modifierDescription.substring(1);
                }).collect(Collectors.toList());

        description.addFirst("&fГарантированные модификаторы:");
        description.add(" ");
        description.add("&7Уровень модификатора зависит от уровня предмета");
        description.add("  ");
        description.add("&7Перековывает %sэпический &7предмет".formatted(ItemRare.EPIC.getColor()));
        description.add("&7С гарантированным модификатором");

        return description;
    }

}
