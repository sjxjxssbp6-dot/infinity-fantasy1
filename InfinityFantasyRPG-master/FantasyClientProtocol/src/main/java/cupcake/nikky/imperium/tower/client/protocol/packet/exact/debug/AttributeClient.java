package cupcake.nikky.imperium.tower.client.protocol.packet.exact.debug;

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
public class AttributeClient {

    private String name;
    private double base;
    private double additional;
    private double increase;
    private double more;

    public AttributeClient write(ByteBuf data) {
        NetUtil.writeUtf8(name, data);
        data.writeDouble(base);
        data.writeDouble(additional);
        data.writeDouble(increase);
        data.writeDouble(more);
        return this;
    }

    public AttributeClient read(ByteBuf data) {
        this.name = NetUtil.readUtf8(data);
        this.base = data.readDouble();
        this.additional = data.readDouble();
        this.increase = data.readDouble();
        this.more = data.readDouble();
        return this;
    }

}
