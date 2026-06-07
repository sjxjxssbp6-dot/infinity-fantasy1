package cupcake.nikky.imperium.tower.protocol.utility;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public final class NumberUtility {

    public int parseInteger(@Nullable final String string) {
        if (string == null || string.isBlank()) return 0;

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
        if (string == null || string.isBlank()) return 0;

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
