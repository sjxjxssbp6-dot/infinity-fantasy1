package cupcake.nikky.imperium.tower.indicator;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.anfanik.steda.api.utility.TextUtility;

@Builder
@Getter
@Setter
public class DamageIndicatorSettings {

    private double damage;
    private boolean critical;
    private boolean evasion;
    private boolean invulnerability;

    public String createText() {
        if (evasion) {
            return TextUtility.colorize("%sПромах", "&e");
        } else if (invulnerability) {
            return TextUtility.colorize("%sНеуязвимость", "&6");
        } else return TextUtility.colorize("%s%.0f", critical ? "&e" : "&f", damage);
    }

}
