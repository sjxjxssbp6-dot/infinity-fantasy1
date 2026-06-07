package cupcake.nikky.imperium.tower.particle;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.ailment.AilmentPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.ailment.AilmentType;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.*;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.settings.SettingsType;
import cupcake.nikky.imperium.tower.client.protocol.particle.ParticleZoneData;
import cupcake.nikky.imperium.tower.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ParticleProvider {

    public void sendPurpleExplosion(@NotNull final Player player, @NotNull final Location location, @NotNull final List<Player> shown) {
        var packet = new PurpleExplosionPacket(location.getX(), location.getY(), location.getZ());
        this.broadcastPacket(player, packet, shown);
    }

    public void sendBinaEffect(@NotNull final Player player, final int durationTicks, @NotNull final Color color, @NotNull final Location location, @NotNull final List<Player> shown) {
        var packet = new BinaParticle(color.getHexadecimal(), location.getX(), location.getY(), location.getZ(), durationTicks);
        this.broadcastPacket(player, packet, shown);
    }

    public void sendHeal(@NotNull final Player player, final int entityId) {
        var packet = new EntityHealParticlePacket(entityId);
        this.broadcastPacket(player, packet, new ArrayList<>(Bukkit.getOnlinePlayers()));
    }

    public void sendPandoraOpen(@NotNull final Player player, @NotNull final Location centerLocation) {
        var packet = new PandoraChestOpenParticle(centerLocation.getX(), centerLocation.getY(), centerLocation.getZ());
        player.<User>getBungeePlayer().sendPacket(packet);
    }

    public void sendPandoraSpawn(@NotNull final Player player, @NotNull final Location centerLocation) {
        var packet = new PandoraChestSpawnParticle(centerLocation.getX(), centerLocation.getY(), centerLocation.getZ());
        player.<User>getBungeePlayer().sendPacket(packet);
    }

    public void sendAilment(@NotNull final Player player, final int entityIdTarget, @NotNull final AilmentType type, final long whenReset) {
        this.broadcastPacket(player, new AilmentPacket(whenReset, type, entityIdTarget), new ArrayList<>(Bukkit.getOnlinePlayers()));
    }

    public void sendLocalEntityBleed(@NotNull final Player player, final int entityId, @NotNull final BloodType bloodType) {
        var packet = new EntityBloodParticlePacket(entityId, bloodType);
        this.broadcastPacket(player, packet, Stream.of(player)
                .filter(player1 -> {
                    var user = player1.<User>getBungeePlayer();
                    return user.findClientSettings().find(SettingsType.BLOOD_EFFECT_VISIBILITY);
                }).toList());
    }

    public void sendLocalLocationDeath(@NotNull final Player player, @NotNull final Location location, @NotNull final DeathType deathType) {
        var packet = new LocationDeathParticlePacket(location.getX(), location.getY(), location.getZ(), deathType);
        this.broadcastPacket(player, packet, Collections.singletonList(player));
    }

    public void sendLocationDeath(@NotNull final Player player, @NotNull final Location location, @NotNull final DeathType deathType) {
        var packet = new LocationDeathParticlePacket(location.getX(), location.getY(), location.getZ(), deathType);
        this.broadcastPacket(player, packet, new ArrayList<>(Bukkit.getOnlinePlayers()));
    }

    public void sendEntityBlood(@NotNull final Player player, final int entityId, @NotNull final BloodType bloodType) {
        var packet = new EntityBloodParticlePacket(entityId, bloodType);
        this.broadcastPacket(player, packet, new ArrayList<>(Bukkit.getOnlinePlayers()).stream()
                .filter(player1 -> {
                    var user = player1.<User>getBungeePlayer();
                    if (user == null || user.isInvalidated() || !user.isLoaded()) return false;
                    return user.findClientSettings().find(SettingsType.BLOOD_EFFECT_VISIBILITY);
                }).toList());
    }

    public void sendEntityLevelUp(@NotNull final Player player, final int entityLevelUpId) {
        var packet = new EntityLevelUpParticlePacket(entityLevelUpId);
        this.broadcastPacket(player, packet, new ArrayList<>(Bukkit.getOnlinePlayers()));
    }

    public void sendIceExplosion(@NotNull final Player center, final double radius, final int entityId) {
        var packet = new EntityIceExplosionParticlePacket(radius, entityId);
        this.broadcastPacket(center, packet, new ArrayList<>(Bukkit.getOnlinePlayers()));
    }

    public void sendSparks(@NotNull final Player center, final double radius, final int entityId) {
        var packet = new EntitySparksParticlePacket(entityId, radius);
        this.broadcastPacket(center, packet, new ArrayList<>(Bukkit.getOnlinePlayers()));
    }

    public void sendEdgeZone(@NotNull final Player player, @NotNull final List<ParticleZoneData> data) {
        player.<User>getBungeePlayer().sendPacket(new EdgeZoneParticlePacket(data));
    }

    private List<? extends Player> broadcastPacket(@NotNull final Player owner, @NotNull final ClientPacket clientPacket, @NotNull final List<? extends Player> players) {
        var result = players.stream().filter(player -> player.canSee(owner)).filter(player -> player.getLocation().distanceSquared(owner.getLocation()) <= 16 * 16).toList();
        result.forEach(player -> player.<User>getBungeePlayer().sendPacket(clientPacket));
        return result;
    }

}
