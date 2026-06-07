package me.anfanik.steda.api.listener;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public interface DefaultListener extends Listener {

    default void register(@NotNull final JavaPlugin plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    default void unregister(){
        HandlerList.unregisterAll(this);
    }

}
