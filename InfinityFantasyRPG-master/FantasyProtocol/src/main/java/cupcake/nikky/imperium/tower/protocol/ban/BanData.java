package cupcake.nikky.imperium.tower.protocol.ban;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.cristalix.core.util.UtilTime;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
public class BanData {

    private UUID uuid;
    private long endTime;
    private String reason;

    public String message() {
        return "Ваш аккаунт заблокирован. Будет разблокирован через: %s. Причина: %s".formatted(UtilTime.makeStr(endTime - Instant.now().toEpochMilli()), reason);
    }

}
