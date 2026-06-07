package cupcake.nikky.imperium.tower.bootstrap.exact.enable;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.bootstrap.BootstrapProcess;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.packet.*;
import cupcake.nikky.imperium.tower.protocol.packet.daily.DailyDataPacket;
import cupcake.nikky.imperium.tower.protocol.packet.time.UserTimeBoardPacket;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.network.Capability;

public class TowerCapabilitiesEnableProcess implements BootstrapProcess {

    @Override
    public void invoke(@NotNull JavaPlugin javaPlugin) {
        PacketRegistry.registerCapabilities(
                Capability.builder().className(GameGlobalMessagePacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).notification(true).build(),
                Capability.builder().className(FractionTopPacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).notification(true).build(),
                Capability.builder().className(GameKickPlayersPacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).notification(true).build(),
                Capability.builder().className(GameWhitelistPacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).notification(true).build(),
                Capability.builder().className(UserTimeBoardPacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).notification(true).build(),
                Capability.builder().className(UserGeneralBoardPacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).notification(true).build(),
                Capability.builder().className(DailyDataPacket.class.getName()).namespace(Fantasy.gameServer().nameSpace()).notification(true).build(),
                Capability.builder().className(GameKickPlayerPacket.class.getName()).notification(true).build()
        );
    }

}
