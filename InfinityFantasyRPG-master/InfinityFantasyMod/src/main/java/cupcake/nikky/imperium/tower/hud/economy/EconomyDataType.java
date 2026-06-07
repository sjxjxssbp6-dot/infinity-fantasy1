package cupcake.nikky.imperium.tower.hud.economy;

import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.data.EconomyStorage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.IntSupplier;

@Getter
@RequiredArgsConstructor
public enum EconomyDataType {

    FLOWER(FantasyImage.FLOWER_1.toTexture(), () -> EconomyStorage.getInstance().getFlower()),
    MINING(FantasyImage.MINE.toTexture(), () -> EconomyStorage.getInstance().getMine()),
    GRAN(FantasyImage.GRAN.toTexture(), () -> EconomyStorage.getInstance().getGran()),
    SPECIAL(FantasyImage.CURRENCY.toTexture(), () -> EconomyStorage.getInstance().getSpecial() + EconomyStorage.getInstance().getPrivateSpecial());

    private final String icon;
    private final IntSupplier dataSupplier;

}
