package cupcake.nikky.imperium.tower.logging;

import cupcake.nikky.imperium.tower.Fantasy;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class ImperiumLoggerService implements LoggerService {

    private final OffsetDateTime date = OffsetDateTime.now(ZoneOffset.UTC).plusHours(3);

    private int logTime, countFiles;

    private FileWriter fileWriter;
    private File logFile;

    @SneakyThrows
    public ImperiumLoggerService() {
        this.createNewWriter();
    }

    private void createNewWriter() throws IOException {
        val directory = new File(Fantasy.getJavaPlugin().getDataFolder(), "logs");
        logFile = new File(directory, String.format("%d-%d-%d-%d-%d-%d", date.getMonthValue(), date.getDayOfMonth(), date.getYear(), date.getHour(), date.getMinute(), countFiles++));

        if (!directory.exists()) Files.createDirectory(directory.toPath());

        fileWriter = new FileWriter(logFile, true);
        logTime = 0;

        if (!logFile.exists()) {
            if (!logFile.createNewFile()) {
                Bukkit.getLogger().warning("Logger missed. Can't create new file.");
            } else {
                Bukkit.getLogger().info("Logger created successfully");
            }
        }
    }

    @Override
    public void log(Player player, String name, String state, String data) {
        if (fileWriter == null) {
            Bukkit.getLogger().warning("Trying to log data while file writer is null.");
            return;
        }

        val logLine = String.format("%s|%s|%s|%s|%s\n", player.getName(), player.getUniqueId().toString(), name, state, data);
        try {
            fileWriter.write(logLine);
            fileWriter.flush();

            logTime += 1;

            if (logTime > 500) {
                this.stopLogger();
                this.createNewWriter();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SneakyThrows
    public void stopLogger() {
        if (fileWriter != null) fileWriter.close();
    }

    @SneakyThrows
    public void onDisable() {
        if (logTime == 0) {
            Files.deleteIfExists(logFile.toPath());
        }
    }

}
