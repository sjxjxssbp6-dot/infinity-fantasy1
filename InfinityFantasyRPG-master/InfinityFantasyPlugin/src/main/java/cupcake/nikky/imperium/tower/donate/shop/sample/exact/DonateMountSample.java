package cupcake.nikky.imperium.tower.donate.shop.sample.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.donate.shop.sample.DonateSample;
import cupcake.nikky.imperium.tower.mount.MountType;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.donate.DonateData;
import cupcake.nikky.imperium.tower.protocol.donate.DonateType;
import cupcake.nikky.imperium.tower.protocol.packet.UserDonateLogPacket;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DonateMountSample implements DonateSample<MountType> {

    private final Map<MountType, Integer> prices = new HashMap<>();

    public DonateMountSample() {
        prices.put(MountType.SKELETON_HORSE, 599);
        prices.put(MountType.ZOMBIE_HORSE, 599);
        prices.put(MountType.GREY_HORSE, 399);
        prices.put(MountType.BROWN_HORSE, 399);
        prices.put(MountType.DARK_BROWN_HORSE, 399);
    }

    @Override
    public Map<MountType, Integer> findPrices() {
        return prices;
    }

    @Override
    public Consumer<Player> createProcess(@NotNull DonateData donateData, @NotNull MountType mountType, int price) {
        return player -> {
            var user = player.<User>getBungeePlayer();
            if (user.getCurrencyHandler().withdrawDonateCurrencyWithPriority(price)) {
                donateData.applyMetadata(mountType.name(), Instant.now().toEpochMilli());
                PacketRegistry.write(Fantasy.gameServer().nameSpace(), new UserDonateLogPacket(player.getUniqueId(), DonateType.MOUNT, mountType.name(), Instant.now().toEpochMilli()));
                Fantasy.clientProvider().notification(player, "&aВы успешно приобрели: ", "&6" + mountType.getSettings().getTitle());
            } else {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, ChatColor.RED + "Произошла ошибка во время выполнения операции");
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
            }
        };
    }

}
