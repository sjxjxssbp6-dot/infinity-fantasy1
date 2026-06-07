package cupcake.nikky.imperium.tower.item.exact.potion;

import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.client.protocol.attribute.ImperiumAttribute;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.ItemObject;
import lombok.val;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpeedPotion extends PotionItem {

    public SpeedPotion() {
        super(1012, "Зелье скорости", Collections.emptyList(), BuffType.POTION_SPEED_4, 3);
    }

    @Override
    public BuffType getPotionBuff() {
        return switch (this.findPotionLevel()) {
            case 1 -> BuffType.POTION_SPEED_4;
            case 2 -> BuffType.POTION_SPEED_7;
            case 3 -> BuffType.POTION_SPEED_10;
            default -> super.getPotionBuff();
        };
    }

    @Override
    public List<String> getAdditionalDescription() {
        val buff = this.getPotionBuff().createNew();
        buff.setLevel(getGeneralSettings().itemLevel());
        buff.getAttributeHandlers().forEach(handler -> handler.accept(getGeneralSettings().itemLevel()));
        return Arrays.asList(String.format("+%.1f%% к скорости передвижения на 15 минут", buff.findAttribute(ImperiumAttribute.MOVEMENT_SPEED).findValue()),
                "&fПерезарядка: &63 секунды");
    }

    @Override
    public ItemObject getObject() {
        return ItemObject.SPEED_POTION;
    }

    @Override
    public ImperiumItem create() {
        return new SpeedPotion();
    }

}
