package cupcake.nikky.imperium.tower.attack.damage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DamageFlag {

    EVASION(1),
    CRITICAL(2),
    NOT_AVAILABLE(4),
    IGNORE_KNOCK_BACK(8),
    INVULNERABILITY(16);

    @Getter
    private final int flag;

}
