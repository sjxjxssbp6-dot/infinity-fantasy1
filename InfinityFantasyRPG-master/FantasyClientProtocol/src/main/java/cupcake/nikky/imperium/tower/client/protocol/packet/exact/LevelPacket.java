package cupcake.nikky.imperium.tower.client.protocol.packet.exact;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LevelPacket extends ClientPacket {

    private int actualLevel;
    private int actualExp;
    private int requirementExt;
    private int expMultiplier;
    private String levelColor;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, actualLevel);
        NetUtil.writeVarInt(data, actualExp);
        NetUtil.writeVarInt(data, requirementExt);
        NetUtil.writeVarInt(data, expMultiplier);
        NetUtil.writeUtf8(data, levelColor);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.actualLevel = NetUtil.readVarInt(data);
        this.actualExp = NetUtil.readVarInt(data);
        this.requirementExt = NetUtil.readVarInt(data);
        this.expMultiplier = NetUtil.readVarInt(data);
        this.levelColor = NetUtil.readUtf8(data);
    }

}
