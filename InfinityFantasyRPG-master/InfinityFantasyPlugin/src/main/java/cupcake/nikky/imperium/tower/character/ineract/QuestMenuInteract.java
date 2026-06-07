package cupcake.nikky.imperium.tower.character.ineract;

import cupcake.nikky.imperium.tower.character.ImperiumCharacter;
import cupcake.nikky.imperium.tower.quest.session.QuestSessionProvider;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class QuestMenuInteract extends DefaultInteract {

    public QuestMenuInteract(ImperiumCharacter character) {
        super(character);
    }

    @Override
    public void interact(@NotNull Player player) {
        QuestSessionProvider.getInstance().createSession(player, getCharacter().getStoredQuests())
                .canReceive((player1, quest) -> quest.canReceive(player1))
                .canView((player1, quest) -> true)
                .npcType(this.getCharacter().getType())
                .openContext();
    }
}
