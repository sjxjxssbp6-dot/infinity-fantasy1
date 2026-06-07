package cupcake.nikky.imperium.tower.client.protocol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SystemNotificationType {

    NONE(0), FINE(1), WARN(2), ERROR(3);

    private final int type;

    public static SystemNotificationType findByType(final int type) {
        for (SystemNotificationType value : values()) {
            if (value.getType() == type) return value;
        }

        return null;
    }

}
