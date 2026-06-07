package cupcake.nikky.imperium.tower.hud.economy;

import cupcake.nikky.imperium.tower.FantasyMod;
import cupcake.nikky.imperium.tower.data.EconomyStorage;
import cupcake.nikky.imperium.tower.hud.DataBox;
import cupcake.nikky.imperium.tower.utility.NumberUtility;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.enginex.element.HorizontalLayout;
import ru.cristalix.enginex.util.Relative;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EconomyContext extends HorizontalLayout {

    @Getter
    private final Map<EconomyDataType, DataBox> elements = new HashMap<>();

    public EconomyContext() {
        super(5D);

        this.initialize();
    }

    public void applyClean(@NotNull final EconomyDataType... types) {
        for (EconomyDataType value : types) {
            val dataBox = new DataBox();
            dataBox.updateContent(value.getIcon(), NumberUtility.format(0));
            this.elements.put(value, dataBox);
        }

        this.clearChilds();
        this.addChild(elements.values().toArray(new DataBox[0]));
    }

    public void apply(@NotNull final EconomyDataType... types) {
        for (EconomyDataType value : types) {
            val dataBox = new DataBox();
            dataBox.updateContent(value.getIcon(), NumberUtility.format(value.getDataSupplier().getAsInt()));
            this.elements.put(value, dataBox);
        }

        this.clearChilds();
        this.addChild(elements.values().toArray(new DataBox[0]));

        this.initializeTooltip();
    }

    public void applyAll() {
        this.apply(EconomyDataType.values());
    }

    private void initializeTooltip() {
        elements.forEach((economyDataType, dataBox) -> {
            switch (economyDataType) {
                case GRAN: {
                    FantasyMod.tooltipHandler().applyHoverTooltip(
                            dataBox,
                            box -> Arrays.asList("Граны", "Точное количество: " + NumberUtility.classicalFormat(economyDataType.getDataSupplier().getAsInt()))
                    );
                    break;
                }
                case MINING: {
                    FantasyMod.tooltipHandler().applyHoverTooltip(
                            dataBox,
                            box -> Arrays.asList("Мирны", "Точное количество: " + NumberUtility.classicalFormat(economyDataType.getDataSupplier().getAsInt()))
                    );
                    break;
                }
                case FLOWER: {
                    FantasyMod.tooltipHandler().applyHoverTooltip(dataBox, "Цветки");
                    break;
                }
                case SPECIAL: {
                    FantasyMod.tooltipHandler().applyHoverTooltip(
                            dataBox,
                            box -> Arrays.asList(
                                    "Фирм-монеты",
                                    "Точное количество: " + NumberUtility.classicalFormat(economyDataType.getDataSupplier().getAsInt()),
                                    "",
                                    "Личные фирм-монеты: " + NumberUtility.classicalFormat(EconomyStorage.getInstance().getPrivateSpecial()),
                                    "Обычные фирм-монеты: " + NumberUtility.classicalFormat(EconomyStorage.getInstance().getSpecial()))
                    );
                    break;
                }
            }
        });
    }

    private void initialize() {
        this.setOrigin(Relative.LEFT).setAlign(Relative.LEFT);
    }

    public void update() {
        this.elements.forEach((economyDataType, dataBox) -> dataBox.updateContent(economyDataType.getIcon(), NumberUtility.format(economyDataType.getDataSupplier().getAsInt())));
    }

}
