package cupcake.nikky.imperium.tower.hud.notification;

import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationData {

    private final SystemNotificationType type;
    private final String[] lines;

    public boolean isWithConfusion() {
        return getType() == SystemNotificationType.WARN || getType() == SystemNotificationType.ERROR;
    }

}
