package cupcake.nikky.imperium.tower.user.settings;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class SettingsStorage {

    private final Map<Class<? extends UserSettings>, UserSettings> settingsMap;

    @SuppressWarnings("unchecked")
    public <T extends UserSettings> T findSettings(@NotNull final Class<? extends T> clazz) {
        return (T) this.settingsMap.get(clazz);
    }

    public List<UserSettings> findAll() {
        return new ArrayList<>(settingsMap.values());
    }

}
