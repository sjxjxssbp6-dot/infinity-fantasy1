package cupcake.nikky.imperium.tower.data;

import cupcake.nikky.imperium.tower.client.protocol.storage.StorageData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerInventoryStorage {

    @Getter
    private static final PlayerInventoryStorage instance = new PlayerInventoryStorage();

    @Getter
    @Setter
    private StorageData storageData;

    public void clear() {
        this.storageData = null;
    }

}
