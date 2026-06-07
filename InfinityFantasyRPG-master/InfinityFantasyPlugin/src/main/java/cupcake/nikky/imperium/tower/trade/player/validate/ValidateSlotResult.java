package cupcake.nikky.imperium.tower.trade.player.validate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ValidateSlotResult {

    FULL_INVENTORY("Не хватает места в инвентаре"),
    INVALID_ITEM("Некоторые предложенные предметы\nне подходят условиям"),
    PLAYER_OFFLINE("Игрок не найден"),
    ACCEPT("");

    private final String error;

}
