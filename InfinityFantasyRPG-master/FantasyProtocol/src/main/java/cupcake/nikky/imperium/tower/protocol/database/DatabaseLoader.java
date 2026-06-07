package cupcake.nikky.imperium.tower.protocol.database;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.Properties;
import java.util.concurrent.ThreadPoolExecutor;

@RequiredArgsConstructor
public class DatabaseLoader {

    private final JavaPlugin javaPlugin;

    @SneakyThrows
    public DatabaseService load(@NotNull final ThreadPoolExecutor executor) {
        val properties = new Properties();
        val props = new File(javaPlugin.getDataFolder(), "database.properties");

        if(!props.exists()) {
            Files.createDirectory(javaPlugin.getDataFolder().toPath());
            Files.createFile(props.toPath());
            Bukkit.getLogger().warning("File with database params is null. Please, configure database.properties and reload the server.");
            return null;
        }

        properties.load(new FileReader(props));

        return new DatabaseService(executor).initialize(properties);
    }

}
