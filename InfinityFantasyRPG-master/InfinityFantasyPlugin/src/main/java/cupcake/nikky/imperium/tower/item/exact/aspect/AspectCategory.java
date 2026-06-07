package cupcake.nikky.imperium.tower.item.exact.aspect;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AspectCategory {

    MOBILITY("Скорость"),
    DEFENSE("Защита"),
    ATTACK("Нападение");

    private final String categoryTitle;

}
