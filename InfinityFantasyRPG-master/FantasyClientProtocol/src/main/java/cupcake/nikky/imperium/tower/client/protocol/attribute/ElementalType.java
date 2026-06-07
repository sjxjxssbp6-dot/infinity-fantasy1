package cupcake.nikky.imperium.tower.client.protocol.attribute;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ElementalType {

    FIRE(ImperiumAttribute.FIRE_RESISTANCE, ImperiumAttribute.FIRE_RESISTANCE_PENETRATION),
    COLD(ImperiumAttribute.COLD_RESISTANCE, ImperiumAttribute.COLD_RESISTANCE_PENETRATION),
    LIGHTNING(ImperiumAttribute.LIGHTNING_RESISTANCE, ImperiumAttribute.LIGHTNING_RESISTANCE_PENETRATION),
    CHAOS(ImperiumAttribute.CHAOS_RESISTANCE, ImperiumAttribute.CHAOS_RESISTANCE_PENETRATION);

    private final ImperiumAttribute resistance;
    private final ImperiumAttribute penetration;

}
