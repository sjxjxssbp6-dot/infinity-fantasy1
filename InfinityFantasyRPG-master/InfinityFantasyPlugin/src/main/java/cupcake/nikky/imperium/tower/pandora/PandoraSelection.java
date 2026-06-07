package cupcake.nikky.imperium.tower.pandora;

import cupcake.nikky.imperium.tower.client.protocol.image.FantasyImage;
import cupcake.nikky.imperium.tower.user.User;
import me.func.mod.Anime;
import me.func.mod.reactive.ReactiveButton;
import me.func.mod.ui.menu.selection.Selection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PandoraSelection {

    public void open(@NotNull final Player player, @NotNull final PandoraData data) {
        var user = player.<User>getBungeePlayer();
        var choicer = Selection.builder()
                .title("Ящик Пандоры")
                .rows(3)
                .columns(3)
                .hint("")
                .build();
        choicer.setStorage(this.createButtons(player, user, choicer, data));
        choicer.open(player);
    }

    private List<ReactiveButton> createButtons(@NotNull final Player player, @NotNull final User user, @NotNull final Selection selection, @NotNull final PandoraData data) {
        var upgradeTree = PandoraProvider.getInstance().getUpgradeTree();
        var nextUpgrade = upgradeTree.nextUpgrade(data);

        var descriptionButton = new ReactiveButton();

        var nextUpgradeButton = new ReactiveButton().title("Следующее усиление").texture(FantasyImage.UPGRADE.toTexture()).hint("");
        if (nextUpgrade == null) nextUpgradeButton.description("Максимальное улучшение").hover("");
        else nextUpgradeButton.description(nextUpgrade.title()).hint("Усилить").hover(
                "Следующее усиление", " ",
                "\uE3FE усилить за %.0f\uE0FE".formatted(nextUpgrade.price(user) * data.getAmplitude()),
                "\uE3FF усилить за %d\uE104".formatted(nextUpgrade.alternatePrice())
        ).onLeftClick((player1, i, reactiveButton) -> {
            var next = upgradeTree.nextUpgrade(data);
            if (next == null) return;

            if (next.tryUpgrade(player.getBungeePlayer(), data, false)) {
                var upgrade = upgradeTree.nextUpgrade(data);

                descriptionButton.description(data.currentUpgrades());
                if (upgrade == null) nextUpgradeButton.description("Максимальное улучшение").hover("").hint("");
                else nextUpgradeButton.description(upgrade.title()).hover(
                        "Следующее усиление", " ",
                        "\uE3FE усилить за %.0f\uE0FE".formatted(upgrade.price(user) * data.getAmplitude()),
                        "\uE3FF усилить за %d\uE104".formatted(upgrade.alternatePrice())
                );
                selection.setStorage(this.createButtons(player, user, selection, data));
            }
        }).onRightClick((player1, i, reactiveButton) -> {
            var next = upgradeTree.nextUpgrade(data);
            if (next == null) return;

            if (next.tryUpgrade(player.getBungeePlayer(), data, true)) {
                var upgrade = upgradeTree.nextUpgrade(data);

                descriptionButton.description(data.currentUpgrades());
                if (upgrade == null) nextUpgradeButton.description("Максимальное улучшение").hover("").hint("");
                else nextUpgradeButton.description(upgrade.title()).hover(
                        "Следующее усиление", " ",
                        "\uE3FE усилить за %.0f\uE0FE".formatted(upgrade.price(user) * data.getAmplitude()),
                        "\uE3FF усилить за %d\uE104".formatted(upgrade.alternatePrice())
                );
                selection.setStorage(this.createButtons(player, user, selection, data));
            }
        });

        return List.of(
                descriptionButton.title("Текущее усиление").description(data.currentUpgrades()).texture(FantasyImage.PAPER.toTexture()).hint(""),
                nextUpgradeButton,
                new ReactiveButton().title("Ящик").hint("Открыть").description("Открыть с текущими улучшениями").texture(FantasyImage.CHEST.toTexture()).hint("")
                        .onLeftClick((player1, i, reactiveButton) -> {
                            if (user.findPandoraSettings().findDataByUUID(data.getUuid()) == null) return;
                            PandoraProvider.getInstance().getPandoraHandler().invalidatePandoraBlockAndSpawnMonster(player, data);
                            Anime.close(player1);
                        })
        );
    }

}
