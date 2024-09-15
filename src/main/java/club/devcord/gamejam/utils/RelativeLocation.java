package club.devcord.gamejam.utils;

import org.bukkit.Location;
import org.bukkit.World;

public record RelativeLocation(double x, double y, double z, float yaw, float pitch) {
    public static RelativeLocation of(double x, double y, double z) {
        return new RelativeLocation(x, y, z, 90, 0);
    }

    public static RelativeLocation of(double x, double y, double z, float yaw, float pitch) {
        return new RelativeLocation(x, y, z, yaw, pitch);
    }

    public static RelativeLocation of(Location location) {
        return new RelativeLocation(location.x(), location.y(), location.z(), location.getYaw(), location.getPitch());
    }

    public Location toBukkitLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof RelativeLocation otherLocation &&
                (int) otherLocation.x == (int) x &&
                (int) otherLocation.y == (int) y &&
                (int) otherLocation.z == (int) z;
    }

    public boolean equalsBukkitLoc(Location otherLocation) {
        return of(otherLocation).equals(this);
    }
}
