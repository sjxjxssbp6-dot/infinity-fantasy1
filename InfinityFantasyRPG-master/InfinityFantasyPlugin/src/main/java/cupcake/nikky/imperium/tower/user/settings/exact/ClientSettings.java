package cupcake.nikky.imperium.tower.user.settings.exact;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsType;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.BaseUserSettings;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientSettings extends BaseUserSettings {

    @Getter
    private final Map<SettingsType, Boolean> settings = new ConcurrentHashMap<>(SettingsType.values().length, 1F, 2);

    public ClientSettings(User user) {
        super(user);
    }

    public boolean find(@NotNull final SettingsType type) {
        if (!settings.containsKey(type)) return true;
        return settings.get(type);
    }

    public void handleClientSettings(@NotNull final Object2BooleanMap<SettingsType> settings) {
        this.settings.clear();
        this.settings.putAll(settings);
        getUser().findSyncHideSettings().setHideAll(settings.getBoolean(SettingsType.PLAYER_VISIBILITY));
    }

}
