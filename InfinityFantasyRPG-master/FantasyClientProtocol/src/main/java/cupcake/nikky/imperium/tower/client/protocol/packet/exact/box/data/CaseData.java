package cupcake.nikky.imperium.tower.client.protocol.packet.exact.box.data;

import dev.xdark.feder.NetUtil;
import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaseData {

    private String title;
    private int price;
    private CaseCurrency currency;
    private CaseRewards caseRewards;

    public void addReward(final int id, @NotNull final CaseExactReward caseExactReward) {
        this.caseRewards.getExactItems().put(id, caseExactReward);
    }

    public void write(ByteBuf data) {
        NetUtil.writeUtf8(data, title);
        NetUtil.writeVarInt(data, price);
        NetUtil.writeVarInt(data, currency.ordinal());
        caseRewards.write(data);
    }

    public void read(ByteBuf data) {
        this.title = NetUtil.readUtf8(data);
        this.price = NetUtil.readVarInt(data);
        this.currency = CaseCurrency.values()[NetUtil.readVarInt(data)];
        this.caseRewards = new CaseRewards().read(data);
    }

}
