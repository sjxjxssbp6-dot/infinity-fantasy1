package cupcake.nikky.imperium.tower.item.exact.potion.event;

import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.potion.PotionItem;

import java.util.List;

public class ChaosResistanceEventPotion extends PotionItem {

    public ChaosResistanceEventPotion() {
        super(1041, "Весеннее зелье усиления стихийных сопротивлений",
                List.of("Увеличение сопротивления хаосу на 20%",
                        "Длительность: &61 час"),
                BuffType.EVENT_CHAOS_RES_BUFF, 5, ItemType.POTION_EVENT);
    }

    @Override
    public String getName() {
        return this.getGeneralName();
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.EVENT_CHAOS_RES_POTION;
    }

    @Override
    public ImperiumItem create() {
        return new ChaosResistanceEventPotion();
    }

}
