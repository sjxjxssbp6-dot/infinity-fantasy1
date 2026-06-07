package cupcake.nikky.imperium.tower.item.exact.potion.event;

import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.potion.PotionItem;

import java.util.List;

public class ExperienceEventPotion extends PotionItem {

    public ExperienceEventPotion() {
        super(1014, "Весеннее зелье усиления опыта",
                List.of("Увеличение получаемого опыта на 40%",
                        "Длительность: &61 час"),
                BuffType.EVENT_EXP_BUFF, 5, ItemType.POTION_EVENT);
    }

    @Override
    public String getName() {
        return this.getGeneralName();
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.EVENT_EXPERIENCE_POTION;
    }

    @Override
    public ImperiumItem create() {
        return new ExperienceEventPotion();
    }

}
