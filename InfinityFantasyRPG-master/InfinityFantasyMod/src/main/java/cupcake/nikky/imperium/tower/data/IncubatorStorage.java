package cupcake.nikky.imperium.tower.data;

import cupcake.nikky.imperium.tower.client.protocol.packet.exact.incubator.IncubatorData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IncubatorStorage {

    @Getter
    private static final IncubatorStorage instance = new IncubatorStorage();

    @Getter
    private final List<IncubatorData> data = new ArrayList<>();

    public void clear() {
        this.data.clear();
    }

}
