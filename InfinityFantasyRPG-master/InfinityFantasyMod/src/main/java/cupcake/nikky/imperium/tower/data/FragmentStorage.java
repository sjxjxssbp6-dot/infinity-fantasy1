package cupcake.nikky.imperium.tower.data;

import lombok.*;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FragmentStorage {

    @Getter
    private static final FragmentStorage instance = new FragmentStorage();

    @Getter @Setter
    private long time;
    @Getter @Setter
    private long respawns;

    public String toStringTime() {
        val duration = time - Instant.now().toEpochMilli();
        if (duration <= 0L) return "0";

        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration - minutes * 60L * 1000);

        return String.format("%d:%s", minutes, seconds < 10 ? "0" + seconds : String.valueOf(seconds));
    }

    public void clear() {
        time = 0L;
        respawns = 0;
    }

}
