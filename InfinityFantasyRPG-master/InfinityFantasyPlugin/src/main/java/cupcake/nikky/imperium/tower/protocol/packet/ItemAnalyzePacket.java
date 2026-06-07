package cupcake.nikky.imperium.tower.protocol.packet;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyze;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyzeContainer;
import cupcake.nikky.imperium.tower.client.protocol.analyze.ItemAnalyzeType;
import cupcake.nikky.imperium.tower.item.ImperiumItem;
import cupcake.nikky.imperium.tower.item.modifier.ModifierPlace;
import cupcake.nikky.imperium.tower.item.modifier.ModifierTier;
import cupcake.nikky.imperium.tower.protocol.ImperiumPacket;
import lombok.val;
import me.func.mod.conversation.ModTransfer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ItemAnalyzePacket extends ImperiumPacket<Player> {

    private final ImperiumItem item;
    private final int index;

    public ItemAnalyzePacket(@NotNull Player client, @Nullable final ImperiumItem item) {
        super(client);
        this.item = item;
        index = -1;
    }

    public ItemAnalyzePacket(@NotNull Player client, final int index) {
        super(client);
        this.item = null;
        this.index = index;
    }

    @Override
    public void notice(int... flags) {
        for (int flag : flags) {
            if (flag == 0) { // open
                this.sendOpenPacket();
            } else if (flag == 1) { // send modifiers
                this.sendModifiers();
            } else if (flag == 2) {
                this.sendClickPacket();
            }
        }
    }

    public void sendOpenPacket() {
        new ModTransfer().send("imp:item-al-open", getClient());
    }

    public void sendClickPacket() {
        if (index == -1) return;
        new ModTransfer().varInt(index).send("imp:item-al-click", getClient());
    }

    public void sendModifiers() {
        if (item == null) return;

        final List<ItemAnalyzeContainer> containers = new ArrayList<>();

        item.getModifierSettings().findPossibleByPlace(ModifierPlace.PREFIX, ModifierPlace.SUFFIX, ModifierPlace.DIRECTION).forEach(modifier -> {
            val container = new ItemAnalyzeContainer();
            val tiers = modifier.getRange().getTiers();
            val size = modifier.getRange().sizeWithoutExalted();
            container.setMaxBaseLevel(tiers.stream().map(ModifierTier::requireBaseLevel).max(Comparator.comparingInt(Integer::intValue)).orElse(-1));
            container.setSumWeights(tiers.stream().map(ModifierTier::weight).reduce(0, Integer::sum));
            container.setTiers(size);
            val modifierDescription = modifier.getModifierDescription().generateCraftDescription("&f", "&f");
            container.setTitle(modifierDescription.startsWith("+") ? modifierDescription.substring(1) : modifierDescription);
            container.setType(modifier.getPlace() == ModifierPlace.PREFIX ?
                    ItemAnalyzeType.PREFIX : modifier.getPlace() == ModifierPlace.SUFFIX ?
                    ItemAnalyzeType.SUFFIX : ItemAnalyzeType.DIRECTION);

            final List<ItemAnalyze> items = new ArrayList<>();

            tiers.forEach(tier ->
                    items.add(
                            new ItemAnalyze(tier.min(),
                                    tier.max(),
                                    tier.amplitude(),
                                    size + 1 - tier.tier(),
                                    tier.requireBaseLevel(),
                                    tier.weight())));
            container.setAnalyzes(items);

            containers.add(container);
        });

        containers.forEach(container -> new ModTransfer().putString(Fantasy.gson().toJson(container)).send("imp:add-item-analyze", getClient()));
    }

}
