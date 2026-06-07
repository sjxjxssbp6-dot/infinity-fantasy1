package cupcake.nikky.imperium.tower.tree.builder.element;

import cupcake.nikky.imperium.tower.tree.NodeType;
import cupcake.nikky.imperium.tower.tree.TreeIcon;
import cupcake.nikky.imperium.tower.tree.builder.position.Segment;
import cupcake.nikky.imperium.tower.tree.PointType;
import cupcake.nikky.imperium.tower.tree.element.TreeElementProcess;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.module.upgradetree.UpgradeElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(staticName = "of")
public class TreeElementBuilder {

    private final Int2ObjectMap<Segment> segments;
    private final PointType pointType;
    private final NodeType defaultNodeType;

    private final Map<Integer, TreeElement> elements = new HashMap<>();

    public TreeElementBuilder with(final int id, @NotNull final TreeIcon icon, @NotNull final TreeElementProcess... processes) {
        return this.with(id, icon, pointType, defaultNodeType, processes);
    }

    public TreeElementBuilder with(final int id, @NotNull final TreeIcon icon, @NotNull final NodeType nodeType, @NotNull final TreeElementProcess... processes) {
        return this.with(id, icon, pointType, nodeType, processes);
    }

    private TreeElementBuilder with(final int id, @NotNull final TreeIcon icon, @NotNull final PointType type, @NotNull final NodeType nodeType, @NotNull final TreeElementProcess... processes) {
        val segment = this.segments.get(id);
        if (segment == null) {
            Bukkit.getLogger().warning("Segment with id = " + id + " is null;");
            return this;
        }

        UpgradeElement upgradeElement = null;

        if (icon.getIcon() == null) {
            upgradeElement = new UpgradeElement(id, nodeType.getSize(), segment.getPosition(), nodeType.getColor(),
                    Arrays.stream(processes).map(TreeElementProcess::description).collect(Collectors.joining("\n")),
                    icon.getImage(), id == 0 ? new int[0] : segment.getWadaParent());
        } else {
            upgradeElement = new UpgradeElement(id, nodeType.getSize(), segment.getPosition(), nodeType.getColor(),
                    Arrays.stream(processes).map(TreeElementProcess::description).collect(Collectors.joining("\n")),
                    icon.getIcon(), id == 0 ? new int[0] : segment.getWadaParent());
        }

        upgradeElement.setAnyParentUpgrade(true);

        this.elements.put(id, new TreeElement(upgradeElement, segment, processes, type));
        return this;
    }

    public Int2ObjectMap<TreeElement> build() {
        return new Int2ObjectOpenHashMap<>(elements);
    }

}
