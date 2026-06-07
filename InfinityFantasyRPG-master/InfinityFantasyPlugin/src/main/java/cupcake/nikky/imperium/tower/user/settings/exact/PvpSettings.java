package cupcake.nikky.imperium.tower.user.settings.exact;

import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.BaseUserSettings;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PvpSettings extends SavableUserSettings {

    private final AtomicBoolean localPvpState = new AtomicBoolean(false);

    public PvpSettings(User user) {
        super(user);
    }

    public void localPvpState(final boolean state) {
        for (;;) {
            var old = localPvpState.get();
            if (localPvpState.compareAndSet(old, state)) return;
        }
    }

    public boolean localPvpState() {
        return localPvpState.get();
    }

    @Override
    public List<Bson> createUpdates() {
        return List.of(Updates.set("localPvp", localPvpState()));
    }

    @Override
    public void readDocument(Document document) {
        this.localPvpState(document.getBoolean("localPvp", false));
    }
}
