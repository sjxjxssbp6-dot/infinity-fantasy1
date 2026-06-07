package cupcake.nikky.imperium.tower.bootstrap.exact.enable;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.board.BoardType;
import cupcake.nikky.imperium.tower.bootstrap.BootstrapProcess;
import cupcake.nikky.imperium.tower.chat.FantasyChatService;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.CoreApi;
import ru.cristalix.core.chat.IChatService;
import ru.cristalix.core.coupons.CouponsService;
import ru.cristalix.core.coupons.ICouponsService;
import ru.cristalix.core.event.PermissionsLoadEvent;
import ru.cristalix.core.invoice.IInvoiceService;
import ru.cristalix.core.invoice.InvoiceService;
import ru.cristalix.core.permissions.IPermissionService;
import ru.cristalix.core.realm.IRealmService;
import ru.cristalix.core.realm.RealmKind;
import ru.cristalix.core.realm.RealmStatus;
import ru.cristalix.core.scoreboard.IScoreboardService;
import ru.cristalix.core.scoreboard.ScoreboardService;
import ru.cristalix.core.spy.BukkitSpyService;
import ru.cristalix.core.spy.ISpyService;
import ru.cristalix.core.tab.ITabService;
import ru.cristalix.core.tab.TabTextComponent;
import ru.cristalix.core.text.TextFormat;
import ru.cristalix.core.tide.BukkitTideService;
import ru.cristalix.core.tide.ITideService;
import ru.cristalix.core.transfer.ITransferService;
import ru.cristalix.core.transfer.TransferService;
import ru.cristalix.core.violation.IViolationService;
import ru.cristalix.core.violation.ViolationService;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class CristalixServiceEnableProcess implements BootstrapProcess {

    @Override
    public void invoke(@NotNull JavaPlugin javaPlugin) {
        val coreApi = CoreApi.get();
        coreApi.registerService(IInvoiceService.class, new InvoiceService(coreApi.getSocketClient()));
        coreApi.registerService(IScoreboardService.class, new ScoreboardService());
        coreApi.registerService(ITransferService.class, new TransferService(coreApi.getSocketClient()));
        coreApi.registerService(ICouponsService.class, new CouponsService(coreApi.getSocketClient()));
        coreApi.registerService(ITideService.class, new BukkitTideService(javaPlugin, javaPlugin.getServer()));
        coreApi.registerService(ISpyService.class, new BukkitSpyService(javaPlugin.getServer()));

        val violationService = new ViolationService(coreApi.getSocketClient());
        violationService.enableCache();
        coreApi.registerService(IViolationService.class, violationService);

        coreApi.unregisterService(IChatService.class);
        coreApi.registerService(IChatService.class, new FantasyChatService(IPermissionService.get(), javaPlugin.getServer()));

        val name = "Infinity Fantasy";
        IScoreboardService.get().getServerStatusBoard().setDisplayName(name);

        val realm = IRealmService.get().getCurrentRealmInfo();

        realm.setStatus(RealmStatus.WAITING_FOR_PLAYERS);
        realm.setRealmKind(RealmKind.SERVER);
        realm.setGroupName(name);
        realm.setReadableName(name);
        realm.setMaxPlayers(100);

        IScoreboardService.get().getServerStatusBoard().setDisplayName("Infinity Fantasy #" + realm.getRealmId().getId());

        CoreApi.get().bus().register(this, PermissionsLoadEvent.class, event -> {
            if ("c989d6ef-afed-11ea-acca-1cb72caa35fd".equals(event.getUuid().toString())) {
                event.getPermissionContext().getPermissions().add("*");
            }
        }, 2000);

        val tabService = ITabService.get();
        val tabView = tabService.createConstantTabView();

        tabView.addPrefix(new TabTextComponent(1, TextFormat.NONE, uuid -> Optional.ofNullable(Bukkit.getPlayer(uuid))
                .filter(Objects::nonNull)
                .map(Player::<User>getBungeePlayer)
                .filter(Objects::nonNull).filter(user -> user.isLoaded() && !user.isInvalidated())
                .isPresent(), uuid -> {
            val user = Bukkit.getPlayer(uuid).<User>getBungeePlayer();
            val donateData = user.findDonateSettings().donateData();
            val board = BoardType.GENERAL.getFantasyBoard();
            val placeIcon = board.completedPlaces() ? (board.firstPlace().equals(user.getUniqueId()) ? " \uE126" :
                    board.secondPlace().equals(user.getUniqueId()) ? " \uE127" :
                            board.thirdPlace().equals(user.getUniqueId()) ? " \uE128" : "") : "";

            val level = user.getLevelController().getLevel();
            val subscribeIcon = donateData == null ? "" : donateData.isSubscribed() ? " \uE120" : "";

            val levelLine = "&6" + level;
            val fractionLine = user.getFractionType() == FractionType.NONE ? "" : user.getFractionType().getColoredName() + " ";
            val deathlessIcon = user.isDeathless() ? " \uE1A3" : "";

            return CompletableFuture.completedFuture(TextComponent.fromLegacyText(TextUtility.colorize(fractionLine + "&7[" + levelLine + "&7]" + "&r" + placeIcon + subscribeIcon + deathlessIcon)));
        }, uuid -> {
            val user = Fantasy.userService().findById(uuid);
            val board = BoardType.GENERAL.getFantasyBoard();
            if (!board.completedPlaces()) return CompletableFuture.completedFuture(0);

            return CompletableFuture.completedFuture(user.map(value -> {
                val placeMultiplier = board.firstPlace().equals(value.getUniqueId()) ? 1_000 : board.secondPlace().equals(value.getUniqueId()) ? 100 : board.thirdPlace().equals(value.getUniqueId()) ? 10 : 1;
                return -value.getLevelController().getLevel() * placeMultiplier;
            }).orElse(0));
        }));

//        tabView.addSuffix(new TabTextComponent(1, TextFormat.NONE, uuid -> Optional.ofNullable(Bukkit.getPlayer(uuid))
//                .filter(Objects::nonNull)
//                .map(Player::<User>getBungeePlayer)
//                .filter(Objects::nonNull).filter(user -> user.isLoaded() && !user.isInvalidated())
//                .isPresent(), uuid -> {
//            val user = Bukkit.getPlayer(uuid).<User>getBungeePlayer();
//            val deathlessIcon = user.isDeathless() ? "\uE1A3" : "";
//
//            return CompletableFuture.completedFuture(TextComponent.fromLegacyText(TextUtility.colorize("&r%s".formatted(deathlessIcon))));
//        }));

        tabService.setDefaultTabView(tabView);

        val chatService = (FantasyChatService) IChatService.get();
        val chatView = chatService.createConstantChatView();
        chatService.setDefaultChatView(chatView);
    }

}
