package cupcake.nikky.imperium.tower.chat;

import cupcake.nikky.imperium.tower.Fantasy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import me.func.mod.ui.MultiChat;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import ru.cristalix.core.permissions.IPermissionService;
import ru.cristalix.core.util.UtilTime;
import ru.cristalix.core.violation.IViolationService;

import java.util.Arrays;
import java.util.HashSet;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChatsInitializer {

    @Getter
    private static final ChatsInitializer instance = new ChatsInitializer();

    private final String[] keys = new String[3];

    public void initialize() {
        MultiChat.createKey("current_realm",
                ModChatBuilder.create()
                        .setKey(ChatType.CURRENT_REALM.name().toLowerCase())
                        .setSymbol("R")
                        .setName("Текущий реалм")
                        .build());
        MultiChat.createKey("system",
                ModChatBuilder.create()
                        .setKey(ChatType.SYSTEM.name().toLowerCase())
                        .setSymbol("S")
                        .setName("Системный чат")
                        .build());
        MultiChat.createKey("notification",
                ModChatBuilder.create()
                        .setKey(ChatType.NOTIFICATION.name().toLowerCase())
                        .setSymbol("N")
                        .setName("Уведомления")
                        .build());

        keys[0] = "current_realm";
        keys[1] = "system";
        keys[2] = "notification";

        MultiChat.registerHandler(keys[0], (player, s) -> {
            if (!s.startsWith("/")) {
                IViolationService.get().getMute(player.getUniqueId()).whenComplete((violation, throwable) -> {
                    if (throwable != null) {
                        Bukkit.getLogger().info(throwable.getMessage());
                        throwable.printStackTrace();
                    } else if (violation == null || violation.isPassed()) {
                        val event = new AsyncPlayerChatEvent(true, player, s, new HashSet<>(Bukkit.getOnlinePlayers()));
                        Fantasy.getChatExecutor().execute(() -> Bukkit.getPluginManager().callEvent(event));
                    } else {
                        IPermissionService.get().getPermissionContext(violation.getWhoBanned()).whenComplete((iPermissionContext, throwable1) -> {
                            if (throwable1 != null) {
                                throwable1.printStackTrace();
                            } else {
                                Fantasy.bukkitProvider().sendMessage(player, ChatType.CURRENT_REALM,
                                        String.format("%s &fзамутил тебя &dна %s &fпо причине &d%s",
                                                iPermissionContext.toDisplayName(), UtilTime.makeStr(violation.getBanTime()), violation.getReason()));
                            }
                        });
                    }
                });
            } else {
                val command = s.substring(1);
                if (Bukkit.getPluginCommand(command) != null) {
                    Bukkit.dispatchCommand(player, command);
                }
            }
        });
    }

    public String[] getKeys() {
        return Arrays.copyOf(keys, keys.length);
    }

}
