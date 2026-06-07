package cupcake.nikky.imperium.tower.item.exact.potion.event;

import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.potion.PotionItem;

import java.util.List;

public class HealthEventPotion extends PotionItem {

    public HealthEventPotion() {
        super(1013, "Весеннее зелье усиления здоровья",
                List.of("Увеличение здоровья на 20%",
                        "Длительность: &61 час"),
                BuffType.EVENT_HEALTH_BUFF, 5, ItemType.POTION_EVENT);
    }

    @Override
    public String getName() {
        return this.getGeneralName();
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.EVENT_HEALTH_POTION;
    }

    @Override
    public ImperiumItem create() {
        return new HealthEventPotion();
    }

}
