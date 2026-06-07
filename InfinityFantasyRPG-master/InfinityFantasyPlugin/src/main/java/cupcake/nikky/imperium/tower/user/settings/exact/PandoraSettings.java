package cupcake.nikky.imperium.tower.user.settings.exact;

import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.pandora.PandoraData;
import cupcake.nikky.imperium.tower.pandora.PandoraProvider;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.BaseUserSettings;
import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.Wada;
import ru.cristalix.wada.module.worldtext.WorldText;
import ru.cristalix.wada.util.Color;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PandoraSettings extends BaseUserSettings {

    @Getter private final List<PandoraData> pandoraList = new CopyOnWriteArrayList<>();
    @Getter private final Map<UUID, WorldText> pandoraText = new ConcurrentHashMap<>();

    public PandoraSettings(User user) {
        super(user);
    }

    public PandoraData findDataByUUID(@NotNull final UUID uuid) {
        return pandoraList.stream().filter(data -> data.getUuid().equals(uuid)).findAny().orElse(null);
    }

    public PandoraData findDataByLocation(@NotNull final Location location) {
        return pandoraList.stream().filter(data -> data.getPandoraLocation().equals(location)).findAny().orElse(null);
    }

    public void createPandora(@NotNull final Location location, @NotNull final Monster monster) {
        if (!location.getBlock().isEmpty()) return;
        var pandoraData = new PandoraData(getUser(), location, monster);

        PandoraProvider.getInstance().getPandoraHandler().createPandoraBlock(getPlayer(), pandoraData);
        pandoraList.add(pandoraData);
        this.pandoraText.put(pandoraData.getUuid(), Wada.get().getWorldTextModule().createWorldText("", location.clone().toCenterLocation().add(0, 2, 0), true, Color.BLACK.alpha(.62)));
        this.update(pandoraData);
        this.pandoraText.get(pandoraData.getUuid()).send(getPlayer());
    }

    public void interactPandora(@NotNull final PandoraData pandoraData) {
        PandoraProvider.getInstance().getPandoraHandler().interactPandoraBlock(getPlayer(), pandoraData);
    }

    public void invalidatePandoraCache(@NotNull final PandoraData data) {
        this.pandoraList.remove(data);
        var worldText = this.pandoraText.remove(data.getUuid());
        if (worldText != null) worldText.remove(getPlayer());
    }

    public void skipAllPandora() {
        this.pandoraText.values().forEach(worldText -> worldText.remove(getPlayer()));
        this.pandoraList.forEach(data -> PandoraProvider.getInstance().getPandoraHandler().invalidatePandoraBlock(getPlayer(), data));
        this.pandoraText.clear();
        this.pandoraList.clear();
    }

    public void update(@NotNull final PandoraData data) {
        var worldText = pandoraText.get(data.getUuid());
        var interactText = "\n\uE3FF Открыть меню взаимодействия";
        var name = data.getMonster().getNameForPlayer(getUser().getUniqueId()).split("\\r?\\n");

        worldText.setText("""
                &6Ящик пандоры&f (%s %s)
                %s
                """.formatted(name[0], name[1], interactText)
        );
    }

}
