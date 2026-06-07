package cupcake.nikky.imperium.tower.item.exact.modification.rule;

import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum CorruptResult {

    SUCCESSFUL(new String[] {"Вы успешно осквернили предмет!"}, SystemNotificationType.FINE),
    FAIL(new String[] {"Неудачное осквернение!", "Уровень осквернения был понижен"}, SystemNotificationType.ERROR),
    CORRUPT_LIMIT(new String[] {"Данный предмет имеет максимально", "возможный уровень осквернения"}, SystemNotificationType.ERROR);

    private final String[] message;
    private final SystemNotificationType notificationType;

}
