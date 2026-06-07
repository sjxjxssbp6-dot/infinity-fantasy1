package cupcake.nikky.imperium.tower.bootstrap;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public interface BootstrapProcess {

    void invoke(@NotNull final JavaPlugin javaPlugin);

}
