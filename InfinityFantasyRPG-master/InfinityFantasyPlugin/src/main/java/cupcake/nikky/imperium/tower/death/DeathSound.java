package cupcake.nikky.imperium.tower.death;

import cupcake.nikky.imperium.tower.Fantasy;
import cupcake.nikky.imperium.tower.sound.AdvancedSounds;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DeathSound {

    private final Sound sound;
    private final AdvancedSounds advancedSounds;

    public DeathSound() {
        this.sound = null;
        this.advancedSounds = null;
    }

    public DeathSound(@NotNull final Sound sound) {
        this.sound = sound;
        this.advancedSounds = null;
    }

    public DeathSound(@NotNull final AdvancedSounds advancedSounds) {
        this.sound = null;
        this.advancedSounds = advancedSounds;
    }

    public void playSound(@NotNull final Location location) {
        if (sound != null) Fantasy.bukkitProvider().sound(location, sound);
        else if (advancedSounds != null) Fantasy.bukkitProvider().sound(location, advancedSounds);
    }

    public void playLocalSound(@NotNull final Player player, @NotNull final Location location) {
        if (sound != null) Fantasy.bukkitProvider().sound(player, location, sound);
        else if (advancedSounds != null) Fantasy.bukkitProvider().sound(player, location, advancedSounds);
    }

}
