package cupcake.nikky.imperium.tower.attack;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import net.minecraft.server.v1_12_R1.PacketPlayInUseEntity;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerAttackReader {

    private final Cache<UUID, Boolean> attackPacketDelay = CacheBuilder.newBuilder()
            .initialCapacity(64)
            .concurrencyLevel(2)
            .expireAfterWrite(50, TimeUnit.MILLISECONDS)
            .build();

    public PlayerAttackReader() {
        Fantasy.protocolProvider().addInboundHandler(PacketPlayInUseEntity.class, "attack_speed_process", event -> {
            val packet = event.getPacket();
            val player = event.getEntityPlayer().getBukkitEntity();

            event.setCancelled(true);

            if (packet.action == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK && attackPacketDelay.getIfPresent(player.getUniqueId()) == null) {
                attackPacketDelay.put(player.getUniqueId(), true);

                val bukkitEntity = this.findEntity((CraftWorld) player.getWorld(), packet.getEntityId());
                if (bukkitEntity == null) return;

                val user = player.<User>getBungeePlayer();
                if (user == null || !user.isCanAttack()) return;

                Fantasy.getAttackExecutor().execute(() -> new PlayerAttackHandler(player, user, bukkitEntity).handle());
            }
        });
    }

    private CraftEntity findEntity(@NotNull final CraftWorld world, final int entityId) {
        val craftEntity = world.getHandle().getEntity(entityId);
        return craftEntity == null ? null : craftEntity.getBukkitEntity();
    }

}