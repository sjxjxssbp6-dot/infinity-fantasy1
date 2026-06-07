package cupcake.nikky.imperium.tower.protocol.injector.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Packet;

@Getter
@RequiredArgsConstructor
public class PacketEvent<T extends Packet<?>> {

    private final EntityPlayer entityPlayer;
    private final T packet;
    @Setter @Accessors(chain = true)
    private boolean cancelled;

}
