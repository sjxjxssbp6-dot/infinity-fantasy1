package cupcake.nikky.imperium.tower.item.modifier;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModifierPlace {

    BASE(0), DIRECTION(1), PREFIX(2), SUFFIX(3);

    private final int position;

}
