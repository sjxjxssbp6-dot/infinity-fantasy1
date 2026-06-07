package cupcake.nikky.imperium.tower.entity.monster.loot.other;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.chat.ChatType;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.entity.monster.Monster;
import cupcake.nikky.imperium.tower.level.LevelStorage;
import cupcake.nikky.imperium.tower.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

@Getter
@AllArgsConstructor
public class GranLoot {

    private int currency;

    public void apply(@NotNull final Collection<User> users, @NotNull final Monster monster) {
        if (currency == 0) return;

        val higherDamageDealer = monster.getBattleData().values().stream().max(Comparator.comparingInt(data -> Math.round((float) data.getDamage()))).orElse(null);

        User receiver;

        if (higherDamageDealer == null)
            receiver = users.stream().max(Comparator.comparingInt(User::getLevel)).orElse(null);
        else receiver = users.stream().filter(user -> user.getUniqueId().equals(higherDamageDealer.getUserUuid())).findFirst().orElse(null);

        if (receiver == null) return;

        val monsterLevel = Math.min(monster.getLevel(), LevelStorage.getInstance().getStartShardEdgeLevel());
        val receiverLevel = Math.min(receiver.getLevelController().getLevel() - 2, LevelStorage.getInstance().getStartShardEdgeLevel());
        val deltaLevel = Math.max(0, receiverLevel - monsterLevel);

        currency /= (int) Math.pow(2, deltaLevel);
        currency = this.calculateCurrencyReward(currency, receiver.getLevelController().getLevel(), monster.getLevel());

        if (currency == 0) return;

        receiver.changeCurrency(CurrencyType.GRAN, currency);
        Fantasy.bukkitProvider().sendMessageToExactChat(ChatType.SYSTEM, "&c+" + currency + " &6гран", Collections.singletonList(receiver.getSpigot()));
        Fantasy.clientProvider().cursorNotify(receiver, TextUtility.colorize("&2+&c" + currency), FantasyImage.GRAN.toTexture());
    }

    private int calculateCurrencyReward(final int defaultGran, final int killerLevel, final int monsterLevel) {
        return Math.round(defaultGran * (killerLevel < monsterLevel ? (monsterLevel - killerLevel + 1) / 1.5F : 1));
    }

}
