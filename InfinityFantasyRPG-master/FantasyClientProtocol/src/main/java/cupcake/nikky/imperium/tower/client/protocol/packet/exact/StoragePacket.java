package cupcake.nikky.imperium.tower.client.protocol.packet.exact;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import cupcake.nikky.imperium.tower.client.protocol.storage.PageState;
import cupcake.nikky.imperium.tower.client.protocol.storage.StorageData;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoragePacket extends ClientPacket {

    private StorageData storageData;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, storageData == null ? 0 : storageData.getPages().size());

        if (storageData == null) return;
        storageData.getPages().forEach((page, state) -> {
            NetUtil.writeVarInt(data, page);
            NetUtil.writeUtf8(data, state.name());
        });
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.storageData = new StorageData();
        val size = NetUtil.readVarInt(data);

        for (int i = 0; i < size; i++) {
            val page = NetUtil.readVarInt(data);
            val state = NetUtil.readUtf8(data);

            storageData.apply(page, PageState.valueOf(state));
        }
    }

}
