package cupcake.nikky.imperium.tower.entity.monster.conduct;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConductType {

    AGGRESSIVE(8),
    NEUTRAL(8);

    private final int range;

}
