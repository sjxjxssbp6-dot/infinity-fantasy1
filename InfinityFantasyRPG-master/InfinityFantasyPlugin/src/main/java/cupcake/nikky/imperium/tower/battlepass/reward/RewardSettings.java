package cupcake.nikky.imperium.tower.battlepass.reward;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RewardSettings {

    private int rewardId;
    private boolean advanced;

    public int getActualRewardId() {
        return rewardId * (advanced ? 100 : 1);
    }

}
