package cupcake.nikky.imperium.tower.buff.exact.regeneration;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.util.Color;

import java.util.Collections;
import java.util.List;

public class RegenerationInTickBuff extends FantasyBuff {

    @Getter private final double percent;
    @Getter private final int duration;

    public RegenerationInTickBuff(final int duration, final double percent) {
        super(duration, "Регенерация здоровья");

        this.percent = percent;
        this.duration = duration;
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList(TextUtility.colorize(String.format("&6Каждую секунду восстанавливает &3%.1f%% &6здоровья в течении &3%dс", percent, duration)));
    }

    @Override
    public void onTick(@NotNull LivingEntity entity) {
        super.onTick(entity);

        if (entity instanceof Player player) {
            var user = player.<User>getBungeePlayer();
            user.changeHealth(user.findAttribute(ImperiumAttribute.HEALTH).findValue() * (percent / 100D));

            Fantasy.clientProvider().vignette((Player) entity, .2, Color.GREEN);
        } else {
            Fantasy.monsterProvider()
                    .findByUUID(entity.getUniqueId())
                    .ifPresent(monster -> monster.changeHealth(monster.findAttribute(ImperiumAttribute.HEALTH).findValue() * (percent / 100D)));
        }
    }
}
