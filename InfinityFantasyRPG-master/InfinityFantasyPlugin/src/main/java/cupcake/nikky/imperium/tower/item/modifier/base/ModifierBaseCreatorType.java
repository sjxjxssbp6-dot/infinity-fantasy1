package cupcake.nikky.imperium.tower.item.modifier.base;

import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ModifierBaseCreatorType {

    FIRM_SWORD(new ModifierBaseCreator().applyPhysicalDamage(9, 19).applyValue(ImperiumAttribute.CRITICAL_DAMAGE, 1, 5), 1),
    FIRM_DAGGER(new ModifierBaseCreator().applyPhysicalDamage(7, 12).applyValue(ImperiumAttribute.CRITICAL_RATE, 1, 2), 1),
    FIRM_SPEAR(new ModifierBaseCreator().applyPhysicalDamage(12, 19), 1),
    FIRM_AXE(new ModifierBaseCreator().applyPhysicalDamage(10, 22), 1),

    REKN_SWORD(new ModifierBaseCreator().applyPhysicalDamage(20, 34).applyValue(ImperiumAttribute.CRITICAL_DAMAGE, 3, 6), 15),
    REKN_DAGGER(new ModifierBaseCreator().applyPhysicalDamage(8, 32).applyValue(ImperiumAttribute.CRITICAL_RATE, 1, 3), 15),
    REKN_SPEAR(new ModifierBaseCreator().applyPhysicalDamage(19, 39), 15),
    REKN_AXE(new ModifierBaseCreator().applyPhysicalDamage(22, 46).applyValue(ImperiumAttribute.MOVEMENT_SPEED, 4, 8), 15),

    SALT_SWORD(new ModifierBaseCreator().applyPhysicalDamage(54, 100).applyValue(ImperiumAttribute.CRITICAL_DAMAGE, 5, 10), 35),
    SALT_DAGGER(new ModifierBaseCreator().applyPhysicalDamage(15, 62).applyValue(ImperiumAttribute.CRITICAL_RATE, 2, 3), 35),
    SALT_SPEAR(new ModifierBaseCreator().applyPhysicalDamage(64, 97).applyValue(ImperiumAttribute.HEALTH, 50, 150), 35),
    SALT_AXE(new ModifierBaseCreator().applyPhysicalDamage(73, 110), 35),

    TARN_SWORD(new ModifierBaseCreator().applyPhysicalDamage(75, 124).applyValue(ImperiumAttribute.CRITICAL_DAMAGE, 5, 15), 50),
    TARN_DAGGER(new ModifierBaseCreator().applyPhysicalDamage(22, 91).applyValue(ImperiumAttribute.CRITICAL_RATE, 2, 4), 50),
    TARN_SPEAR(new ModifierBaseCreator().applyPhysicalDamage(90, 135), 50),
    TARN_AXE(new ModifierBaseCreator().applyPhysicalDamage(97, 145).applyValue(ImperiumAttribute.CRITICAL_RATE, 2, 3), 50),

    KELN_SWORD(new ModifierBaseCreator().applyPhysicalDamage(74, 154).applyValue(ImperiumAttribute.CRITICAL_DAMAGE, 10, 25), 60),
    KELN_DAGGER(new ModifierBaseCreator().applyPhysicalDamage(26, 105).applyValue(ImperiumAttribute.CRITICAL_RATE, 2, 5), 60),
    KELN_SPEAR(new ModifierBaseCreator().applyPhysicalDamage(121, 164), 60),
    KELN_AXE(new ModifierBaseCreator().applyPhysicalDamage(115, 172).applyValue(ImperiumAttribute.CRITICAL_RATE, 1, 4), 60),

    FIRM_HELMET(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 1, 2), 1),
    FIRM_BODY(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 2, 3).applyValue(ImperiumAttribute.REGENERATION_HEALTH, 10, 20), 1),
    FIRM_LEGGINGS(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 2, 3), 1),
    FIRM_BOOTS(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 1, 2).applyValue(ImperiumAttribute.MOVEMENT_SPEED, 3, 6), 1),

    REKN_HELMET(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 2, 4), 15),
    REKN_BODY(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 2, 6)
            .applyValue(ImperiumAttribute.REGENERATION_HEALTH, 50, 100)
            .applyValue(ImperiumAttribute.MOVEMENT_SPEED, 2, 5), 15
    ),
    REKN_LEGGINGS(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 2, 4).applyValue(ImperiumAttribute.HEALTH, 50, 150), 15),
    REKN_BOOTS(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 2, 6), 15),

    SALT_HELMET(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 4, 6), 35),
    SALT_BODY(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 4, 8).applyValue(ImperiumAttribute.REGENERATION_HEALTH, 75, 125), 35),
    SALT_LEGGINGS(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 4, 8), 35),
    SALT_BOOTS(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 4, 6), 35),

    TARN_HELMET(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 6, 8), 50),
    TARN_BODY(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 6, 10).applyValue(ImperiumAttribute.REGENERATION_HEALTH, 100, 200), 50),
    TARN_LEGGINGS(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 6, 10), 50),
    TARN_BOOTS(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 6, 8).applyValue(ImperiumAttribute.CHAOS_RESISTANCE, 10, 15), 50),

    KELN_HELMET(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 10, 12), 60),
    KELN_BODY(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 10, 15).applyValue(ImperiumAttribute.REGENERATION_HEALTH, 300, 600), 60),
    KELN_LEGGINGS(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 10, 15), 60),
    KELN_BOOTS(new ModifierBaseCreator().applyValue(ImperiumAttribute.DEFENSE, 10, 12)
            .applyValue(ImperiumAttribute.FIRE_RESISTANCE, 10, 15)
            .applyValue(ImperiumAttribute.MOVEMENT_SPEED, 5, 10), 60
    );

    private final ModifierBaseCreator creator;
    private final int requiredUserLevel;

}
