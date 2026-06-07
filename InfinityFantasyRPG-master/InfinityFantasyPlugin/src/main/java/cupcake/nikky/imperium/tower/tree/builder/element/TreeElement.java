package cupcake.nikky.imperium.tower.tree.builder.element;

import cupcake.nikky.imperium.tower.tree.builder.position.Segment;
import cupcake.nikky.imperium.tower.tree.PointType;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.cristalix.wada.module.upgradetree.UpgradeElement;

@Getter
@RequiredArgsConstructor
public class TreeElement {

    private final UpgradeElement upgradeElement;
    private final Segment segment;
    private final TreeElementProcess[] elementProcess;
    private final PointType pointType;

}
