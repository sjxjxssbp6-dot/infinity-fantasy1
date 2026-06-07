package cupcake.nikky.imperium.tower.corpse;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.currency.CurrencyType;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterData;
import cupcake.nikky.imperium.tower.entity.monster.data.MonsterDataHandler;
import cupcake.nikky.imperium.tower.entity.monster.exact.Alp;
import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

@RequiredArgsConstructor
public class CorpseInteract {

    private final Player player;
    private final Location corpseLocation;

    @EventHandler
    public void onCorpseInteract() {
        val location = corpseLocation.toBlockLocation();

        location.setYaw(0);
        location.setPitch(0);

        if (!CorpsePoints.getInstance().isCorpse(location)) return;

        val reward = CorpsePoints.getInstance().findReward(location);
        if (reward <= 0) {
            Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Произошла ошибка", "Попробуйте позже");
        } else {
            val user = player.<User>getBungeePlayer();
            val corpseSettings = user.findCorpseSettings();
            if (corpseSettings == null) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Произошла ошибка", "Попробуйте позже");
                return;
            }

            if (corpseSettings.isCorpseDetected(location)) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Вы уже находили ранее", "Пропавшего воина");
            } else {
                if (user.getCurrencyHandler().depositCurrency(CurrencyType.FIRM_PRIVATE, reward)) {
                    corpseSettings.registerDetectedCorpse(location);
                    Fantasy.clientProvider().cursorNotify(user, TextUtility.colorize("&2+&e" + reward), FantasyImage.CURRENCY.toTexture());
                    Fantasy.clientProvider().notification(player, SystemNotificationType.FINE, "Обнаружен пропавший воин!", "За каждого Вы получаете награду!");

                    var data = MonsterData.builder().level(user.getLevel()).unique(false).build();
                    var emptyMonster = new Alp(corpseLocation, data);
                    data.getLootWeightPool().setInitialSize(0);

                    var pool = data.getLootWeightPool().generateItemPool(user, emptyMonster, 10);
                    Fantasy.runLater(() -> pool.forEach(itemStack -> Fantasy.bukkitProvider().spawnItem(location, user.getUniqueId(), itemStack)));
                }
            }
        }

    }

}
