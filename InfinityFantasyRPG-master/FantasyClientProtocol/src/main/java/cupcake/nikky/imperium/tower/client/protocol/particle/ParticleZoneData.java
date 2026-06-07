package cupcake.nikky.imperium.tower.client.protocol.particle;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParticleZoneData {

    private double xCenter;
    private double yCenter;
    private double zCenter;

    private double xSize;
    private double ySize;
    private double zSize;

    public void write(ByteBuf data) {
        data.writeDouble(xCenter);
        data.writeDouble(yCenter);
        data.writeDouble(zCenter);
        data.writeDouble(xSize);
        data.writeDouble(ySize);
        data.writeDouble(zSize);
    }

    public void read(ByteBuf data) {
        this.xCenter = data.readDouble();
        this.yCenter = data.readDouble();
        this.zCenter = data.readDouble();
        this.xSize = data.readDouble();
        this.ySize = data.readDouble();
        this.zSize = data.readDouble();
    }

}
