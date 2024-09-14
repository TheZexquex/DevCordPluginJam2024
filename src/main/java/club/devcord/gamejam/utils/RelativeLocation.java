package club.devcord.gamejam.utils;

import org.bukkit.Location;
import org.bukkit.World;

public record RelativeLocation(int x, int y, int z, float yaw, float pitch) {
    public static RelativeLocation of(int x, int y, int z) {
        return new RelativeLocation(x, y, z, 90, 0);
    }

    public static RelativeLocation of(int x, int y, int z, float yaw, float pitch) {
        return new RelativeLocation(x, y, z, yaw, pitch);
    }

    public Location toBukkitLocation(World world) {
        return new Location(world, x + 0.5, y + 0.5, z + 0.5, yaw, pitch);
    }
}
