package cupcake.nikky.imperium.tower.aura;

import com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.listener.DefaultListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.module.aura.Aura;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FantasyAuraManager implements DefaultListener {

    private static final FantasyAuraManager INSTANCE = new FantasyAuraManager();

    private final Map<UUID, Aura> auraMap = new HashMap<>();
    private final List<UUID> ignoreAura = new ArrayList<>();

    public static FantasyAuraManager get() {
        return INSTANCE;
    }

    public boolean isIgnore(@NotNull final UUID uuid) {
        return this.ignoreAura.contains(uuid);
    }

    public void addToIgnoreList(@NotNull final UUID uuid) {
        val player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        this.ignoreAura.add(uuid);
        auraMap.values().forEach(aura -> aura.remove(player));
    }

    public void invalidateIgnore(@NotNull final UUID uuid) {
        val player = Bukkit.getPlayer(uuid);
        if (player == null) return;

        this.ignoreAura.remove(uuid);
        auraMap.values().forEach(aura -> aura.send(player));
    }

    public void add(Aura aura) {
        aura.send(Bukkit.getOnlinePlayers().stream().filter(player -> !isIgnore(player.getUniqueId())).collect(Collectors.toList()));
        this.auraMap.put(aura.getUuid(), aura);
    }

    public void remove(Aura aura) {
        if (this.auraMap.containsKey(aura.getUuid())) {
            this.auraMap.remove(aura.getUuid()).remove(Bukkit.getOnlinePlayers());
        }
    }

    public void removeEntityAura(int entityId) {
        for (Aura aura : new ArrayList<>(auraMap.values())) {
            if (aura.getEntityId() != entityId)
                continue;
            remove(aura);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerWadaLoaded(PlayerJoinEvent event) {
        auraMap.values().forEach(aura -> aura.send(event.getPlayer()));
    }

    @EventHandler
    public void onEntityRemove(EntityRemoveFromWorldEvent event) {
        Entity entity = event.getEntity();
        if(entity instanceof Player) return;

        removeEntityAura(entity.getEntityId());
    }

    @EventHandler
    public void onPlayerQuite(PlayerQuitEvent event) {
        this.removeEntityAura(event.getPlayer().getEntityId());
        this.invalidateIgnore(event.getPlayer().getUniqueId());
    }

}

