package cupcake.nikky.imperium.tower.client.protocol.packet.exact.box;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data.CaseData;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseOpenPacket extends ClientPacket {

    private CaseData caseData;

    @Override
    public void serverWrite(ByteBuf data) {
        caseData.write(data);
    }

    @Override
    public void clientRead(ByteBuf data) {
        this.caseData = new CaseData();
        caseData.read(data);
    }
}
