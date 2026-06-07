package cupcake.nikky.imperium.tower.client.protocol.packet.exact;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class CurrencyPacket extends ClientPacket {

    private int gran;
    private int mines;
    private int special;
    private int privateSpecial;
    private int flowers;

    @Override
    public void serverWrite(ByteBuf data) {
        NetUtil.writeVarInt(data, gran);
        NetUtil.writeVarInt(data, mines);
        NetUtil.writeVarInt(data, special);
        NetUtil.writeVarInt(data, privateSpecial);
        NetUtil.writeVarInt(data, flowers);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.gran = NetUtil.readVarInt(data);
        this.mines = NetUtil.readVarInt(data);
        this.special = NetUtil.readVarInt(data);
        this.privateSpecial = NetUtil.readVarInt(data);
        this.flowers = NetUtil.readVarInt(data);
    }

}
