package club.devcord.gamejam.logic.spawner;

import club.devcord.gamejam.CursedBedwarsPlugin;
import club.devcord.gamejam.logic.GameMap;
import club.devcord.gamejam.logic.settings.GameSettings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;

public class ItemSpawnScheduler {
    private final GameMap map;

    public ItemSpawnScheduler(CursedBedwarsPlugin plugin) {
        this.map = plugin.game().gameMap();

        Bukkit.getScheduler().runTaskTimer(plugin, this::spawnIron, 0L, durationToTicks(GameSettings.IRON_SPAWN_DELAY));
        Bukkit.getScheduler().runTaskTimer(plugin, this::spawnGold, 0L, durationToTicks(GameSettings.GOLD_SPAWN_DELAY));
        Bukkit.getScheduler().runTaskTimer(plugin, this::spawnDiamonds, 0L, durationToTicks(GameSettings.DIAMOND_SPAWN_DELAY));
    }

    private long durationToTicks(Duration duration) {
        return (long) ((duration.toMillis() / 1000.0) * 20L);
    }

    private void spawnIron() {
        var world = map.bukkitWorld();
        GameSettings.IRON_SPAWN_LOCATIONS.forEach(relativeLocation -> {
            var location = new Location(world, relativeLocation.x(), relativeLocation.y(), relativeLocation.z());
            world.dropItem(location, new ItemStack(Material.RAW_IRON));
        });
    }

    private void spawnGold() {
        var world = map.bukkitWorld();
        GameSettings.GOLD_SPAWN_LOCATIONS.forEach(relativeLocation -> {
            var location = new Location(world, relativeLocation.x(), relativeLocation.y(), relativeLocation.z());
            world.dropItem(location, new ItemStack(Material.RAW_GOLD));
        });
    }

    private void spawnDiamonds() {
        var world = map.bukkitWorld();
        GameSettings.DIAMOND_SPAWN_LOCATIONS.forEach(relativeLocation -> {
            var location = new Location(world, relativeLocation.x(), relativeLocation.y(), relativeLocation.z());
            world.dropItem(location, new ItemStack(Material.DIAMOND));
        });
    }
}
