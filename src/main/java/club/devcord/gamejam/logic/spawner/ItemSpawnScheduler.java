package club.devcord.gamejam.logic.spawner;

import club.devcord.gamejam.BuggyBedwarsPlugin;
import club.devcord.gamejam.logic.Game;
import club.devcord.gamejam.logic.settings.GameSettings;
import club.devcord.gamejam.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.time.Duration;

public class ItemSpawnScheduler {
    private final Game game;

    public ItemSpawnScheduler(BuggyBedwarsPlugin plugin) {
        this.game = plugin.game();

        Bukkit.getScheduler().runTaskTimer(plugin, this::spawnIron, 0L, TimeUtils.durationToTicks(GameSettings.IRON_SPAWN_DELAY));
        Bukkit.getScheduler().runTaskTimer(plugin, this::spawnGold, 0L, TimeUtils.durationToTicks(GameSettings.GOLD_SPAWN_DELAY));
        Bukkit.getScheduler().runTaskTimer(plugin, this::spawnDiamonds, 0L, TimeUtils.durationToTicks(GameSettings.DIAMOND_SPAWN_DELAY));
    }

    private void spawnIron() {
        var world = game.gameMap().bukkitWorld();
        GameSettings.IRON_SPAWN_LOCATIONS.forEach(relativeLocation -> {
            var location = relativeLocation.toBukkitLocation(world);
            world.dropItem(location, new ItemStack(Material.RAW_IRON)).setVelocity(new Vector(0,-1,0));
        });
    }

    private void spawnGold() {
        var world = game.gameMap().bukkitWorld();
        GameSettings.GOLD_SPAWN_LOCATIONS.forEach(relativeLocation -> {
            var location = relativeLocation.toBukkitLocation(world);
            world.dropItem(location, new ItemStack(Material.RAW_GOLD)).setVelocity(new Vector(0,-1,0));
        });
    }

    private void spawnDiamonds() {
        var world = game.gameMap().bukkitWorld();
        GameSettings.DIAMOND_SPAWN_LOCATIONS.forEach(relativeLocation -> {
            var location = relativeLocation.toBukkitLocation(world);
            world.dropItem(location, new ItemStack(Material.DIAMOND)).setVelocity(new Vector(0,-1,0));
        });
    }
}
