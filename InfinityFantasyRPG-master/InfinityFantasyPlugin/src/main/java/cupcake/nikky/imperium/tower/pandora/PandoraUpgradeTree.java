package cupcake.nikky.imperium.tower.pandora;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class PandoraUpgradeTree {

    private final PandoraUpgrade[] upgrades;

    public PandoraUpgrade nextUpgrade(@NotNull final PandoraData pandoraData) {
        if (upgrades.length == pandoraData.getCurrentUpgrade() + 1) return null;
        return upgrades[pandoraData.getCurrentUpgrade() + 1];
    }

    public static class PandoraUpgradeTreeBuilder {

        private final List<PandoraUpgrade> upgrades = new ArrayList<>();

        public PandoraUpgradeTreeBuilder appendUpgrade(PandoraUpgrade... upgrades) {
            this.upgrades.addAll(Arrays.asList(upgrades));
            return this;
        }

        public PandoraUpgradeTree build() {
            return new PandoraUpgradeTree(upgrades.toArray(new PandoraUpgrade[0]));
        }

    }

}
