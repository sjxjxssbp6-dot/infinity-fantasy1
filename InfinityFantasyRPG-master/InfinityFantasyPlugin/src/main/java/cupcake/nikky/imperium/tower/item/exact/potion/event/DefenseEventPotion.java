package cupcake.nikky.imperium.tower.item.exact.potion.event;

import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.potion.PotionItem;

import java.util.List;

public class DefenseEventPotion extends PotionItem {

    public DefenseEventPotion() {
        super(1012, "Весеннее зелье усиления защиты",
                List.of("Увеличение защиты на 15%",
                        "Длительность: &61 час"),
                BuffType.EVENT_DEFENSE_BUFF, 5, ItemType.POTION_EVENT);
    }

    @Override
    public String getName() {
        return this.getGeneralName();
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.EVENT_DEFENSE_POTION;
    }

    @Override
    public ImperiumItem create() {
        return new DefenseEventPotion();
    }

}
