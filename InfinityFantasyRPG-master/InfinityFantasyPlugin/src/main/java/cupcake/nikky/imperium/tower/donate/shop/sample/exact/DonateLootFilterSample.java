package cupcake.nikky.imperium.tower.donate.shop.sample.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.donate.shop.sample.DonateSample;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.donate.DonateData;
import cupcake.nikky.imperium.tower.protocol.donate.DonateType;
import cupcake.nikky.imperium.tower.donate.SubscriptionDurationType;
import cupcake.nikky.imperium.tower.protocol.packet.UserDonateLogPacket;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class DonateLootFilterSample implements DonateSample<SubscriptionDurationType> {

    private final Map<SubscriptionDurationType, Integer> prices = new HashMap<>();

    public DonateLootFilterSample() {
        prices.put(SubscriptionDurationType.WEEK_2, 350);
        prices.put(SubscriptionDurationType.WEEK_4, 650);
        prices.put(SubscriptionDurationType.MONTH_2, 1200);
        prices.put(SubscriptionDurationType.MONTH_6, 3500);
    }

    @Override
    public Map<SubscriptionDurationType, Integer> findPrices() {
        return prices;
    }

    @Override
    public Consumer<Player> createProcess(@NotNull final DonateData donateData, @NotNull final SubscriptionDurationType durationType, int price) {
        return player -> {
            var user = player.<User>getBungeePlayer();
            val hasLootFilter = donateData.isDonateLootFilter();

            if (hasLootFilter && TimeUnit.MILLISECONDS.toDays(donateData.getSubscriptionTimeLong() + durationType.getDuration()) > 365 * 2) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Вы не можете продлить", "выше лимита данную подписку");
                return;
            }

            if (user.getCurrencyHandler().withdrawDonateCurrencyWithPriority(price)) {
                this.process(donateData, durationType).accept(player);
            } else {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, ChatColor.RED + "Произошла ошибка во время выполнения операции");
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
            }
        };
    }

    public Consumer<Player> process(@NotNull final DonateData donateData, @NotNull final SubscriptionDurationType durationType) {
        return player -> {
            val hasLootFilter = donateData.isDonateLootFilter();
            donateData.updateLootFilter(durationType);

            val additional = (int) TimeUnit.MILLISECONDS.toDays(durationType.getDuration());
            Fantasy.clientProvider().notification(player, "&aВы успешно " + (hasLootFilter ? "продлили" : "приобрели"), "&aрасширенный фильтр предметов", "&aна &6" + additional + " &a" +
                    TextUtility.transformViaAmount(additional, "день", "дня", "дней"));

            player.<User>getBungeePlayer().findDonateSettings().forceUpdateInDataBase();

            PacketRegistry.write(Fantasy.gameServer().nameSpace(), new UserDonateLogPacket(player.getUniqueId(), DonateType.LOOT_FILTER_SUBSCRIPTION, durationType.name(), Instant.now().toEpochMilli()));
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
            Fantasy.bukkitProvider().sound(player, AdvancedSounds.MESSAGE);
        };
    }

}
