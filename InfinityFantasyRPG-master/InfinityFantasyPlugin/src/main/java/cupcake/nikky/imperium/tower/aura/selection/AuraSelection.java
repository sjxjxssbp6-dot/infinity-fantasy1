package cupcake.nikky.imperium.tower.aura.selection;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.aura.data.AuraAnimationTitleConverter;
import cupcake.nikky.imperium.tower.aura.data.AuraObject;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.func.mod.reactive.ReactiveButton;
import me.func.mod.ui.menu.selection.Selection;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@RequiredArgsConstructor
public class AuraSelection {

    private final Player player;
    private final Map<AuraObject, ReactiveButton> buttons = new HashMap<>();

    public void open() {
        val user = player.<User>getBungeePlayer();
        if (user == null) return;

        val builder = Selection.builder().title("Выбор ауры").columns(3).rows(4).hint("Выбрать");

        final List<ReactiveButton> storage = new ArrayList<>();

        Arrays.stream(AuraObject.values()).sorted(Comparator.comparingInt(auraObject -> {
            if (auraObject == AuraObject.NONE || auraObject == AuraObject.NONE_ALL) return Integer.MIN_VALUE;
            var canUse = auraObject.getFantasyAura().auraRequirement().getAcceptRequirement().test(player);
            return canUse ? auraObject.ordinal() * -1000 : auraObject.ordinal();
        })).forEachOrdered(auraObject -> {
            val button = new ReactiveButton();
            val aura = auraObject.getFantasyAura();

            if (auraObject == AuraObject.NONE || auraObject == AuraObject.NONE_ALL) {
                button.material(Material.BARRIER);
            } else {
                button.texture(aura.resourceLocation());
            }

            val canUse = aura.auraRequirement().getAcceptRequirement().test(player);

            storage.add(button.hover(this.createDescription(auraObject, user, canUse))
                    .title(auraObject.getFantasyAura().title())
                    .description(user.getCurrentAura() == auraObject ? "&aВыбрана" : " ")
                    .onLeftClick((player1, i, reactiveButton) -> {
                        val newestCanUse = aura.auraRequirement().getAcceptRequirement().test(player);
                        if (!newestCanUse) {
                            Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                            return;
                        }

                        auraObject.apply(player1);
                        reactiveButton.hover(this.createDescription(auraObject, user, newestCanUse));

                        buttons.forEach((keyObject, reactiveButton1) -> {
                            if (keyObject == auraObject) return;

                            reactiveButton1.setDescription(" ");
                            reactiveButton1.hover(this.createDescription(keyObject, user, keyObject.getFantasyAura().auraRequirement().getAcceptRequirement().test(player1)));
                        });

                        reactiveButton.setDescription("&aВыбрана");
                        Fantasy.bukkitProvider().sound(player, AdvancedSounds.CLICK_1);
                    }));

            buttons.put(auraObject, button);
        });

        builder.storage(storage).build().open(player);
    }

    private List<String> createDescription(@NotNull final AuraObject auraObject, @NotNull final User user, final boolean canUse) {
        val fantasyAura = auraObject.getFantasyAura();

        final List<String> result = new ArrayList<>();
        result.add("&6" + fantasyAura.title());
        result.add("&fАнимация: &6" + AuraAnimationTitleConverter.convert(fantasyAura.animation()));
        if (fantasyAura.auraRequirement().getRequirementDescription() != null && !canUse) {
            result.add("\n&fТребования:");
            result.add(fantasyAura.auraRequirement().getRequirementDescription());
        }
        if (canUse && user.getCurrentAura() != auraObject)
            result.add("\n&r\uE3FE Использовать");

        return result;
    }

}
