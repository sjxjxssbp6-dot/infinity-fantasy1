package cupcake.nikky.imperium.tower.donate.shop.sample.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.blood.BloodObject;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.death.DeathObject;
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

public class DonateDeathSample implements DonateSample<DeathObject> {

    private final Map<DeathObject, Integer> prices = Map.of(
            DeathObject.RED_LINE_UP, DeathObject.RED_LINE_UP.getPrice(),
            DeathObject.DARK_EXPLOSION, DeathObject.DARK_EXPLOSION.getPrice(),
            DeathObject.PURPLE_EXPLOSION, DeathObject.PURPLE_EXPLOSION.getPrice()
    );

    @Override
    public Map<DeathObject, Integer> findPrices() {
        return prices;
    }

    @Override
    public Consumer<Player> createProcess(@NotNull DonateData donateData, @NotNull DeathObject deathObject, int price) {
        return player -> {
            var user = player.<User>getBungeePlayer();
            if (user.getCurrencyHandler().withdrawDonateCurrencyWithPriority(price)) {
                donateData.applyMetadata(deathObject.name(), Instant.now().toEpochMilli());
                PacketRegistry.write(Fantasy.gameServer().nameSpace(), new UserDonateLogPacket(player.getUniqueId(), DonateType.DEATH_EFFECT, deathObject.name(), Instant.now().toEpochMilli()));
                Fantasy.clientProvider().notification(player, "&aВы успешно приобрели: ", deathObject.getTitle());
            } else {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, ChatColor.RED + "Произошла ошибка во время выполнения операции");
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
            }
        };
    }

}
