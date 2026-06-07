package cupcake.nikky.imperium.tower.donate.shop.sample.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.blood.BloodObject;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.donate.DonateData;
import cupcake.nikky.imperium.tower.donate.shop.sample.DonateSample;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.donate.DonateType;
import cupcake.nikky.imperium.tower.protocol.packet.UserDonateLogPacket;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Map;
import java.util.function.Consumer;

public class DonateBloodSample implements DonateSample<BloodObject> {

    private final Map<BloodObject, Integer> prices = Map.of(
            BloodObject.WHITE_BLOOD, BloodObject.WHITE_BLOOD.getPrice(),
            BloodObject.HEART_BLOOD, BloodObject.HEART_BLOOD.getPrice(),
            BloodObject.MULTI_COLORED_BLOOD, BloodObject.MULTI_COLORED_BLOOD.getPrice()
    );

    @Override
    public Map<BloodObject, Integer> findPrices() {
        return prices;
    }

    @Override
    public Consumer<Player> createProcess(@NotNull DonateData donateData, @NotNull BloodObject bloodObject, int price) {
        return player -> {
            var user = player.<User>getBungeePlayer();
            if (user.getCurrencyHandler().withdrawDonateCurrencyWithPriority(price)) {
                donateData.applyMetadata(bloodObject.name(), Instant.now().toEpochMilli());
                PacketRegistry.write(Fantasy.gameServer().nameSpace(), new UserDonateLogPacket(player.getUniqueId(), DonateType.BLOOD, bloodObject.name(), Instant.now().toEpochMilli()));
                Fantasy.clientProvider().notification(player, "&aВы успешно приобрели: ", bloodObject.getTitle());
            } else {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, ChatColor.RED + "Произошла ошибка во время выполнения операции");
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
            }
        };
    }

}
