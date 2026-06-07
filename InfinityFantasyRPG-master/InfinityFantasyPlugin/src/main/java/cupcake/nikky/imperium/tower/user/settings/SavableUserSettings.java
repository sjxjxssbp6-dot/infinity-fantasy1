package cupcake.nikky.imperium.tower.user.settings;

import cupcake.nikky.imperium.tower.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class SavableUserSettings implements UserSettings {

    private final User user;
    private Player player;

    @Override
    public Player getPlayer() {
        if (player == null)
            player = user.getSpigot();
        return player;
    }

    public void forceUpdateInDataBase() {
        user.saveData(this.createUpdates());
    }

    public abstract List<Bson> createUpdates();

    public abstract void readDocument(Document document);

}
