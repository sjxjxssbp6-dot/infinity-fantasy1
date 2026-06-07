package cupcake.nikky.imperium.tower.item.exact.potion.event;

import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.potion.PotionItem;

import java.util.List;

public class ElementalResistanceEventPotion extends PotionItem {

    public ElementalResistanceEventPotion() {
        super(1044, "Весеннее зелье усиления стихийных сопротивлений",
                List.of("Увеличение стихийных сопротивлений на 25%",
                        "Длительность: &61 час"),
                BuffType.EVENT_ELEM_RES_BUFF, 5, ItemType.POTION_EVENT);
    }

    @Override
    public String getName() {
        return this.getGeneralName();
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.EVENT_ELEM_RES_POTION;
    }

    @Override
    public ImperiumItem create() {
        return new ElementalResistanceEventPotion();
    }

}

