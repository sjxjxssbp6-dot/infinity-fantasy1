package cupcake.nikky.imperium.tower.tree.provider;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.SystemNotificationType;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import cupcake.nikky.imperium.tower.tree.TreeCharacterElementGenerator;
import cupcake.nikky.imperium.tower.tree.TreeCharacterPositionGenerator;
import cupcake.nikky.imperium.tower.tree.TreeEdgeElementGenerator;
import cupcake.nikky.imperium.tower.tree.TreeEdgePositionGenerator;
import cupcake.nikky.imperium.tower.tree.builder.element.TreeElement;
import cupcake.nikky.imperium.tower.tree.PointType;
import cupcake.nikky.imperium.tower.user.User;
import it.unimi.dsi.fastutil.ints.*;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.Wada;
import ru.cristalix.wada.module.upgradetree.TreeScheme;

import java.util.*;

@Getter
public class TreeProvider {

    @Accessors(fluent = true)
    private TreeScheme scheme;
    private Int2ObjectMap<TreeElement> data;

    public void openScheme(@NotNull final Player player) {
        val tree = player.<User>getBungeePlayer().findTreeSettings();
        if (tree == null) return;

        this.updateDescription(player);
        scheme.openMenu(player);
    }

    public TreeProvider loadScheme() {
        val builder = Wada.get().getTreeManager().schemeBuilder();
        val segments = new TreeCharacterPositionGenerator().generate();
        segments.putAll(new TreeEdgePositionGenerator().generate());

        this.data = new TreeCharacterElementGenerator().generate(segments);
        this.data.putAll(new TreeEdgeElementGenerator().generate(segments));
        this.data = new Int2ObjectOpenHashMap<>(this.data);

        this.data.forEach((id, element) -> builder.addElement(element.getUpgradeElement()));

        this.scheme = builder.build();
        //this.scheme.setColoredLines(true);
        this.scheme.setMultiWayMode(true);

        this.scheme.onReset(player -> {
            val playerTree = player.<User>getBungeePlayer().findTreeSettings();
            val size = playerTree.findUpgradesSize();

            if (playerTree.getResetPoints() < size) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Необходимо очков восстановления: " + size + ChatColor.RESET + "\uE1CC");
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                return;
            }

            playerTree.changeResetPoints(-size);
            playerTree.invalidateUpgrades();

            this.updateDescription(player);
            this.updateResetButton(player, true);
            this.scheme.sendData(player, new IntLists.EmptyList());
        });

        this.scheme.onAnyClick((player, id, mouse) -> {
            if (mouse != 1) return;

            val playerTree = player.<User>getBungeePlayer().findTreeSettings();
            val upgrades = playerTree.findUpgradeIds();
            if (!upgrades.contains(id)) return;

            if (playerTree.getResetPoints() == 0) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Необходимо очков восстановления: 1" + ChatColor.RESET + "\uE1CC");
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                return;
            }

            if (!this.canReset(id, playerTree.findUpgrades())) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Вы не можете восстановить", "данное искажение.");
                Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                return;
            }

            playerTree.changeResetPoints(-1);
            playerTree.removeUpgrade(id);

            this.updateDescription(player);
            this.updateResetButton(player, upgrades.size() - 1 == 0);
            this.scheme.sendData(player, playerTree.findUpgradeIds());
        });

        this.scheme.onUpgrade((player, id, mouse) -> {
            if (mouse != 0 && mouse != 1) return false;

            val playerTree = player.<User>getBungeePlayer().findTreeSettings();
            val currentUpgrades = playerTree.findUpgrades();

            if (mouse == 1) {
                if (!playerTree.findUpgradeIds().contains(id)) {
                    Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                    return false;
                } else if (playerTree.getResetPoints() == 0) {
                    Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Недостаточно очков восстановления " + ChatColor.RESET + "\uE1CC");
                    Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                    return false;
                }

                Fantasy.bukkitProvider().sound(player, AdvancedSounds.TAP);
                return false;
            }

            val clickedUpgrade = Fantasy.treeProvider().getData().get(id.intValue());

            if (playerTree.findFreePoints(clickedUpgrade.getPointType()) <= 0) {
                Fantasy.clientProvider().notification(player, SystemNotificationType.ERROR, "Недостаточно искажений " + ChatColor.RESET + clickedUpgrade.getPointType().getIcon());
                return false;
            }

            if (clickedUpgrade.getSegment().getId() != 0) {
                if (currentUpgrades.stream().noneMatch(treeElement -> Arrays.stream(clickedUpgrade.getSegment().getParent()).anyMatch(id1 -> treeElement.getSegment().getId() == id1))) {
                    Fantasy.bukkitProvider().sound(player, AdvancedSounds.CONFUSION);
                    return false;
                }
            }

            playerTree.addUpgrade(id);
            this.updateDescription(player);
            this.updateResetButton(player);

            return true;
        });

        return this;
    }

    private boolean canReset(final int resetId, @NotNull final List<TreeElement> upgrades) {
        val copyUpgrades = upgrades.stream().filter(treeElement -> treeElement.getSegment().getId() != resetId).toList();
        val resetElement = getData().get(resetId);
        val resetElementParents = resetElement.getSegment().getParent();

        if (resetId == 0 && !copyUpgrades.isEmpty()) return false;

        final Int2ObjectMap<TreeElement> data = new Int2ObjectOpenHashMap<>();
        copyUpgrades.forEach(treeElement -> data.put(treeElement.getSegment().getId(), treeElement));

        for (int resetElementParent : resetElementParents) {
            var resetData = data.get(resetElementParent);
            if (resetData == null || resetElementParent == 0) continue;
            var list = new IntArrayList();
            list.add(resetId);
            if (!this.canReachSomeWay(data, resetData, list))
                return false;
        }
        return true;
    }

    private boolean canReachSomeWay(@NotNull final Int2ObjectMap<TreeElement> upgrades, @NotNull final TreeElement validate, final IntList skipId) {
        if (validate.getSegment().getId() == 0) return true;
        for (int validateId : validate.getSegment().getParent()) {
            if (skipId.contains(validateId)) continue;
            if (validateId == 0) return true;

            val nextElement = upgrades.get(validateId);
            if (nextElement == null) continue;

            skipId.add(validate.getSegment().getId());
            if (this.canReachSomeWay(upgrades, nextElement, skipId))
                return true;
        }

        return false;
    }

    public void initializeScheme(@NotNull final Player player) {
        val tree = player.<User>getBungeePlayer().findTreeSettings();
        if (tree == null) return;

        val characterPoints = tree.findFreePoints(PointType.CHARACTER);
        val edgePoints = tree.findFreePoints(PointType.EDGE);

        val upgrades = tree.findUpgradeIds();

        scheme.send(player);
        scheme.sendData(player, upgrades);
        this.updateDescription(player, characterPoints, edgePoints, tree.getResetPoints());
        scheme.sendResetButtonStatus(player, true, !upgrades.isEmpty());
    }

    public void updateDescription(@NotNull final Player player) {
        val tree = player.<User>getBungeePlayer().findTreeSettings();

        val characterPoints = tree.findFreePoints(PointType.CHARACTER);
        val edgePoints = tree.findFreePoints(PointType.EDGE);

        this.updateDescription(player, characterPoints, edgePoints, tree.getResetPoints());
    }

    public void updateDescription(@NotNull final Player player, final int characterPoints, final int edgePoints, final int resetPoints) {
        scheme.sendDescription(player,
                "Искажения персонажа: " + characterPoints + PointType.CHARACTER.getIcon() +
                        "\nИскажения Грани: " + edgePoints + PointType.EDGE.getIcon() +
                        "\n\nОчки восстановления: " + resetPoints + "\uE1CC" +
                        "\n\n\uE3FE " + ChatColor.GRAY + "Использовать искажение" +
                        "\n\uE3FF " + ChatColor.GRAY + "Восстановить искажение");
    }

    public void updateResetButton(@NotNull final Player player) {
        val tree = player.<User>getBungeePlayer().findTreeSettings();
        this.updateResetButton(player, tree.findUpgradeIds().isEmpty());
    }

    public void updateResetButton(@NotNull final Player player, final boolean isZeroUpgrades) {
        scheme.sendResetButtonStatus(player, true, !isZeroUpgrades);
    }

}
