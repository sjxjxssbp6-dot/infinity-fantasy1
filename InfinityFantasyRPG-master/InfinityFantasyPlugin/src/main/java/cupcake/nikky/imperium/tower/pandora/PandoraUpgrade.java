package cupcake.nikky.imperium.tower.pandora;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.donate.DonateType;
import cupcake.nikky.imperium.tower.protocol.packet.UserDonateLogPacket;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.function.BiConsumer;

public record PandoraUpgrade(String title, int price, int alternatePrice, BiConsumer<User, PandoraData> upgradeHandler) {

    public boolean tryUpgrade(@NotNull final User user, @NotNull final PandoraData data, final boolean donate) {
        var player = user.getSpigot();
        if (donate && !user.getCurrencyHandler().withdrawDonateCurrencyWithPriority(alternatePrice())
                || !donate && !user.getCurrencyHandler().withdrawCurrency(CurrencyType.GRAN, (int) (price(user) * data.getAmplitude()))) {
            this.cantUpgrade(player);
            return false;
        }

        if (donate) {
            PacketRegistry.write(
                    Fantasy.gameServer().nameSpace(),
                    new UserDonateLogPacket(user.getUniqueId(), DonateType.PANDORA_CHEST, alternatePrice(), Instant.now().toEpochMilli())
            );
        }

        upgradeHandler.accept(user, data);
        data.setCurrentUpgrade(data.getCurrentUpgrade() + 1);
        var text = "&6%.0f&r%s".formatted(donate ? alternatePrice() : price(user) * data.getAmplitude(), donate ? "\uE104" : "\uE0FE");
        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fВы &6улучшили &fящик Пандоры за %s".formatted(text));
        Fantasy.clientProvider().notification(player, SystemNotificationType.FINE, "&fВы &6улучшили &fящик Пандоры за %s".formatted(text));
        Fantasy.bukkitProvider().sound(player, AdvancedSounds.COINS_GIVE);
        return true;
    }

    private void cantUpgrade(@NotNull final Player player) {
        Fantasy.bukkitProvider().sendMessage(player, ChatType.SYSTEM, "&fНедостаточно средств");
        Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "&fНедостаточно средств");
        Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
    }

    public int price(@NotNull final User user) {
        return (int) Math.round(price() * user.findAttribute(ImperiumAttribute.PANDORA_BASE_PRICE).findValue());
    }

}
