package cupcake.nikky.imperium.tower.death;

import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle.DeathType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum DeathObject {

    NONE(DeathType.NONE, new DeathSound(), new ItemStack(Material.BARRIER), "", "&6Без эффекта", -1),
    RED_LINE_UP(DeathType.RED_LINE_UP, new DeathSound(Sound.ENTITY_GENERIC_EXTINGUISH_FIRE), null, FantasyImage.DEATH_RED_FADE.toTexture(), "&6Красное сияние", 699),
    DARK_EXPLOSION(DeathType.SMALL_DARK_EXPLOSION, new DeathSound(Sound.ENTITY_GENERIC_EXPLODE), null, FantasyImage.DEATH_DARK_EXPLOSION.toTexture(), "&6Темный взрыв", 499),
    PURPLE_EXPLOSION(DeathType.PURPLE_EXPLOSION, new DeathSound(Sound.ENTITY_ENDERMEN_SCREAM), null, FantasyImage.DEATH_PURPLE_EXPLOSION.toTexture(), "&6Фиолетовый взрыв из искр", 599);

    private final DeathType deathType;
    private final DeathSound deathSound;
    private final ItemStack icon;
    private final String texture;
    private final String title;
    private final int price;

    public String getRequirementDescription() {
        return "&6Покупка в магазине";
    }

    public static DeathObject findByType(@NotNull final DeathType type) {
        return Arrays.stream(values()).filter(deathObject -> deathObject.getDeathType() == type).findAny().orElse(null);
    }

}
