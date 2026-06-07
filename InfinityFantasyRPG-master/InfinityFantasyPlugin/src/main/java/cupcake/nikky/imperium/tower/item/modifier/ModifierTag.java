package cupcake.nikky.imperium.tower.item.modifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModifierTag {

    LIFE("Здоровье"),
    MANA("Мана"),
    PHYSICAL("Физическое"),
    ELEMENTAL("Стихийное"),
    ELEMENTAL_DAMAGE("Стихийный урон"),
    LIGHTNING("Молния"),
    FIRE("Огненное"),
    CHAOS("Хаос"),
    COLD("Холод"),
    RESISTANCE("Сопротивление"),
    CRITICAL("Критическое"),
    DAMAGE("Урон"),
    DEFENCE("Защита"),
    SPEED("Скорость"),
    ATTACK("Атака"),
    DROP("Добыча"),
    OVER_TIME("Продолжительное");

    private final String name;

}
