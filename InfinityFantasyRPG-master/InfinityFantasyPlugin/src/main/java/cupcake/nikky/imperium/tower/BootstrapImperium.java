package cupcake.nikky.imperium.tower;

import cupcake.nikky.imperium.tower.bootstrap.BootstrapProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BootstrapImperium extends JavaPlugin {

    @Override
    public void onEnable() {
        BootstrapProvider.getInstance().invokeEnable(this);
    }

    @Override
    public void onDisable() {
        BootstrapProvider.getInstance().invokeDisable(this);
    }

}
