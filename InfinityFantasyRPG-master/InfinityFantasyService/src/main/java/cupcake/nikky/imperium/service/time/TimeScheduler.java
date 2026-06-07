package cupcake.nikky.imperium.service.time;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import cupcake.nikky.imperium.service.Fantasy;
import cupcake.nikky.imperium.service.config.DailySettings;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.database.Collections;
import cupcake.nikky.imperium.tower.protocol.packet.daily.DailyDataPacket;
import cupcake.nikky.imperium.tower.protocol.trade.npc.TradeDuration;
import lombok.val;

import java.time.DayOfWeek;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class TimeScheduler implements Runnable {

    @Override
    public void run() {
        val date = OffsetDateTime.now(ZoneOffset.UTC).plusHours(3);

        val dailyContainer = Fantasy.configurationService().findByClass(DailySettings.class).orElse(null);
        if (dailyContainer == null) return;

        val lastUpdateDay = dailyContainer.getDay();

        val currentDay = date.getDayOfYear();

        if (currentDay != lastUpdateDay) {
            val isNextWeek = date.getDayOfWeek() == DayOfWeek.MONDAY;
            dailyContainer.setDay(currentDay);

            if (isNextWeek)
                dailyContainer.setWeek(currentDay);

            dailyContainer.save();
            PacketRegistry.write(Fantasy.gameServer().nameSpace(), new DailyDataPacket(true, isNextWeek));

            var combinedUpdates = Updates.combine(Updates.set("dailyQuests", new ArrayList<>()), Updates.set(TradeDuration.DAY.getFieldName(), ""));
            if (isNextWeek) combinedUpdates = Updates.combine(combinedUpdates, Updates.set(TradeDuration.WEEK.getFieldName(), ""));

            Fantasy.databaseService().collection(Collections.USERS).updateMany(Filters.empty(), combinedUpdates, new UpdateOptions().upsert(true));
        }
    }

}
