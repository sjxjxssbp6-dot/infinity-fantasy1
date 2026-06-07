package cupcake.nikky.imperium.tower.donate.shop.sample.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.Buff;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.donate.shop.sample.DonateSample;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.donate.DonateData;
import cupcake.nikky.imperium.tower.protocol.donate.DonateType;
import cupcake.nikky.imperium.tower.protocol.packet.UserDonateLogPacket;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DonateBuffSample implements DonateSample<Buff> {

    private final Map<Buff, Integer> prices = new HashMap<>();

    public DonateBuffSample() {
        Arrays.asList(BuffType.EXPERIENCE_BUFF, BuffType.SUPPORT_ATTACK_BUFF, BuffType.ATTACK_BUFF, BuffType.CRITICAL_DAMAGE_BUFF, BuffType.DEFENSE_BUFF).forEach(type -> {
            val buff = type.createNew();
            prices.put(buff, 299);
        });
    }

    @Override
    public Map<Buff, Integer> findPrices() {
        return prices;
    }

    @Override
    public Consumer<Player> createProcess(@NotNull DonateData donateData, @NotNull Buff buff, int price) {
        return player -> {
            var user = player.<User>getBungeePlayer();
            if (user.getCurrencyHandler().withdrawDonateCurrencyWithPriority(price)) {
                Fantasy.buffService().appendBuff(donateData.getUserUuid(), buff.getBuffType());
                Fantasy.clientProvider().notification(player, "&aВы успешно приобрели: ", "&6" + buff.getTitle());
                PacketRegistry.write(Fantasy.gameServer().nameSpace(), new UserDonateLogPacket(player.getUniqueId(), DonateType.BUFF, buff.getBuffType().name(), Instant.now().toEpochMilli()));
            } else {
                Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, ChatColor.RED + "Произошла ошибка во время выполнения операции");
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
            }
        };
    }

}
