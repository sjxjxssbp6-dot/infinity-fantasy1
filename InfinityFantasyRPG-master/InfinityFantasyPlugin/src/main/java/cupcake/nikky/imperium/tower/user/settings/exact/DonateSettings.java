package cupcake.nikky.imperium.tower.user.settings.exact;

import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.donate.DonateData;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import me.func.mod.conversation.ModTransfer;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;

@Accessors(fluent = true)
public class DonateSettings extends SavableUserSettings {

    @Getter
    private DonateData donateData;

    public DonateSettings(User user) {
        super(user);
    }

    @Override
    public List<Bson> createUpdates() {
        return List.of(Updates.set("donateData", donateData.serialize()));
    }

    @Override
    public void readDocument(Document document) {
        val donateDataSerialized = document.get("donateData", "");
        this.donateData = DonateData.deserialize(getUser().getUniqueId(), donateDataSerialized, List.of(
                donateData -> Fantasy.clientProvider().subscribeIndicator(getUser())
        ));
    }

}
