package club.devcord.gamejam.logic;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class GameMap {
    private final World bukkitWorld;

    public GameMap() {
        var originalWorld = Bukkit.getWorld("world").getWorldFolder();
        var newWorldPath = Path.of(originalWorld.getParent(), "game").toFile();

        try {
            FileUtils.copyDirectory(originalWorld, newWorldPath);
            FileUtils.delete(new File(newWorldPath, "uid.dat"));
        } catch (IOException exception) {
            // Do nothing
        }

        this.bukkitWorld = Bukkit.createWorld(WorldCreator.name("game"));
    }

    public World bukkitWorld() {
        return bukkitWorld;
    }
}
