package cupcake.nikky.imperium.tower.fraction;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.protocol.PacketRegistry;
import cupcake.nikky.imperium.tower.protocol.Service;
import cupcake.nikky.imperium.tower.protocol.fraction.FractionType;
import cupcake.nikky.imperium.tower.protocol.packet.UserFractionChange;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FractionProvider {

    @Getter
    private static final FractionProvider instance = new FractionProvider();

    private final AtomicReference<FractionType> topFraction = new AtomicReference<>();
    private final AtomicReference<FractionType> buffFraction = new AtomicReference<>(FractionType.NONE);

    public FractionType findTopFraction() {
        return this.topFraction.get();
    }

    public void setTopFraction(@NotNull final FractionType fraction) {
        for (;;) {
            var old = findTopFraction();
            if (topFraction.compareAndSet(old, fraction)) break;
        }
    }

    public FractionType findBuffFraction() {
        return this.buffFraction.get();
    }

    public void setBuffFraction(@NotNull final FractionType fraction) {
        for (;;) {
            var old = findBuffFraction();
            if (buffFraction.compareAndSet(old, fraction)) break;
        }
    }

    public void sendChangePacket(@NotNull final FractionType oldType, @NotNull final FractionType newType) {
        PacketRegistry.write(Fantasy.gameServer().nameSpace(), new UserFractionChange(oldType, newType));
    }

}
