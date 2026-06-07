package cupcake.nikky.imperium.tower.selection;

import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.aura.selection.AuraSelection;
import cupcake.nikky.imperium.tower.blood.BloodSelection;
import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.death.DeathSelection;
import cupcake.nikky.imperium.tower.item.Icons;
import cupcake.nikky.imperium.tower.item.ItemRare;
import cupcake.nikky.imperium.tower.mount.selection.MountSelection;
import lombok.Getter;
import me.func.mod.reactive.ReactiveButton;
import me.func.mod.ui.menu.selection.Selection;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.item.Items;

public class GeneralSelection {

    @Getter
    private static final GeneralSelection instance = new GeneralSelection();

    private final Selection selection;
    private final ItemStack mountIcon = Items.builder().type(Material.MONSTER_EGG).meta(SpawnEggMeta.class, spawnEggMeta -> spawnEggMeta.setSpawnedType(EntityType.HORSE)).build();
    private final String auraIcon = AuraObject.CHRISTMAS_AURA.getFantasyAura().resourceLocation();
    private final ItemStack bloodIcon = Icons.WHITE_BLOOD;
    private final String deathIcon = FantasyImage.DEATH_RED_FADE.toTexture();

    public GeneralSelection() {
        this.selection = Selection.builder()
                .title("Выбор визуала")
                .rows(4)
                .columns(3)
                .hint("Открыть")
                .storage(
                        new ReactiveButton().item(mountIcon).title("&6Спутники").onLeftClick((player, i, reactiveButton) -> new MountSelection(player).open()),
                        new ReactiveButton().item(bloodIcon).title("&6Кровь").onLeftClick((player, i, reactiveButton) -> new BloodSelection(player).open()),
                        new ReactiveButton().texture(auraIcon).title("&6Ауры").onLeftClick((player, i, reactiveButton) -> new AuraSelection(player).open()),
                        new ReactiveButton().texture(deathIcon).title("&6Эффекты смерти")
                                .hover("&fПодобные эффекты появляются после",
                                        "&fубийства игрока, а также монстра",
                                        "редкости %sэпический &fи выше".formatted(ItemRare.EPIC.getColor())
                                )
                                .onLeftClick((player, i, reactiveButton) -> new DeathSelection(player).open())
                )
                .build();
    }

    public void open(@NotNull final Player player) {
        this.selection.open(player);
    }

}
