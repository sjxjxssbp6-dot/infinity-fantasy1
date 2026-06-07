package cupcake.nikky.imperium.tower.buff.exact.aspect;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.LivingEntity;

import java.util.Collections;
import java.util.List;

public class RegenerationDistortionDebuff extends FantasyBuff {

    public RegenerationDistortionDebuff(final double value, final int duration) {
        super(duration, "Искажение регенерации");

        this.registerPercentAttribute(ImperiumAttribute.REGENERATION_HEALTH, value);
        this.registerPercentAttribute(ImperiumAttribute.REGENERATION_HEALTH_IN_FIGHT, value);
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList(TextUtility.colorize("&6Находясь в бою регенерирует &31% &6здоровья в секунду"));
    }

    @Override
    public void onReceive(LivingEntity entity) {
        Fantasy.monsterProvider().findByUUID(entity.getUniqueId()).ifPresent(monster ->
                getAttributes().getAll().forEach(pair ->
                        getAttributes().register(pair.getFirst(), monster.findAttribute(ImperiumAttribute.HEALTH).findValue() * (pair.getSecond().getValue() / 100D))));
    }

}
