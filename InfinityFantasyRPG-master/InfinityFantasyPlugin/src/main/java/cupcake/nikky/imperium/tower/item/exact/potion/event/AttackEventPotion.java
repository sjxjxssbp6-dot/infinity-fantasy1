package cupcake.nikky.imperium.tower.item.exact.potion.event;

import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import cupcake.nikky.imperium.tower.item.ItemType;
import cupcake.nikky.imperium.tower.item.exact.potion.PotionItem;

import java.util.List;

public class AttackEventPotion extends PotionItem {

    public AttackEventPotion() {
        super(1015, "Весеннее зелье усиления атаки",
                List.of("Увеличение силы атаки и силы стихии на 15%",
                        "Длительность: &61 час"),
                BuffType.EVENT_ATTACK_BUFF, 5, ItemType.POTION_EVENT);
    }

    @Override
    public String getName() {
        return this.getGeneralName();
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.EVENT_ATTACK_POTION;
    }

    @Override
    public ImperiumItem create() {
        return new AttackEventPotion();
    }

}
