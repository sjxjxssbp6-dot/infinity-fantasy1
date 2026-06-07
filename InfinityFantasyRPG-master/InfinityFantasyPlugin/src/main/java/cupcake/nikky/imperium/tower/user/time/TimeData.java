package cupcake.nikky.imperium.tower.user.time;

import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.protocol.board.TimeType;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@RequiredArgsConstructor
@AllArgsConstructor
public class TimeData {

    private final TimeType timeType;
    private int level;
    private int duration;
    private int corrupt;

    public void tryUpdate(final int level, final int duration, final int corrupt) {
        if (this.getScore() > getScore(level, corrupt, duration)) return;

        this.setLevel(level);
        this.setDuration(duration);
        this.setCorrupt(corrupt);
    }

    private double getScore(final int level, final int corrupt, final int duration) {
        if (level == 0 || level - LevelStorage.getInstance().getBreakThroughLevel() < 0) return 0;

        val timeLimit = 3D * 60D * 1_000D;
        return (level - LevelStorage.getInstance().getBreakThroughLevel() + 1) * Math.max(corrupt, 1) * (1D - duration / timeLimit);
    }

    public double getScore() {
        return getScore(getLevel(), getCorrupt(), getDuration());
    }

}
