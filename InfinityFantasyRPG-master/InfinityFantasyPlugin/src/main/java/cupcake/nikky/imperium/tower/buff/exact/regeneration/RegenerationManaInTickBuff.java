package cupcake.nikky.imperium.tower.buff.exact.regeneration;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import lombok.Getter;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class RegenerationManaInTickBuff extends FantasyBuff {

    @Getter
    private final double percent;
    @Getter private final int duration;

    public RegenerationManaInTickBuff(final int duration, final double percent) {
        super(duration, "Регенерация маны");

        this.percent = percent;
        this.duration = duration;
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList(TextUtility.colorize(String.format("&6Каждую секунду восстанавливает &3%.1f%% &6маны в течении &3%dс", percent, duration)));
    }

    @Override
    public void onTick(@NotNull LivingEntity entity) {
        super.onTick(entity);

        if (entity instanceof Player) {
            Fantasy.userService().findById(entity.getUniqueId()).ifPresent(user ->
                    user.getManaController().changeMana(user.findAttribute(ImperiumAttribute.MANA).findValue() * (percent / 100D)));
        }
    }
}
