package cupcake.nikky.imperium.tower.client.protocol.packet.exact.craftbench;

import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ModifierData {

    private String craftTitle;
    private int price;
    private int accessCode;

    public void write(ByteBuf data) {
        NetUtil.writeUtf8(data, craftTitle);
        NetUtil.writeVarInt(data, price);
        NetUtil.writeVarInt(data, accessCode);
    }

    public void read(ByteBuf data) {
        this.craftTitle = NetUtil.readUtf8(data);
        this.price = NetUtil.readVarInt(data);
        this.accessCode = NetUtil.readVarInt(data);
    }

    public String getAccessWarnText() {
        if (accessCode == 0) return "";
        else if (accessCode == 1) return "&cНедостаточно места для модификатора";
        else if (accessCode == 2) return "&cНедостаточный уровень предмета";
        else if (accessCode == 3) return "&cДанный модификатор уже присутствует";
        return "&cПопробуйте позже";
    }

}
