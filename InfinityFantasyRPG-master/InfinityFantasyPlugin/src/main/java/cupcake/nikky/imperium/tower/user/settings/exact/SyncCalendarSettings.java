package cupcake.nikky.imperium.tower.user.settings.exact;

import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.SavableUserSettings;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
public class SyncCalendarSettings extends SavableUserSettings {

    private int lastDay;
    private long lastGet;

    public SyncCalendarSettings(User user) {
        super(user);
    }

    public void updateState() {
        this.lastDay += 1;
        this.lastGet = Instant.now().toEpochMilli();

        if (lastDay == 8) lastDay = 1;
    }

    public int getNextDay() {
        return getLastDay() == 7 ? 1 : getLastDay() + 1;
    }

    public boolean canTake() {
        return TimeUnit.MILLISECONDS.toDays(Instant.now().toEpochMilli() - lastGet) > 0;
    }

    @Override
    public List<Bson> createUpdates() {
        return List.of(
                Updates.set("lastDayCalendar", lastDay),
                Updates.set("lastGetCalendarTime", lastGet)
        );
    }

    @Override
    public void readDocument(Document document) {
        this.lastDay = document.getInteger("lastDayCalendar", 0);
        this.lastGet = document.get("lastGetCalendarTime", Instant.now().toEpochMilli() - TimeUnit.DAYS.toMillis(1));
    }
}
