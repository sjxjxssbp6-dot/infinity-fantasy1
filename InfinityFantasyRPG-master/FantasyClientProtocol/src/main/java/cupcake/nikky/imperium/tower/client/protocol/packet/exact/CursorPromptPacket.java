package cupcake.nikky.imperium.tower.client.protocol.packet.exact;

import cupcake.nikky.imperium.tower.client.protocol.cursor.CursorPromptData;
import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CursorPromptPacket extends ClientPacket {

    private List<CursorPromptData> data = new ArrayList<>();

    public CursorPromptPacket applyPrompt(@NotNull final CursorPromptData data) {
        this.data.add(data);
        return this;
    }

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, this.data.size());
        this.data.forEach(cursorPromptData -> {
            NetUtil.writeUtf8(data, cursorPromptData.getText());
            NetUtil.writeUtf8(data, cursorPromptData.getTexture());
        });
    }

    @Override
    public void clientRead(ByteBuf data) {
        val size = NetUtil.readVarInt(data);
        for (int i = 0; i < size; i++) {
            this.data.add(new CursorPromptData(NetUtil.readUtf8(data), NetUtil.readUtf8(data)));
        }
    }

}
