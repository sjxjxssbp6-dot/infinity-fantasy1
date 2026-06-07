package cupcake.nikky.imperium.tower.data;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsType;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import lombok.Getter;

public class SettingsStorage {

    @Getter
    private static final SettingsStorage instance = new SettingsStorage();

    @Getter
    private final Object2BooleanMap<SettingsType> settings = new Object2BooleanOpenHashMap<>();

    public void clear() {
        settings.clear();
    }

}
