package cupcake.nikky.imperium.tower.item.exact.potion;

import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.buff.exact.potion.PotionHeal;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import lombok.val;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HealPotion extends PotionItem {

    public HealPotion() {
        super(1013, "Зелье здоровья", Collections.emptyList(), BuffType.POTION_HEAL_4, 5);
    }

    @Override
    public BuffType getPotionBuff() {
        return switch (this.findPotionLevel()) {
            case 1 -> BuffType.POTION_HEAL_4;
            case 2 -> BuffType.POTION_HEAL_7;
            case 3 -> BuffType.POTION_HEAL_10;
            default -> super.getPotionBuff();
        };
    }

    @Override
    public List<String> getAdditionalDescription() {
        val buff = this.getPotionBuff().createNew(0, 0);
        buff.setLevel(getGeneralSettings().itemLevel());
        buff.getAttributeHandlers().forEach(handler -> handler.accept(getGeneralSettings().itemLevel()));
        return Arrays.asList(String.format("Восстанавливает %.1f%% здоровья в течении 7 секунд", ((PotionHeal) buff).getPercent() * 10), "&fПерезарядка: &65 секунд");
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.HEAL_POTION;
    }

    @Override
    public ImperiumItem create() {
        return new HealPotion();
    }

}
