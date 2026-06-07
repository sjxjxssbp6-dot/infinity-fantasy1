package cupcake.nikky.imperium.tower;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.*;

@Getter
@RequiredArgsConstructor
public class Season {

    private final String title;
    private final String number;
    private final String patchNoteUrl;

    public int seasonDuration() {
        return (int) (seasonEnd() - seasonStart());
    }

    public long seasonStart() {
        return LocalDate.of(2024, 9, 6).toEpochSecond(LocalTime.of(0, 0, 0), ZoneOffset.UTC);
    }

    public int currentSeasonDuration() {
        return (int) (Instant.now().toEpochMilli() / 1000 - seasonStart());
    }

    public int seasonLeft() {
        return (int) (seasonEnd() - Instant.now().toEpochMilli() / 1000);
    }

    public long seasonEnd() {
        var date = LocalDate.of(2024, 9, 6);
        return date.plusDays(61L).toEpochSecond(LocalTime.of(0, 0, 0), ZoneOffset.UTC);
    }

}
