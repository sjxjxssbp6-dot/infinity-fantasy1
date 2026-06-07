package cupcake.nikky.imperium.service.fraction;

import com.mongodb.client.model.Filters;
import cupcake.nikky.imperium.service.Fantasy;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.database.Collections;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.protocol.game.GameStorage;
import cupcake.nikky.imperium.tower.protocol.packet.FractionTopPacket;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import ru.cristalix.core.realm.RealmId;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class FractionService {

    private AtomicInteger timeChanges = new AtomicInteger();

    private final Map<FractionType, Long> cached = new ConcurrentHashMap<>(4, 1F, 2);

    public void updateChanges() {
        if (timeChanges.addAndGet(1) >= 50) {
            this.findTopFraction();
        }
    }

    private FractionType compareData() {
        FractionType topFraction = FractionType.NONE;

        if (cached.getOrDefault(FractionType.ORDER, 0L) > cached.getOrDefault(FractionType.CHAOS, 0L) + 50)
            topFraction = FractionType.ORDER;
        else if (cached.getOrDefault(FractionType.CHAOS, 0L) > cached.getOrDefault(FractionType.ORDER, 0L) + 50)
            topFraction = FractionType.CHAOS;

        return topFraction;
    }

    public void sendUpdates() {
        PacketRegistry.write(Fantasy.gameServer().nameSpace(), new FractionTopPacket(this.compareData()));
    }

    public void findTopFraction() {
        Fantasy.databaseService().asyncCollection(Collections.USERS).thenAcceptAsync(documentMongoCollection -> {
            val chaos = documentMongoCollection.countDocuments(Filters.eq("fraction", FractionType.CHAOS.name()));
            val order = documentMongoCollection.countDocuments(Filters.eq("fraction", FractionType.ORDER.name()));

            cached.put(FractionType.CHAOS, chaos);
            cached.put(FractionType.ORDER, order);
        });
    }

}
