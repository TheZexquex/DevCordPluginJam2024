package club.devcord.gamejam.utils;

import org.bukkit.Location;
import org.bukkit.World;

public record RelativeLocation(double x, double y, double z) {
    public static RelativeLocation of(double x, double y, double z) {
        return new RelativeLocation(x, y, z);
    }

    public Location toBukkitLocation(World world) {
        return new Location(world, x, y, z);
    }
}
