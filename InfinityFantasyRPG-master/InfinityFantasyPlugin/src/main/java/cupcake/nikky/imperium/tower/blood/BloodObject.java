package cupcake.nikky.imperium.tower.blood;

import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.BloodType;
import cupcake.nikky.imperium.tower.item.Icons;
import cupcake.nikky.imperium.tower.tree.TreeIcon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public enum BloodObject {

    BASE_BLOOD(BloodType.DEFAULT, null, FantasyImage.BASE_BLOOD.toTexture(), "&6Красная кровь", -1),
    WHITE_BLOOD(BloodType.WHITE, null, FantasyImage.WHITE_BLOOD.toTexture(), "&6Белая кровь", 599),
    MULTI_COLORED_BLOOD(BloodType.MULTI_COLORED, null, FantasyImage.MULTICOLORED_BLOOD.toTexture(), "&6Разноцветная кровь", 699),
    HEART_BLOOD(BloodType.HEART, null, FantasyImage.HEART_BLOOD.toTexture(), "&6Кровь в виде сердец", 699);

    private final BloodType bloodType;
    private final ItemStack icon;
    private final String texture;
    private final String title;
    private final int price;

    public String getRequirementDescription() {
        return "&6Покупка в магазине";
    }

}
