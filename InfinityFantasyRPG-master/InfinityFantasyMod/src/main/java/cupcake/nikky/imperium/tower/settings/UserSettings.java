package cupcake.nikky.imperium.tower.settings;

import cupcake.nikky.imperium.tower.FantasyMod;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserSettings {

    @Getter
    private static final UserSettings instance = new UserSettings();

    private final String fileName = "infinity-fantasy-settings.json";

    @Getter
    private final Map<String, String> data = new HashMap<>();

    public void saveCurrentSettings() {
        try {
            Files.write(Paths.get(fileName), FantasyMod.gson().toJson(data).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCurrentSettings() {
        Map<String, String> data = null;

        try {
            val allLines = Files.readAllLines(Paths.get(fileName));
            if (allLines.isEmpty()) return;

            data = FantasyMod.gson().fromJson(allLines.get(0), Map.class);
        } catch (IOException e) {
            this.saveCurrentSettings();
        }

        if (data != null)
            this.data.putAll(data);
    }

}
