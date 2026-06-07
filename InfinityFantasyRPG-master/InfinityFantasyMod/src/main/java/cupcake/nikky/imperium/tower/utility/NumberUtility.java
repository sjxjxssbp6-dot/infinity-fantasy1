package cupcake.nikky.imperium.tower.utility;

import lombok.experimental.UtilityClass;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.NavigableMap;
import java.util.TreeMap;

@UtilityClass
public final class NumberUtility {

    private final NavigableMap<Long, String> suffixes = new TreeMap<>();
    private final DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);

    static {
        suffixes.put(1_000L, "k");
        suffixes.put(1_000_000L, "M");
        suffixes.put(1_000_000_000L, "G");
        suffixes.put(1_000_000_000_000L, "T");
        suffixes.put(1_000_000_000_000_000L, "P");
        suffixes.put(1_000_000_000_000_000_000L, "E");

        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
    }

    public String classicalFormat(long value) {
        return formatter.format(value);
    }

    public String classicalFormat(double value) {
        return formatter.format(value);
    }

    public String format(long value) {
        if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
        if (value < 0) return "-" + format(-value);
        if (value < 1000) return Long.toString(value);

        val e = suffixes.floorEntry(value);
        val divideBy = e.getKey();
        val suffix = e.getValue();

        val truncated = value / (divideBy / 10);
        val hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10D);

        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    public int parseInteger(@Nullable final String string) {
        if (string == null || string.trim().isEmpty()) return 0;

        var negative = false;
        var position = 0;
        var length = string.length();
        var limit = -Integer.MAX_VALUE;

        if (length > 0) {
            var firstChar = string.charAt(0);
            if (firstChar < '0') {
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                } else if (firstChar != '+') return 0;

                if (length == 1) return 0;
                position++;
            }

            var multMin = limit / 10;
            var result = 0;

            while (position < length) {
                var digit = Character.digit(string.charAt(position++), 10);
                if (digit < 0 || result < multMin) return 0;

                result *= 10;

                if (result < limit + digit) return 0;

                result -= digit;
            }

            return negative ? result : -result;
        }

        return 0;
    }

    public long parseLong(@Nullable final String string) {
        if (string == null || string.trim().isEmpty()) return 0;

        var negative = false;
        var position = 0;
        var length = string.length();
        var limit = -Long.MAX_VALUE;

        if (length > 0) {
            var firstChar = string.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Long.MIN_VALUE;
                } else if (firstChar != '+') return 0;

                if (length == 1)
                    return 0;

                position++;
            }

            var multmin = limit / 10L;
            var result = 0L;

            while (position < length) {
                var digit = Character.digit(string.charAt(position++), 10);
                if (digit < 0 || result < multmin)
                    return 0;

                result *= 10;

                if (result < limit + digit)
                    return 0;

                result -= digit;
            }
            return negative ? result : -result;
        }

        return 0;
    }

}
