package cupcake.nikky.imperium.tower.mount.selection;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.mount.MountData;
import cupcake.nikky.imperium.tower.mount.MountType;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.user.User;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.func.mod.reactive.ReactiveButton;
import me.func.mod.ui.menu.selection.Selection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@RequiredArgsConstructor
public class MountSelection {

    private final Player player;
    private final Map<MountType, ReactiveButton> buttons = new HashMap<>();

    public void open() {
        val user = player.<User>getBungeePlayer();
        if (user == null) return;

        if (user.getLevel() < 3) {
            Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR,  "Данное меню доступно", "только с &e3 &cуровня!");
            return;
        }

        val builder = Selection.builder().title("Выбор спутника").columns(3).rows(4).hint("Выбрать");

        final List<ReactiveButton> storage = new ArrayList<>();

        for (MountType value : MountType.values()) {
            val button = new ReactiveButton();
            val mountSettings = value.getSettings();

            val canUse = mountSettings.getMountRequirement().getAcceptRequirement().test(player);

            if (mountSettings.getIcon() == null) {
                button.texture(mountSettings.getTexture());
            } else button.item(mountSettings.getIcon());

            storage.add(button.hover(this.createDescription(value, user, canUse))
                    .title(mountSettings.getTitle())
                    .description(user.getCurrentMountData().getMountType() == value ? "&aВыбран" : " ")
                    .onLeftClick((player1, i, reactiveButton) -> {
                        val newestCanUse = mountSettings.getMountRequirement().getAcceptRequirement().test(player);
                        if (!newestCanUse) {
                            Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                            return;
                        }

                        user.setMountData(new MountData(null, value));
                        reactiveButton.hover(this.createDescription(value, user, newestCanUse));

                        buttons.forEach((mountType, reactiveButton1) -> {
                            if (mountType == value) return;

                            reactiveButton1.setDescription(" ");
                            reactiveButton1.hover(this.createDescription(mountType, user, mountSettings.getMountRequirement().getAcceptRequirement().test(player1)));
                        });

                        reactiveButton.setDescription("&aВыбран");
                        Fantasy.bukkitProvider().sound(player, AdvancedSounds.CLICK_1);
                    }));

            buttons.put(value, button);
        }

        builder.storage(storage).build().open(player);
    }

    private List<String> createDescription(@NotNull final MountType mountType, @NotNull final User user, final boolean canUse) {
        val mountSettings = mountType.getSettings();

        final List<String> result = new ArrayList<>();
        result.add("&6" + mountSettings.getTitle());
        result.add("&fУвеличение скорости: &6" + mountSettings.getSpeed() + "%");
        if (mountSettings.getMountRequirement().getRequirementDescription() != null && !canUse) {
            result.add("\n&fТребования:");
            result.add(mountSettings.getMountRequirement().getRequirementDescription());
        }
        if (canUse && user.getCurrentMountData().getMountType() != mountType)
            result.add("\n&r\uE3FE Использовать");

        return result;
    }

}
