package cupcake.nikky.imperium.tower.item.exact.gear.weapon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WeaponType {

    FIRM_SWORD(760, "Фирм-клинок"),
    FIRM_DAGGER(740, "Фирм-кинжал"),
    FIRM_AXE(760, "Фирм-топор"),
    FIRM_SPEAR(800, "Фирм-копье"),

    REKN_SWORD(720, "Рекн-клинок"),
    REKN_DAGGER(740, "Рекн-кинжал"),
    REKN_AXE(780, "Рекн-топор"),
    REKN_SPEAR(780, "Рекн-копье"),

    SALT_SWORD(700, "Салт-клинок"),
    SALT_DAGGER(700, "Салт-кинжал"),
    SALT_AXE(750, "Салт-топор"),
    SALT_SPEAR(760, "Салт-копье"),

    TARN_SWORD(680, "Тарн-клинок"),
    TARN_DAGGER(680, "Тарн-кинжал"),
    TARN_AXE(780, "Тарн-топор"),
    TARN_SPEAR(880, "Тарн-копье"),

    KELN_SWORD(660, "Келн-клинок"),
    KELN_DAGGER(720, "Келн-кинжал"),
    KELN_AXE(800, "Келн-топор"),
    KELN_SPEAR(820, "Келн-копье");

    private final int attackSpeed;
    private final String title;

}
