package cupcake.nikky.imperium.tower.entity.monster.statistic;

import cupcake.nikky.imperium.tower.entity.monster.MonsterType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MonsterStatistic {

    @Getter
    private static final MonsterStatistic instance = new MonsterStatistic();

    private int higherLevel = -1;

    public int findHigherMonsterLevel() {
        if (this.higherLevel == -1) {
            for (MonsterType value : MonsterType.values())
                if (!value.getMonsterData().isUnique() && value.getMonsterData().getLevel() > higherLevel)
                    higherLevel = value.getMonsterData().getLevel();
        }

        return higherLevel;
    }

}
