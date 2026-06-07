package cupcake.nikky.imperium.tower.item.exact.potion;

import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import lombok.val;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CriticalRatePotion extends PotionItem {

    public CriticalRatePotion() {
        super(1014, "Зелье увеличения шанса крит. удара", Collections.emptyList(), BuffType.POTION_CRITICAL_CHANCE_4, 3);
    }

    @Override
    public BuffType getPotionBuff() {
        return switch (this.findPotionLevel()) {
            case 1 -> BuffType.POTION_CRITICAL_CHANCE_4;
            case 2 -> BuffType.POTION_CRITICAL_CHANCE_7;
            case 3 -> BuffType.POTION_CRITICAL_CHANCE_10;
            default -> super.getPotionBuff();
        };
    }

    @Override
    public List<String> getAdditionalDescription() {
        val buff = this.getPotionBuff().createNew();
        buff.setLevel(getGeneralSettings().itemLevel());
        buff.getAttributeHandlers().forEach(handler -> handler.accept(getGeneralSettings().itemLevel()));
        return Arrays.asList(String.format("%.0f%% увеличение шанса крит. удара на 15 минут", buff.getAttributes().find(ImperiumAttribute.CRITICAL_RATE).getIncrease() * 100),
                "&fПерезарядка: &63 секунды");
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.POTION_CRITICAL_CHANCE;
    }

    @Override
    public ImperiumItem create() {
        return new CriticalRatePotion();
    }
}
