package cupcake.nikky.imperium.tower.buff.exact.monster.yihochma;

import cupcake.nikky.imperium.tower.buff.FantasyBuff;

import java.util.List;

public class HochmaStabilityBuff extends FantasyBuff {

    public HochmaStabilityBuff() {
        super(5, "Стабильность");

        this.setShouldInvalidateAfterDeath(true);
    }

    @Override
    public List<String> getDescription() {
        return List.of(
                "&cПо окончанию действия убивает",
                "&cвсех игроков, принимающих участие в бою"
        );
    }
}
