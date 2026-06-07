package cupcake.nikky.imperium.tower.buff.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.buff.FantasyBuff;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import lombok.val;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.LivingEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ElementalResistanceInvert extends FantasyBuff {

    public ElementalResistanceInvert() {
        super(3, "Инвертация стихий");
    }

    @Override
    public void onReceive(LivingEntity entity) {
        this.addAttributeHandler(level -> {
            val resistances = Arrays.asList(ImperiumAttribute.FIRE_RESISTANCE, ImperiumAttribute.COLD_RESISTANCE, ImperiumAttribute.CHAOS_RESISTANCE, ImperiumAttribute.LIGHTNING_RESISTANCE);
            Fantasy.userService().findById(entity.getUniqueId()).ifPresent(user ->
                    resistances.forEach(attribute -> this.registerValueAttribute(attribute, user.findAttribute(attribute).findValue() * -2)));
        });
    }

    @Override
    public List<String> getDescription() {
        return Collections.singletonList(TextUtility.colorize("&6Ваши сопротивления инвертируются"));
    }

}
