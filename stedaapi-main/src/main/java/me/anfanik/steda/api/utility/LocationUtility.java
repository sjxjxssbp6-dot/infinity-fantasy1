package me.anfanik.steda.api.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationUtility {

    public static String serialize(Location location) {
        return serialize(location, false);
    }

    public static String serialize(Location location, boolean withoutWorld) {
        if (location == null) {
            return null;
        }

        val yaw = location.getYaw();
        val pitch = location.getPitch();

        String serializedLocation = String.format("%s%.1f %.1f %.1f", withoutWorld ? "" : location.getWorld().getName() + " ", location.getX(), location.getY(), location.getZ());

        if (yaw + pitch != 0F) serializedLocation += String.format(" %.1f %.1f", yaw, pitch);

        return serializedLocation;
    }

    public static Location deserialize(String serializedString) {
        return deserialize(serializedString, null);
    }

    public static Location deserialize(String serializedString, World world) {
        if (serializedString == null) {
            return null;
        }

        String[] split = serializedString.split(" ");

        int index = 0;

        if (world == null) {
            world = Bukkit.getWorld(split[index++]);
            if (world == null) {
                return null;
            }
        }

        Location location = new Location(
                world,
                Double.parseDouble(split[index++]),
                Double.parseDouble(split[index++]),
                Double.parseDouble(split[index++])
        );

        if (split.length >= 5) {
            location.setYaw(Float.parseFloat(split[index++]));
            location.setPitch(Float.parseFloat(split[index]));
        }

        return location;
    }

    public static boolean isLocationChanged(@NotNull final Location before, @NotNull final Location after, final boolean withYawAndPitch) {
        if(before.getX() != after.getX() || before.getY() != after.getY() || before.getZ() != after.getZ()){
            return true;
        } else if(withYawAndPitch){
            return before.getYaw() != after.getYaw() || before.getPitch() != after.getPitch();
        }
        return false;
    }

}
