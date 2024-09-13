package club.devcord.gamejam.logic;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.UUID;

public class GameMap {
    private final World world;

    public GameMap() {
        String worldName = UUID.randomUUID().toString();
        this.world = Bukkit.createWorld(WorldCreator.name(worldName).copy(Bukkit.getWorld("world")));
    }

    public World getBukkitWorld() {
        return world;
    }
}
