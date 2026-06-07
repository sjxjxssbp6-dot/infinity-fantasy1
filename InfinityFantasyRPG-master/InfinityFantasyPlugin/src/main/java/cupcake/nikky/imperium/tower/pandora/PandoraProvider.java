package cupcake.nikky.imperium.tower.pandora;

import lombok.Getter;

public final class PandoraProvider {

    @Getter private static final PandoraProvider instance = new PandoraProvider();
    @Getter private final PandoraUpgradeTree upgradeTree;
    @Getter private final PandoraHandler pandoraHandler = new PandoraHandler();

    private PandoraProvider() {
        this.upgradeTree = new PandoraUpgradeTree.PandoraUpgradeTreeBuilder()
                .appendUpgrade(
                        new PandoraUpgrade("25% увеличение\nпредметов", 500, 3, (user, pandoraData) -> pandoraData.increaseAttemptsAmount(25)),
                        new PandoraUpgrade("35% увеличение\nпредметов", 750, 4,  (user, pandoraData) -> pandoraData.increaseAttemptsAmount(35)),
                        new PandoraUpgrade("50% увеличение\nпредметов", 2000, 6, (user, pandoraData) -> pandoraData.increaseAttemptsAmount(50)),
                        new PandoraUpgrade("75% увеличение\nпредметов", 3000, 7, (user, pandoraData) -> pandoraData.increaseAttemptsAmount(75)),
                        new PandoraUpgrade("100% увеличение\nпредметов", 5000, 9, (user, pandoraData) -> pandoraData.increaseAttemptsAmount(100)),
                        new PandoraUpgrade("115% увеличение\nпредметов", 10000, 12, (user, pandoraData) -> pandoraData.increaseAttemptsAmount(115)),
                        new PandoraUpgrade("150% увеличение\nпредметов", 15000, 15, (user, pandoraData) -> pandoraData.increaseAttemptsAmount(150)),
                        new PandoraUpgrade("200% увеличение\nпредметов", 20000, 18, (user, pandoraData) -> pandoraData.increaseAttemptsAmount(200)),
                        new PandoraUpgrade("250% увеличение\nпредметов", 25000, 21, (user, pandoraData) -> pandoraData.increaseAttemptsAmount(250)),
                        new PandoraUpgrade("300% увеличение\nпредметов", 30000, 24, (user, pandoraData) -> pandoraData.increaseAttemptsAmount(300))
                )
                .build();
    }

}
