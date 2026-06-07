package cupcake.nikky.imperium.tower.buff.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attack.damage.DamageType;
import cupcake.nikky.imperium.tower.attack.damage.InfluenceType;
import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NecrosisDebuff extends FantasyBuff {

    private double damage;

    public NecrosisDebuff(final double value, final int duration) {
        super(duration, "Некроз");
        this.setShouldInvalidateAfterDeath(true);
        this.registerPercentAttribute(ImperiumAttribute.MOVEMENT_SPEED, -25D);
        this.damage = value;
    }

    @Override
    public List<String> getDescription() {
        val result = super.getDescription();
        result.add(TextUtility.colorize("&6Периодический урон в &3%.1fед.", damage));
        return result;
    }

    @Override
    public void onTick(@NotNull final LivingEntity entity) {
        super.onTick(entity);

        Fantasy.damageProvider().processDamage(null, entity).setDamage(InfluenceType.PHYSICAL, damage).setDamageType(DamageType.OVER_TIME).apply();
    }

}
