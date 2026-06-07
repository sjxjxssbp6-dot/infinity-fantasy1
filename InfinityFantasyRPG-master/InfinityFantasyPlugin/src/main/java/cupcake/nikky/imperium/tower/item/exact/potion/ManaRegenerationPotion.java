package cupcake.nikky.imperium.tower.item.exact.potion;

import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.buff.exact.potion.PotionMana;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import lombok.val;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ManaRegenerationPotion extends PotionItem {

    public ManaRegenerationPotion() {
        super(1044, "Зелье маны", Collections.emptyList(), BuffType.POTION_REGENERATION_MANA_4, 30);
    }

    @Override
    public BuffType getPotionBuff() {
        return switch (this.findPotionLevel()) {
            case 1 -> BuffType.POTION_REGENERATION_MANA_4;
            case 2 -> BuffType.POTION_REGENERATION_MANA_7;
            case 3 -> BuffType.POTION_REGENERATION_MANA_10;
            default -> super.getPotionBuff();
        };
    }

    @Override
    public List<String> getAdditionalDescription() {
        val buff = this.getPotionBuff().createNew(0, 0);
        buff.setLevel(getGeneralSettings().itemLevel());
        buff.getAttributeHandlers().forEach(handler -> handler.accept(getGeneralSettings().itemLevel()));
        return Arrays.asList(String.format("Восстанавливает %.1f%% маны в течении 5 секунд", ((PotionMana) buff).getPercent() * 5), "&fПерезарядка: &630 секунд");
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.MANA_REGENERATION_POTION;
    }

    @Override
    public ImperiumItem create() {
        return new ManaRegenerationPotion();
    }

}
