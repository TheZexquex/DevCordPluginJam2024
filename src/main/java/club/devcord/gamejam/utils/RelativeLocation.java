package club.devcord.gamejam.utils;

import org.bukkit.Location;
import org.bukkit.World;

public record RelativeLocation(int x, int y, int z) {
    public static RelativeLocation of(int x, int y, int z) {
        return new RelativeLocation(x, y, z);
    }

    public Location toBukkitLocation(World world) {
        return new Location(world, x, y, z);
    }
}
