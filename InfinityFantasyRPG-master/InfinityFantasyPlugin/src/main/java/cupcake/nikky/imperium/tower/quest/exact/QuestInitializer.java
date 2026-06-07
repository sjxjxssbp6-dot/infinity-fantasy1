package cupcake.nikky.imperium.tower.quest.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.beacon.BeaconObjects;
import cupcake.nikky.imperium.tower.quest.Quest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.wada.Wada;
import ru.cristalix.wada.util.Color;

public abstract class QuestInitializer {

    public void applyBeaconNotification(@NotNull final Player player, @NotNull final BeaconObjects beaconObjects) {
        Fantasy.clientProvider().notification(player, "&aТелепорты обновлены!", "&7Добавлен: &6" + beaconObjects.getBeaconSettings().title());
    }

    public String findQuestTitle(final int questId) {
        return Fantasy.questService().findQuest(questId).map(Quest::getTitle).orElse("Unknown quest");
    }

    public void applyBlockOutline(@NotNull final Player player, @NotNull final Location location) {
        Wada.get().getBlocksOutlineModule().show(location, Color.GREEN, player);
    }

}
