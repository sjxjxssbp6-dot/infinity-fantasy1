package cupcake.nikky.imperium.tower.currency;

import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import me.func.mod.conversation.ModTransfer;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public final class ClientCurrencyNotification {

    private final String channel = "imp:update-data-box";
    private final User user;

    public void notice(@NotNull final CurrencyType currencyType, final int value) {
        new ModTransfer()
                .varInt(currencyType.getClientId())
                .varInt(value)
                .send(channel, user.getSpigot());
    }

    public void noticeAll() {
        for (CurrencyType value : CurrencyType.values()) {
            this.notice(value, user.getCurrency(value));
        }
    }

}
