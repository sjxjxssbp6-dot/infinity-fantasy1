package cupcake.nikky.imperium.tower.item.exact.gear.armour;

import cupcake.nikky.imperium.tower.item.modifier.base.ModifierBaseCreatorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BaseType {

    FIRM(
            "Фирм-Шлем", ModifierBaseCreatorType.FIRM_HELMET,
            "Фирм-Нагрудник", ModifierBaseCreatorType.FIRM_BODY,
            "Фирм-Поножи", ModifierBaseCreatorType.FIRM_LEGGINGS,
            "Фирм-Боты", ModifierBaseCreatorType.FIRM_BOOTS),
    REKN(
            "Рекн-Шлем", ModifierBaseCreatorType.REKN_HELMET,
            "Рекн-Нагрудник", ModifierBaseCreatorType.REKN_BODY,
            "Рекн-Поножи", ModifierBaseCreatorType.REKN_LEGGINGS,
            "Рекн-Боты", ModifierBaseCreatorType.REKN_BOOTS),
    SALT(
            "Салт-Шлем", ModifierBaseCreatorType.SALT_HELMET,
            "Салт-Нагрудник", ModifierBaseCreatorType.SALT_BODY,
            "Салт-Поножи", ModifierBaseCreatorType.SALT_LEGGINGS,
            "Салт-Боты", ModifierBaseCreatorType.SALT_BOOTS
    ),
    TARN("Тарн-Шлем", ModifierBaseCreatorType.TARN_HELMET,
            "Тарн-Нагрудник", ModifierBaseCreatorType.TARN_BODY,
            "Тарн-Поножи", ModifierBaseCreatorType.TARN_LEGGINGS,
            "Тарн-Боты", ModifierBaseCreatorType.TARN_BOOTS
    ),
    KELN("Келн-Шлем", ModifierBaseCreatorType.KELN_HELMET,
            "Келн-Нагрудник", ModifierBaseCreatorType.KELN_BODY,
            "Келн-Поножи", ModifierBaseCreatorType.KELN_LEGGINGS,
            "Келн-Боты", ModifierBaseCreatorType.KELN_BOOTS
    );

    private final String helmetTitle;
    private final ModifierBaseCreatorType helmetBaseCreatorType;
    private final String bodyTitle;
    private final ModifierBaseCreatorType bodyBaseCreatorType;
    private final String leggingsTitle;
    private final ModifierBaseCreatorType leggingsBaseCreatorType;
    private final String bootsTitle;
    private final ModifierBaseCreatorType bootsBaseCreatorType;

}
