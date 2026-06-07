package cupcake.nikky.imperium.tower.bootstrap;

import cupcake.nikky.imperium.tower.bootstrap.exact.disable.GeneralDisableProcess;
import cupcake.nikky.imperium.tower.bootstrap.exact.enable.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BootstrapProvider {

    @Getter
    private static final BootstrapProvider instance = new BootstrapProvider();

    private final BootstrapProcess[] onEnable = {
            new InitProjectProcess(),
            new TowerCapabilitiesEnableProcess(),
            new MapServiceProcess(),
            new CristalixServiceEnableProcess(),
            new GeneralEnableProcess(),
            new PacketListenerEnableProcess(),
            new ClientPacketListenerProcess(),
            new PluginMessageProcess(),
            new BukkitPacketListenerProcess()
    };

    private final BootstrapProcess[] onDisable = {
            new GeneralDisableProcess()
    };

    public void invokeEnable(@NotNull final JavaPlugin javaPlugin) {
        for (BootstrapProcess bootstrapProcess : onEnable) {
            bootstrapProcess.invoke(javaPlugin);
            Bukkit.getLogger().info(bootstrapProcess.getClass().getSimpleName() + " enabled");
        }
    }

    public void invokeDisable(@NotNull final JavaPlugin javaPlugin) {
        for (BootstrapProcess bootstrapProcess : onDisable) {
            bootstrapProcess.invoke(javaPlugin);
        }
    }

}
