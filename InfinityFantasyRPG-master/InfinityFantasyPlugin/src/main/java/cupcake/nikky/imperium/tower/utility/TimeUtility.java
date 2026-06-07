package cupcake.nikky.imperium.tower.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeUtility {

    public static long tradeWeekReset() {
        val localDate = LocalDateTime.now(ZoneId.of("GMT+3")).withHour(0).withMinute(0).withSecond(0).with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        return localDate.atZone(ZoneId.of("GMT+3")).toInstant().toEpochMilli();
    }

    public static long tradeDayReset() {
        val localDate = LocalDateTime.now(ZoneId.of("GMT+3")).plusDays(1).withHour(0).withMinute(0).withSecond(0);
        return localDate.atZone(ZoneId.of("GMT+3")).toInstant().toEpochMilli();
    }

    public static String formatSeconds(final long totalSeconds) {
        return TimeUtility.formatSeconds((int) totalSeconds);
    }

    public static String formatSeconds(final int totalSeconds) {
        val days = (int) Math.floor(totalSeconds / 86400f);
        val hours = (int) Math.floor((totalSeconds - 86400f * days) / 3600f);
        val minutes = (int) Math.floor((totalSeconds - 86400f * days - 3600 * hours) / 60f);
        val seconds = totalSeconds % 60;

        val result = new StringBuilder();

        Arrays.asList(
                days > 0 ? days + "д" : "",
                hours > 0 ? hours + "ч" : "",
                (days > 0  || hours > 0) ? "" : minutes > 0 ? minutes + "мин" : "",
                (days > 0  || hours > 0) ? "" : seconds > 0 ? seconds + "сек" : ""
        ).forEach(time -> {
            if (result.length() > 0 && !time.isEmpty()) result.append(" ");
            result.append(time);
        });

        return result.toString();
    }

}
