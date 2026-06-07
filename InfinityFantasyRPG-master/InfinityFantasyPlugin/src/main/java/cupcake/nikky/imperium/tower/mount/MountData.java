package cupcake.nikky.imperium.tower.mount;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

@Getter
public class MountData {

    private final Entity mountEntity;
    private final MountType mountType;
    @Setter private long time;

    public MountData(@Nullable final Entity mountEntity, @NotNull final MountType mountType) {
        this.mountEntity = mountEntity;
        this.mountType = mountType;
        this.time = Instant.now().toEpochMilli();
    }

    public boolean tryUnmount() {
        if (mountEntity != null) {
            mountEntity.remove();
            return true;
        }
        return false;
    }

}
