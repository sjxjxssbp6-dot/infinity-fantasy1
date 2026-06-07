package cupcake.nikky.imperium.tower.bootstrap.exact.enable;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.board.BoardType;
import cupcake.nikky.imperium.tower.bootstrap.BootstrapProcess;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.chat.FantasyChatService;
import cupcake.nikky.imperium.tower.fraction.FractionProvider;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.board.TimeType;
import cupcake.nikky.imperium.tower.protocol.packet.*;
import cupcake.nikky.imperium.tower.protocol.packet.connection.game.GameConnectionPacket;
import cupcake.nikky.imperium.tower.protocol.packet.connection.game.GameConnectionPingPacket;
import cupcake.nikky.imperium.tower.protocol.packet.connection.game.GameDisconnectPacket;
import cupcake.nikky.imperium.tower.protocol.packet.daily.DailyDataPacket;
import cupcake.nikky.imperium.tower.protocol.packet.time.UserTimeBoardPacket;
import cupcake.nikky.imperium.tower.trade.npc.PlayerTradeData;
import cupcake.nikky.imperium.tower.quest.Quest;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.CoreApi;
import ru.cristalix.core.chat.IChatService;
import ru.cristalix.core.realm.RealmId;

public class PacketListenerEnableProcess implements BootstrapProcess {

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(@NotNull JavaPlugin javaPlugin) {
        PacketRegistry.addListener(GameConnectionPingPacket.class, (realmId, gameConnectionPingPacket) -> PacketRegistry.write(new GameConnectionPacket(Fantasy.gameServer().getState())));

        PacketRegistry.addListener(FractionTopPacket.class, (realmId, fractionTopPacket) -> FractionProvider.getInstance().setTopFraction(fractionTopPacket.getType()));

        PacketRegistry.addListener(GameScheduledReloadPacket.class, (realmId, gameSyncReloadPacket) -> Fantasy.autoRestartProvider().startRestartProcess(5));

        PacketRegistry.addListener(GameKickPlayersPacket.class, (realmId, gameKickPlayersPacket) ->
                Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("По тех. причинам")));

        PacketRegistry.addListener(GameWhitelistPacket.class, (realmId, gameWhitelistPacket) -> Fantasy.getJavaPlugin().getServer().setWhitelist(gameWhitelistPacket.isStatement()));

        PacketRegistry.addListener(UserTimeBoardPacket.class, (realmId, userTimeBoardPacket) -> {
            val data = userTimeBoardPacket.getData();
            if (data.values().stream().anyMatch(timeData -> timeData.getTimeType() == TimeType.HOCHMA)) {
                BoardType.HOCHMA.getFantasyBoard().handlePacket(userTimeBoardPacket);
            } else {
                BoardType.BINA.getFantasyBoard().handlePacket(userTimeBoardPacket);
            }
        });

        PacketRegistry.addListener(UserGeneralBoardPacket.class, (realmId, userGeneralBoardPacket) ->
                BoardType.GENERAL.getFantasyBoard().handlePacket(userGeneralBoardPacket));

        PacketRegistry.addListener(DailyDataPacket.class, (realmId, dailyDataPacket) -> {
            if (dailyDataPacket.isNextDay()) {
                val questService = Fantasy.questService();
                val quests = questService.findAllQuests().stream().filter(Quest::isDaily).toList();

                Fantasy.userService().findAll().forEach(user -> {
                    val questedPlayer = user.findQuestSettings();

                    quests.forEach(quest -> questedPlayer.invalidatePassedQuest(quest.getId()));
                });

                Fantasy.tradeNpcService().findAll().forEach(PlayerTradeData::invalidateDayDuration);
            }

            if (dailyDataPacket.isNextWeek()) {
                Fantasy.tradeNpcService().findAll().forEach(PlayerTradeData::invalidateWeekDuration);
            }
        });

        PacketRegistry.addListener(GameKickPlayerPacket.class, (realmId, gameKickPlayerPacket) -> {
            Fantasy.runLater(() -> {
                val player = Bukkit.getPlayer(gameKickPlayerPacket.getPlayerUuid());
                if (player != null) {
                    player.kickPlayer(gameKickPlayerPacket.getReason());
                }
            });
        });

        PacketRegistry.addListener(GameDisconnectPacket.class, (realmId, gameDisconnectPacket) -> {
            Bukkit.getLogger().info("Received disconnect packet, reload flag: " + gameDisconnectPacket.isReload() + ", realm sender: " + realmId.getRealmName());
            if (gameDisconnectPacket.isReload()) {
                Fantasy.autoRestartProvider().startRestartProcess(1);
            } else {
                Fantasy.autoRestartProvider().shutdownProcess();
            }
        });

        PacketRegistry.addListener(GameGlobalMessagePacket.class, (realmId, gameGlobalMessagePacket) -> {
            if (gameGlobalMessagePacket.isPlayer()) {
                ((FantasyChatService) CoreApi.get().getService(IChatService.class)).processGlobalMessage(
                        gameGlobalMessagePacket.getPlayerUuid(),
                        gameGlobalMessagePacket.getFractionType(),
                        gameGlobalMessagePacket.getPlayerBaseLevel(),
                        RealmId.of(gameGlobalMessagePacket.getRealmNameSource()),
                        gameGlobalMessagePacket.getMessage(),
                        gameGlobalMessagePacket.isDeathless(),
                        gameGlobalMessagePacket.isSubscribed(),
                        gameGlobalMessagePacket.getUserDescription(),
                        ImperiumItem.deserialize(gameGlobalMessagePacket.getSerializedItem()));
            } else {
                Fantasy.bukkitProvider().sendMessage(ChatType.NOTIFICATION, gameGlobalMessagePacket.getMessage(), Bukkit.getOnlinePlayers());
            }
        });

        PacketRegistry.write(Fantasy.gameServer().nameSpace(), new GameConnectionPacket(Fantasy.gameServer().getState()));
    }

}
