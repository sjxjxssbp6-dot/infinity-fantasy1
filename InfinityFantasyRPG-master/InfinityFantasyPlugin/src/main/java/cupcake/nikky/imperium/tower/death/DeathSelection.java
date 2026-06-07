package cupcake.nikky.imperium.tower.death;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.func.mod.reactive.ReactiveButton;
import me.func.mod.ui.menu.selection.Selection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class DeathSelection {

    private final Player player;
    private final Map<DeathObject, ReactiveButton> buttons = new HashMap<>();

    public void open() {
        val user = player.<User>getBungeePlayer();
        if (user == null) return;

        var donateData = player.<User>getBungeePlayer().findDonateSettings().donateData();
        if (donateData == null) return;

        val builder = Selection.builder().title("Выбор эффекта смерти").columns(3).rows(4).hint("Выбрать");

        final List<ReactiveButton> storage = new ArrayList<>();

        for (DeathObject value : DeathObject.values()) {
            val button = new ReactiveButton();

            if (value.getIcon() != null) {
                button.item(value.getIcon());
            } else {
                button.texture(value.getTexture());
            }

            val canUse = value == DeathObject.NONE || donateData.containsMetadata(value.name());

            storage.add(button.hover(this.createDescription(value, user, canUse))
                    .title(value.getTitle())
                    .description(user.getCurrentDeathType() == value.getDeathType() ? "&aВыбрана" : " ")
                    .onLeftClick((player1, i, reactiveButton) -> {
                        val newestCanUse = value == DeathObject.NONE || donateData.containsMetadata(value.name());
                        if (!newestCanUse) {
                            Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                            return;
                        }

                        user.setCurrentDeathType(value.getDeathType());
                        reactiveButton.hover(this.createDescription(value, user, newestCanUse));

                        buttons.forEach((deathObject, reactiveButton1) -> {
                            if (deathObject == value) return;

                            reactiveButton1.setDescription(" ");
                            reactiveButton1.hover(this.createDescription(deathObject, user, newestCanUse));
                        });

                        reactiveButton.setDescription("&aВыбрана");
                        Fantasy.bukkitProvider().sound(player, AdvancedSounds.CLICK_1);
                    }));

            buttons.put(value, button);
        }

        builder.storage(storage).build().open(player);
    }

    private List<String> createDescription(@NotNull final DeathObject deathObject, @NotNull final User user, final boolean canUse) {
        final List<String> result = new ArrayList<>();
        result.add("&6" + deathObject.getTitle());
        if (!canUse) {
            result.add("\n&fТребования:");
            result.add(deathObject.getRequirementDescription());
        }
        if (canUse && user.getCurrentDeathType() != deathObject.getDeathType())
            result.add("\n&r\uE3FE Использовать");

        return result;
    }

}
