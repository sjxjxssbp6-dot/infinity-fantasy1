package cupcake.nikky.imperium.tower.protocol;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Getter
@RequiredArgsConstructor
@Deprecated
public abstract class ImperiumPacket<T> {

    @NotNull private final T client;

    public void notice() {
        this.notice(0);
    }

    public abstract void notice(final int... flags);

}
