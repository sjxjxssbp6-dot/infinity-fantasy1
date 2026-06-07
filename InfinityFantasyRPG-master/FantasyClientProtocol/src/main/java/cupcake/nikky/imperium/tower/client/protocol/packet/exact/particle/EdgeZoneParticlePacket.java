package cupcake.nikky.imperium.tower.client.protocol.packet.exact.particle;

import cupcake.nikky.imperium.tower.client.protocol.packet.ClientPacket;
import cupcake.nikky.imperium.tower.client.protocol.particle.ParticleZoneData;
import io.netty.buffer.ByteBuf;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EdgeZoneParticlePacket extends ClientPacket {

    private List<ParticleZoneData> zones = new ArrayList<>();

    @Override
    public void serverWrite(ByteBuf data) {
        zones.forEach(zone -> zone.write(data));
    }

    @Override
    public void clientRead(ByteBuf data) {
        while(data.isReadable()) {
            val zone = new ParticleZoneData();
            zone.read(data);
            zones.add(zone);
        }
    }
}
