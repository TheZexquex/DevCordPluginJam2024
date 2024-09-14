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

    public Location toBukkitLocation(World world) {
        return new Location(world, x, y, z, yaw, pitch);
    }
}
