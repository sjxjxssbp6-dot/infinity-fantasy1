package cupcake.nikky.imperium.tower.user.settings.exact;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.attribute.AttributeMap;
import cupcake.nikky.imperium.tower.buff.BuffType;
import cupcake.nikky.imperium.tower.user.User;
import cupcake.nikky.imperium.tower.user.settings.BaseUserSettings;
import cupcake.nikky.imperium.tower.zone.Zone;
import lombok.Getter;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class FragmentSettings extends BaseUserSettings {

    private final AtomicLong exitTime = new AtomicLong(0L);
    private final AtomicInteger respawnLeft = new AtomicInteger(0);

    private final AtomicInteger fragmentItemQuantity = new AtomicInteger(0);
    private final AtomicInteger fragmentExperienceIncrease = new AtomicInteger(0);
    private final AtomicInteger fragmentCorruption = new AtomicInteger(0);

    private final AtomicInteger fragmentEventStarted = new AtomicInteger(0);

    private final AtomicReference<Location> lastLocation = new AtomicReference<>();
    private final AtomicReference<Zone> fragmentZone = new AtomicReference<>();
    @Getter private final AttributeMap fragmentAttributeMap = new AttributeMap();

    private final List<String> descriptionOfFragmentModifiers = new CopyOnWriteArrayList<>();

    public FragmentSettings(User user) {
        super(user);
    }

    public boolean isInFragment() {
        return getExitTime() > Instant.now().toEpochMilli();
    }

    public void setFragmentZone(@Nullable final Zone zone) {
        for (;;) {
            var old = fragmentZone.get();
            if (fragmentZone.compareAndSet(old, zone)) break;
        }
    }

    public int getRespawnLeft() {
        return respawnLeft.get();
    }

    public void setRespawnLeft(final int respawnLeft) {
        for (;;) {
            var old = getRespawnLeft();
            if (this.respawnLeft.compareAndSet(old, respawnLeft)) break;
        }
    }

    public int changeRespawnLeft(final int delta) {
        for (;;) {
            var old = getRespawnLeft();
            if (this.respawnLeft.compareAndSet(old, old + delta)) break;
        }

        return getRespawnLeft();
    }

    public Zone getFragmentZone() {
        return fragmentZone.get();
    }

    public void setLastLocation(@Nullable final Location location) {
        for (;;) {
            var old = lastLocation.get();
            if (lastLocation.compareAndSet(old, location)) break;
        }
    }

    public Location getLastLocation() {
        return lastLocation.get();
    }

    public void setFragmentAttributeMap(@NotNull final AttributeMap attributeMap) {
        this.fragmentAttributeMap.replace(attributeMap);
    }

    public void setDescriptionOfFragmentModifiers(@NotNull final List<String> descriptionOfFragmentModifiers) {
        this.descriptionOfFragmentModifiers.clear();
        this.descriptionOfFragmentModifiers.addAll(descriptionOfFragmentModifiers);
    }

    public List<String> getDescriptionOfFragmentModifiers() {
        return new ArrayList<>(descriptionOfFragmentModifiers);
    }

    public void setFragmentEventStarted(final int fragmentEventStarted) {
        for (;;) {
            var old = this.fragmentEventStarted.get();
            if (this.fragmentEventStarted.compareAndSet(old, fragmentEventStarted)) break;
        }
    }

    public int getFragmentEventStarted() {
        return fragmentEventStarted.get();
    }

    public void setFragmentExperienceIncrease(final int experienceIncrease) {
        for (;;) {
            var old = fragmentExperienceIncrease.get();
            if (this.fragmentExperienceIncrease.compareAndSet(old, experienceIncrease)) break;
        }
    }

    public int getFragmentCorruption() {
        return fragmentCorruption.get();
    }

    public void setFragmentCorruption(final int corruption) {
        for (;;) {
            var old = fragmentCorruption.get();
            if (this.fragmentCorruption.compareAndSet(old, corruption)) break;
        }
    }

    public int getFragmentExperienceIncrease() {
        return fragmentExperienceIncrease.get();
    }

    public void setFragmentItemQuantity(final int itemQuantity) {
        for (;;) {
            var old = fragmentItemQuantity.get();
            if (this.fragmentItemQuantity.compareAndSet(old, itemQuantity)) break;
        }
    }

    public int getFragmentItemQuantity() {
        return fragmentItemQuantity.get();
    }

    public void setExitTime(final long time) {
        var update = false;

        for (;;) {
            var old = exitTime.get();
            if (this.exitTime.compareAndSet(old, time)) {
                if (old == time) break;
                update = true;
                break;
            }
        }

        if (update) {
            if (time == 0L) {
                setFragmentZone(null);
                Fantasy.buffService().invalidateBuff(getUser().getUniqueId(), BuffType.FRAGMENT_DEBUFF_INFO);
                Fantasy.runLater(() -> getUser().findPandoraSettings().skipAllPandora());
            }
            var player = getUser().getSpigot();
            if (player == null) return;

            Fantasy.clientProvider().updateFragmentTime(player);
        }
    }

    public long getExitTime() {
        return exitTime.get();
    }

}