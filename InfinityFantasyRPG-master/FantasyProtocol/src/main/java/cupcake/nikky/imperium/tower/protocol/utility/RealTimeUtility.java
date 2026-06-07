package cupcake.nikky.imperium.tower.protocol.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RealTimeUtility {

    private static final ZoneId zone = ZoneId.of("GMT+3");

    private static DayOfWeek dayOfWeek;
    private static int hour;
    private static int minute;
    private static long millis;

    public static void catchDateTime() {
        val dateTime = LocalDateTime.now(zone);

        millis = dateTime.atZone(zone).toInstant().toEpochMilli();
        hour = dateTime.getHour();
        minute = dateTime.getMinute();
        dayOfWeek = dateTime.getDayOfWeek();
    }

    public static ZoneId zone() {
        return zone;
    }

    public static long millis() {
        return millis;
    }

    public static int hour() {
        return hour;
    }

    public static int minute() {
        return minute;
    }

    public static DayOfWeek dayOfWeek() {
        return dayOfWeek;
    }

}
