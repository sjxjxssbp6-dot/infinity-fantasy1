package cupcake.nikky.imperium.tower.trade.player.request;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.display.IDisplayService;
import ru.cristalix.core.display.data.SocialsButton;
import ru.cristalix.core.display.messages.SocialsNotify;
import ru.cristalix.core.permissions.IPermissionService;
import ru.cristalix.wada.util.Color;

import java.time.Instant;
import java.util.List;

public record TradeRequestData(@NotNull Player sender, @NotNull Player receiver, long time) {

    public TradeRequestData(@NotNull Player sender, @NotNull Player receiver) {
        this(sender, receiver, Instant.now().toEpochMilli());
    }

    public boolean isExpired() {
        return Instant.now().toEpochMilli() - time > 30 * 1_000;
    }

    public void processNotify() {
        var senderContextFuture = IPermissionService.get().getPermissionContext(sender().getUniqueId());
        var receiverContextFuture = IPermissionService.get().getPermissionContext(receiver().getUniqueId());

        senderContextFuture.thenAccept(senderContext -> receiverContextFuture.thenAccept(receiverContext -> {
            Fantasy.bukkitProvider().sendMessage(sender(), ChatType.SYSTEM, "&fВы отправили предложение обмена игроку %s".formatted(receiverContext.toDisplayName()));
            Fantasy.bukkitProvider().sound(sender(), AdvancedSounds.CLICK_1);

            Fantasy.bukkitProvider().sound(receiver(), AdvancedSounds.MESSAGE);
            IDisplayService.get().sendSocialsNotify(receiver().getUniqueId(), SocialsNotify.builder()
                    .source(sender().getUniqueId())
                    .text(TextUtility.colorize("&fОбмен от %s", senderContext.toDisplayName()))
                    .type("other")
                    .timeout(1_000 * 30)
                    .buttons(List.of(SocialsButton.builder()
                            .text("Принять")
                            .command("/trade accept %s".formatted(sender.getName()))
                            .color(Color.GREY.getDecimal())
                            .removeNotification(true)
                            .build(), SocialsButton.builder()
                            .text("Отклонить")
                            .command("/trade decline %s".formatted(sender.getName()))
                            .color(Color.GREY.getDecimal())
                            .removeNotification(true)
                            .build()))
                    .backgroundColor(Color.DARK_GREY.getDecimal())
                    .timeoutBarColor(Color.GREY.getDecimal())
                    .build());
        }));
    }

}
