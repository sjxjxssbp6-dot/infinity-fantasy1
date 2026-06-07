package cupcake.nikky.imperium.tower.entity.monster.modifier;

import cupcake.nikky.imperium.tower.buff.BuffType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ModifierAffix {

    private final int minAmount;
    private final int maxAmount;
    private final List<BuffType> amplifiers;

}
