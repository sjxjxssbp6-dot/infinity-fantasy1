package cupcake.nikky.imperium.tower.donate.shop.sample.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.donate.shop.sample.DonateSample;
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
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;

public class DonateAuraSample implements DonateSample<AuraObject> {

    private final Map<AuraObject, Integer> prices = new EnumMap<>(AuraObject.class);

    public DonateAuraSample() {
        for (AuraObject value : AuraObject.values()) {
            if (value == AuraObject.NONE || value == AuraObject.NONE_ALL || value == AuraObject.PREMIUM_AURA) continue;
            prices.put(value, 599);
        }
    }

    @Override
    public Map<AuraObject, Integer> findPrices() {
        return prices;
    }

    @Override
    public Consumer<Player> createProcess(@NotNull DonateData donateData, @NotNull AuraObject auraObject, int price) {
        return player -> {
            var user = player.<User>getBungeePlayer();
            if (user.getCurrencyHandler().withdrawDonateCurrencyWithPriority(price)) {
                donateData.applyMetadata(auraObject.name(), Instant.now().toEpochMilli());
                auraObject.apply(player);
                PacketRegistry.write(Fantasy.gameServer().nameSpace(), new UserDonateLogPacket(player.getUniqueId(), DonateType.AURA, auraObject.name(), Instant.now().toEpochMilli()));
                Fantasy.clientProvider().notification(player, "&aВы успешно приобрели: ", "&6" + auraObject.getFantasyAura().title());
            } else {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, ChatColor.RED + "Произошла ошибка во время выполнения операции");
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
            }
        };
    }

}
